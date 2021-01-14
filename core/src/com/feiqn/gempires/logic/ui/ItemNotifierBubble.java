package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;

public class ItemNotifierBubble extends Image {
    // A type-agnostic bubble for quickly collecting resources from any structure

    public Rectangle bounds;

    final public Structure parentStructure;

    final private ItemNotifierBubble self = this;

    public ItemNotifierBubble(TextureRegion region, Structure parent) {
        super(region);

        this.parentStructure = parent;
        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        this.setSize(1,1);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                parentStructure.collectResource();
                self.remove();
            }
        });

    }

    public void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }
}
