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

package de.tudresden.inf.lat.jcel.core.saturation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.tudresden.inf.lat.jcel.core.axiom.normalized.NormalizedIntegerAxiom;
import de.tudresden.inf.lat.jcel.core.axiom.normalized.RI3Axiom;
import de.tudresden.inf.lat.jcel.core.datatype.IdGenerator;

/**
 * <ul>
 * <li>SR-3 : r &#8728; s &#8849; t &#8605; s<sup>-</sup> &#8728; r<sup>-</sup>
 * &#8849; t<sup>-</sup></li>
 * </ul>
 * 
 * @author Julian Mendez
 */
public class SR3Rule implements SaturationRule {

	private IdGenerator idGenerator = null;

	/**
	 * Constructs a new SR-3 rule.
	 */
	public SR3Rule(IdGenerator generator) {
		if (generator == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		this.idGenerator = generator;
	}

	@Override
	public Set<NormalizedIntegerAxiom> apply(
			Set<NormalizedIntegerAxiom> originalSet) {
		if (originalSet == null) {
			throw new IllegalArgumentException("Null argument.");
		}

		Set<NormalizedIntegerAxiom> ret = new HashSet<NormalizedIntegerAxiom>();
		ret.addAll(originalSet);
		for (NormalizedIntegerAxiom normalizedAxiom : originalSet) {
			if (normalizedAxiom instanceof RI3Axiom) {
				RI3Axiom axiom = (RI3Axiom) normalizedAxiom;
				Integer invLeftSubProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getLeftSubProperty());
				Integer invRightSubProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getRightSubProperty());
				Integer invSuperProp = getIdGenerator()
						.createOrGetInverseObjectPropertyOf(
								axiom.getSuperProperty());
				RI3Axiom newAxiom = new RI3Axiom(invRightSubProp,
						invLeftSubProp, invSuperProp);
				ret.add(newAxiom);
			}
		}
		return Collections.unmodifiableSet(ret);
	}

	private IdGenerator getIdGenerator() {
		return this.idGenerator;
	}

}