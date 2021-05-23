package com.feiqn.gempires.logic.characters.heroes.dark;

import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.models.ElementalType;

public class Vivain extends HeroCard {

    public Vivain(CastleScreen parentCastle) {
        super(parentCastle.game.gempiresAssetHandler.vivainCardTexture, parentCastle, parentCastle.game.gempiresAssetHandler.vivainTexture, ElementalType.VOID);

        heroName = "Vivain";
        heroTitle = "Bat Queen";
        heroDescription = "";
        heroAbilityTitle = "";
        heroAbilityDescription = "";

//        elementalType = ElementalType.VOID;

        heroID = HeroList.VIVAIN;

//        initialiseThumbnail(element);
        manuallySetStats(30, 1.5f, 300);
    }

    @Override
    public void heroAbility() {

    }
}
