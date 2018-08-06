package ru.bus.listener;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.bus.event.TestRemoteApplicationEvent;

@Component
public class TestEventListener implements ApplicationListener<TestRemoteApplicationEvent> {

    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(TestEventListener.class);

    @NotNull
    private final ServiceMatcher serviceMatcher;

    @Autowired
    public TestEventListener(@NotNull ServiceMatcher serviceMatcher) {
        this.serviceMatcher = serviceMatcher;
    }

    @Override
    public void onApplicationEvent(TestRemoteApplicationEvent event) {
        if (serviceMatcher.getServiceId().equals(event.getOriginService())) {
            LOG.info("Skip self sent event. Event='{}'", event);
            return;
        }
        LOG.info("Got test event with message: '{}'", event.getMessage());
    }
}
