package org.estatio.domlink;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Administration", menuBar = DomainServiceLayout.MenuBar.SECONDARY)
public class LinkMenu {

    @Action(semantics = SemanticsOf.SAFE)
    public List<Link> allLinks() {
        return linkRepo.allLinks();
    }

    public Link newLink(
            final ApplicationTenancy applicationTenancy,
            final String packageName,
            final String className,
            final String name,
            final String urlTemplate) {
        return linkRepo.newLink(
                applicationTenancy,
                getaClass(packageName, className),
                name,
                urlTemplate);
    }

    public String validateNewLink(
            final ApplicationTenancy applicationTenancy,
            final String packageName,
            final String className,
            final String name,
            final String urlTemplate) {
       return getaClass(packageName, className) == null ? "Class not found" :null;
    }

    private Class<?> getaClass(final String packageName, final String className) {
        try {
            return Class.forName(String.format("%s.%s", packageName, className));
        } catch (ClassNotFoundException e) {
            // Do nothing
            return null;
        }
    }

    @Inject
    LinkRepository linkRepo;
}
