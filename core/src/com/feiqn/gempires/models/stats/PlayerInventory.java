package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.Heroes;

import java.util.HashMap;

public class PlayerInventory {
    // includes: all resources a player owns, combat and ascension items, and how many of them

    private final Preferences pref;

    private final CastleScreen parentCastle;

    private int pureGemCount;

    private HashMap<Heroes, Integer> commonUnits;

    private float foodCount,
                  maxFood,
                  oreCount,
                  maxOre,
                  arcanaCount,
                  maxArcana;


    public PlayerInventory(CastleScreen parent) {
        parentCastle = parent;
        pref         = Gdx.app.getPreferences("PlayerInventory");
        commonUnits = new HashMap<>();

        if(!pref.contains("foodCount")) {
            pref.putFloat("foodCount", 100);
        }
        foodCount    = pref.getFloat("foodCount");

        if(!pref.contains("oreCount")) {
            pref.putFloat("oreCount", 100);
        }
        oreCount     = pref.getFloat("oreCount");

        if(!pref.contains("arcanaCount")) {
            pref.putFloat("arcanaCount", 50);
        }
        arcanaCount  = pref.getFloat("arcanaCount");

        if(!pref.contains("maxFood")) {
            pref.putFloat("maxFood", 200);
        }
        maxFood      = pref.getFloat("maxFood");

        if(!pref.contains("maxOre")) {
            pref.putFloat("maxOre", 200);
        }
        maxOre       = pref.getFloat("maxOre");

        if(!pref.contains("maxArcana")) {
            pref.putFloat("maxArcana", 100);
        }
        maxArcana    = pref.getFloat("maxArcana");

        fillCommonUnits();

        pref.flush();
    }

    public void calculateMaximums() {
        for(Structure structure : parentCastle.castleStats.getStructures()) {
            switch (structure.structureType) {
                case FARM:
                case SILO:
                    maxFood += structure.getResourceCapacity();
                    break;
                case MINE:
                case WAREHOUSE:
                    maxOre += structure.getResourceCapacity();
                    break;
                case LIBRARY:
                case ARCHIVE:
                    maxArcana += structure.getResourceCapacity();
                default:
                    break;
            }
        }
    }

    private void fillCommonUnits() {
        for(int i = 0; i < Heroes.values().length; i++) {
            final Heroes hero = Heroes.values()[i];
            switch (hero) {
                case DARING_CHEF:
                case DREAMY_DRUID:
                case LAZY_LABORER:
                case ORPHANED_YOUTH:
                case SEASICK_SAILOR:
                case VENGEFUL_FARMER:
                case DISPARATE_DIGGER:
                case CUNNING_CRAFTSMAN:
                    if(pref.contains("count" + hero)) {
                        final int count = pref.getInteger("count" + hero);
                        for(int c = 0; c < count; c++) {
                            addCommonUnit(hero);
                        }
                    }
                default:
                    break;
            }
        }
    }

    // ADDING

    // TODO: store bravery tokens

    public void addFood(float foodToAdd) {
        if(this.foodCount != maxFood) {
            if(this.foodCount + foodToAdd <= maxFood) {
                this.foodCount += foodToAdd;
            } else {
                this.foodCount = maxFood;
            }

            pref.putFloat("foodCount", this.foodCount);
            pref.flush();
        }
    }

    public void addOre(float oreToAdd) {
        if(this.oreCount != maxOre) {
            if(this.oreCount + oreToAdd <= maxOre) {
                this.oreCount += oreToAdd;
            } else {
                this.oreCount = maxOre;
            }

            pref.putFloat("oreCount", this.oreCount);
            pref.flush();
        }
    }

    public void addArcana(float arcanaToAdd) {
        if(this.arcanaCount != maxArcana) {
            if (this.arcanaCount + arcanaToAdd <= maxArcana) {
                this.arcanaCount += arcanaToAdd;
            } else {
                this.arcanaCount = maxArcana;
            }

            pref.putFloat("arcanaCount", arcanaCount);
            pref.flush();
        }
    }

    public void addCommonUnit(Heroes unitToAdd) {
        if(!commonUnits.containsKey(unitToAdd)) {
            commonUnits.put(unitToAdd, 1);
        } else {
            final int currentInt = commonUnits.get(unitToAdd);
            commonUnits.put(unitToAdd, currentInt + 1);
        }

        final int i = commonUnits.get(unitToAdd);
        pref.putInteger("count" + unitToAdd, i);
        pref.flush();
    }

    // SUBTRACTING
    public void subtractFood(float foodToSubtract) {
        if(this.foodCount - foodToSubtract >= 0) {
            this.foodCount -= foodToSubtract;
        } else {
            this.foodCount = 0;
        }

        pref.putFloat("foodCount", this.foodCount);
        pref.flush();
    }

    public void subtractOre(float oreToSubtract) {
        if(this.oreCount - oreToSubtract >= 0) {
            this.oreCount -= oreToSubtract;
        } else {
            this.oreCount = 0;
        }

        pref.putFloat("oreCount", oreCount);
        pref.flush();
    }

    public void subtractArcana(float arcanaToSubtract) {
        if(this.arcanaCount - arcanaToSubtract >= 0) {
            this.arcanaCount -= arcanaToSubtract;
        } else {
            this.arcanaCount = 0;
        }

        pref.putFloat("arcanaCount", arcanaCount);
        pref.flush();
    }

    public void subtractCommonUnit(Heroes unitToSubtract) {
        final int currentInt = commonUnits.get(unitToSubtract);
        commonUnits.put(unitToSubtract, currentInt - 1);
        pref.putInteger("count" + unitToSubtract, currentInt - 1);
        pref.flush();
    }

    // GETTERS
    public float getFoodCount() { return foodCount; }
    public float getArcanaCount() { return arcanaCount; }
    public float getOreCount() { return oreCount; }
    public int getCommonUnitCount(Heroes unit) { return commonUnits.get(unit); }

}
