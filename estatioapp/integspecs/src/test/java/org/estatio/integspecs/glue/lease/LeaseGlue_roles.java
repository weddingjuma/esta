/*
 *  Copyright 2012-2014 Eurocommercial Properties NV
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
package org.estatio.integspecs.glue.lease;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import com.google.common.collect.Lists;
import com.googlecode.totallylazy.Strings;

import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;

import org.joda.time.LocalDate;

import org.apache.isis.core.specsupport.specs.CukeGlueAbstract;
import org.apache.isis.core.specsupport.specs.V;

import org.estatio.dom.agreement.AgreementRole;
import org.estatio.dom.agreement.AgreementRoleType;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.party.Party;
import org.estatio.integspecs.spectransformers.EIO;
import org.estatio.integspecs.spectransformers.EMO;

public class LeaseGlue_roles extends CukeGlueAbstract {

    public static class AgreementRoleDesc {
        @XStreamConverter(EIO.AgreementRoleType.class) private AgreementRoleType type;
        @XStreamConverter(V.LyyyyMMdd.class) private LocalDate startDate;
        @XStreamConverter(V.LyyyyMMdd.class) private LocalDate endDate;
        @XStreamConverter(EMO.Lease.class) private Lease agreement;
        @XStreamConverter(EMO.Party.class) private Party party;
        private String indicated;
    }
    
    
    
    // //////////////////////////////////////

    @Given(".*lease has no.* roles$")
    public void given_lease_has_no_roles() throws Throwable {
        Lease lease = getVar("lease", null, Lease.class);
        assertThat(lease.getRoles().size(), equalTo(0));
    }

    @Given("^the lease.* roles collection contains:$")
    public void given_the_lease_s_roles_collection_contains(final List<AgreementRoleDesc> listOfActuals) throws Throwable {
        final Lease lease = getVar("lease", null, Lease.class);
        assertThat(lease.getRoles().isEmpty(), is(true));
        for (AgreementRoleDesc ard : listOfActuals) {
            final AgreementRole ar = lease.createRole(ard.type, ard.party, ard.startDate, ard.endDate);
            if(!Strings.isEmpty(ard.indicated)) {
                putVar("agreementRole", "indicated", ar);
            }
        }
    }

    @When("^.* add.* agreement role.*type \"([^\"]*)\".* start date \"([^\"]*)\".* end date \"([^\"]*)\".* party \"([^\"]*)\"$")
    public void when_add_agreement_role_with_type_with_start_date_and_end_date(
            @Transform(EIO.AgreementRoleType.class) final AgreementRoleType type, 
            @Transform(V.LyyyyMMdd.class) final LocalDate startDate, 
            @Transform(V.LyyyyMMdd.class) final LocalDate endDate,
            @Transform(EMO.Party.class) final Party party) throws Throwable {
      
        nextTransaction();
        
        final Lease lease = getVar("lease", null, Lease.class);

        try {
            wrap(lease).newRole(type, party, startDate, endDate);
        } catch(Exception ex) {
            putVar("exception", "exception", ex);
        }
    }



    // //////////////////////////////////////

    @Then("^.*lease's roles collection should contain:$")
    public void then_leases_roles_collection_should_contain(
            final List<AgreementRoleDesc> listOfExpecteds) throws Throwable {
        
        nextTransaction();

        final Lease lease = getVar("lease", null, Lease.class);
        
        final SortedSet<AgreementRole> roles = lease.getRoles();
        final ArrayList<AgreementRole> rolesList = Lists.newArrayList(roles);
        assertTableEquals(listOfExpecteds, rolesList);
    }


}
