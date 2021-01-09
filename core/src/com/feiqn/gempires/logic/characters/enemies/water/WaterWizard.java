package com.feiqn.gempires.logic.characters.enemies.water;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.models.Element;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;

public class WaterWizard extends Enemy {

    public WaterWizard(TextureRegion region) {
        super(region, 5, 5, 10);
        this.element = Element.WATER;
        this.beastType = Bestiary.WATER_WIZARD;
        this.setSize(2, 2);

    }

}
