package ru.gpb.rkk.bus.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

@JsonTypeName("TestRemoteEvent")
@Deprecated
public class TestRemoteApplicationEvent extends RemoteApplicationEvent {

    @Nullable
    private String message;

    public TestRemoteApplicationEvent() {

    }

    public TestRemoteApplicationEvent(@Nullable String message) {
        this.message = message;
    }

    public TestRemoteApplicationEvent(Object source, String originService,
                                      String destinationService, @Nullable String message) {
        super(source, originService, destinationService);
        this.message = message;
    }

    public TestRemoteApplicationEvent(Object source, String originService, @Nullable String message) {
        super(source, originService);
        this.message = message;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
