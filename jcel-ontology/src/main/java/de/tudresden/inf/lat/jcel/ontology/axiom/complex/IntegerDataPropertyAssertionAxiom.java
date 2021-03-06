/*
 *
 * Copyright (C) 2009-2017 Julian Mendez
 *
 *
 * This file is part of jcel.
 *
 *
 * The contents of this file are subject to the GNU Lesser General Public License
 * version 3
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Alternatively, the contents of this file may be used under the terms
 * of the Apache License, Version 2.0, in which case the
 * provisions of the Apache License, Version 2.0 are applicable instead of those
 * above.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.IntegerAnnotation;

/**
 * This class models an assertion that relates a data property and a pair of
 * individuals. <br>
 * This is: r(a,b)
 * 
 * @author Julian Mendez
 */
public class IntegerDataPropertyAssertionAxiom implements ComplexIntegerAxiom {

	private final Set<Integer> individualsInSignature;
	private final int object;
	private final int property;
	private final int subject;
	private final Set<IntegerAnnotation> annotations;
	private final int hashCode;

	/**
	 * Constructs a new data property assertion axiom.
	 * 
	 * @param objectProp
	 *            data property
	 * @param subjectInd
	 *            source individual
	 * @param objectInd
	 *            target individual
	 * @param annotations
	 *            annotations
	 */
	IntegerDataPropertyAssertionAxiom(int objectProp, int subjectInd, int objectInd,
			Set<IntegerAnnotation> annotations) {
		Objects.requireNonNull(annotations);
		this.property = objectProp;
		this.subject = subjectInd;
		this.object = objectInd;

		Set<Integer> individualsInSignature = new HashSet<>();
		individualsInSignature.add(this.subject);
		individualsInSignature.add(this.object);
		this.individualsInSignature = Collections.unmodifiableSet(individualsInSignature);
		this.annotations = annotations;
		this.hashCode = this.property
				+ 0x1F * (this.subject + 0x1F * (this.object + 0x1F * this.annotations.hashCode()));
	}

	@Override
	public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = (this == obj);
		if (!ret && (obj instanceof IntegerDataPropertyAssertionAxiom)) {
			IntegerDataPropertyAssertionAxiom other = (IntegerDataPropertyAssertionAxiom) obj;
			ret = getProperty().equals(other.getProperty()) && getSubject().equals(other.getSubject())
					&& getObject().equals(other.getObject()) && getAnnotations().equals(other.getAnnotations());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		return Collections.emptySet();
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
		return Collections.unmodifiableSet(this.individualsInSignature);
	}

	/**
	 * Returns the individual representing the object.
	 * 
	 * @return the individual representing the object
	 */
	public Integer getObject() {
		return this.object;
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		return Collections.singleton(getProperty());
	}

	/**
	 * Returns the data property in the axiom.
	 * 
	 * @return the data property in the axiom
	 */
	public Integer getProperty() {
		return this.property;
	}

	/**
	 * Returns the individual representing the subject.
	 * 
	 * @return the individual representing the subject
	 */
	public Integer getSubject() {
		return this.subject;
	}

	@Override
	public Set<IntegerAnnotation> getAnnotations() {
		return Collections.unmodifiableSet(this.annotations);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(ComplexIntegerAxiomConstant.DataPropertyAssertion);
		sbuf.append(ComplexIntegerAxiomConstant.LEFT_PAR);
		sbuf.append(getProperty().toString());
		sbuf.append(ComplexIntegerAxiomConstant.SP);
		sbuf.append(getSubject().toString());
		sbuf.append(ComplexIntegerAxiomConstant.SP);
		sbuf.append(getObject().toString());
		sbuf.append(ComplexIntegerAxiomConstant.RIGHT_PAR);
		return sbuf.toString();
	}

}
