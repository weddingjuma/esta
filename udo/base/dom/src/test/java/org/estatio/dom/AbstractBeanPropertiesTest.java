/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
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
package org.estatio.dom;

import org.jmock.auto.Mock;
import org.junit.Rule;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.estatio.dom.PojoTester.FixtureDatumFactory;

public abstract class AbstractBeanPropertiesTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    protected DomainObjectContainer mockContainer;

    protected PojoTester newPojoTester() {
        final PojoTester pojoTester = PojoTester.relaxed()
			.withFixture(FixtureDatumFactoriesForJoda.dates())
            .withFixture(DomainObjectContainer.class, mockContainer)
            ;
        return pojoTester;
    }

    protected static <T> FixtureDatumFactory<T> pojos(Class<T> compileTimeType) {
        return FixtureDatumFactoriesForAnyPojo.pojos(compileTimeType, compileTimeType);
    }

    protected static <T> FixtureDatumFactory<T> pojos(Class<T> compileTimeType, Class<? extends T> runtimeType) {
        return FixtureDatumFactoriesForAnyPojo.pojos(compileTimeType, runtimeType);
    }

}
