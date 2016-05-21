package org.estatio.app.services.userprofile;

import org.isisaddons.module.security.app.user.MeService;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.userprof.UserProfileService;
import org.estatio.dom.UdoDomainService;

@DomainService
@DomainServiceLayout(menuBar = DomainServiceLayout.MenuBar.TERTIARY)
public class EstatioUserProfileService extends UdoDomainService<EstatioUserProfileService> implements UserProfileService {

    public EstatioUserProfileService() {
        super(EstatioUserProfileService.class);
    }

    @Programmatic
    @Override
    public String userProfileName() {
        final ApplicationUser currentUser = meService.me();
        final ApplicationTenancy tenancy = currentUser.getTenancy();
        if(tenancy != null) {
            return String.format("%s (%s)", currentUser.getName(), tenancy.getName());
        } else {
            return String.format("%s", currentUser.getName());
        }
    }

    // //////////////////////////////////////

    @javax.inject.Inject
    MeService meService;

}
