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

package de.tudresden.inf.lat.jcel.ontology.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.datatype.IntegerClassExpressionWord;

/**
 * This class models an existential restriction with object properties, this is:
 * &exist; r <i>.</i> C , where r is an object property expression and C is a
 * class expression.
 * 
 * @author Julian Mendez
 */
public class IntegerObjectSomeValuesFrom implements IntegerClassExpression {

	private final IntegerClassExpression filler;
	private final int hashCode;
	private final boolean normalized;
	private final IntegerObjectPropertyExpression property;

	/**
	 * Constructs an existential restriction.
	 * 
	 * @param propertyExpression
	 *            property expression
	 * @param classExpression
	 *            class expression
	 */
	protected IntegerObjectSomeValuesFrom(IntegerObjectPropertyExpression propertyExpression,
			IntegerClassExpression classExpression) {
		Objects.requireNonNull(propertyExpression);
		Objects.requireNonNull(classExpression);
		this.property = propertyExpression;
		this.filler = classExpression;
		this.normalized = classExpression.isLiteral();
		this.hashCode = propertyExpression.hashCode() + (31 * classExpression.hashCode());
	}

	@Override
	public <T> T accept(IntegerClassExpressionVisitor<T> visitor) {
		Objects.requireNonNull(visitor);
		return visitor.visit(this);
	}

	@Override
	public boolean containsBottom() {
		return this.filler.containsBottom();
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && (o instanceof IntegerObjectSomeValuesFrom)) {
			IntegerObjectSomeValuesFrom other = (IntegerObjectSomeValuesFrom) o;
			ret = getProperty().equals(other.getProperty()) && getFiller().equals(other.getFiller());
		}
		return ret;
	}

	@Override
	public Set<Integer> getClassesInSignature() {
		Set<Integer> ret = new HashSet<>();
		ret.addAll(getFiller().getClassesInSignature());
		return Collections.unmodifiableSet(ret);
	}

	@Override
	public Set<Integer> getDataPropertiesInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getDatatypesInSignature() {
		return Collections.emptySet();
	}

	/**
	 * Returns the filler.
	 * 
	 * @return the filler
	 */
	public IntegerClassExpression getFiller() {
		return this.filler;
	}

	@Override
	public Set<Integer> getIndividualsInSignature() {
		return Collections.emptySet();
	}

	@Override
	public Set<Integer> getObjectPropertiesInSignature() {
		Set<Integer> ret = new HashSet<>();
		ret.addAll(getFiller().getObjectPropertiesInSignature());
		ret.addAll(getProperty().getObjectPropertiesInSignature());
		return Collections.unmodifiableSet(ret);
	}

	public IntegerObjectPropertyExpression getProperty() {
		return this.property;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean hasOnlyClasses() {
		return this.normalized;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(IntegerClassExpressionWord.ObjectSomeValuesFrom);
		sbuf.append(IntegerClassExpressionWord.LEFT_PAR);
		sbuf.append(getProperty().toString());
		sbuf.append(IntegerClassExpressionWord.SP);
		sbuf.append(getFiller().toString());
		sbuf.append(IntegerClassExpressionWord.RIGHT_PAR);
		return sbuf.toString();
	}

}
