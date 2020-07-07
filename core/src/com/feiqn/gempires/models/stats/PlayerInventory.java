package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PlayerInventory {
    // includes: all resources a player owns, combat and ascension items, and how many of them

    private Preferences pref;

    private float foodCount,
                  maxFood,
                  oreCount,
                  maxOre,
                  arcanaCount,
                  maxArcana;

    public PlayerInventory() {
        pref = Gdx.app.getPreferences("Player Inventory");
        foodCount = pref.getFloat("foodCount");
        oreCount = pref.getFloat("oreCount");
        arcanaCount = pref.getFloat("arcanaCount");
    }

    // ADDING
    public void addFood(float foodCount) {
        this.foodCount += foodCount;
        pref.putFloat("foodCount", foodCount);
        pref.flush();
    }

    public void addOre(float oreCount) {
        this.oreCount += oreCount;
        pref.putFloat("oreCount", oreCount);
        pref.flush();
    }

    public void addArcana(float arcanaCount) {
        this.arcanaCount += arcanaCount;
        pref.putFloat("arcanaCount", arcanaCount);
        pref.flush();
    }

    // SUBTRACTING
    public void subtractFood(float foodCount) {
        this.foodCount -= foodCount; // TODO: check for < 0
        pref.putFloat("foodCount", foodCount);
        pref.flush();
    }

    public void subtractOre(float oreCount) {
        this.oreCount -= oreCount;
        pref.putFloat("oreCount", oreCount);
        pref.flush();
    }

    public void subtractArcana(float arcanaCount) {
        this.arcanaCount -= arcanaCount;
        pref.putFloat("arcanaCount", arcanaCount);
        pref.flush();
    }

    // GETTERS
    public float getFoodCount() {
        return foodCount;
    }

    public float getArcanaCount() {
        return arcanaCount;
    }

    public float getOreCount() {
        return oreCount;
    }
}
