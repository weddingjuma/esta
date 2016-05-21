/*
 * Copyright 2012-2015 Eurocommercial Properties NV
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.estatio.dom.budgeting.keyitem;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.estatio.dom.UdoDomainRepositoryAndFactory;
import org.estatio.dom.asset.Unit;
import org.estatio.dom.budgeting.keytable.KeyTable;

import java.math.BigDecimal;
import java.util.List;

@DomainService(repositoryFor = KeyItem.class, nature = NatureOfService.DOMAIN)
@DomainServiceLayout()
public class KeyItemRepository extends UdoDomainRepositoryAndFactory<KeyItem> {

    public KeyItemRepository() {
        super(KeyItemRepository.class, KeyItem.class);
    }

    // //////////////////////////////////////

    public KeyItem newItem(
            final KeyTable keyTable,
            final Unit unit,
            final BigDecimal sourceValue,
            final BigDecimal keyValue) {
        KeyItem keyItem = newTransientInstance();
        keyItem.setKeyTable(keyTable);
        keyItem.setUnit(unit);
        keyItem.setSourceValue(sourceValue);
        keyItem.setValue(keyValue);
        persistIfNotAlready(keyItem);

        return keyItem;
    }

    public String validateNewItem(
            final KeyTable keyTable,
            final Unit unit,
            final BigDecimal sourceValue,
            final BigDecimal keyValue) {

        if (sourceValue.compareTo(BigDecimal.ZERO) <= 0) {
            return "sourceValue cannot be zero or less than zero";
        }

        if (keyValue.compareTo(BigDecimal.ZERO) < 0) {
            return "keyValue cannot be less than zero";
        }

        if (findByKeyTableAndUnit(keyTable, unit)!=null) {
            return "there is already a key item for this unit";
        }

        return null;
    }

    // //////////////////////////////////////

    @Programmatic
    public KeyItem findByKeyTableAndUnit(KeyTable keyTable, Unit unit){
        return uniqueMatch("findByKeyTableAndUnit", "keyTable", keyTable, "unit", unit);
    }


    // //////////////////////////////////////

    @Programmatic
    public List<KeyItem> allBudgetKeyItems() {
        return allInstances();
    }
}
