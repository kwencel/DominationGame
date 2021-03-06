package com.domination.game.players.MarcinP_AI.entities;

import com.domination.game.ai_types.FakeCell;
import com.domination.game.players.MarcinP_AI.MarcinP_AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Cell {

    public final FakeCell fakeCell;
    public final List<Cell> closestFriendlyCellList = new ArrayList<Cell>();
    public final List<Cell> closestHostileCellList = new ArrayList<Cell>();
    final List<Bacteria> closestIncomingFriendlyBacteriaList = new ArrayList<Bacteria>();
    final List<Bacteria> closestIncomingHostileBacteriaList = new ArrayList<Bacteria>();
    final List<Cell> closestNeutralCellList = new ArrayList<Cell>();
    final MarcinP_AI aiEngine;
    private int expectedBacteriaAmount;

    Cell(FakeCell fakeCell, MarcinP_AI aiEngine) {
        this.aiEngine = aiEngine;
        this.fakeCell = fakeCell;
    }

    public void init() {
        addBacteriasToLists();
        addCellsToLists();

        sortBacteriaListByClosest(closestIncomingFriendlyBacteriaList);
        sortBacteriaListByClosest(closestIncomingHostileBacteriaList);
        sortCellListByClosest(closestFriendlyCellList);
        sortCellListByClosest(closestHostileCellList);
        sortCellListByClosest(closestNeutralCellList);
        expectedBacteriaAmount = expectedFinalBacteriaAmount();
    }

    protected abstract void addBacteriasToLists();

    protected abstract void addCellsToLists();

    private void sortBacteriaListByClosest(List<Bacteria> list) {
        list.sort(new Comparator<Bacteria>() {
            @Override
            public int compare(Bacteria bacteria, Bacteria t1) {
                if (bacteria.fakeBacteria.endTime < t1.fakeBacteria.endTime) return -1;
                else if (bacteria.fakeBacteria.endTime > t1.fakeBacteria.endTime) return 1;
                else return 0;
            }
        });
    }

    private void sortCellListByClosest(List<Cell> list) {
        final Cell thisCell = this;
        list.sort(new Comparator<Cell>() {
            @Override
            public int compare(Cell cell, Cell t1) {
                float distance1 = aiEngine.distanceBetweenCells(thisCell, cell);
                float distance2 = aiEngine.distanceBetweenCells(thisCell, t1);
                if (distance1 < distance2) return -1;
                else if (distance1 > distance2) return 1;
                else return 0;
            }
        });
    }

    public int expectedBacteriaAmountAfterTime(double time) {
        int amount = this.fakeCell.bacteriaAmount;

        long currTime = System.currentTimeMillis();
        for (Bacteria bacteria : closestIncomingHostileBacteriaList) {
            if (bacteria.fakeBacteria.endTime - currTime <= time) {
                amount -= bacteria.fakeBacteria.amount;
            }
        }
        for (Bacteria bacteria : closestIncomingFriendlyBacteriaList) {
            if (bacteria.fakeBacteria.endTime - currTime <= time) {
                amount += bacteria.fakeBacteria.amount;
            }
        }
        amount += (int) time / 1000;
        return amount;
    }

    int expectedFinalBacteriaAmount() {
        int amount = this.fakeCell.bacteriaAmount;
        for (Bacteria bacteria : closestIncomingHostileBacteriaList) {
            amount -= bacteria.fakeBacteria.amount;
        }
        for (Bacteria bacteria : closestIncomingFriendlyBacteriaList) {
            amount += bacteria.fakeBacteria.amount;
        }
        return amount;
    }

}
