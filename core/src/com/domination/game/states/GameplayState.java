package com.domination.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.domination.game.Game;
import com.domination.game.engine.GameplayWrapper;
import com.domination.game.engine.ResourceManager;
import com.domination.game.entities.Bacteria;
import com.domination.game.entities.Cell;
import com.domination.game.entities.GraphicalEntity;
import com.domination.game.players.Player;
import com.domination.game.players.defaultAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameplayState extends GameState{
    List<Player> playerList = new ArrayList<Player>();
    public List<Cell> cellList = new ArrayList<Cell>();
    public List<Bacteria> bacteriaList = new ArrayList<Bacteria>();
    private int AISet, AISetLeft;
    public GameplayState(Game game, SpriteBatch batch, int AISet, int AISetLeft) {
        super(game, batch);
        this.AISet = AISet;
        this.AISetLeft = AISetLeft;
    }

    @Override
    public void init() {
        GraphicalEntity background = new GraphicalEntity((Texture) ResourceManager.getInstance().get("Background"),batch);
        background.sprite.setScale(Gdx.graphics.getWidth()/background.sprite.getWidth(),Gdx.graphics.getHeight()/background.sprite.getHeight());
        background.sprite.setX(-background.sprite.getWidth()/2+Gdx.graphics.getWidth()/2);
        background.sprite.setY(-background.sprite.getHeight()/2+Gdx.graphics.getHeight()/2);
        entityManager.add(background);
        setPlayers();
        generateMap(10);
        addCellsAndBacteriaToEntityManager();
        for (Player player : playerList) {
            player.start();
        }
    }

    protected void setPlayers() {
        switch(AISet){
            case(0):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
            case(1):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
            case(2):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
            case(3):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
            case(4):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
            case(5):
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.2f, 0.8f, 0.8f, 1.f)));
                playerList.add(new defaultAI(new GameplayWrapper(this), new Color(0.8f, 0.2f, 0.1f, 1f)));
                break;
        }

    }

    @Override
    public void update() {
        super.update();
        // Remove bacteria that reached the destination cell
        for (Bacteria bacteria : bacteriaList) {
            if (bacteria.isBroken()) {
                bacteriaList.remove(bacteria);
            }
        }
        for (Cell cell : cellList){
            if (checkCollisionWithOtherCells(cell)) {
                cell.stopMoving();
            }
        }
    }

    protected void addCellsAndBacteriaToEntityManager() {
        for (Bacteria bacteria : bacteriaList)
            entityManager.add(bacteria);
        for (Cell cell : cellList)
            entityManager.add(cell);
    }
    protected void generateMap(int cellNumber){
        cellNumber/=2;
        Random random = new Random();
        if(playerList.size()==2) {
            int middleX,middleY;
            float mapCenterX=Gdx.graphics.getWidth()/2.f;

            middleX=random.nextInt(Gdx.graphics.getWidth()/2-Cell.radius*2)+Cell.radius;
            middleY=random.nextInt(Gdx.graphics.getHeight()-Cell.radius*2)+Cell.radius;
            cellList.add(new Cell(playerList.get(0), mapCenterX+middleX, middleY, this.batch));
            cellList.add(new Cell(playerList.get(1), mapCenterX-middleX, middleY, this.batch));
            int i=0;
            while (i < cellNumber - 1) {
                middleX=random.nextInt(Gdx.graphics.getWidth()/2-Cell.radius*2)+Cell.radius;
                middleY=random.nextInt(Gdx.graphics.getHeight()-Cell.radius*2)+Cell.radius;
                if (! checkCollisionWithAllCells(mapCenterX + middleX,middleY)){
                    cellList.add(new Cell(null, mapCenterX+middleX, middleY, this.batch));
                    cellList.add(new Cell(null, mapCenterX-middleX, middleY, this.batch));
                    i++;
                }
            }
        }
    }

    private Boolean checkCollisionWithAllCells(float x, float y){
        for (Cell cell : cellList) {
            float distance = (float) Math.sqrt(Math.pow(cell.getCenterX() - x,2) + Math.pow(cell.getCenterY() - y,2));
            if (distance <= 2 * cell.getRadius())
                return true;
        }
        return false;
    }
    private Boolean checkCollisionWithOtherCells(Cell cell){
        float x = cell.getCenterX();
        float y = cell.getCenterY();
        for (Cell secondCell : cellList) {
            if (cell == secondCell) continue;
            float distance = (float) Math.sqrt(Math.pow(secondCell.getCenterX() - x,2) + Math.pow(secondCell.getCenterY() - y,2));
            if (distance <= 2 * secondCell.getRadius())
                return true;
        }
        return false;
    }

    public synchronized Boolean sendBacteria(Cell source, Cell destination, Player player) {
        if (source != null && destination != null && source.getPlayer() == player && source.getAmount() > 1 && !source.isBroken() && !destination.isBroken()) {
            Bacteria bacteria = new Bacteria(player, source, destination, source.getBacteriaAmount(), batch);
            source.handleOutgoingBacteria(bacteria);
            entityManager.add(bacteria);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.debug("KeyDown", Integer.valueOf(keycode).toString());
        switch (keycode) {
            case Input.Keys.ESCAPE:
                game.popGameState();
                Gdx.app.debug("KeyDown", "Esc");
                return true;
            case Input.Keys.SPACE:
        }
        return false;
    }

}
