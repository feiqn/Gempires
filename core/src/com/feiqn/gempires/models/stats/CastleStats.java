package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.castle.Silo;
import com.feiqn.gempires.logic.castle.Structure;

public class CastleStats {
    // includes: which buildings are where, what level they are, if units are garrisoned and which units those are, and summoner stats
    // TODO: serialise pref to Json for storage, write Json parser

    private Preferences pref;

    public CastleScreen parentCastle;

    public Array<Structure> structures;

    private int goddessStatueLevel,
                goddessStatueExp,
                summonerLevel,
                summonerExp;

    public CastleStats(CastleScreen parent) {
        this.parentCastle = parent;

        pref = Gdx.app.getPreferences("Player Castle Stats");

        goddessStatueLevel = pref.getInteger("goddessStatueLevel");
        goddessStatueExp   = pref.getInteger("goddessStatueExp");
        summonerLevel      = pref.getInteger("summonerLevel");
        summonerExp        = pref.getInteger("summonerExp");

        structures = new Array<>(); // TODO: write a helper method for adding / reading Arrays in pref

        // debug
        final Silo silo = new Silo(parentCastle.foodIcon, parentCastle);
        structures.add(silo);
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

    public void setGoddessStatueExp(int goddessStatueExp) {
        this.goddessStatueExp = goddessStatueExp;
        pref.putInteger("goddessStatueExp", goddessStatueExp);
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
    public int getGoddessStatueExp() {
        return goddessStatueExp;
    }

}
