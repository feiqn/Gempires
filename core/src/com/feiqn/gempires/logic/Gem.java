package com.feiqn.gempires.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.models.ElementalType;

import java.util.Random;


public class Gem extends Image {
    // What is a gem, but a miserable front for microtransactions?

    public Rectangle bounds;

    public int GemColor;

    public ElementalType elementalType;

    public int GemIndex, // all positions indexed from 0
               positionInRow,
               positionInColumn;

    private final MatchScreen parentMatchScreen;

    public Gem(final MatchScreen parentMatchScreen, int color) {
        super(parentMatchScreen.game.gempiresAssetHandler.gemTextures[color]);
        this.parentMatchScreen = parentMatchScreen;
        switch(color){
            case 0:
                this.elementalType = ElementalType.NATURE;
                break;
            case 1:
                this.elementalType = ElementalType.VOID;
                break;
            case 2:
                this.elementalType = ElementalType.FIRE;
                break;
            case 3:
                this.elementalType = ElementalType.STONE;
                break;
            case 4:
                this.elementalType = ElementalType.ELECTRIC;
                break;
            case 5:
                this.elementalType = ElementalType.WATER;
                break;
            case 6:
                this.elementalType = ElementalType.PURE;
                break;
            case 7:
            default:
                break;
        }

        this.GemColor = color;
        this.GemIndex = -1;

        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Gem.this.parentMatchScreen.allowUserInput) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                // Gdx.app.log("touchUP", "x: " + x + ", y: " + y);
                if(Gem.this.parentMatchScreen.allowUserInput) {
                    Gem.this.parentMatchScreen.allowUserInput = false;
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    Gem.this.parentMatchScreen.checkBoundsThenSwap(x, y, GemIndex);
                }
            }
        });
    }

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
//        this.setSize(1, 1);
        this.GemColor = gemColor; // TODO: refactor to use Element enum
        this.GemIndex = gemIndex; // TODO: getting gemIndex may be redundant with the addition of positionInRow/Column
        this.parentMatchScreen = parentMatchScreen;
        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Gem.this.parentMatchScreen.allowUserInput) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                // Gdx.app.log("touchUP", "x: " + x + ", y: " + y);
                if(Gem.this.parentMatchScreen.allowUserInput) {
                    Gem.this.parentMatchScreen.allowUserInput = false;
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    Gem.this.parentMatchScreen.checkBoundsThenSwap(x, y, GemIndex);
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

