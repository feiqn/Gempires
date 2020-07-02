package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

public class Farm extends Structure {

    public Farm(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        structureType = Type.FARM;

        itemNotifierBubble = new ItemNotifierBubble(parent.foodIcon, this);
    }
}
