package com.google.sample.cloudvision;

/**
 * Created by SuKim on 15/08/2017.
 */

/**
 * Provides a singleton instance for the application event bus. For the sake of simplicity it's done via the singleton,
 * however, some dependency injection directly into interested classes.
 */
public class EventBus {

    private static final EventBus sBus = new EventBus();

    public static EventBus getInstance() {
        return sBus;
    }


    private EventBus() {

    }

}
