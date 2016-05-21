package org.estatio.canonical.bankmandate.v1;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.canonical.DtoFactoryAbstract;
import org.estatio.dom.DtoMappingHelper;
import org.estatio.dom.bankmandate.BankMandate;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class BankMandateDtoFactory extends DtoFactoryAbstract {

    @Programmatic
    public BankMandateDto newDto(final BankMandate bankMandate) {
        final BankMandateDto dto = new BankMandateDto();

        dto.setSelf(mappingHelper.oidDtoFor(bankMandate));

        dto.setReference(fixup(bankMandate.getReference()));
        dto.setScheme(bankMandate.getScheme().forDto());
        dto.setSequenceType(bankMandate.getSequenceType().forDto());
        dto.setSignatureDate(asXMLGregorianCalendar(bankMandate.getSignatureDate()));
        dto.setBankAccount(mappingHelper.oidDtoFor(bankMandate.getBankAccount()));

        dto.setStatus(Status.OPEN); // not currently in the estatio dom, so hard-coded for now

        return dto;
    }

    // TODO: We've added a suffix because agreement names must be unique, remove after closing https://incodehq.atlassian.net/browse/EST-684
    String fixup(final String reference) {
        if (reference.endsWith("-M")){
            return reference.substring(0,reference.length()-2);
        }
        return reference;
    }

    @Inject
    DtoMappingHelper mappingHelper;
}
