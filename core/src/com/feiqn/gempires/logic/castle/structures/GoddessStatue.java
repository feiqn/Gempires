package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;

public class GoddessStatue extends Structure {

    public GoddessStatue(GempiresGame game) {
        super(game.gempiresAssetHandler.goddessStatueTexture, game);

        this.structureType = StructureType.GODDESS_STATUE;
        this.setSize(1,2);
    }
}
