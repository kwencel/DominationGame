package com.domination.game.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.domination.game.Game;
import com.domination.game.engine.EntityManager;

public class GameState implements InputProcessor {
    protected Game game;
    protected SpriteBatch spriteBatch;
    protected EntityManager entityManager = new EntityManager();

    public GameState(Game game, SpriteBatch spriteBatch){
        this.game = game;
        this.spriteBatch = spriteBatch;
    }
    public void init() { }

    public void update() { }

    public void cleanUp() { }

    public void draw() { entityManager.drawAll(); }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}