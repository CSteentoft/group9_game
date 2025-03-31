package io.github.group9.components;

import com.badlogic.ashley.core.Component;

/**
 * A generic component to label an entity with a string tag (e.g. \"player\").
 * This stays in core so plugins can use it without referencing plugin code from core.
 */
public class TagComponent implements Component {
    public String tag = "";

    public TagComponent() { }

    public TagComponent(String tag) {
        this.tag = tag;
    }
}
