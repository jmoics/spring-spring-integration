package com.lil.springintegration.service;

import com.lil.springintegration.endpoint.TechSupportMessageFilter;
import com.lil.springintegration.endpoint.TechSupportMessageHandler;
import com.lil.springintegration.manage.DashboardManager;
import com.lil.springintegration.domain.AppSupportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.*;

public class StatusMonitorService {

    static Logger logger = LoggerFactory.getLogger(DashboardManager.class);

    private AppSupportStatus currentLocalStatus;

    // TODO - refactor to use Spring Dependency Injection
    private AbstractSubscribableChannel statusMonitorChannel;

    public StatusMonitorService() {
        statusMonitorChannel = (PublishSubscribeChannel) DashboardManager.getDashboardContext().getBean("statusMonitorChannel");
        statusMonitorChannel.subscribe(new ServiceMessageHandler());
    }

    public static class ServiceMessageFilter extends TechSupportMessageFilter {
        protected boolean filterMessage(AppSupportStatus status) {
            return status.isUpdateRequired() || status.isDeviceOut();
        }
    }

    private class ServiceMessageHandler extends TechSupportMessageHandler {
        protected void receive(AppSupportStatus status) {
            setCurrentSupportStatus(status);
        }
    }

    private void setCurrentSupportStatus(AppSupportStatus status) {
        this.currentLocalStatus = status;
    }
}
