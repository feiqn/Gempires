package com.feiqn.gempires.logic.heroes.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.heroes.Hero;

public class Leif extends Hero {

    public Leif(TextureRegion region) {
        super(region);

        heroName = "Leif";
        heroTitle = "Flighty Planeswalker";
        heroDescription = "A youth from the Western lands of Rivaile. A natural leader who is skilled with animals.";
        heroAbilityTitle = "";
        heroAbilityDescription = "";

        element = Element.NATURE;

        // base stats
        level = 1;
        rating = 3;
        health = 100;
        strength = 10;
        defense = 5;
    }

    @Override
    public void heroAbility() {

    }
}
