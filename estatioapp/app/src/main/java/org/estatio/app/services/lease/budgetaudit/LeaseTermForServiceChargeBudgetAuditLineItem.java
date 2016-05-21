/*
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
package org.estatio.app.services.lease.budgetaudit;

import java.math.BigDecimal;

import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Paged;
import org.apache.isis.applib.annotation.ViewModel;

import org.estatio.app.EstatioViewModel;
import org.estatio.dom.lease.LeaseTermForServiceCharge;
import org.estatio.dom.utils.TitleBuilder;

@Paged(Integer.MAX_VALUE)
@MemberGroupLayout(columnSpans = { 4, 4, 4, 0 }, left = { "Selected" }, right = { "Next" })
@ViewModel
public class LeaseTermForServiceChargeBudgetAuditLineItem extends EstatioViewModel {

    /**
     * A viewmodel needs a public noarg constructor
     */
    public LeaseTermForServiceChargeBudgetAuditLineItem() {}

    public String title() {
        return TitleBuilder.start()
                .withName(getLeaseTerm())
                .toString();
    }

    public LeaseTermForServiceChargeBudgetAuditLineItem(LeaseTermForServiceCharge leaseTerm) {
        this.leaseTerm = leaseTerm;
        this.auditedValue = leaseTerm.getAuditedValue();
        this.budgetedValue = leaseTerm.getBudgetedValue();
        this.nextLeaseTerm = (LeaseTermForServiceCharge) leaseTerm.getNext();
        if (this.nextLeaseTerm != null) {
            this.nextBudgetedValue = nextLeaseTerm.getBudgetedValue();
        }
    }

    private LeaseTermForServiceCharge leaseTerm;

    @MemberOrder(name = "Selected", sequence = "1")
    public LeaseTermForServiceCharge getLeaseTerm() {
        return leaseTerm;
    }

    public void setLeaseTerm(LeaseTermForServiceCharge leaseTerm) {
        this.leaseTerm = leaseTerm;
    }

    // //////////////////////////////////////

    private BigDecimal budgetedValue;

    @javax.jdo.annotations.Column(scale = 2, allowsNull = "true")
    @Optional
    @MemberOrder(name = "Selected", sequence = "2")
    public BigDecimal getBudgetedValue() {
        return budgetedValue;
    }

    public void setBudgetedValue(final BigDecimal budgetedValue) {
        this.budgetedValue = budgetedValue;
    }

    // //////////////////////////////////////

    private BigDecimal auditedValue;

    @javax.jdo.annotations.Column(scale = 2, allowsNull = "true")
    @Optional
    @MemberOrder(name = "Selected", sequence = "3")
    public BigDecimal getAuditedValue() {
        return auditedValue;
    }

    public void setAuditedValue(final BigDecimal auditedValue) {
        this.auditedValue = auditedValue;
    }

    // //////////////////////////////////////

    private LeaseTermForServiceCharge nextLeaseTerm;

    @MemberOrder(name = "Next", sequence = "1")
    public LeaseTermForServiceCharge getNextLeaseTerm() {
        return nextLeaseTerm;
    }

    public void setNextLeaseTerm(LeaseTermForServiceCharge nextLeaseTerm) {
        this.nextLeaseTerm = nextLeaseTerm;
    }

    // //////////////////////////////////////

    private BigDecimal nextBudgetedValue;

    @Optional
    @MemberOrder(name = "Next", sequence = "2")
    public BigDecimal getNextBudgetedValue() {
        return nextBudgetedValue;
    }

    public void setNextBudgetedValue(final BigDecimal budgetedValue) {
        this.nextBudgetedValue = budgetedValue;
    }

}
