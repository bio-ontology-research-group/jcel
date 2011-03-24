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
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.IntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.IntegerSubClassAxiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerDescription;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerObjectIntersectionOf;

/**
 * Slightly modified version of NR3-3 . The original rule is:
 * 
 * <ul>
 * <li>NR 3-3 : B &sube; C &cap; D &rarr; &rarr; B &sube; C, B &sube; D</li>
 * </ul>
 * 
 * @author Julian Mendez
 */
class NormalizerNR3_3 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(IntegerSubClassAxiom classAxiom) {
		Set<IntegerAxiom> ret = null;
		IntegerDescription subClass = classAxiom.getSubClass();
		IntegerDescription superClass = classAxiom.getSuperClass();
		if (subClass.isLiteral()
				&& superClass instanceof IntegerObjectIntersectionOf) {
			IntegerObjectIntersectionOf intersection = (IntegerObjectIntersectionOf) superClass;
			ret = new HashSet<IntegerAxiom>();
			Set<IntegerDescription> operands = intersection.getOperands();
			for (IntegerDescription operand : operands) {
				ret.add(new IntegerSubClassAxiom(subClass, operand));
			}
		}
		return ret;
	}

	public NormalizerNR3_3() {
	}
}
