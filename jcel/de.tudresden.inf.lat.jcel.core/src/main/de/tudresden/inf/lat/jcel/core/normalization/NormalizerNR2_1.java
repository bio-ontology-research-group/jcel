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
import java.util.List;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.complex.IntegerSubPropertyChainOfAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI1Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI2Axiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * Applies the following rule:
 * 
 * <ul>
 * <li>NR 2-1 : r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k</sub> &#8849; s
 * &#8605; r<sub>1</sub> &#8728; &hellip; &#8728; r<sub>k-1</sub> &#8849; u, u
 * &#8728; r<sub>k</sub> &#8849; s</li>
 * </ul>
 * 
 * This rule was slightly modified to return only normalized axioms.
 * 
 * @author Julian Mendez
 */
class NormalizerNR2_1 implements NormalizationRule {

	public static Set<IntegerAxiom> apply(
			IntegerSubPropertyChainOfAxiom propertyAxiom,
			IdGenerator nameGenerator) {
		if (propertyAxiom == null) {
			throw new IllegalArgumentException("Null argument.");
		}
		if (nameGenerator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<IntegerAxiom> ret = new HashSet<IntegerAxiom>();
		List<Integer> propertyList = propertyAxiom.getPropertyChain();
		Integer superProperty = propertyAxiom.getSuperProperty();
		while (propertyList.size() > 2) {
			int lastPos = propertyList.size() - 1;
			Integer lastPropertyName = propertyList.get(lastPos);
			Integer newPropertyName = nameGenerator.createNewPropertyId();
			ret.add(new RI3Axiom(newPropertyName, lastPropertyName,
					superProperty));
			propertyList = propertyList.subList(0, lastPos);
			superProperty = newPropertyName;
		}
		if (propertyList.size() == 2) {
			Iterator<Integer> it = propertyList.iterator();
			Integer first = it.next();
			Integer second = it.next();
			ret.add(new RI3Axiom(first, second, superProperty));
		} else if (propertyList.size() == 1) {
			ret
					.add(new RI2Axiom(propertyList.iterator().next(),
							superProperty));
		} else if (propertyList.size() == 0) {
			ret.add(new RI1Axiom(superProperty));
		}
		return ret;
	}

	public NormalizerNR2_1() {
	}
}