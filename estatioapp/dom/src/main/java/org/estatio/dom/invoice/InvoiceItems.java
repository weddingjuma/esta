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
package org.estatio.dom.invoice;

import java.util.List;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.UdoDomainRepositoryAndFactory;
import org.estatio.dom.lease.invoicing.InvoiceItemForLease;

@DomainService(repositoryFor = InvoiceItem.class)
@DomainServiceLayout(
        named = "Invoices",
        menuBar = DomainServiceLayout.MenuBar.PRIMARY,
        menuOrder = "50.5")
public class InvoiceItems extends UdoDomainRepositoryAndFactory<InvoiceItem> {

    public InvoiceItems() {
        super(InvoiceItems.class, InvoiceItem.class);
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @Programmatic
    public InvoiceItem newInvoiceItem(
            final Invoice invoice,
            final @ParameterLayout(named = "Due date") LocalDate dueDate) {
        InvoiceItem invoiceItem = newTransientInstance(InvoiceItemForLease.class);
        invoiceItem.setInvoice(invoice);
        invoiceItem.setDueDate(dueDate);
        invoiceItem.setUuid(java.util.UUID.randomUUID().toString());
        persistIfNotAlready(invoiceItem);
        return invoiceItem;
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.SAFE, restrictTo = RestrictTo.PROTOTYPING)
    @MemberOrder(name = "Invoices", sequence = "99")
    public List<InvoiceItem> allInvoiceItems() {
        return allInstances();
    }

}
