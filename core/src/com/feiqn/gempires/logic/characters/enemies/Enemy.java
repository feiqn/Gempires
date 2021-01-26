package com.feiqn.gempires.logic.characters.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.models.Element;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Image {
    // TODO: this class shares a fair bit of code with HeroCard class. Eventually the shared code should be combined into a mutual superclass.

    public Rectangle bounds;

    // Every enemy subclass must define base stats in its constructor method
    private float strength,
                  defense,
                  maxHealth,
                  currentHealth;

    public int level;

    public Element element;
    public Bestiary beastType;
    public ArrayList<ItemList> loot;

    public Enemy(TextureRegion region, float baseStr, float baseDef, float baseHP) {
        super(region);

        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

        loot = new ArrayList<>();
        this.strength = baseStr;
        this.defense = baseDef;
        this.maxHealth = baseHP;
        this.currentHealth = baseHP;
        this.level = 0;
        initLootTable();
    }

    public void initLootTable() {
        // For child class.
        loot.add(ItemList.THYME);

        // TEMPLATE:

//        switch(this.level) {
//        ...
//        case 99:
//        case 98:
//        case 97:
//        case 96:
//        case 95:
//        case 94:
//        case 93:
//        case 92:
//        case 91:
//        case 90:
//        case 89:
//        case 88:
//        case 87:
//        case 86:
//        case 85:
//        case 84:
//        case 83:
//        case 82:
//        case 81:
//        case 80:
//        case 79:
//        case 78:
//        case 77:
//        case 76:
//        case 75:
//        case 74:
//        case 73:
//        case 72:
//        case 71:
//        case 70:
//        case 69:
//        case 68:
//        case 67:
//        case 66:
//        case 65:
//        case 64:
//        case 63:
//        case 62:
//        case 61:
//        case 60:
//        case 59:
//        case 58:
//        case 57:
//        case 56:
//        case 55:
//        case 54:
//        case 53:
//        case 52:
//        case 51:
//        case 50:
//        case 49:
//        case 48:
//        case 47:
//        case 46:
//        case 45:
//        case 44:
//        case 43:
//        case 42:
//        case 41:
//        case 40:
//        case 39:
//        case 38:
//        case 37:
//        case 36:
//        case 35:
//        case 34:
//        case 33:
//        case 32:
//        case 31:
//        case 30:
//        case 29:
//        case 28:
//        case 27:
//        case 26:
//        case 25:
//        case 24:
//        case 23:
//        case 22:
//        case 21:
//        case 20:
//        case 19:
//        case 18:
//        case 17:
//        case 16:
//        case 15:
//        case 14:
//        case 13:
//        case 12:
//        case 11:
//        case 10:
//        case 9:
//        case 8:
//        case 7:
//        case 6:
//        case 5:
//        case 4:
//        case 3:
//        case 2:
//        case 1:
//        break;
//        }
    }

    public ArrayList<ItemList> rollLoot() {
        // This can be Overridden by child class if needed.
        // TODO: May also implement a switch for certain items to always give more than one.

        final ArrayList<ItemList> rolledLoot = new ArrayList<>();

        final Random random = new Random();

        for(ItemList item : loot) {
            final float f = random.nextFloat();
            if(f > .5f) {
                rolledLoot.add(item);
            }
        }

        return rolledLoot;
    }

    public void scaleToLevel(int level) {
        for(int l = 0; l < level; l++) {
            this.level++;
            strength *= 1.25f;
            defense *= 1.25f;
            maxHealth *= 1.25f;
            currentHealth = maxHealth;
        }
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

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        bounds = new Rectangle(getX(), getY(), getWidth() *.9f, getHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        bounds.setX(x);
        bounds.setY(y);
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
