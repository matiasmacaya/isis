/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.bdd.common.fixtures.perform;

import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.Disabled;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.Hidden;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.PerformCheckThatAbstract;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.Usable;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.Visible;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.property.Contains;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.property.DoesNotContain;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.property.Empty;
import org.apache.isis.viewer.bdd.common.fixtures.perform.checkthat.property.NotEmpty;

public class CheckProperty extends PerformCheckThatAbstract {

    public CheckProperty(final Perform.Mode mode) {
        super("check property", OnMemberColumn.REQUIRED, mode, new Hidden(), new Visible(), new Disabled(), new Usable(), new Contains(), new DoesNotContain(), new Empty(), new NotEmpty());
    }

}