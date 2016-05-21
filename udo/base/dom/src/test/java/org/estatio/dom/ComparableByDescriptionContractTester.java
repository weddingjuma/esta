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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import org.apache.isis.core.unittestsupport.comparable.ComparableContractTester;


public class ComparableByDescriptionContractTester<T extends WithDescriptionComparable<T>> {

    private final Class<T> cls;
    
    public ComparableByDescriptionContractTester(Class<T> cls) {
        this.cls = cls;
    }

    public void test() {
        System.out.println("ComparableByDescriptionContractTester: " + cls.getName());
        new ComparableContractTester<>(orderedTuples()).test();

        testToString();
    }

    protected void testToString() {
        final String str = "ABC";
        
        final T withDescription = newWithDescription(str);
        String expectedToString = Objects.toStringHelper(withDescription).add("description", "ABC").toString();
        
        assertThat(withDescription.toString(), is(expectedToString));
    }

    @SuppressWarnings("unchecked")
    protected List<List<T>> orderedTuples() {
        return listOf(
                listOf(
                        newWithDescription(null), 
                        newWithDescription("ABC"), 
                        newWithDescription("ABC"), 
                        newWithDescription("DEF")));
    }
    
    private T newWithDescription(String reference) {
        final T wr = newWithDescription();
        wr.setDescription(reference);
        return wr;
    }

    private T newWithDescription() {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> List<E> listOf(E... elements) {
        return Lists.newArrayList(elements);
    }

}
