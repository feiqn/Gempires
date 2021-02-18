package com.feiqn.gempires.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.models.ElementalType;

public class AttackToken extends Image {

    public ElementalType elementalType;
    public Rectangle bounds;

    public AttackToken(TextureRegion region, ElementalType e){
        super(region);
        this.elementalType = e;
        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.setSize(1,1);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        bounds.setX(x);
        bounds.setY(y);
    }

    public void updateBounds() {
        bounds.setX(getX());
        bounds.setY(getY());
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
