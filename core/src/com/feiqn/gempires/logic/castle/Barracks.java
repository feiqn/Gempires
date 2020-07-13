package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.ui.HeroRosterPopup;

public class Barracks extends Structure {

    public Barracks(Texture region, CastleScreen parent) {
        super(region, parent);

        structureType = Type.BARRACKS;

        this.setSize(1,1);
    }
}
