package com.feiqn.gempires.logic.heroes.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.heroes.HeroCard;

public class Leif extends HeroCard {

    public Leif(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        heroName = "Leif";
        heroTitle = "Flighty Planeswalker";
        heroDescription = "A youth from the Western lands of Rivaile. A natural leader who is skilled with animals.";
        heroAbilityTitle = "";
        heroAbilityDescription = "";

        element = Element.NATURE;

        initialiseThumbnail(element);
        initializeStats(10, 5, 100);
    }

    @Override
    public void heroAbility() {

    }
}







