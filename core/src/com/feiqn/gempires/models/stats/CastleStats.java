package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.castle.Silo;
import com.feiqn.gempires.logic.castle.Structure;

public class CastleStats {
    // includes: which buildings are where, what level they are, if units are garrisoned and which units those are, and summoner stats

    private final Preferences pref;

    public CastleScreen parentCastle;

    public DelayedRemovalArray<Structure> structures;

    private int goddessStatueLevel,
                summonerLevel,
                summonerExp;

    public CastleStats(CastleScreen parent) {
        this.parentCastle = parent;

        pref = Gdx.app.getPreferences("PlayerCastleStats");

        if(!pref.contains("goddessStatueLevel")) {
            pref.putInteger("goddessStatueLevel", 1);
        }
        goddessStatueLevel = pref.getInteger("goddessStatueLevel");

        if(!pref.contains("summonerLevel")) {
            pref.putInteger("summonerLevel", 1);
        }
        summonerLevel      = pref.getInteger("summonerLevel");

        if(!pref.contains("summonerExp")) {
            pref.putInteger("summonerExp", 0);
        }
        summonerExp        = pref.getInteger("summonerExp");

        structures = new DelayedRemovalArray<>();

        if(!pref.contains("newGame?")) {
            pref.putBoolean("newGame?", false);
            // do first time setup stuff here
            flushStructures();
        } else {
            fillStructures();
        }

        // debug
        final Silo silo = new Silo(parentCastle.foodIcon, parentCastle);
        structures.add(silo);
    }

    private void fillStructures() {
        // load
    }

    private void flushStructures() {
        // save
    }

    // SETTERS
    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
        pref.putInteger("summonerLevel", summonerLevel);
        pref.flush();
    }

    public void setGoddessStatueLevel(int goddessStatueLevel) {
        this.goddessStatueLevel = goddessStatueLevel;
        pref.putInteger("goddessStatueLevel", goddessStatueLevel);
        pref.flush();
    }


    // GETTERS
    public int getSummonerExp() {
        return summonerExp;
    }
    public int getSummonerLevel() {
        return summonerLevel;
    }
    public int getGoddessStatueLevel() {
        return goddessStatueLevel;
    }

}
