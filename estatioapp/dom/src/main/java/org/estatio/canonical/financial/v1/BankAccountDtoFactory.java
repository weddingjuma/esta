package org.estatio.canonical.financial.v1;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.dom.DtoMappingHelper;
import org.estatio.dom.financial.bankaccount.BankAccount;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class BankAccountDtoFactory {

    @Programmatic
    public BankAccountDto newDto(final BankAccount bankAccount) {
        final BankAccountDto dto = new BankAccountDto();

        dto.setSelf(mappingHelper.oidDtoFor(bankAccount));
        dto.setAccountNumber(bankAccount.getAccountNumber());
        dto.setBankParty(mappingHelper.oidDtoFor(bankAccount.getBank()));
        dto.setBranchCode(bankAccount.getBranchCode());
        dto.setExternalReference(bankAccount.getExternalReference());
        dto.setIban(bankAccount.getIban());
        dto.setBic(bankAccount.getBic());
        dto.setName(bankAccount.getName());
        dto.setNationalBankCode(bankAccount.getNationalBankCode());
        dto.setNationalCheckCode(bankAccount.getNationalCheckCode());
        dto.setOwnerParty(mappingHelper.oidDtoFor(bankAccount.getOwner()));
        dto.setReference(bankAccount.getReference());
        return dto;
    }

    @Inject
    DtoMappingHelper mappingHelper;
}
