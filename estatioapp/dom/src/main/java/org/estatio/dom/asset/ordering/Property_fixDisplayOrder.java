/*
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.estatio.dom.asset.ordering;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.asset.Property;

@Mixin
public class Property_fixDisplayOrder {

    private final Property property;

    public Property_fixDisplayOrder(Property property) {
        this.property = property;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(
            named = "Fix",
            cssClassFa = "sort-alpha-asc"
    )
    @MemberOrder(name = "displayOrder", sequence = "1")
    public PropertyOrderingViewModel $$() {
        return container.injectServicesInto(new PropertyOrderingViewModel(property));
    }

    @Inject
    DomainObjectContainer container;

}
