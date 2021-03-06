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

package org.apache.isis.core.runtime.persistence.container;

import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService2;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.metamodel.adapter.oid.Oid;
import org.apache.isis.core.metamodel.adapter.oid.RootOid;
import org.apache.isis.core.metamodel.services.ServicesInjectorSpi;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.core.runtime.system.persistence.PersistenceSession;

/**
 * Helper class that encapsulates the processing performed by domain object
 * containers that are performing a resolve.
 * 
 * <p>
 * Implementation note: rather than inject in its dependencies, we instead look
 * up dependencies from the {@link IsisContext}. This is necessary, for the
 * {@link PersistenceSession} at least, because class enhancers may hold a
 * reference to the factory as part of the generated bytecode. Since the
 * {@link PersistenceSession} could change over the lifetime of the instance,
 * we must always look the {@link PersistenceSession} from the {@link IsisContext}.
 * The same applies to the {@link ServicesInjectorSpi}.
 */
public class DomainObjectContainerResolve {

    public DomainObjectContainerResolve() {
    }

    public Object lookup(final Bookmark bookmark, final BookmarkService2.FieldResetPolicy fieldResetPolicy) {
        RootOid oid = RootOid.create(bookmark);
        final ObjectAdapter adapter = adapterFor(oid);
        if(adapter == null) {
            return null;
        }
        if(fieldResetPolicy == BookmarkService2.FieldResetPolicy.RESET) {
            getPersistenceSession().refreshRootInTransaction(adapter);
        } else {
            getPersistenceSession().loadObjectInTransaction(oid);
        }
        return adapter.getObject();
    }

    public Bookmark bookmarkFor(Object domainObject) {
        final ObjectAdapter adapter = adapterFor(domainObject);
        final Oid oid = adapter.getOid();
        if(oid == null) {
            // values cannot be bookmarked
            return null;
        }
        if(!(oid instanceof RootOid)) {
            // must be root
            return null;
        }
        final RootOid rootOid = (RootOid) oid;
        return rootOid.asBookmark();
    }

    public Bookmark bookmarkFor(final Class<?> cls, final String identifier) {
        final ObjectSpecification objectSpec = getSpecificationLoader().loadSpecification(cls);
        String objectType = objectSpec.getSpecId().asString();
        return new Bookmark(objectType, identifier);
    }

    public void resolve(final Object parent) {
        final ObjectAdapter adapter = adapterFor(parent);
        getPersistenceSession().refreshRootInTransaction(adapter);
    }

    public void resolve(final Object parent, final Object field) {
        if (field == null) {
            resolve(parent);
        }
    }

    private ObjectAdapter adapterFor(final Object object) {
        return getAdapterManager().adapterFor(object);
    }

    private ObjectAdapter adapterFor(final RootOid oid) {
        return getPersistenceSession().adapterFor(oid);
    }

    // /////////////////////////////////////////////////////////////////
    // Dependencies (looked up from context)
    // /////////////////////////////////////////////////////////////////

    protected PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }

    protected AdapterManager getAdapterManager() {
        return getPersistenceSession();
    }

    protected SpecificationLoaderSpi getSpecificationLoader() {
        return IsisContext.getSpecificationLoader();
    }



}
