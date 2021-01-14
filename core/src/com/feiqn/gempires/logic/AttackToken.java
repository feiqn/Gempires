package com.feiqn.gempires.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.models.Element;

public class AttackToken extends Image {

    public Element element;

    public AttackToken(TextureRegion region, Element e){
        super(region);
        this.element = e;
        this.setSize(1,1);
    }
}
