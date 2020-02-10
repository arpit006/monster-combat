/*
 * Copyright © 2019 IBM Corp. All rights reserved.
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
package dev.ebullient.dnd.mechanics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Size {
    TINY,
    SMALL,
    MEDIUM,
    LARGE,
    HUGE,
    GARGANTUAN;

    public static final List<Size> allValues = Collections.unmodifiableList(Arrays.asList(values()));

    public static Size getOne() {
        return allValues.get(Dice.range(allValues.size()));
    }
}
