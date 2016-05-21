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
package org.estatio.fixture.financial;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.estatio.dom.financial.FinancialAccount;
import org.estatio.dom.financial.FinancialAccountTransaction;
import org.estatio.dom.financial.FinancialAccountTransactions;
import org.estatio.dom.financial.FinancialAccounts;
import org.estatio.dom.party.Parties;
import org.estatio.dom.party.Party;
import org.estatio.fixture.EstatioFixtureScript;

public abstract class FinancialAccountTransactionAbstract extends EstatioFixtureScript {

    protected FinancialAccountTransactionAbstract(String friendlyName, String localName) {
        super(friendlyName, localName);
    }

    protected FinancialAccountTransaction createFinancialAccountTransaction(String partyStr, LocalDate date, BigDecimal amount, ExecutionContext executionContext) {
        Party party = parties.findPartyByReference(partyStr);
        FinancialAccount financialAccount = financialAccounts.findAccountsByOwner(party).get(0);

        FinancialAccountTransaction financialAccountTransaction = financialAccountTransactions.newTransaction(
                financialAccount,
                date,
                "Fixture transaction",
                amount);
        executionContext.addResult(this, financialAccountTransaction);

        return financialAccountTransaction;
    }

    // //////////////////////////////////////

    @Inject
    private Parties parties;

    @Inject
    private FinancialAccounts financialAccounts;

    @Inject
    private FinancialAccountTransactions financialAccountTransactions;

}
