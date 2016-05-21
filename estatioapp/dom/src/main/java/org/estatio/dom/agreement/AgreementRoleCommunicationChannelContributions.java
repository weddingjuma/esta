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
package org.estatio.dom.agreement;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.UdoDomainRepositoryAndFactory;
import org.estatio.dom.communicationchannel.CommunicationChannel;

@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY
)
public class AgreementRoleCommunicationChannelContributions extends UdoDomainRepositoryAndFactory<AgreementRoleCommunicationChannel> {

    public AgreementRoleCommunicationChannelContributions() {
        super(AgreementRoleCommunicationChannelContributions.class, AgreementRoleCommunicationChannel.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    @CollectionLayout(render = RenderType.LAZILY)
    public List<AgreementRoleCommunicationChannel> agreements(
            final CommunicationChannel communicationChannel) {
        return agreementRoleCommunicationChannelRepository.findByCommunicationChannel(communicationChannel);
    }


    @javax.inject.Inject
    private AgreementRoleCommunicationChannelRepository agreementRoleCommunicationChannelRepository;
}
