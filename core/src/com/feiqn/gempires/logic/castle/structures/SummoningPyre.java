package com.feiqn.gempires.logic.castle.structures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;

public class SummoningPyre extends Structure {

    // Used for replaying campaign stages and other special levels via unique non consumable items.

    public SummoningPyre(GempiresGame game) {
        super(game.gempiresAssetHandler.summoningPyreTexture, game);

        structureType = StructureType.SUMMONING_PYRE;

        this.setSize(1.5f, 1.5f);
    }
}
