package net.mcskirmish.event.update;

import net.mcskirmish.event.SkirmishEvent;

public class UpdateEvent extends SkirmishEvent {

    private final UpdateType type;

    /**
     * Event called periodically respective to the {@link UpdateType}
     *
     * @param type represents the time that has passed
     */
    public UpdateEvent(UpdateType type) {
        this.type = type;
    }

    public UpdateType getType() {
        return type;
    }

}
