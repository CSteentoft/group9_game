module org.example.collision {
    requires com.badlogic.gdx;
    requires com.badlogic.ashley;
    requires java.logging;
    requires java.desktop;

    exports org.example;
    exports org.example.components;
    exports org.example.systems;
    exports org.common;

    opens org.example to com.badlogic.gdx;
    opens org.common to com.badlogic.gdx;
}
