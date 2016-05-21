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
package org.estatio.dom.financial.bankaccount;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.IsisApplibModule.ActionDomainEvent;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import org.estatio.dom.JdoColumnLength;
import org.estatio.dom.asset.financial.FixedAssetFinancialAccountRepository;
import org.estatio.dom.financial.FinancialAccount;
import org.estatio.dom.financial.utils.IBANHelper;
import org.estatio.dom.financial.utils.IBANValidator;
import org.estatio.dom.geography.Country;
import org.estatio.dom.party.Party;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
// no @DatastoreIdentity nor @Version, since inherited from supertype
@DomainObject(editing = Editing.DISABLED)
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class BankAccount
        extends FinancialAccount {

    @Column(name = "bankPartyId", allowsNull = "true")
    @Getter @Setter
    private Party bank;

    @Column(allowsNull = "false", length = JdoColumnLength.TYPE_ENUM)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
    private BankAccountType bankAccountType;

    @Column(name = "countryId", allowsNull = "true")
    @Getter @Setter
    private Country country;

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.IBAN)
    @Getter @Setter
    private String iban;

    public boolean isValidIban() {
        return IBANValidator.valid(getIban());
    }

    @Action(hidden = Where.EVERYWHERE)
    public BankAccount verifyIban() {
        IBANHelper.verifyAndUpdate(this);
        return this;
    }

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.NATIONAL_CHECK_CODE)
    @Getter @Setter
    private String nationalCheckCode;

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.NATIONAL_BANK_CODE)
    @Getter @Setter
    private String nationalBankCode;

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.BRANCH_CODE)
    @Getter @Setter
    private String branchCode;

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.ACCOUNT_NUMBER)
    @Getter @Setter
    private String accountNumber;

    @Column(allowsNull = "true", length = JdoColumnLength.BankAccount.ACCOUNT_NUMBER)
    @Getter @Setter
    private String bic;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    public BankAccount change(
            @ParameterLayout(typicalLength = JdoColumnLength.BankAccount.IBAN)
            final String iban,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String bic,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String externalReference) {
        setIban(iban);
        setName(iban);
        setBic(bic);
        setExternalReference(externalReference);
        // TODO: Changing references is not really a good thing. in this case
        // there's no harm but we should come up with a pattern where we
        // archive the old reference
        setReference(iban);

        return this;
    }

    public String validateChange(
            final String iban,
            final String bic,
            final String externalReference) {
        if (!IBANValidator.valid(iban)) {
            return "Not a valid IBAN number";
        }
        return null;
    }

    public String default0Change() {
        return getIban();
    }

    public String default1Change() {
        return getBic();
    }

    public String default2Change() {
        return getExternalReference();
    }

    public BankAccount refresh() {
        IBANHelper.verifyAndUpdate(this);
        return this;
    }

    @Action(domainEvent = BankAccount.RemoveEvent.class, semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public void remove() {
        getContainer().remove(this);
        getContainer().flush();
        return;
    }

    public static class RemoveEvent extends ActionDomainEvent<BankAccount> {
        private static final long serialVersionUID = 1L;
    }

    @Inject
    FixedAssetFinancialAccountRepository fixedAssetFinancialAccountRepository;

    @Inject
    BankAccounts bankAccounts;

}
