package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.structures.Structure;
import com.feiqn.gempires.logic.stages.match3.dark.Void_1;
import com.feiqn.gempires.logic.stages.match3.water.Water_1;
import com.feiqn.gempires.models.CampaignLevelID;

public class CampaignSelector extends Structure {

    public CampaignLevelID campaignLevelID;
    private CastleScreen parent;

    public CampaignSelector(TextureRegion region, GempiresGame game, CampaignLevelID id) {
        super(region, game);

        this.parent = game.castle;
        campaignLevelID = id;
        this.structureType = StructureType.CAMPAIGN_SELECTOR;
    }

    @Override
    public void selectLevel() {
        switch(campaignLevelID) {
            case FIRE_1:
            case FIRE_2:

            case VOID_1:
                parent.game.setScreen(new Void_1(parent.game));
                break;
            case VOID_2:

            case STONE_1:
            case STONE_2:

            case WATER_1:
                 parent.game.setScreen(new Water_1(parent.game));
                break;
            case WATER_2:
            case WATER_3:
            case WATER_4:
            case WATER_5:

            case NATURE_1:
            case NATURE_2:

            case ELECTRIC_1:
            case ELECTRIC_2:
            default:
                break;
        }
    }
}
