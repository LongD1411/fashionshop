package com.project.shopapp.componens;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CategoryDeletedEvent extends ApplicationEvent {
    private final String thumbnail;

    public CategoryDeletedEvent(Object source, String thumbnail) {
        super(source);
        this.thumbnail = thumbnail;
    }
}
