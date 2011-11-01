/*
 * Copyright 2009 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 * jcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcel.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.tudresden.inf.lat.jcel.protege.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.TreeSet;

import org.coode.xml.XMLWriter;
import org.coode.xml.XMLWriterImpl;
import org.coode.xml.XMLWriterNamespaceManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;

/**
 * This class makes an XML representation of the inferred data in an
 * OWLReasoner.
 * 
 * @author Julian Mendez
 */
public class OWLReasonerXMLOutput {

	private static final String attIRI = "IRI";
	private static final String CLASS = "Class";
	private static final String CLASS_ASSERTION = "ClassAssertion";
	private static final String DECLARATION = "Declaration";
	private static final String EQUIVALENT_CLASSES = "EquivalentClasses";
	private static final String EQUIVALENT_OBJECT_PROPERTIES = "EquivalentObjectProperties";
	private static final String NAMED_INDIVIDUAL = "NamedIndividual";
	private static final String OBJECT_PROPERTY = "ObjectProperty";
	private static final String OBJECT_PROPERTY_ASSERTION = "ObjectPropertyAssertion";
	private static final String ONTOLOGY = "Ontology";
	private static final String SUB_CLASS_OF = "SubClassOf";
	private static final String SUB_OBJECT_PROPERTY_OF = "SubObjectPropertyOf";
	private static final String xmlns = "http://www.w3.org/2002/07/owl#";
	private static final String xmlnsRdfKey = "rdf";
	private static final String xmlnsRdfPrefix = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String xmlnsRdfsKey = "rdfs";
	private static final String xmlnsRdfsPrefix = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String xmlnsXmlKey = "xml";
	private static final String xmlnsXmlPrefix = "http://www.w3.org/XML/1998/namespace";
	private static final String xmlnsXsdKey = "xsd";
	private static final String xmlnsXsdPrefix = "http://www.w3.org/2001/XMLSchema#";

	private OWLReasoner reasoner = null;
	private XMLWriter writer = null;

	public OWLReasonerXMLOutput(OWLReasoner reasoner) {
		this.reasoner = reasoner;
	}

	private Set<OWLClass> flattenClasses(Set<Set<OWLClass>> setOfSets) {
		Set<OWLClass> ret = new TreeSet<OWLClass>();
		for (Set<OWLClass> set : setOfSets) {
			ret.addAll(set);
		}
		return ret;
	}

	private Set<OWLObjectProperty> flattenProperties(
			Set<Set<OWLObjectProperty>> setOfSets) {
		Set<OWLObjectProperty> ret = new TreeSet<OWLObjectProperty>();
		for (Set<OWLObjectProperty> set : setOfSets) {
			ret.addAll(set);
		}
		return ret;
	}

	private void render() throws OWLReasonerException {
		try {

			OWLOntology ontology = this.reasoner.getLoadedOntologies()
					.iterator().next();
			Set<OWLClass> classSet = new TreeSet<OWLClass>();
			classSet.addAll(ontology.getClassesInSignature());
			Set<OWLObjectProperty> propertySet = new TreeSet<OWLObjectProperty>();
			propertySet.addAll(ontology.getObjectPropertiesInSignature());
			Set<OWLIndividual> individualSet = new TreeSet<OWLIndividual>();
			individualSet.addAll(ontology.getIndividualsInSignature());

			renderDeclaration(classSet);
			renderDeclaration(propertySet);
			renderDeclaration(individualSet);

			Set<OWLClass> classesToVisit = new TreeSet<OWLClass>();
			classesToVisit.addAll(classSet);
			while (!classesToVisit.isEmpty()) {
				OWLClass cls = classesToVisit.iterator().next();
				classesToVisit.remove(cls);
				Set<OWLClass> equivClasses = this.reasoner
						.getEquivalentClasses(cls);
				renderEquivalentClasses(equivClasses);
				classesToVisit.removeAll(equivClasses);
			}

			Set<OWLObjectProperty> propertiesToVisit = new TreeSet<OWLObjectProperty>();
			propertiesToVisit.addAll(propertySet);
			while (!propertiesToVisit.isEmpty()) {
				OWLObjectProperty property = propertiesToVisit.iterator()
						.next();
				propertiesToVisit.remove(property);
				Set<OWLObjectProperty> equivProperties = this.reasoner
						.getEquivalentProperties(property);
				renderEquivalentObjectProperties(equivProperties);
				propertiesToVisit.removeAll(equivProperties);
			}

			for (OWLClass subClass : classSet) {
				Set<OWLClass> superClasses = new TreeSet<OWLClass>();
				superClasses.addAll(flattenClasses(this.reasoner
						.getSuperClasses(subClass)));
				for (OWLClass superClass : superClasses) {
					renderSubClassOf(subClass, superClass);
				}
			}

			for (OWLObjectProperty subProperty : propertySet) {
				Set<OWLObjectProperty> superProperties = new TreeSet<OWLObjectProperty>();
				superProperties.addAll(flattenProperties(this.reasoner
						.getSuperProperties(subProperty)));
				for (OWLObjectProperty superProperty : superProperties) {
					renderSubObjectPropertyOf(subProperty, superProperty);
				}
			}

			for (OWLClass cls : classSet) {
				Set<OWLIndividual> instances = new TreeSet<OWLIndividual>();
				instances.addAll(this.reasoner.getIndividuals(cls, true));
				for (OWLIndividual individual : instances) {
					renderClassAssertion(cls, individual);
				}
			}

			for (OWLObjectProperty property : propertySet) {
				for (OWLIndividual individual : individualSet) {
					Set<OWLIndividual> propertyValues = new TreeSet<OWLIndividual>();
					propertyValues.addAll(this.reasoner.getRelatedIndividuals(
							individual, property.asOWLObjectProperty()));
					for (OWLIndividual otherIndividual : propertyValues) {
						renderObjectPropertyAssertion(property, individual,
								otherIndividual);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	private void renderClassAssertion(OWLClass cls, OWLIndividual individual)
			throws IOException {
		this.writer.writeStartElement(CLASS_ASSERTION);
		renderEntity(cls);
		renderEntity(individual);
		this.writer.writeEndElement();

	}

	private void renderDeclaration(Set<? extends OWLEntity> entities)
			throws IOException {
		for (OWLEntity elem : entities) {
			this.writer.writeStartElement(DECLARATION);
			renderEntity(elem);
			this.writer.writeEndElement();
		}
	}

	private void renderEntity(OWLEntity entity) throws IOException {
		if (entity instanceof OWLClass) {
			this.writer.writeStartElement(CLASS);
		} else if (entity instanceof OWLObjectProperty) {
			this.writer.writeStartElement(OBJECT_PROPERTY);
		} else if (entity instanceof OWLIndividual) {
			this.writer.writeStartElement(NAMED_INDIVIDUAL);
		} else {
			throw new IllegalStateException("Entity cannot be rendered : '"
					+ entity + "'.");
		}
		this.writer.writeAttribute(attIRI, entity.getURI().toString());
		this.writer.writeEndElement();
	}

	private void renderEntitySet(Set<? extends OWLEntity> entitySet)
			throws IOException {
		Set<OWLEntity> set = new TreeSet<OWLEntity>();
		set.addAll(entitySet);
		for (OWLEntity entity : set) {
			renderEntity(entity);
		}
	}

	private void renderEquivalentClasses(Set<OWLClass> classSet)
			throws IOException {
		this.writer.writeStartElement(EQUIVALENT_CLASSES);
		renderEntitySet(classSet);
		this.writer.writeEndElement();
	}

	private void renderEquivalentObjectProperties(
			Set<OWLObjectProperty> propertySet) throws IOException {
		this.writer.writeStartElement(EQUIVALENT_OBJECT_PROPERTIES);
		renderEntitySet(propertySet);
		this.writer.writeEndElement();
	}

	private void renderObjectPropertyAssertion(OWLObjectProperty property,
			OWLIndividual subject, OWLIndividual object) throws IOException {
		this.writer.writeStartElement(OBJECT_PROPERTY_ASSERTION);
		renderEntity(property);
		renderEntity(subject);
		renderEntity(object);
		this.writer.writeEndElement();
	}

	private void renderSubClassOf(OWLClass subClass, OWLClass superClass)
			throws IOException {
		this.writer.writeStartElement(SUB_CLASS_OF);
		renderEntity(subClass);
		renderEntity(superClass);
		this.writer.writeEndElement();
	}

	private void renderSubObjectPropertyOf(OWLObjectProperty subProperty,
			OWLObjectProperty superProperty) throws IOException {
		this.writer.writeStartElement(SUB_OBJECT_PROPERTY_OF);
		renderEntity(subProperty);
		renderEntity(superProperty);
		this.writer.writeEndElement();
	}

	public void toXML(OutputStream out) {
		try {
			XMLWriterNamespaceManager manager = new XMLWriterNamespaceManager(
					xmlns);
			OutputStreamWriter output = new OutputStreamWriter(out);

			manager.setPrefix(xmlnsRdfsKey, xmlnsRdfsPrefix);
			manager.setPrefix(xmlnsXsdKey, xmlnsXsdPrefix);
			manager.setPrefix(xmlnsRdfKey, xmlnsRdfPrefix);
			manager.setPrefix(xmlnsXmlKey, xmlnsXmlPrefix);

			this.writer = new XMLWriterImpl(output, manager);
			this.writer.setEncoding("UTF-8");
			this.writer.setWrapAttributes(false);
			this.writer.startDocument(ONTOLOGY);
			render();
			output.flush();
			this.writer.endDocument();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (OWLReasonerException e) {
			throw new RuntimeException(e);
		}
	}
}
