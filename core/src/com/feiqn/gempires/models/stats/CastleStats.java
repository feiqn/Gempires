package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CastleStats {
    // includes: which buildings are where, what level they are, if units are garrisoned and which units those are, and summoner stats
    // TODO: serialise pref to Json for storage, write Json parser

    private Preferences pref;

    private int goddessStatueLevel;
    private int goddessStatueExp;

    private int summonerLevel;
    private int summonerExp;

    public CastleStats() {
        pref = Gdx.app.getPreferences("Player Castle Stats");
        goddessStatueLevel = pref.getInteger("goddessStatueLevel");
        goddessStatueExp = pref.getInteger("goddessStatueExp");
        summonerLevel = pref.getInteger("summonerLevel");
        summonerExp = pref.getInteger("summonerExp");
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
