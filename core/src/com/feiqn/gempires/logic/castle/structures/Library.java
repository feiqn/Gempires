package com.feiqn.gempires.logic.castle.structures;

import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

public class Library extends Structure {

    public Library(GempiresGame game) {
        super(game.gempiresAssetHandler.libraryTexture, game);

        structureType = StructureType.LIBRARY;

        itemNotifierBubble = new ItemNotifierBubble(game.gempiresAssetHandler.arcanaTexture, this);
        this.setSize(1.5f, 1.5f);
    }

}
