package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.characters.heroes.Heroes;
import com.feiqn.gempires.logic.characters.heroes.nature.Leif;

import java.util.Random;

public class HeroRoster {
    // includes: which heroes are owned, and the levels / stats / equipment of said heroes

    private final Preferences pref;

    private DelayedRemovalArray<HeroCard> heroList;

    public int numberOfHeroes;

    private final CastleScreen parentCastle;

    public HeroRoster(CastleScreen parent) {
        pref = Gdx.app.getPreferences("PlayerHeroRoster");
        heroList = new DelayedRemovalArray<>();

        parentCastle = parent;

        if(!pref.contains("NumberOfHeroes")) {
            // Welcome to Gempires.
            pref.putInteger("NumberOfHeroes", 0);

        }
        numberOfHeroes = pref.getInteger("NumberOfHeroes");

        if(numberOfHeroes > 0) {
            fillHeroes();
        } else {
            Gdx.app.log("HeroRoster", "New Player");
            // TODO: add starter heroes
            // addHero(new Leif(parentCastle.natureCardRegion, parentCastle));
        }

        pref.flush();
    }

    public void flushHeroes() {
        // Save

        for(HeroCard hero : heroList) {
            pref.putString("name" + hero.heroID, hero.heroName);
            pref.putInteger("level" + hero.heroID, hero.getLevel());
            pref.putInteger("bravery" + hero.heroID, hero.getBravery());
            pref.putBoolean("isPure" + hero.heroID, hero.getPurity());
        }

        pref.flush();
    }

    private void restoreHero(HeroCard hero) {

        final int lvl = pref.getInteger("level" + hero.heroID);
        final int brv = pref.getInteger("bravery" + hero.heroID);
        final boolean pure =  pref.getBoolean("isPure" + hero.heroID);

        final int targetTrueLevel = ((brv * 100) - 100) + lvl;

        hero.scaleToTrueLevel(targetTrueLevel);

        heroList.add(hero);
    }

    private void fillHeroes() {
        // Load
        Gdx.app.log("restore", "restoring...");

        for(int i = 0; i < Heroes.values().length; i++) {
            final Heroes h = Heroes.values()[i];
            if(pref.contains("name" + h)){
                switch(h) {
                    case CUNNING_CRAFTSMAN:
                    case DISPARATE_DIGGER:
                    case VENGEFUL_FARMER:
                    case SEASICK_SAILOR:
                    case ORPHANED_YOUTH:
                    case LAZY_LABORER:
                    case DREAMY_DRUID:
                    case DARING_CHEF:
                        // ZERO-STAR, COMMON-NOUN UNITS ARE LOADED BY PlayerInventory
                        break;
                    case LEIF:
                        Gdx.app.log("restore", "restored leif");
                        final Leif leif = new Leif(parentCastle.natureCardRegion, parentCastle);
                        restoreHero(leif);
                        break;
                }
            }
        }

        pref.flush();
    }

    public void goddessSummon() {
        final Random random = new Random();
        final int randomIndex = random.nextInt(Heroes.values().length);
        final Heroes newHero = Heroes.values()[randomIndex];

        Gdx.app.log("summon", "got: " + newHero);

        switch(newHero) {
            case DARING_CHEF:
            case DREAMY_DRUID:
            case LAZY_LABORER:
            case ORPHANED_YOUTH:
            case SEASICK_SAILOR:
            case VENGEFUL_FARMER:
            case DISPARATE_DIGGER:
            case CUNNING_CRAFTSMAN:
                parentCastle.playerInventory.addCommonUnit(newHero);
                break;
            case LEIF:
                if(!pref.contains("name" + newHero)) {
                    addHero(new Leif(parentCastle.natureCardRegion, parentCastle));
                } else {
                    // TODO: add braveryToken
                }
                break;
            default:
                break;
        }

    }

    public void addHero(HeroCard hero) {
        heroList.add(hero);
        numberOfHeroes++;
        pref.putInteger("NumberOfHeroes", numberOfHeroes);
        flushHeroes();
    }

    public void removeHero(HeroCard hero) {
        heroList.begin();
        heroList.removeValue(hero, true);
        heroList.end();

        pref.remove("name" + hero.heroID);
        pref.remove("level" + hero.heroID);
        pref.remove("bravery" + hero.heroID);
        pref.remove("isPure" + hero.heroID);

        numberOfHeroes--;
        pref.putInteger("NumberOfHeroes", numberOfHeroes);

        pref.flush();
    }

    public DelayedRemovalArray<HeroCard> getHeroList(){
        return heroList;
    }

}
