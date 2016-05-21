package org.estatio.canonical.bankmandate.v1;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.canonical.DtoFactoryAbstract;
import org.estatio.canonical.financial.v1.BankAccountDto;
import org.estatio.canonical.financial.v1.BankAccountDtoFactory;
import org.estatio.dom.agreement.AgreementRole;
import org.estatio.dom.agreement.AgreementRoleRepository;
import org.estatio.dom.agreement.AgreementRoleType;
import org.estatio.dom.agreement.AgreementRoleTypeRepository;
import org.estatio.dom.agreement.AgreementType;
import org.estatio.dom.agreement.AgreementTypeRepository;
import org.estatio.dom.bankmandate.BankMandate;
import org.estatio.dom.bankmandate.BankMandateConstants;
import org.estatio.dom.financial.FinancialAccount;
import org.estatio.dom.financial.FinancialAccountType;
import org.estatio.dom.financial.FinancialAccounts;
import org.estatio.dom.financial.bankaccount.BankAccount;
import org.estatio.dom.party.Party;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class PartyBankAccountsAndMandatesDtoFactory extends DtoFactoryAbstract {

    @Programmatic
    public BankAccountsAndMandatesDto newDto(final Party party) {

        final BankAccountsAndMandatesDto dto = new BankAccountsAndMandatesDto();

        final List<FinancialAccount> financialAccountList = financialAccounts.findAccountsByTypeOwner(FinancialAccountType.BANK_ACCOUNT, party);
        final List<BankAccountDto> bankAccountDtos =
                financialAccountList.stream()
                        .map(x -> bankAccountDtoFactory.newDto((BankAccount) x))
                        .collect(Collectors.toList());

        dto.setBankAccounts(bankAccountDtos);

        final AgreementType bankMandateAt = agreementTypeRepository.find(BankMandateConstants.AT_MANDATE);
        final AgreementRoleType debtorOfMandate = agreementRoleTypeRepository.findByAgreementTypeAndTitle(bankMandateAt, BankMandateConstants.ART_DEBTOR);
        final List<AgreementRole> agreementRoles = agreementRoleRepository.findByPartyAndType(party, debtorOfMandate);

        final List<BankMandateDto> mandateDtos =
                agreementRoles.stream()
                        .map(x -> x.getAgreement())
                        .map(x -> bankMandateDtoFactory.newDto((BankMandate) x))
                        .collect(Collectors.toList());

        dto.setBankMandates(mandateDtos);

        return dto;
    }

    @Inject
    FinancialAccounts financialAccounts;
    @Inject
    BankAccountDtoFactory bankAccountDtoFactory;

    @Inject
    AgreementRoleTypeRepository agreementRoleTypeRepository;
    @Inject
    AgreementTypeRepository agreementTypeRepository;
    @Inject
    AgreementRoleRepository agreementRoleRepository;
    @Inject
    BankMandateDtoFactory bankMandateDtoFactory;
}
