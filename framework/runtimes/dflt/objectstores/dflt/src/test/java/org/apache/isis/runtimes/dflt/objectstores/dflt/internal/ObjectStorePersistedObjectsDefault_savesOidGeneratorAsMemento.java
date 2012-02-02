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

package org.apache.isis.runtimes.dflt.objectstores.dflt.internal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.runtimes.dflt.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;
import org.apache.isis.runtimes.dflt.runtime.persistence.oidgenerator.simple.SimpleOidGenerator.Memento;

public class ObjectStorePersistedObjectsDefault_savesOidGeneratorAsMemento {

    private ObjectStorePersistedObjectsDefault persistedObjects;

    private final Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private SimpleOidGenerator.Memento mockMemento;

    @Before
    public void setUp() throws Exception {
        persistedObjects = new ObjectStorePersistedObjectsDefault();
        mockMemento = context.mock(SimpleOidGenerator.Memento.class);
    }

    @Test
    public void noOidGeneratorInitially() throws Exception {
        final Memento oidGeneratorMemento = persistedObjects.getOidGeneratorMemento();
        assertThat(oidGeneratorMemento, is(nullValue()));
    }

    @Test
    public void oidGeneratorStoredOnceSaved() throws Exception {
        persistedObjects.saveOidGeneratorMemento(mockMemento);
        final Memento oidGeneratorMemento = persistedObjects.getOidGeneratorMemento();
        assertThat(oidGeneratorMemento, is(mockMemento));
    }

}