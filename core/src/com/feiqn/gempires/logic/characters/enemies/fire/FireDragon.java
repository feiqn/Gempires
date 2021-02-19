package com.feiqn.gempires.logic.characters.enemies.fire;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.models.ElementalType;

import java.util.ArrayList;
import java.util.Random;

public class FireDragon extends Enemy {

    public FireDragon(GempiresGame game) {
        super(game, game.gempiresAssetHandler.fireDragonTextureRegion, 20, 15, 450);
        this.elementalType = ElementalType.FIRE;
        this.beastType = Bestiary.FIRE_DRAGON;
    }

    @Override
    public void initLootTable() {
        loot = new ArrayList<>();
        final Random random = new Random();

        switch(this.level) {
            case 99:
            case 98:
            case 97:
            case 96:
            case 95:
            case 94:
            case 93:
            case 92:
            case 91:
            case 90:
            case 89:
            case 88:
            case 87:
            case 86:
            case 85:
            case 84:
            case 83:
            case 82:
            case 81:
            case 80:
                final float r1 = random.nextFloat();
                if(r1 > .7f) {
                    loot.add(ItemList.EMBERSTONE);
                }
            case 79:
            case 78:
            case 77:
            case 76:
            case 75:
            case 74:
            case 73:
            case 72:
            case 71:
            case 70:
            case 69:
            case 68:
            case 67:
            case 66:
            case 65:
            case 64:
            case 63:
            case 62:
            case 61:
            case 60:
            case 59:
            case 58:
            case 57:
            case 56:
            case 55:
            case 54:
            case 53:
            case 52:
            case 51:
            case 50:
            case 49:
            case 48:
            case 47:
            case 46:
            case 45:
            case 44:
            case 43:
            case 42:
            case 41:
                final float r = random.nextFloat();
                if(r > .3f) {
                    loot.add(ItemList.TOOLS);
                } else {
                    loot.add(ItemList.LEATHER_ARMOR);
                }
            case 40:
            case 39:
            case 38:
            case 37:
            case 36:
            case 35:
            case 34:
            case 33:
            case 32:
            case 31:
            case 30:
            case 29:
            case 28:
            case 27:
            case 26:
            case 25:
            case 24:
            case 23:
            case 22:
            case 21:
            case 20:
            case 19:
            case 18:
                loot.add(ItemList.HEALTH_POTION_SMALL);
            case 17:
            case 16:
            case 15:
            case 14:
            case 13:
            case 12:
            case 11:
            case 10:
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                loot.add(ItemList.BATTERED_RELIC);
                loot.add(ItemList.COPPER_CHUNK);
                loot.add(ItemList.OAK_LOGS);
                loot.add(ItemList.BASIL);
                break;
        }
    }
}
