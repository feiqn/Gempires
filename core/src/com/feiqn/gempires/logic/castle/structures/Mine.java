package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

public class Mine extends Structure {

    public Mine(GempiresGame game) {
        super(game.gempiresAssetHandler.mineTexture, game);

        structureType = StructureType.MINE;

        itemNotifierBubble = new ItemNotifierBubble(game.gempiresAssetHandler.oreTexture, this);
        this.setSize(1.5f, 1.5f);
    }
}
