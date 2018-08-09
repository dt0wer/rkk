package ru.gpb.rkk.bus.listener;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.gpb.rkk.bus.event.TestRemoteApplicationEvent;

@Component
@Deprecated
public class TestEventListener implements ApplicationListener<TestRemoteApplicationEvent> {

    @NotNull
    private static final Logger LOG = LoggerFactory.getLogger(TestEventListener.class);

    @Override
    public void onApplicationEvent(TestRemoteApplicationEvent event) {
        LOG.info("Got test event with message: '{}'", event.getMessage());
    }
}
