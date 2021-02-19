package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;

public class Barracks extends Structure {

    public Barracks(GempiresGame game) {
        super(game.gempiresAssetHandler.barracksTexture, game);

        structureType = StructureType.BARRACKS;

        this.setSize(2,2);
    }
}
