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

package de.tudresden.inf.lat.jcel.owlapi.main;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.impl.DefaultNode;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLNamedIndividualNodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNodeSet;
import org.semanticweb.owlapi.util.Version;

import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelClassifier;
import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelClassifierImpl;
import de.tudresden.inf.lat.jcel.owlapi.classifier.JcelModel;

/**
 * This class implements the available functionality of a
 * <code>JcelReasoner</code>. This is an auxiliary class used by
 * <code>JcelReasoner</code>.
 * 
 * @author Julian Mendez
 * 
 * @see JcelReasoner
 */
public class JcelReasonerProcessor {

	private static final Logger logger = Logger
			.getLogger(JcelReasonerProcessor.class.getName());

	private static final String reasonerName = "jcel";
	private JcelClassifier classifier = null;
	private JcelModel model = null;
	private OWLOntology rootOntology = null;
	private long timeOut = Long.MAX_VALUE;

	public JcelReasonerProcessor(OWLOntology rootOnt) {
		if (rootOnt == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOnt;
		this.classifier = new JcelClassifierImpl(getOWLNothing(),
				getOWLThing(), getOWLBottomObjectProperty(),
				getOWLTopObjectProperty());
	}

	public JcelReasonerProcessor(OWLOntology rootOnt,
			ReasonerProgressMonitor monitor) {
		if (rootOnt == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (monitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.rootOntology = rootOnt;
		this.classifier = new JcelClassifierImpl(getOWLNothing(),
				getOWLThing(), getOWLBottomObjectProperty(),
				getOWLTopObjectProperty(), monitor);
	}

	private void assertClassification() throws JcelReasonerException {
		if (!isClassified()) {
			throw new JcelReasonerException(
					"The ontology has not been classified!");
		}
	}

	private NodeSet<OWLClass> convertToNodeSetOfOWLClass(
			Set<Set<OWLEntity>> setOfSets) {
		Set<Node<OWLClass>> nodeSet = new HashSet<Node<OWLClass>>();
		for (Set<OWLEntity> elem : setOfSets) {
			nodeSet.add(NodeFactory.getOWLClassNode(pickOWLClasses(elem)));
		}
		return new OWLClassNodeSet(nodeSet);
	}

	private NodeSet<OWLNamedIndividual> convertToNodeSetOfOWLNamedIndividual(
			Set<OWLNamedIndividual> individualSet) {

		Set<Node<OWLNamedIndividual>> nodeSet = new HashSet<Node<OWLNamedIndividual>>();
		for (OWLNamedIndividual individual : individualSet) {
			nodeSet.add(NodeFactory.getOWLNamedIndividualNode(individual));
		}
		return new OWLNamedIndividualNodeSet(nodeSet);
	}

	private NodeSet<OWLObjectProperty> convertToNodeSetOfOWLObjectProperty(
			Set<Set<OWLObjectProperty>> setOfSets) {
		Set<Node<OWLObjectProperty>> nodeSet = new HashSet<Node<OWLObjectProperty>>();
		for (Set<OWLObjectProperty> elem : setOfSets) {
			nodeSet.add(NodeFactory.getOWLObjectPropertyNode(elem));
		}
		return new OWLObjectPropertyNodeSet(nodeSet);
	}

	public void dispose() throws JcelReasonerException {
		this.rootOntology = null;
	}

	public Set<OWLEntity> flatten(Set<Set<OWLEntity>> setOfSets) {
		if (setOfSets == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<OWLEntity> ret = new HashSet<OWLEntity>();
		for (Set<OWLEntity> set : setOfSets) {
			for (OWLEntity elem : set) {
				ret.add(elem);
			}
		}
		return ret;
	}

	public Node<OWLClass> getBottomClassNode() {
		return NodeFactory.getOWLClassNode(getEquivClasses(getOWLNothing()));
	}

	public Node<OWLObjectProperty> getBottomObjectPropertyNode() {
		return getEquivalentObjectProperties(getOWLBottomObjectProperty());
	}

	public JcelClassifier getClassifier() {
		return this.classifier;
	}

	public NodeSet<OWLNamedIndividual> getDifferentIndividuals(
			OWLNamedIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDifferentIndividuals(OWLNamedIndividual)");
	}

	public NodeSet<OWLClass> getDisjointClasses(OWLClassExpression clExp,
			boolean direct) {
		if (clExp == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDisjointClasses(OWLClassExpression)");
	}

	public NodeSet<OWLObjectProperty> getDisjointObjectProperties(
			OWLObjectPropertyExpression propExpr, boolean direct) {
		if (propExpr == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		throw new JcelReasonerException(
				"Not implemented: getDisjointObjectProperties(OWLObjectPropertyExpression)");
	}

	public Node<OWLClass> getEquivalentClasses(
			OWLClassExpression classExpression) throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Node<OWLClass> ret = null;
		if (isClassified()) {
			ret = NodeFactory.getOWLClassNode(pickOWLClasses(getModel()
					.getOWLEntityGraph().getEquivalents(
							classExpression.asOWLClass())));
		}
		return ret;
	}

	public Node<OWLObjectProperty> getEquivalentObjectProperties(
			OWLObjectPropertyExpression propertyExpression) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLObjectProperty property = propertyExpression.asOWLObjectProperty();
		DefaultNode<OWLObjectProperty> ret = null;
		if (isClassified()) {
			ret = NodeFactory.getOWLObjectPropertyNode(getModel()
					.getOWLObjectPropertyGraph().getEquivalents(property));
		}
		return ret;
	}

	private Set<OWLClass> getEquivClasses(OWLClass cls)
			throws JcelReasonerException {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		if (isClassified()) {
			ret = pickOWLClasses(getModel().getOWLEntityGraph().getEquivalents(
					cls));
		}
		return ret;
	}

	public NodeSet<OWLNamedIndividual> getInstances(
			OWLClassExpression classExpression, boolean direct)
			throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		OWLClass type = classExpression.asOWLClass();
		Set<OWLEntity> entitySet = flatten(getModel().getOWLEntityGraph()
				.getSubElements(type, direct));
		return convertToNodeSetOfOWLNamedIndividual(pickOWLNamedIndividuals(entitySet));
	}

	private JcelModel getModel() {
		return this.model;
	}

	public NodeSet<OWLNamedIndividual> getObjectPropertyValues(
			OWLNamedIndividual individual,
			OWLObjectPropertyExpression propertyExpression) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		NodeSet<OWLNamedIndividual> ret = null;
		if (isClassified()) {
			Set<OWLObjectPropertyAssertionAxiom> axiomSet = getRootOntology()
					.getObjectPropertyAssertionAxioms(individual);
			Set<OWLNamedIndividual> individualSet = new HashSet<OWLNamedIndividual>();
			for (OWLObjectPropertyAssertionAxiom axiom : axiomSet) {
				if (propertyExpression.equals(axiom.getProperty())) {
					individualSet.add(axiom.getObject().asOWLNamedIndividual());
				}
			}
			ret = convertToNodeSetOfOWLNamedIndividual(individualSet);
		}
		return ret;
	}

	public OWLObjectProperty getOWLBottomObjectProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLBottomObjectProperty();
	}

	public OWLClass getOWLNothing() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLNothing();
	}

	public OWLClass getOWLThing() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLThing();
	}

	public OWLObjectProperty getOWLTopObjectProperty() {
		return getRootOntology().getOWLOntologyManager().getOWLDataFactory()
				.getOWLTopObjectProperty();
	}

	public ReasonerProgressMonitor getProgressMonitor() {
		return getClassifier().getProgressMonitor();
	}

	public String getReasonerName() {
		return reasonerName;
	}

	public Version getReasonerVersion() {
		Version ret = new Version(0, 0, 0, 0);
		String versionId = JcelReasonerProcessor.class.getPackage()
				.getImplementationVersion();
		if (versionId != null) {
			StringTokenizer stok = new StringTokenizer(versionId, ".");
			int major = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int minor = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int patch = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			int build = stok.hasMoreTokens() ? Integer.parseInt(stok
					.nextToken()) : 0;
			ret = new Version(major, minor, patch, build);
		}
		return ret;
	}

	public OWLOntology getRootOntology() {
		return this.rootOntology;
	}

	public Node<OWLNamedIndividual> getSameIndividuals(
			OWLNamedIndividual individual) {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Node<OWLNamedIndividual> ret = null;
		if (isClassified()) {
			ret = NodeFactory
					.getOWLNamedIndividualNode(pickOWLNamedIndividuals(getModel()
							.getOWLEntityGraph().getEquivalents(individual)));
		}
		return ret;
	}

	public NodeSet<OWLClass> getSubClasses(OWLClassExpression description,
			boolean direct) {
		if (description == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLClass(getModel().getOWLEntityGraph()
				.getSubElements(description.asOWLClass(), direct));
	}

	public NodeSet<OWLObjectProperty> getSubProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct)
			throws JcelReasonerException {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLObjectProperty(getModel()
				.getOWLObjectPropertyGraph().getSubElements(
						propertyExpression.asOWLObjectProperty(), direct));
	}

	public NodeSet<OWLClass> getSuperClasses(
			OWLClassExpression classExpression, boolean direct) {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLClass(getModel().getOWLEntityGraph()
				.getSuperElements(classExpression.asOWLClass(), direct));
	}

	public NodeSet<OWLObjectProperty> getSuperProperties(
			OWLObjectPropertyExpression propertyExpression, boolean direct) {
		if (propertyExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return convertToNodeSetOfOWLObjectProperty(getModel()
				.getOWLObjectPropertyGraph().getSuperElements(
						propertyExpression.asOWLObjectProperty(), direct));
	}

	public long getTimeOut() {
		return this.timeOut;
	}

	public Node<OWLClass> getTopClassNode() {
		return NodeFactory.getOWLClassNode(getEquivClasses(getOWLThing()));
	}

	public Node<OWLObjectProperty> getTopObjectPropertyNode() {
		return getEquivalentObjectProperties(getOWLTopObjectProperty());
	}

	public NodeSet<OWLClass> getTypes(OWLNamedIndividual individual,
			boolean direct) throws JcelReasonerException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		NodeSet<OWLClass> ret = null;
		if (isClassified()) {
			Set<Set<OWLEntity>> types = getModel().getOWLEntityGraph()
					.getSuperElements(individual, direct);
			ret = convertToNodeSetOfOWLClass(types);
		}
		return ret;
	}

	public Node<OWLClass> getUnsatisfiableClasses() {
		Set<OWLClass> classSet = getEquivClasses(getOWLNothing());
		if (classSet.size() == 1) {
			classSet = new HashSet<OWLClass>();
		}
		return NodeFactory.getOWLClassNode(classSet);
	}

	public boolean hasType(OWLNamedIndividual individual,
			OWLClassExpression type, boolean direct)
			throws JcelReasonerException {
		if (individual == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = false;
		if (type instanceof OWLClass) {
			ret = flatten(
					getModel().getOWLEntityGraph().getSuperElements(individual,
							direct)).contains(type.asOWLClass());
		} else {
			throw new JcelReasonerException(
					"Only implemented for classes: hasType('" + individual
							+ "', '" + type + "', " + direct + ")");
		}
		return ret;
	}

	public void interrupt() {
		getClassifier().interrupt();
	}

	public boolean isClassified() throws JcelReasonerException {
		return this.model != null;
	}

	public boolean isConsistent() throws JcelReasonerException {
		return !isEquivalentClass(getOWLNothing(), getOWLThing())
				&& pickOWLNamedIndividuals(
						getModel().getOWLEntityGraph().getEquivalents(
								getOWLNothing())).isEmpty();
	}

	public boolean isEntailmentCheckingSupported(AxiomType<?> axiomType) {
		if (axiomType == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return false;
	}

	private boolean isEquivalentClass(OWLClassExpression clsC,
			OWLClassExpression clsD) throws JcelReasonerException {
		assertClassification();
		return getModel().getOWLEntityGraph().getEquivalents(clsC.asOWLClass())
				.contains(clsD.asOWLClass());
	}

	public boolean isSatisfiable(OWLClassExpression classExpression)
			throws JcelReasonerException {
		if (classExpression == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		boolean ret = true;
		if (isClassified()) {
			if (classExpression.isClassExpressionLiteral()) {
				ret = !isEquivalentClass(classExpression.asOWLClass(),
						getOWLNothing());
			} else {
				Set<OWLEntity> referredEntities = classExpression
						.getSignature();
				Set<OWLClass> inconsistentClasses = getEquivClasses(getOWLNothing());
				for (OWLEntity current : referredEntities) {
					if (current.isOWLClass()
							&& inconsistentClasses.contains(current)) {
						ret = false;
					}
				}
			}
		}
		return ret;
	}

	private Set<OWLClass> pickOWLClasses(Set<OWLEntity> set) {
		Set<OWLClass> ret = new HashSet<OWLClass>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLClass) {
				ret.add(entity.asOWLClass());
			}
		}
		return ret;
	}

	private Set<OWLNamedIndividual> pickOWLNamedIndividuals(Set<OWLEntity> set) {
		Set<OWLNamedIndividual> ret = new HashSet<OWLNamedIndividual>();
		for (OWLEntity entity : set) {
			if (entity instanceof OWLNamedIndividual) {
				ret.add(entity.asOWLNamedIndividual());
			}
		}
		return ret;
	}

	public void prepareReasoner() {
		logger.fine("classifying " + getRootOntology().getOntologyID());
		getClassifier().resetAndLoad(getRootOntology().getAxioms());
		this.model = new JcelModel(getClassifier().getRelationGraph(),
				getClassifier().getTypeGraph(), getClassifier()
						.getReflexiveProperties(), getClassifier()
						.getTransitiveProperties());

	}
}