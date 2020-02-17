/*
 * Copyright © 2019,2020 IBM Corp. All rights reserved.
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
package dev.ebullient.dnd;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.ebullient.dnd.beastiary.Beastiary;
import dev.ebullient.dnd.combat.Encounter;
import dev.ebullient.dnd.combat.RoundResult;
import dev.ebullient.dnd.combat.TargetSelector;
import dev.ebullient.dnd.mechanics.Dice;
import io.micrometer.core.annotation.Timed;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/combat")
public class CombatController {
    static final Logger logger = LoggerFactory.getLogger(CombatController.class);

    final Beastiary beastiary;
    final CombatMetrics metrics;

    public CombatController(Beastiary beastiary, CombatMetrics metrics) {
        this.beastiary = beastiary;
        this.metrics = metrics;
        logger.debug("Controller initialized bestiary={}, metrics={}", this.beastiary, this.metrics);
    }

    @Timed
    @GetMapping(path = "/any", produces = "application/json")
    Publisher<RoundResult> any() {
        return go(Dice.range(5) + 2);
    }

    @Timed
    @GetMapping(path = "/faceoff", produces = "application/json")
    Publisher<RoundResult> faceoff() {
        return go(2);
    }

    @Timed
    @GetMapping(path = "/melee", produces = "application/json")
    Publisher<RoundResult> melee() {
        return go(Dice.range(4) + 3);
    }

    Publisher<RoundResult> go(int howMany) {

        Encounter encounter = beastiary.buildEncounter()
                .setHowMany(howMany)
                .setTargetSelector(pickOne(howMany))
                .build();

        return Flux.push(emitter -> {
            int totalRounds = 0;

            while (!encounter.isFinal()) {
                totalRounds++;
                RoundResult result = encounter.oneRound();
                metrics.endRound(result);

                emitter.next(result);
            }

            emitter.complete();
            metrics.endEncounter(encounter, totalRounds);
        });
    }

    TargetSelector pickOne(int howMany) {
        int which = Dice.range(5);
        switch (which) {
            case 4:
                return TargetSelector.SelectBiggest;
            case 3:
                return TargetSelector.SelectSmallest;
            case 2:
                return TargetSelector.SelectByHighestRelativeHealth;
            case 1:
                return TargetSelector.SelectByLowestRelativeHealth;
            default:
            case 0:
                return TargetSelector.SelectAtRandom;
        }
    }
}