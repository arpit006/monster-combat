/*
 * Copyright © 2020 IBM Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package dev.ebullient.dnd.beastiary.compendium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.ebullient.dnd.mechanics.Dice;

public class Multiattack {

    List<String> combinations;

    public List<String> getCombinations() {
        return combinations;
    }

    public void setCombinations(List<String> combinations) {
        if (combinations.isEmpty()) {
            throw new IllegalArgumentException("Multiattack can not have an empty list of attack combinations");
        }
        this.combinations = combinations;
    }

    @JsonIgnore
    public String randomCombination() {
        if (combinations.size() == 1) {
            return combinations.get(0);
        }
        return combinations.get(Dice.range(combinations.size()));
    }
}