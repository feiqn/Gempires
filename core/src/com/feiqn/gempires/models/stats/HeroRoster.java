package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import com.feiqn.gempires.logic.characters.heroes.nature.Leif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HeroRoster {
    // includes: teams, which heroes are owned, and the levels / stats / equipment of said heroes

    private final Preferences pref;

    private final DelayedRemovalArray<HeroCard> ownedHeroes;
    private final ArrayList<ArrayList<HeroCard>> teams;
    public int defaultTeam;

    private final HashMap<HeroList, Integer> braveryTokens;

    public int numberOfHeroes;

    private final CastleScreen parentCastle;

    public HeroRoster(CastleScreen parent) {
        defaultTeam = 0;
        pref = Gdx.app.getPreferences("PlayerHeroRoster");
        ownedHeroes = new DelayedRemovalArray<>();
        braveryTokens = new HashMap<>();
        teams = new ArrayList<>();
        teams.add(new ArrayList<HeroCard>()); // TODO: better
//        initTeams();

        parentCastle = parent;

        if(!pref.contains("NumberOfHeroes")) {
            // Welcome to Gempires.
            pref.putInteger("NumberOfHeroes", 0);

        }
        numberOfHeroes = pref.getInteger("NumberOfHeroes");

        if(numberOfHeroes > 0) {
            fillHeroes();
            fillBraveryTokens();
            fillTeams();
        } else {
            Gdx.app.log("HeroRoster", "New Player");

            // TODO: add starter heroes
            final HeroCard leif = new Leif(parentCastle.natureCardTexture, parentCastle);
            addHero(leif);
            teams.get(defaultTeam).add(leif);

            flushTeams();
        }

        pref.flush();
    }

    public void initTeams() {
        for(int l = 0; l < teams.size(); l++) {
            final ArrayList<HeroCard> nt = new ArrayList<>();
            teams.add(nt);
        }
    }

    public void flushTeams() {
        // save teams<>

        for(int t = 0; t < teams.size(); t++) {
            final ArrayList<HeroCard> thisTeam = teams.get(t);

            for(int teamSize = 0; teamSize < thisTeam.size(); teamSize++) {

                if(thisTeam.get(teamSize) != null) {
                    final HeroCard thisHero = thisTeam.get(teamSize);
                    pref.putString("Team" + t + "Member" + teamSize, "" + thisHero.heroName.toUpperCase());
                } else {
                    pref.putString("Team" + t + "Member" + teamSize, "none");
                }
            }
        }

        pref.flush();
    }

    public void fillTeams() {
        // load teams<>

        for(int t = 0; t < teams.size(); t++) {
            final ArrayList<HeroCard> thisTeam = teams.get(t);

            for (int teamSize = 0; teamSize <= thisTeam.size(); teamSize++) {

                final String string = pref.getString("Team" + t + "Member" + teamSize);
                // TODO: surely there's a better way to do this.

                switch(string) {
                    case "LEIF":
                        for(HeroCard hero : ownedHeroes) {
                            if(hero.heroID == HeroList.LEIF) {
                                thisTeam.add(hero);
                                break;
                            }
                        }
                        break;
                    case "none":
                        break;
                }
            }
        }
    }

    public void flushHeroes() {
        // Save

        for(HeroCard hero : ownedHeroes) {
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

        ownedHeroes.add(hero);
    }

    private void fillHeroes() {
        // Load

        for(int i = 0; i < HeroList.values().length; i++) {
            final HeroList h = HeroList.values()[i];
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
                        final Leif leif = new Leif(parentCastle.natureCardTexture, parentCastle);
                        restoreHero(leif);
                        break;
                }
            }
        }
        pref.flush();
    }

    private void fillBraveryTokens() {
        for(int i = 0; i < HeroList.values().length; i++) {
            final HeroList hero = HeroList.values()[i];
            if(pref.contains("countBraveryToken" + hero)) {
                final int count = pref.getInteger("countBraveryToken" + hero);
                for(int c = 0; c < count; c++) {
                    addBraveryToken(hero);
                }
            }
        }
    }

    public void goddessSummon() {
        final Random random = new Random();
        final int randomIndex = random.nextInt(HeroList.values().length);
        final HeroList newHero = HeroList.values()[randomIndex];

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
                    addHero(new Leif(parentCastle.natureCardTexture, parentCastle));
                } else {
                    addBraveryToken(newHero);
                }
                break;
            default:
                break;
        }

    }

    public void addBraveryToken(HeroList hero) {
        if(!braveryTokens.containsKey(hero)) {
            braveryTokens.put(hero, 1);
        } else {
            final int currentInt = braveryTokens.get(hero);
            braveryTokens.put(hero, currentInt + 1);
        }

        final int i = braveryTokens.get(hero);
        pref.putInteger("countBraveryToken" + hero, i);
        pref.flush();
    }

    public ArrayList<HeroCard> getTeam(int index) { return teams.get(index); }

    public void addHero(HeroCard hero) {
        ownedHeroes.add(hero);
        numberOfHeroes++;
        pref.putInteger("NumberOfHeroes", numberOfHeroes);
        flushHeroes();
    }

    public void removeHero(HeroCard hero) {
        ownedHeroes.begin();
        ownedHeroes.removeValue(hero, true);
        ownedHeroes.end();

        pref.remove("name" + hero.heroID);
        pref.remove("level" + hero.heroID);
        pref.remove("bravery" + hero.heroID);
        pref.remove("isPure" + hero.heroID);

        numberOfHeroes--;
        pref.putInteger("NumberOfHeroes", numberOfHeroes);

        pref.flush();
    }

    public DelayedRemovalArray<HeroCard> getOwnedHeroes(){ return ownedHeroes; }
    public int getBraveryTokenCount(HeroList hero) { return braveryTokens.get(hero); }

}
