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
package dev.ebullient.dnd.combat;

import java.util.List;

/**
 * @see dev.ebullient.dnd.client.RoundResult for deserialization from JSON
 */
public interface RoundResult {

    /**
     * @see dev.ebullient.dnd.client.AttackEvent for deserialization from JSON
     */
    public interface Event {

        String getName();

        String getType();

        Combatant getActor();

        Combatant getTarget();

        boolean isCritical();

        boolean isHit();

        boolean isSaved();

        String hitOrMiss();

        int getDamageAmount();

        int getAttackModifier();

        int getDifficultyClass();
    }

    List<? extends Event> getEvents();

    List<? extends Combatant> getSurvivors();

    int getNumCombatants();

    int getSizeDelta();

    int getCrDelta();

    int getNumTypes();

    String getSelector();
}
