package it.buniva.strage.event;

import org.springframework.context.ApplicationEvent;

public class UserAddedSuccessfullyEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private boolean UserAddedSuccessfully;

    public UserAddedSuccessfullyEvent(Object source, boolean UserAddedSuccessfully) {
        super(source);
        this.UserAddedSuccessfully = UserAddedSuccessfully;
    }

    public boolean isUserAddedSuccessfully() {
        return UserAddedSuccessfully;
    }

    public void setUserAddedSuccessfully(boolean userAddedSuccessfully) {
        this.UserAddedSuccessfully = userAddedSuccessfully;
    }
}
