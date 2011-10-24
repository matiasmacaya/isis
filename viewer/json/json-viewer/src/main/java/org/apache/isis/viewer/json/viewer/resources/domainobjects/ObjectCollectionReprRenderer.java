/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.viewer.json.viewer.resources.domainobjects;

import java.util.List;
import java.util.Map;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionFacet;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionFacetUtils;
import org.apache.isis.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.viewer.json.applib.JsonRepresentation;
import org.apache.isis.viewer.json.applib.RepresentationType;
import org.apache.isis.viewer.json.viewer.ResourceContext;
import org.apache.isis.viewer.json.viewer.representations.LinkFollower;
import org.apache.isis.viewer.json.viewer.representations.LinkBuilder;
import org.apache.isis.viewer.json.viewer.representations.Rel;
import org.apache.isis.viewer.json.viewer.representations.RendererFactory;
import org.apache.isis.viewer.json.viewer.representations.RendererFactoryRegistry;
import org.apache.isis.viewer.json.viewer.representations.ReprRenderer;
import org.apache.isis.viewer.json.viewer.representations.ReprRendererFactoryAbstract;
import org.apache.isis.viewer.json.viewer.resources.domaintypes.DomainTypeReprRenderer;
import org.apache.isis.viewer.json.viewer.resources.domaintypes.TypeCollectionReprRenderer;

import com.google.common.collect.Lists;

public class ObjectCollectionReprRenderer extends AbstractObjectMemberReprRenderer<ObjectCollectionReprRenderer, OneToManyAssociation> {

    public static class Factory extends ReprRendererFactoryAbstract {

        public Factory() {
            super(RepresentationType.OBJECT_COLLECTION);
        }

        @Override
        public ReprRenderer<?,?> newRenderer(ResourceContext resourceContext, LinkFollower linkFollower, JsonRepresentation representation) {
            return new ObjectCollectionReprRenderer(resourceContext, linkFollower, getRepresentationType(), representation);
        }
    }

    private ObjectCollectionReprRenderer(ResourceContext resourceContext, LinkFollower linkFollower, RepresentationType representationType, JsonRepresentation representation) {
        super(resourceContext, linkFollower, representationType, representation);
    }
    
    public JsonRepresentation render() {
        // id and memberType are put eagerly
        
        putDisabledReasonIfDisabled();
        
        JsonRepresentation extensions = getExtensions();
        putExtensionsIsisProprietary(extensions);
        
        JsonRepresentation links = getLinks();
        addLinksFormalDomainModel(links, resourceContext);
        addLinksIsisProprietary(links, resourceContext);

        return representation;
    }



    /////////////////////////////////////////////////////
    // details link
    /////////////////////////////////////////////////////

    /**
     * Mandatory hook method to support x-ro-follow-links
     */
    @Override
    protected void followDetailsLink(JsonRepresentation detailsLink) {
        RendererFactory factory = RendererFactoryRegistry.instance.find(RepresentationType.OBJECT_COLLECTION);
        final ObjectCollectionReprRenderer renderer = 
                (ObjectCollectionReprRenderer) factory.newRenderer(getResourceContext(), getLinkFollower(), JsonRepresentation.newMap());
        renderer.with(new ObjectAndCollection(objectAdapter, objectMember));
        detailsLink.mapPut("value", renderer.render());
    }

    /////////////////////////////////////////////////////
    // mutators
    /////////////////////////////////////////////////////

    @Override
    public ObjectCollectionReprRenderer withMutatorsIfEnabled() {
        if(usability().isVetoed()) {
            return cast(this);
        }
        Map<String, MutatorSpec> mutators = memberType.getMutators();
        for(String mutator: mutators.keySet()) {
            MutatorSpec mutatorSpec = mutators.get(mutator);
            if(hasMemberFacet(mutatorSpec.mutatorFacetType)) {
                
                JsonRepresentation arguments = mutatorArgs(mutatorSpec);
                JsonRepresentation detailsLink = 
                        linkToBuilder.linkToMember(mutatorSpec.rel, memberType, objectMember, mutatorSpec.suffix)
                        .withHttpMethod(mutatorSpec.httpMethod)
                        .withArguments(arguments)
                        .build();
                representation.mapPut(mutator, detailsLink);
            }
        }
        return cast(this);
    }


    protected JsonRepresentation mutatorArgs(MutatorSpec mutatorSpec) {
        final JsonRepresentation repr = JsonRepresentation.newMap();
        if(mutatorSpec.arguments.isNone()) {
            return repr;
        }
        if(mutatorSpec.arguments.isOne()) {
            JsonRepresentation argValues = JsonRepresentation.newArray(1);
            return argValues;
        }
        throw new UnsupportedOperationException("should be overridden if bodyArgs is not 0 or 1");
    }
    

    @Override
    protected Object valueRep() {
        ObjectAdapter valueAdapter = objectMember.get(objectAdapter);
        if(valueAdapter == null) {
            return null;
        }
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(valueAdapter);
        List<JsonRepresentation> list = Lists.newArrayList();
        for (final ObjectAdapter elementAdapter : facet.iterable(valueAdapter)) {

            LinkBuilder newBuilder = DomainObjectReprRenderer.newLinkToBuilder(resourceContext, Rel.OBJECT, elementAdapter);

			list.add(newBuilder.build());
        }
        
        return list;
    }

    /////////////////////////////////////////////////////
    // extensions and links
    /////////////////////////////////////////////////////
    
    private void putExtensionsIsisProprietary(JsonRepresentation extensions) {
        final CollectionSemantics semantics = CollectionSemantics.determine(resourceContext, objectMember);
        extensions.mapPut("collectionSemantics", semantics.name().toLowerCase());
    }

    private void addLinksFormalDomainModel(JsonRepresentation links, ResourceContext resourceContext) {
        links.arrayAdd(TypeCollectionReprRenderer.newLinkToBuilder(resourceContext, Rel.DESCRIBEDBY, objectAdapter.getSpecification(), objectMember).build());
    }

    private void addLinksIsisProprietary(JsonRepresentation links, ResourceContext resourceContext) {
    }



}