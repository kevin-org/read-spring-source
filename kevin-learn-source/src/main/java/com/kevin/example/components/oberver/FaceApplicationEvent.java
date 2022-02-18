package com.kevin.example.components.oberver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class FaceApplicationEvent extends ApplicationContextEvent {


    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public FaceApplicationEvent(ApplicationContext source) {
        super(source);
    }
}
