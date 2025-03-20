package io.github.group9;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        GameScreen gameScreen = new GameScreen();
        setScreen(new GameScreen());
    }
}

