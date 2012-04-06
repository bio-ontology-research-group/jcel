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

package de.tudresden.inf.lat.jcel.coreontology.axiom;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class models an ontology with the property to look up by axiom type.
 * 
 * @author Julian Mendez
 */
public class ExtendedOntologyImpl implements ExtendedOntology,
		NormalizedIntegerAxiomVisitor<Boolean> {

	private Map<Integer, Set<GCI0Axiom>> mapOfGCI0 = new HashMap<Integer, Set<GCI0Axiom>>();
	private Map<Integer, Set<GCI1Axiom>> mapOfGCI1 = new HashMap<Integer, Set<GCI1Axiom>>();
	private Map<Integer, Set<GCI2Axiom>> mapOfGCI2 = new HashMap<Integer, Set<GCI2Axiom>>();
	private Map<Integer, Set<GCI3Axiom>> mapOfGCI3A = new HashMap<Integer, Set<GCI3Axiom>>();
	private Map<Integer, Set<GCI3Axiom>> mapOfGCI3r = new HashMap<Integer, Set<GCI3Axiom>>();
	private Map<Integer, Set<NominalAxiom>> mapOfNominalAxiom = new HashMap<Integer, Set<NominalAxiom>>();
	private Map<Integer, Set<RangeAxiom>> mapOfRangeAxiom = new HashMap<Integer, Set<RangeAxiom>>();
	private Map<Integer, Set<RI2Axiom>> mapOfRI2r = new HashMap<Integer, Set<RI2Axiom>>();
	private Map<Integer, Set<RI2Axiom>> mapOfRI2s = new HashMap<Integer, Set<RI2Axiom>>();
	private Map<Integer, Set<RI3Axiom>> mapOfRI3ByLeft = new HashMap<Integer, Set<RI3Axiom>>();
	private Map<Integer, Set<RI3Axiom>> mapOfRI3ByRight = new HashMap<Integer, Set<RI3Axiom>>();
	private Set<NormalizedIntegerAxiom> normalizedAxiomSet = new HashSet<NormalizedIntegerAxiom>();
	private Set<Integer> setOfAllObjectProperties = new HashSet<Integer>();
	private Set<Integer> setOfClasses = new HashSet<Integer>();
	private Set<Integer> setOfFunctionalObjectProperties = new HashSet<Integer>();
	private Set<Integer> setOfReflexiveObjectProperties = new HashSet<Integer>();
	private Set<Integer> setOfTransitiveObjectProperties = new HashSet<Integer>();

	/**
	 * Constructs an empty ontology.
	 */
	public ExtendedOntologyImpl() {
	}

	@Override
	public void addClass(int classId) {
		this.setOfClasses.add(classId);
	}

	private void addEntities(NormalizedIntegerAxiom axiom) {
		this.setOfAllObjectProperties.addAll(axiom
				.getObjectPropertiesInSignature());
		this.setOfClasses.addAll(axiom.getClassesInSignature());
	}

	private void addGCI0Axiom(int classId, GCI0Axiom axiom) {
		if (this.mapOfGCI0.get(classId) == null) {
			this.mapOfGCI0.put(classId, new HashSet<GCI0Axiom>());
		}
		this.mapOfGCI0.get(classId).add(axiom);
	}

	private void addGCI1Axiom(int classId, GCI1Axiom axiom) {
		if (this.mapOfGCI1.get(classId) == null) {
			this.mapOfGCI1.put(classId, new HashSet<GCI1Axiom>());
		}
		this.mapOfGCI1.get(classId).add(axiom);
	}

	private void addGCI2Axiom(int classId, GCI2Axiom axiom) {
		if (this.mapOfGCI2.get(classId) == null) {
			this.mapOfGCI2.put(classId, new HashSet<GCI2Axiom>());
		}
		this.mapOfGCI2.get(classId).add(axiom);
	}

	private void addGCI3Axiom(Map<Integer, Set<GCI3Axiom>> internalMap,
			GCI3Axiom axiom, int key) {
		if (internalMap.get(key) == null) {
			internalMap.put(key, new HashSet<GCI3Axiom>());
		}
		internalMap.get(key).add(axiom);
	}

	private void addNominalAxiom(int individualId, NominalAxiom axiom) {
		if (this.mapOfNominalAxiom.get(individualId) == null) {
			this.mapOfNominalAxiom.put(individualId,
					new HashSet<NominalAxiom>());
		}
		this.mapOfNominalAxiom.get(individualId).add(axiom);
	}

	@Override
	public void addObjectProperty(int objectProperty) {
		this.setOfAllObjectProperties.add(objectProperty);
	}

	private void addRangeAxiom(int propertyId, RangeAxiom axiom) {
		if (this.mapOfRangeAxiom.get(propertyId) == null) {
			this.mapOfRangeAxiom.put(propertyId, new HashSet<RangeAxiom>());
		}
		this.mapOfRangeAxiom.get(propertyId).add(axiom);
	}

	private void addTo(int property, RI3Axiom axiom,
			Map<Integer, Set<RI3Axiom>> map) {
		Set<RI3Axiom> axiomSet = map.get(property);
		if (axiomSet == null) {
			axiomSet = new HashSet<RI3Axiom>();
			map.put(property, axiomSet);
		}
		axiomSet.add(axiom);
	}

	@Override
	public void clear() {
		this.normalizedAxiomSet.clear();
		this.setOfClasses.clear();
		this.setOfAllObjectProperties.clear();
		this.mapOfGCI0.clear();
		this.mapOfGCI1.clear();
		this.mapOfGCI2.clear();
		this.mapOfGCI3A.clear();
		this.mapOfGCI3r.clear();
		this.mapOfRI2r.clear();
		this.mapOfRI2s.clear();
		this.mapOfRI3ByLeft.clear();
		this.mapOfRI3ByRight.clear();
		this.mapOfNominalAxiom.clear();
		this.mapOfRangeAxiom.clear();
		this.setOfTransitiveObjectProperties.clear();
		this.setOfFunctionalObjectProperties.clear();
		this.setOfReflexiveObjectProperties.clear();
	}

	@Override
	public Set<NormalizedIntegerAxiom> getAxiomSet() {
		return Collections.unmodifiableSet(this.normalizedAxiomSet);
	}

	@Override
	public Set<Integer> getClassSet() {
		return Collections.unmodifiableSet(this.setOfClasses);
	}

	@Override
	public Set<Integer> getFunctionalObjectProperties() {
		return Collections
				.unmodifiableSet(this.setOfFunctionalObjectProperties);
	}

	@Override
	public Set<GCI0Axiom> getGCI0Axioms(int classId) {
		Set<GCI0Axiom> ret = this.mapOfGCI0.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI1Axiom> getGCI1Axioms(int classId) {
		Set<GCI1Axiom> ret = this.mapOfGCI1.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI2Axiom> getGCI2Axioms(int classId) {
		Set<GCI2Axiom> ret = this.mapOfGCI2.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI3Axiom> getGCI3AAxioms(int classId) {
		Set<GCI3Axiom> ret = this.mapOfGCI3A.get(classId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI3Axiom> getGCI3rAAxioms(int propertyId,
			int leftClassId) {
		Set<GCI3Axiom> ret = new HashSet<GCI3Axiom>();
		Set<GCI3Axiom> set1 = this.mapOfGCI3A.get(leftClassId);
		if (set1 != null) {
			ret.addAll(set1);
		}
		Set<GCI3Axiom> set2 = this.mapOfGCI3r.get(propertyId);
		if (set2 == null) {
			ret = Collections.emptySet();
		} else {
			ret.retainAll(set2);

		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<GCI3Axiom> getGCI3rAxioms(int objectPropertyId) {
		Set<GCI3Axiom> ret = this.mapOfGCI3r.get(objectPropertyId);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<Integer> getObjectPropertySet() {
		return Collections.unmodifiableSet(this.setOfAllObjectProperties);
	}

	@Override
	public Set<Integer> getReflexiveObjectProperties() {
		return Collections.unmodifiableSet(this.setOfReflexiveObjectProperties);
	}

	@Override
	public Set<RI2Axiom> getRI2rAxioms(int elem) {
		Set<RI2Axiom> ret = this.mapOfRI2r.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<RI2Axiom> getRI2sAxioms(int elem) {
		Set<RI2Axiom> ret = this.mapOfRI2s.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByLeft(int elem) {
		Set<RI3Axiom> ret = this.mapOfRI3ByLeft.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<RI3Axiom> getRI3AxiomsByRight(int elem) {
		Set<RI3Axiom> ret = this.mapOfRI3ByRight.get(elem);
		if (ret == null) {
			ret = Collections.emptySet();
		}
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<Integer> getTransitiveObjectProperties() {
		return Collections
				.unmodifiableSet(this.setOfTransitiveObjectProperties);
	}

	@Override
	public void load(Set<NormalizedIntegerAxiom> axiomSet) {
		if (axiomSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.normalizedAxiomSet.addAll(axiomSet);
		for (NormalizedIntegerAxiom axiom : axiomSet) {
			axiom.accept(this);
			addEntities(axiom);
		}
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("[");
		sbuf.append("map of GCI0 =" + this.mapOfGCI0.toString());
		sbuf.append("map of GCI1 =" + this.mapOfGCI1.toString());
		sbuf.append("map of GCI2 =" + this.mapOfGCI2.toString());
		sbuf.append("map of GCI3 =" + this.mapOfGCI3r.toString());
		sbuf.append("map of RI2 =" + this.mapOfRI2r.toString());
		sbuf.append("set of functional ="
				+ this.setOfFunctionalObjectProperties.toString());
		sbuf.append("set of reflexive ="
				+ this.setOfReflexiveObjectProperties.toString());
		sbuf.append("set of transitive ="
				+ this.setOfTransitiveObjectProperties.toString());
		sbuf.append("]");
		return sbuf.toString();
	}

	@Override
	public Boolean visit(FunctObjectPropAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.setOfFunctionalObjectProperties.add(axiom.getProperty());
		return true;
	}

	@Override
	public Boolean visit(GCI0Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI0Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	@Override
	public Boolean visit(GCI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		List<Integer> operandSet = axiom.getOperands();
		for (Integer currentOperand : operandSet) {
			Set<Integer> currentSet = new HashSet<Integer>();
			currentSet.addAll(operandSet);
			currentSet.remove(currentOperand);
			addGCI1Axiom(currentOperand, axiom);
		}
		return true;
	}

	@Override
	public Boolean visit(GCI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI2Axiom(axiom.getSubClass(), axiom);
		return true;
	}

	@Override
	public Boolean visit(GCI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addGCI3Axiom(this.mapOfGCI3A, axiom, axiom.getClassInSubClass());
		addGCI3Axiom(this.mapOfGCI3r, axiom, axiom.getPropertyInSubClass());
		return true;
	}

	@Override
	public Boolean visit(NominalAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addNominalAxiom(axiom.getIndividual(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RangeAxiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		addRangeAxiom(axiom.getProperty(), axiom);
		return true;
	}

	@Override
	public Boolean visit(RI1Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.setOfReflexiveObjectProperties.add(axiom.getSuperProperty());
		return true;
	}

	@Override
	public Boolean visit(RI2Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer subProperty = axiom.getSubProperty();
		if (this.mapOfRI2r.get(subProperty) == null) {
			this.mapOfRI2r.put(subProperty, new HashSet<RI2Axiom>());
		}
		this.mapOfRI2r.get(subProperty).add(axiom);

		Integer superProperty = axiom.getSuperProperty();
		if (this.mapOfRI2s.get(superProperty) == null) {
			this.mapOfRI2s.put(superProperty, new HashSet<RI2Axiom>());
		}
		this.mapOfRI2s.get(superProperty).add(axiom);

		return true;
	}

	@Override
	public Boolean visit(RI3Axiom axiom) {
		if (axiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Integer left = axiom.getLeftSubProperty();
		Integer right = axiom.getRightSubProperty();
		addTo(left, axiom, this.mapOfRI3ByLeft);
		addTo(right, axiom, this.mapOfRI3ByRight);
		if (left.equals(axiom.getSuperProperty())
				&& right.equals(axiom.getSuperProperty())) {
			this.setOfTransitiveObjectProperties.add(left);
		}
		return true;
	}

}
