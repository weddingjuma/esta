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
package org.estatio.dom.budgeting.budgetcalculation;

import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.estatio.dom.EstatioDomainObject;
import org.estatio.dom.apptenancy.WithApplicationTenancyProperty;
import org.estatio.dom.lease.LeaseTermForServiceCharge;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import javax.jdo.annotations.*;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
//      ,schema = "budget"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Queries({
        @Query(
                name = "findByLeaseTerm", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculationLink " +
                        "WHERE leaseTerm == :leaseTerm"),
        @Query(
                name = "findByBudgetCalculationAndLeaseTerm", language = "JDOQL",
                value = "SELECT " +
                        "FROM org.estatio.dom.budgeting.budgetcalculation.BudgetCalculationLink " +
                        "WHERE leaseTerm == :leaseTerm " +
                        "&& budgetCalculation == :budgetCalculation")
})
@DomainObject(autoCompleteRepository = BudgetCalculationLinkRepository.class)
public class BudgetCalculationLink extends EstatioDomainObject<BudgetCalculationLink> implements WithApplicationTenancyProperty {

    public BudgetCalculationLink() {
        super("budgetCalculation, leaseTerm");
    }

    @Getter @Setter
    @Column(allowsNull = "false", name="budgetCalculationId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    private BudgetCalculation budgetCalculation;

    @Getter @Setter
    @Column(allowsNull = "false", name="LeaseTermId")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    private LeaseTermForServiceCharge leaseTerm;

    @Override
    @PropertyLayout(hidden = Where.EVERYWHERE)
    public ApplicationTenancy getApplicationTenancy() {
        return getBudgetCalculation().getApplicationTenancy();
    }

    @Programmatic
    public void remove(){
        getContainer().remove(this);
    }

    public String title(){
        return "Budget calculation link";
    }
}

