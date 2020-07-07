package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.feiqn.gempires.logic.heroes.HeroCard;


public class HeroRoster {
    // includes: which heroes are owned, and the levels / stats / equipment of said heroes
    // TODO: serialise pref to Json for storage, write Json parser

    private Preferences pref;

    private Array<HeroCard> heroes;

    public HeroRoster() {
        pref = Gdx.app.getPreferences("Player Hero Roster");

    }

    public void addHero(HeroCard hero) {
        heroes.add(hero);
    }

    public void removeHero(HeroCard hero) {
        heroes.removeValue(hero, true);
    }

    // TODO: list members of each hero and add them to pref

}
