package com.feiqn.gempires.logic.characters.heroes.nature;

import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.models.ElementalType;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;

public class Leif extends HeroCard {

    public Leif(CastleScreen parent) {
        super(parent.game.gempiresAssetHandler.natureCardTexture, parent, parent.game.gempiresAssetHandler.vivainTexture, ElementalType.NATURE); // DEBUG

        heroName = "Leif";
        heroTitle = "Flighty Planeswalker";
        heroDescription = "A youth from the West. A natural leader who is skilled with animals.";
        heroAbilityTitle = "";
        heroAbilityDescription = "";

        elementalType = ElementalType.NATURE;

        heroID = HeroList.LEIF;

//        initialiseThumbnail(element);
        manuallySetStats(10, 5, 100);
    }

    @Override
    public void heroAbility() {

    }
}







