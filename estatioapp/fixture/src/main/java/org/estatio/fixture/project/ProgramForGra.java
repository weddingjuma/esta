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
package org.estatio.fixture.project;

import org.estatio.dom.party.Party;
import org.estatio.fixture.party.OrganisationForMediaXNl;
import org.estatio.fixture.party.PersonForJohnDoeNl;
import org.estatio.fixture.party.PersonForLinusTorvaldsNl;

public class ProgramForGra extends ProgramAbstract {

    public static final String PROGRAM_REFERENCE = "GRA_P1";
    public static final String AT_PATH_COUNTRY = "/";

    @Override
    protected void execute(ExecutionContext executionContext) {

        // prereqs
        if (isExecutePrereqs()) {
            executionContext.executeChild(this, new OrganisationForMediaXNl());
            executionContext.executeChild(this, new PersonForJohnDoeNl());
            executionContext.executeChild(this, new PersonForLinusTorvaldsNl());
        }

        // exec
        Party owner = parties.findPartyByReference(PersonForLinusTorvaldsNl.REF);
        Party manager = parties.findPartyByReference(PersonForJohnDoeNl.REF);

        createProgram(
                AT_PATH_COUNTRY, PROGRAM_REFERENCE, "Program 1", "Increase overall profit", owner, manager,
                executionContext);
    }

}
