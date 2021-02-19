package com.feiqn.gempires.logic.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.GempiresGame;

public class Combatant extends Image {

    public float strength,
                  defense,
                  maxHealth,
                  currentHealth;

    public final GempiresGame game;

    public Combatant(GempiresGame game, TextureRegion region) {
        super(region);
        this.game = game;
    }

    public void healPercentage(float percentage) {
        final float heal = maxHealth * percentage;
        healStaticAmount(heal);
    }
    public void healStaticAmount(float heal) {
        if(currentHealth + heal < maxHealth) {
            currentHealth += heal;
        } else {
            resetCurrentHealth();
        }
    }
    public void resetCurrentHealth() {
        currentHealth = maxHealth;
    }
    public void applyDamage(float damage) {
        currentHealth -= damage;
    }

    // GETTERS
    public float getStrength() {
        return strength;
    }
    public float getDefense() {
        return defense;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float getCurrentHealth() {
        return currentHealth;
    }
}
