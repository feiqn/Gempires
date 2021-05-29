package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

public class Farm extends Structure {

    public Farm(GempiresGame game) {
        super(game.gempiresAssetHandler.farmTexture, game);

        structureType = StructureType.FARM;

        itemNotifierBubble = new ItemNotifierBubble(game.gempiresAssetHandler.foodTexture, this);

        this.setSize(3f, 1.5f);
    }
}
