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

package de.tudresden.inf.lat.jcel.ontology.axiom.normalized;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerClassExpressionWord;

/**
 * Axiom of the form:
 * <ul>
 * <li>A &#8849; &exist; r <i>.</i> B</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class GCI2Axiom implements NormalizedIntegerAxiom {

	private final Integer classInSuperClass;
	private final Integer propertyInSuperClass;
	private final Integer subClass;

	/**
	 * Constructs a new GCI-2 axiom.
	 * 
	 * @param leftCl
	 *            subclass identifier
	 * @param rightProp
	 *            object property identifier
	 * @param rightCl
	 *            class identifier for the right-hand part
	 */
	public GCI2Axiom(Integer leftCl, Integer rightProp, Integer rightCl) {
		if (leftCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightProp == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (rightCl == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.subClass = leftCl;
		this.propertyInSuperClass = rightProp;
		this.classInSuperClass = rightCl;
	}

	@Override
	public <T> T accept(NormalizedIntegerAxiomVisitor<T> visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof GCI2Axiom) {
			GCI2Axiom other = (GCI2Axiom) o;
			ret = getSubClass().equals(other.getSubClass())
					&& getClassInSuperClass().equals(
							other.getClassInSuperClass())
					&& getPropertyInSuperClass().equals(
							other.getPropertyInSuperClass());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(this.subClass);
		ret.add(this.classInSuperClass);
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Returns the class in the right-hand part of the axiom.
	 * 
	 * @return the class in the right-hand part of the axiom
	 */
	public Integer getClassInSuperClass() {
		return this.classInSuperClass;
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(getPropertyInSuperClass());
		return Collections.unmodifiableSet(ret);
	}

	/**
	 * Returns the object property on the right-hand part of the axiom.
	 * 
	 * @return the object property on the right-hand part of the axiom
	 */
	public Integer getPropertyInSuperClass() {
		return this.propertyInSuperClass;
	}

	/**
	 * Returns the subclass in the axiom.
	 * 
	 * @return the subclass in the axiom
	 */
	public Integer getSubClass() {
		return this.subClass;
	}

	@Override
	public int hashCode() {
		return getSubClass().hashCode() + 31
				* getClassInSuperClass().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(NormalizedIntegerAxiomConstant.GCI2);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getSubClass());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(NormalizedIntegerAxiomConstant.openPar);
		sbuf.append(getPropertyInSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.sp);
		sbuf.append(getClassInSuperClass());
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		sbuf.append(NormalizedIntegerAxiomConstant.closePar);
		return sbuf.toString();
	}

}