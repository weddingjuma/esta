/*
 *
 *  Copyright 2012-2015 Eurocommercial Properties NV
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
package org.estatio.dom.budgeting.allocation;

import java.math.BigDecimal;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.estatio.dom.EstatioDomainObject;
import org.estatio.dom.apptenancy.WithApplicationTenancyProperty;
import org.estatio.dom.budgeting.budget.Budget;
import org.estatio.dom.budgeting.budgetitem.BudgetItem;
import org.estatio.dom.budgeting.keytable.KeyTable;
import org.estatio.dom.charge.Charge;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE
//       ,schema = "budget"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Queries({
        @Query(
                name = "findByBudgetItem", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.allocation.BudgetItemAllocation " +
                        "WHERE budgetItem == :budgetItem "),
        @Query(
                name = "findByKeyTable", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.allocation.BudgetItemAllocation " +
                        "WHERE keyTable == :keyTable "),
        @Query(
                name = "findByChargeAndBudgetItemAndKeyTable", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.allocation.BudgetItemAllocation " +
                        "WHERE charge == :charge && budgetItem == :budgetItem && keyTable == :keyTable ")
})
@Unique(name = "ScheduleItem_charge_budgetItem_keyTable_UNQ", members = {"charge", "budgetItem", "keyTable"})
@DomainObject()
public class BudgetItemAllocation extends EstatioDomainObject<BudgetItemAllocation> implements WithApplicationTenancyProperty {

    public BudgetItemAllocation() {
        super("budgetItem, keyTable");
    }

    //region > identificatiom
    public String title() {

        return "Allocation of "
                .concat(getBudgetItem().getCharge().getName()
                .concat(" on "))
                .concat(getCharge().getName()
                .concat(" for ")
                .concat(getPercentage().setScale(0).toString()
                .concat("%")));
    }
    //endregion


    @Column(allowsNull = "false", name = "chargeId")
    @Getter @Setter
    private Charge charge;

    @Column(name="keyTableId", allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private KeyTable keyTable;

    @ActionLayout(hidden = Where.EVERYWHERE)
    public BudgetItemAllocation changeKeyTable(final KeyTable keyTable) {
        setKeyTable(keyTable);
        return this;
    }

    public KeyTable default0ChangeKeyTable(final KeyTable keyTable) {
        return getKeyTable();
    }

    public String validateChangeKeyTable(final KeyTable keyTable) {
        if (keyTable.equals(null)) {
            return "KeyTable can't be empty";
        }
        return null;
    }

    // ////////////////////////////////////////

    @Column(allowsNull = "false", name = "budgetItemId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private BudgetItem budgetItem;


    @Column(allowsNull = "false", scale = 6)
    @Getter @Setter
    private BigDecimal percentage;

    @Action(restrictTo = RestrictTo.PROTOTYPING)
    public BudgetItemAllocation updatePercentage(final BigDecimal percentage) {
        setPercentage(percentage.setScale(6, BigDecimal.ROUND_HALF_UP));
        return this;
    }

    public BigDecimal default0UpdatePercentage(final BigDecimal percentage) {
        return getPercentage();
    }

    public String validateUpdatePercentage(final BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(new BigDecimal(100)) > 0) {
            return "percentage should be in range 0 - 100";
        }
        return null;
    }

    // ////////////////////////////////////////

    @Action(restrictTo = RestrictTo.PROTOTYPING, semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public Budget deleteBudgetItemAllocation() {
        removeIfNotAlready(this);
        return this.getBudgetItem().getBudget();
    }

    @Override
    @MemberOrder(sequence = "7")
    @PropertyLayout(hidden = Where.EVERYWHERE)
    public ApplicationTenancy getApplicationTenancy() {
        return getBudgetItem().getBudget().getApplicationTenancy();
    }

}