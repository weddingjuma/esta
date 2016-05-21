package org.estatio.dom.lease.status;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.eventbus.EventBusService;

import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemStatus;
import org.estatio.dom.lease.LeaseStatus;
import org.estatio.services.clock.ClockService;

@DomainService(nature = NatureOfService.DOMAIN)
public class LeaseStatusService {

    public LeaseStatus refreshStatus(Lease lease) {
        LeaseStatus newStatus = statusOf(lease);
        if (!lease.getStatus().equals(newStatus)) {
            lease.setStatus(newStatus);
        }
        return newStatus;
    }

    LeaseStatus statusOf(Lease lease) {
        int itemCount = 0;
        int disabledCount = 0;

        for (LeaseItem item : lease.getItems()) {
            itemCount++;
            LeaseItemStatus itemStatus = refreshStatus(item);
            if (itemStatus.equals(LeaseItemStatus.SUSPENDED)) {
                disabledCount++;
            }
        }
        if (itemCount > 0 && itemCount == disabledCount) {
            return LeaseStatus.SUSPENDED;
        }
        if (itemCount > 0 && disabledCount > 0) {
            return LeaseStatus.SUSPENDED_PARTIALLY;
        }
        if (lease.getTenancyEndDate() != null && clockService.now().isAfter(lease.getTenancyEndDate())) {
            return LeaseStatus.TERMINATED;
        }
        return LeaseStatus.ACTIVE;

    }

    // //////////////////////////////////////

    public LeaseItemStatus refreshStatus(LeaseItem leaseItem) {
        LeaseItemStatus newStatus = statusOf(leaseItem);
        if (!leaseItem.getStatus().equals(newStatus)) {
            leaseItem.setStatus(newStatus);
        }
        return newStatus;
    }

    LeaseItemStatus statusOf(LeaseItem item) {
        if (item.getStatus() == LeaseItemStatus.SUSPENDED) {
            return item.getStatus();
        }
        if (item.getEndDate() != null && clockService.now().isAfter(item.getEndDate())) {
            return LeaseItemStatus.TERMINATED;
        }
        return LeaseItemStatus.ACTIVE;
    }

    // //////////////////////////////////////

    @Subscribe
    @Programmatic
    public void on(Lease.ResumeAllEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource());
        default:
            break;
        }
    }

    @Subscribe
    @Programmatic
    public void on(Lease.SuspendAllEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource());
        default:
            break;
        }
    }

    @Subscribe
    @Programmatic
    public void on(Lease.TerminateEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource());
        default:
            break;
        }
    }

    @Subscribe
    @Programmatic
    public void on(Lease.ChangeDatesEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource());
        default:
            break;
        }
    }

    // //////////////////////////////////////

    @Subscribe
    @Programmatic
    public void on(LeaseItem.ResumeEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource().getLease());
        default:
            break;
        }
    }

    @Subscribe
    @Programmatic
    public void on(LeaseItem.SuspendEvent ev) {
        switch (ev.getEventPhase()) {
        case EXECUTED:
            refreshStatus(ev.getSource().getLease());
        default:
            break;
        }
    }

    // //////////////////////////////////////

    @Programmatic
    @PostConstruct
    public void postConstruct() {
        eventBusService.register(this);
    }

    @Programmatic
    @PreDestroy
    public void preDestroy() {
        eventBusService.unregister(this);
    }

    @javax.inject.Inject
    private EventBusService eventBusService;

    // //////////////////////////////////////

    @Inject
    ClockService clockService;

}
