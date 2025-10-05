/*
 * Copyright (c) 2025. LibreFit
 *
 * This file is part of LibreFit
 *
 * LibreFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreFit.  If not, see <https://www.gnu.org/licenses/>.
 *
 * LibreFit is subject to additional terms covering author attribution and
 * trademark usage, as found in the accompanying ADDITIONAL_TERMS.md file.
 */

package org.librefit.util.fuzzySearch

import kotlin.math.round

object FuzzySearch {

    /**
     * This function calculates the similarity of two strings by not requiring that the strings
     * match exactly in alignment or location. Rather than forcing a full-length comparison,
     * the function extracts matching blocks and adjusts the similarity score based on their
     * alignment, addressing the problem of misplaced matches.
     *
     * The decay factor `k` is a number that controls how much the matching score is reduced when
     * the matching part of the string comes later in the longer string. A higher `k` means a larger
     * penalty for matches that start later, while a lower 'k` means a smaller drop.
     *
     *
     * ```
     * partialRatio("pullups","pullups")  // Returns 100
     * partialRatio("pullups","pull ups") // Returns 86
     * partialRatio("pullups","pu ups")   // Returns 67
     *
     * partialRatio("ab crunch machine","ab")      // Returns 100
     * partialRatio("ab crunch machine","crunch")  // Returns 91
     * partialRatio("ab crunch machine","machine") // Returns 78
     * ```
     *
     * This file was imported from [fuzzywuzzy-kotlin](https://github.com/jens-muenker/fuzzywuzzy-kotlin)
     *
     * @param s1 the first string
     * @param s2 the second string
     * @return a similarity score as an integer percentage (0 to 100)
     */
    fun partialRatio(s1: String, s2: String): Int {

        val shorter: String
        val longer: String

        if (s1.length < s2.length) {

            shorter = s1
            longer = s2

        } else {

            shorter = s2
            longer = s1

        }

        val matchingBlocks = DiffUtils.getMatchingBlocks(shorter, longer)

        val scores = ArrayList<Double>()

        // increase to penalize more (default is 1)
        val k = 0.2

        for (mb in matchingBlocks) {

            val dist = mb.dpos - mb.spos

            val longStart = if (dist > 0) dist else 0
            var longEnd = longStart + shorter.length

            if (longEnd > longer.length) longEnd = longer.length

            val longSubstr = longer.substring(longStart, longEnd)

            var ratio = DiffUtils.getRatio(shorter, longSubstr)

            val decay = shorter.length.toDouble() / (shorter.length + k * longStart)
            ratio *= decay

            if (ratio > .995) {
                return 100
            } else {
                scores.add(ratio)
            }
        }

        return round(100 * scores.max()).toInt()

    }
}