package com.feiqn.gempires;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class Gem extends Image {
    Rectangle bounds;
    int GemColor, GemIndex, BoardRows, BoardColumns;
    ArrayList<Vector2> slots;
    ArrayList<Gem> gems;

    // TODO: add an overload constructor

    public Gem(final TextureRegion region, final int gemColor, final ArrayList<Gem> gemsArray, final ArrayList<Vector2> slotsArray, final int gemIndex, final int boardRows, final int boardColumns) {
        // GREEN 0
        // PURPLE 1
        // RED 2
        // ORANGE 3
        // YELLOW 4
        // BLUE 5
        // CLEAR 6

        super(region);
        setSize(1, 1);
        BoardRows = boardRows;
        BoardColumns = boardColumns;
        GemColor = gemColor;
        GemIndex = gemIndex;
        slots = slotsArray;
        gems = gemsArray;
        bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                checkForMatches(x, y, gemColor, gemIndex);
            }
        });
    }

    public void checkForMatches(float mouseUpAtX, float mouseUpAtY, int gemColor, int gemIndex) {

        // I can reorganize some of these parameters later so less data needs to be passed each call
        int targetedIndex;
        int targetColor = gemColor;
        boolean outOfBounds = false;
        boolean outOfDownBounds = false;
        boolean outOfUpBounds = false;

        if (mouseUpAtX > 0.5f && mouseUpAtY < 1 && mouseUpAtY > -1 && gemIndex != gems.size() - 1) { // MOVE RIGHT -> TODO: adjust margins
            targetedIndex = gemIndex + 1;
            if(gemIndex % BoardColumns == BoardColumns - 2) {
                outOfBounds = true;
            }
            if(gemIndex - (BoardColumns * 2) < 0) {
                outOfDownBounds = true;
            }
            if(gemIndex + (BoardColumns * 2) > gems.size()) {
                outOfUpBounds = true;
            }
            if (!outOfBounds && gemColor == gems.get(targetedIndex + 1).getGemColor() && gemColor == gems.get(targetedIndex + 2).getGemColor() ||
                !outOfDownBounds && gemColor == gems.get(targetedIndex - BoardColumns).getGemColor() && gemColor == gems.get(targetedIndex - (BoardColumns * 2)).getGemColor() ||
                !outOfUpBounds && gemColor == gems.get(targetedIndex + BoardColumns).getGemColor() && gemColor == gems.get(targetedIndex + (BoardColumns * 2)).getGemColor() ||
                !outOfDownBounds && !outOfUpBounds && gemColor == gems.get(targetedIndex + BoardColumns).getGemColor() && gemColor == gems.get(targetedIndex - BoardColumns).getGemColor()) {

                    // TODO: better implementation would be to go ahead and move the gems for one frame, call checkWholeBoardForMatches(), and if no matches are found, move the gems back.

                    Gdx.app.log("GoodMatch", "Good match to the right");
                    swapGems(gems.get(gemIndex), gems.get(targetedIndex));
                    checkWholeBoardForMatches();
            }
        } else if (mouseUpAtX < -0.5f && mouseUpAtY < 1 && mouseUpAtY > -1) { // MOVE LEFT <-
                Gdx.app.log("moveLeft", "Move left");
        } else if (mouseUpAtX < 1 && mouseUpAtX > -1 && mouseUpAtY < -0.5f) { // MOVE DOWN v
            Gdx.app.log("moveDown", "Move down");
        } else if (mouseUpAtX < 1 && mouseUpAtX > -1 && mouseUpAtY > 0.5f) { // MOVE UP ^
            Gdx.app.log("moveUp", "Move up");
        }
    }

    public void checkWholeBoardForMatches() {
        // TODO: add all matches to an array of gems to be destroy()ed later
        // iterate through entire board as-is for matches
//            if (gemColor == gems.get(targetedIndex).getGemColor() && gemColor == gems.get(targetedIndex + 1).getGemColor() &&!outOfBounds) {
//                Gdx.app.log("3Match", "3 in a row match");
//            }
    }

    public void swapGems(Gem origin, Gem destination) {
        final Vector2 clickedSlot = new Vector2(origin.getX(), origin.getY());
        origin.setXY(destination.getX(), destination.getY());
        destination.setXY(clickedSlot.x, clickedSlot.y);
    }

    public void drop() {

    }

    public void destroy() {
        // take an array of gems to remove from the board, them call drop() on all gems
    }

    public int getGemColor() {
        return GemColor;
    }

    public int getGemIndex() {
        return GemIndex;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void setXY(float pX,float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }
}

