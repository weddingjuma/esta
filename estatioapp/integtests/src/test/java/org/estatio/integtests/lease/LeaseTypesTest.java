package org.estatio.integtests.lease;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseType;
import org.estatio.dom.lease.LeaseTypes;
import org.estatio.dom.lease.Leases;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.lease.LeaseForOxfTopModel001Gb;
import org.estatio.fixture.lease.LeaseItemAndTermsForOxfTopModel001;
import org.estatio.integtests.EstatioIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LeaseTypesTest extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                executionContext.executeChild(this, new EstatioBaseLineFixture());

                executionContext.executeChild(this, new LeaseItemAndTermsForOxfTopModel001());
            }
        });
    }

    @Inject
    Leases leases;

    @Inject
    LeaseTypes leaseTypes;

    Lease lease;

    @Before
    public void setUp() throws Exception {
        lease = leases.findLeaseByReference(LeaseForOxfTopModel001Gb.REF);
    }

    public static class FindByReference extends LeaseTypesTest {

        @Test
        public void findByReference() throws Exception {
            LeaseType leaseType = leaseTypes.findByReference(lease.getLeaseType().getReference());
            assertThat(leaseType, is(lease.getLeaseType()));
        }
    }

}
