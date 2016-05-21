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

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import org.junit.Test;
import org.reflections.Reflections;


/**
 * Automatically tests all domain objects implementing {@link WithNameComparable}.
 * 
 * <p>
 * Any that cannot be instantiated are skipped; manually test using
 * {@link ComparableByNameContractTester}.
 */
public abstract class ComparableByNameContractTestAbstract_compareTo {

    private final String packagePrefix;
    private Map<Class<?>, Class<?>> noninstantiableSubstitutes;

    protected ComparableByNameContractTestAbstract_compareTo(String packagePrefix, 
            ImmutableMap<Class<?>, Class<?>> noninstantiableSubstitutes) {
        this.packagePrefix = packagePrefix;
        this.noninstantiableSubstitutes = noninstantiableSubstitutes;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void searchAndTest() {
        Reflections reflections = new Reflections(packagePrefix);
        
        Set<Class<? extends WithNameComparable>> subtypes = 
                reflections.getSubTypesOf(WithNameComparable.class);
        for (Class<? extends WithNameComparable> subtype : subtypes) {
            if(subtype.isInterface() || subtype.isAnonymousClass() || subtype.isLocalClass() || subtype.isMemberClass()) {
                // skip (probably a testing class)
                continue;
            }
            subtype = instantiable(subtype);
            test(subtype);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Class<? extends WithNameComparable> instantiable(Class<? extends WithNameComparable> cls) {
        final Class<?> substitute = noninstantiableSubstitutes.get(cls);
        return (Class<? extends WithNameComparable>) (substitute!=null?substitute:cls);
    }

    private <T extends WithNameComparable<T>> void test(Class<T> cls) {
        new ComparableByNameContractTester<>(cls).test();
    }

}
