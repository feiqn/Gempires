package com.feiqn.gempires.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;


public class Gem extends Image {
    // What is a gem, but a miserable front for microtransactions?

    public Rectangle bounds;

    public int GemColor;

    public int GemIndex, // all positions indexed from 0
               positionInRow,
               positionInColumn;

    final private MatchScreen matchScreen;

    public Gem(final TextureRegion region, final int gemColor, int gemIndex, final MatchScreen parentMatchScreen) {
        // GREEN  0
        // PURPLE 1
        // RED    2
        // ORANGE 3
        // YELLOW 4
        // BLUE   5
        // PURE   6
        // BLANK  7

        super(region);
        this.setSize(1, 1);
        this.GemColor = gemColor; // TODO: refactor to use Element enum
        this.GemIndex = gemIndex; // TODO: getting gemIndex may be redundant with the addition of positionInRow/Column
        this.matchScreen = parentMatchScreen;
        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(matchScreen.allowUserInput) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                // Gdx.app.log("touchUP", "x: " + x + ", y: " + y);
                if(matchScreen.allowUserInput) {
                    matchScreen.allowUserInput = false;
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    matchScreen.checkBoundsThenSwap(x, y, GemIndex);
                }
            }
        });
    }

    public void setToBlank() {
        this.GemColor = 7;
        setColor(.1f, .1f, .1f, 0f);
        for(EventListener l : getListeners()) {
            removeListener(l);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        bounds.setX(x);
        bounds.setY(y);
    }
}

