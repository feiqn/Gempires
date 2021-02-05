package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.logic.items.ItemList;

import java.util.HashMap;

public class PlayerInventory {
    // includes: all resources a player owns, combat and ascension items, and how many of them

    private final Preferences pref;

    private final CastleScreen parentCastle;

    private final HashMap<ItemList, Integer> items;

    private final HashMap<HeroList, Integer> commonUnits;

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
        items = new HashMap<>();

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
        fillItems();

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

    private void fillItems() {
        for(int i = 0; i < ItemList.values().length; i++) {
            final ItemList item = ItemList.values()[i];
            if(pref.contains("count" + item)) {
                final int count = pref.getInteger("count" + item);
                for(int c = 0; c < count; c++) {
                    addItem(item);
                }
            }
        }
    }

    private void fillCommonUnits() {
        for(int i = 0; i < HeroList.values().length; i++) {
            final HeroList hero = HeroList.values()[i];
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
    public void addItem(ItemList item) {
        if(!items.containsKey(item)) {
            items.put(item, 1);
        } else {
            final int currentInt = items.get(item);
            items.put(item, currentInt + 1);
        }

        final int i = items.get(item);
        pref.putInteger("count" + item, i);
        pref.flush();
    }
    public void addCommonUnit(HeroList unitToAdd) {
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
    public void subtractCommonUnit(HeroList unitToSubtract) {
        final int currentInt = commonUnits.get(unitToSubtract);
        commonUnits.put(unitToSubtract, currentInt - 1);
        pref.putInteger("count" + unitToSubtract, currentInt - 1);
        pref.flush();
    }
    public void subtractItem(ItemList itemToSubtract) {
        final int currentInt = items.get(itemToSubtract);
        items.put(itemToSubtract, currentInt - 1);
        pref.putInteger("count" + itemToSubtract, currentInt - 1);
        pref.flush();
    }

    // GETTERS
    public float getFoodCount() { return foodCount; }
    public float getArcanaCount() { return arcanaCount; }
    public float getOreCount() { return oreCount; }
    public int getCommonUnitCount(HeroList unit) { return commonUnits.get(unit); }
    public int getItemCount(ItemList item) {
        if(items.containsKey(item)) {
            return items.get(item);
        } else {
            return  0;
        }
    }

}
