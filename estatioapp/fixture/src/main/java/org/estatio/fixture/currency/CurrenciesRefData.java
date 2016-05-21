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
package org.estatio.fixture.currency;

import javax.inject.Inject;
import org.estatio.dom.currency.Currencies;
import org.estatio.dom.currency.Currency;
import org.estatio.fixture.EstatioFixtureScript;


public class CurrenciesRefData extends EstatioFixtureScript {

    public static final String EUR = "EUR";
    public static final String SEK = "SEK";
    public static final String GBP = "GBP";
    public static final String USD = "USD";

    @Override
    protected void execute(ExecutionContext executionContext) {
        createCurrency(EUR, "Euro", executionContext);
        createCurrency(SEK, "Swedish krona", executionContext);
        createCurrency(GBP, "Pound sterling", executionContext);
        createCurrency(USD, "US dollar", executionContext);
    }

    private void createCurrency(String reference, String name, ExecutionContext executionContext) {
        final Currency currency = currencies.findOrCreateCurrency(reference, name);
        executionContext.addResult(this, currency.getReference(), currency);
    }

    @Inject
    Currencies currencies;

}
