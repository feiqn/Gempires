package com.feiqn.gempires.logic.characters.heroes.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.logic.characters.heroes.Heroes;
import com.feiqn.gempires.models.Element;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;

public class Leif extends HeroCard {

    public Leif(TextureRegion region, CastleScreen parent) {
        super(region, parent);

        heroName = "Leif";
        heroTitle = "Flighty Planeswalker";
        heroDescription = "A youth from the West. A natural leader who is skilled with animals.";
        heroAbilityTitle = "";
        heroAbilityDescription = "";

        element = Element.NATURE;

        heroID = Heroes.LEIF;

        initialiseThumbnail(element);
        initializeStats(10, 5, 100);
    }

    @Override
    public void heroAbility() {

    }
}







