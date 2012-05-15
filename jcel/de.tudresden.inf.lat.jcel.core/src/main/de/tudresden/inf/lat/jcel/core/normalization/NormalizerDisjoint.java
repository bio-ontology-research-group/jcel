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

package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerDisjointClassesAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubClassOfAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClass;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerClassExpression;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDatatype;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;

/**
 * This class models a transformation rule that normalizes an axiom of disjoint
 * classes.
 * 
 * @author Julian Mendez
 */
class NormalizerDisjoint implements NormalizationRule {

	public static Set<IntegerAxiom> apply(
			IntegerDisjointClassesAxiom disjointAxiom) {
		if (disjointAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerClassExpression> classExpressionSet = disjointAxiom
				.getClassExpressions();
		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		for (Iterator<IntegerClassExpression> firstIt = classExpressionSet
				.iterator(); firstIt.hasNext();) {
			IntegerClassExpression firstClassExpression = firstIt.next();
			for (Iterator<IntegerClassExpression> secondIt = classExpressionSet
					.iterator(); secondIt.hasNext();) {
				IntegerClassExpression secondClassExpression = secondIt.next();
				if (!firstClassExpression.equals(secondClassExpression)) {
					Set<IntegerClassExpression> pair = new HashSet<IntegerClassExpression>();
					pair.add(firstClassExpression);
					pair.add(secondClassExpression);
					IntegerObjectIntersectionOf intersection = new IntegerObjectIntersectionOf(
							pair);
					IntegerSubClassOfAxiom subClassAxiom = new IntegerSubClassOfAxiom(
							intersection, new IntegerClass(
									IntegerDatatype.classBottomElement));
					ret.add(subClassAxiom);
				}
			}
		}
		return ret;

	}

	public NormalizerDisjoint() {
	}
}