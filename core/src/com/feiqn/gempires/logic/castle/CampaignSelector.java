package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.stages.match3.water.Water_1;
import com.feiqn.gempires.models.CampaignLevelID;

public class CampaignSelector extends Structure {

    public CampaignLevelID campaignLevelID;
    private CastleScreen parent;

    public CampaignSelector(TextureRegion region, CastleScreen parent, CampaignLevelID id) {
        super(region, parent);

        this.parent = parent;
        campaignLevelID = id;
        this.structureType = StructureType.CAMPAIGN_SELECTOR;
    }

    @Override
    public void selectLevel() {
        switch(campaignLevelID) {
            case FIRE_1:
            case FIRE_2:

            case VOID_1:
            case VOID_2:

            case STONE_1:
            case STONE_2:

            case WATER_1:
                parent.game.setScreen(new Water_1(parent.game, parent.playerInventory));
                break;
            case WATER_2:
            case WATER_3:
            case WATER_4:
            case WATER_5:

            case NATURE_1:
            case NATURE_2:

            case ELECTRIC_1:
            case ELECTRIC_2:
                break;
            default:
                break;
        }
    }
}
