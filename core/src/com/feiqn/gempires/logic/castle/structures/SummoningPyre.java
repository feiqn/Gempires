package com.feiqn.gempires.logic.castle.structures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;

public class SummoningPyre extends Structure {

    public SummoningPyre(GempiresGame game) {
        super(game.gempiresAssetHandler.summoningPyreTexture, game);
    }
}
