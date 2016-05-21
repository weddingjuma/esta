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
package org.estatio.fixture.lease;

public class LeaseItemAndTermsForOxfMiracl005Gb extends LeaseItemAndTermsAbstract {

    @Override
    protected void execute(ExecutionContext fixtureResults) {
        createLeaseTermsForOxfMiracl005(fixtureResults);
    }

    private void createLeaseTermsForOxfMiracl005(ExecutionContext executionContext) {

        executionContext.executeChild(this, new LeaseItemAndLeaseTermForRentOf2ForOxfMiracl005Gb());
        executionContext.executeChild(this, new LeaseItemAndLeaseTermForServiceChargeOf2ForOxfMiracl005Gb());
        executionContext.executeChild(this, new LeaseItemAndLeaseTermForTurnoverRentForOxfMiracl005Gb());
        executionContext.executeChild(this, new LeaseItemAndLeaseTermForDiscountForOxfMiracl005Gb());
        executionContext.executeChild(this, new LeaseItemAndLeaseTermForPercentageForOxfMiracl005Gb());
        executionContext.executeChild(this, new LeaseItemAndLeaseTermForDepositForOxfMiracl005Gb());
    }

}
