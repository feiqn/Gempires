package com.feiqn.gempires.models.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.*;

public class CastleStats {
    // includes: which buildings are where, what level they are, if units are garrisoned and which units those are, and summoner stats

    private final Preferences pref;

    public CastleScreen parentCastle;

    private DelayedRemovalArray<Structure> structures;

    private int goddessStatueLevel,
                summonerLevel,
                summonerExp,
                numberOfStructures;

    public CastleStats(CastleScreen parent) {
        this.parentCastle = parent;

        pref = Gdx.app.getPreferences("PlayerCastleStats");

        if(!pref.contains("NumberOfStructures")) {
            pref.putInteger("NumberOfStructures", 0);
        }
        numberOfStructures = pref.getInteger("NumberOfStructures");

        if(!pref.contains("GoddessStatueLevel")) {
            pref.putInteger("GoddessStatueLevel", 1);
        }
        goddessStatueLevel = pref.getInteger("GoddessStatueLevel");

        if(!pref.contains("SummonerLevel")) {
            pref.putInteger("SummonerLevel", 1);
        }
        summonerLevel      = pref.getInteger("SummonerLevel");

        if(!pref.contains("SummonerExp")) {
            pref.putInteger("SummonerExp", 0);
        }
        summonerExp        = pref.getInteger("SummonerExp");

        structures = new DelayedRemovalArray<>();

        if(!pref.contains("NewGame?")) {
            pref.putBoolean("NewGame?", false);
            // do first time setup stuff here
            final Barracks barracks = new Barracks(parentCastle.barracksTexture, parentCastle);
            barracks.setPosition(59.5f,64.5f);
            addStructure(barracks);

            final GoddessStatue goddessStatue = new GoddessStatue(parentCastle.goddessStatueTexture, parentCastle);
            goddessStatue.setPosition(55.5f, 64.5f);
            addStructure(goddessStatue);

            final Mine mine = new Mine(parentCastle.mineTexture, parentCastle);
            mine.setPosition(51.5f, 64f);
            addStructure(mine);

            final Farm farm = new Farm(parentCastle.farmTexture, parentCastle);
            farm.setPosition(52.5f, 63.5f);
            addStructure(farm);
        } else {
            fillStructures();
        }

        pref.flush();
    }

    private void fillStructures() {
        // load
        for(int i = 0; i < Structure.StructureType.values().length; i++) {
            final Structure.StructureType structType = Structure.StructureType.values()[i];
            final int structNum;
            if(!pref.contains("NumberOfStructureType" + structType)) {
                structNum = 0;
            } else {
                structNum = pref.getInteger("NumberOfStructureType" + structType);
            }
            for(int s = 0; s <= structNum; s++) {
                if(pref.contains("UniqueStructureName" + structType + s)) {
                    final int goalLevel = pref.getInteger("UniqueStructureNameLevel" + structType + s);
                    final float destinationX = pref.getFloat("UniqueStructureNameX" + structType + s);
                    final float destinationY = pref.getFloat("UniqueStructureNameY" + structType + s);

                    final Structure str;

                    switch(structType) {
                        case FARM:
                            str = new Farm(parentCastle.farmTexture, parentCastle);

                            Gdx.app.log("fillStructs", "farm");
                            str.setX(destinationX);// TO BE DELETED
                            str.setY(destinationY);// TO BE DELETED
                            while(str.getLevel() < goalLevel) {// TO BE DELETED
                                str.levelUp();// TO BE DELETED
                            }// TO BE DELETED
                            structures.add(str);// TO BE DELETED

                            break;
                        case MINE:
                            str = new Mine(parentCastle.mineTexture, parentCastle);

                            Gdx.app.log("fillStructs", "mine");
                            str.setX(destinationX);// TO BE DELETED
                            str.setY(destinationY);// TO BE DELETED
                            while(str.getLevel() < goalLevel) {// TO BE DELETED
                                str.levelUp();// TO BE DELETED
                            }// TO BE DELETED
                            structures.add(str);// TO BE DELETED

                            break;
                        case SILO:
                        case ALTAR:
                        case TURRET:
                        case ACADEMY:
                        case ARCHIVE:
                        case LIBRARY:
                        case BARRACKS:
                            str = new Barracks(parentCastle.barracksTexture, parentCastle);

                            str.setX(destinationX);// TO BE DELETED
                            str.setY(destinationY);// TO BE DELETED
                            while(str.getLevel() < goalLevel) {// TO BE DELETED
                                str.levelUp();// TO BE DELETED
                            }// TO BE DELETED
                            structures.add(str);// TO BE DELETED

                            break;
                        case GARRISON:
                        case ALCHEMIST:
                        case BARRICADE:
                        case WAREHOUSE:
                        case CLAN_TOWER:
                        case GODDESS_STATUE:
                            str = new GoddessStatue(parentCastle.goddessStatueTexture, parentCastle);

                            str.setX(destinationX);// TO BE DELETED
                            str.setY(destinationY);// TO BE DELETED
                            while(str.getLevel() < goalLevel) { // TO BE DELETED
                                str.levelUp(); // TO BE DELETED
                            } // TO BE DELETED
                            structures.add(str); // TO BE DELETED

                        case SUMMONING_CIRCLE:
                            break;
                    }
                    // TODO: delete redundant code above and uncomment below, after all structure textures have been finished
//                    str.setX(destinationX);
//                    str.setY(destinationY);
//                    while(str.getLevel() < goalLevel) {
//                        str.levelUp();
//                    }
//                    structures.add(str);
                }
            }
        }
    }

    private void flushStructures() {
        // save
        for(Structure.StructureType t : Structure.StructureType.values()) {
            if(pref.contains("NumberOfStructureType" + t)) {
                pref.putInteger("NumberOfStructureType" + t, 0);
            }
        }

        for(Structure struct : structures) {
            final int numberOfStructureType = pref.getInteger("NumberOfStructureType" + struct.structureType);

            pref.putString("UniqueStructureName" + struct.structureType + numberOfStructureType, "" + struct.structureType);
            pref.putInteger("UniqueStructureNameLevel" + struct.structureType + numberOfStructureType, struct.getLevel());

            /* TODO: because of how relative positions are set in CastleScreen currently, saving positions in this
                     way likely will not work as hoped. I believe this problem will solve itself after buildable
                     tiles are finished, however.
            */
            pref.putFloat("UniqueStructureNameX" + struct.structureType + numberOfStructureType, struct.getX());
            pref.putFloat("UniqueStructureNameY" + struct.structureType + numberOfStructureType, struct.getY());

            pref.putInteger("NumberOfStructureType" + struct.structureType, numberOfStructureType + 1);
        }

        pref.flush();
    }

    // SETTERS
    public void addStructure(Structure struct) {
        structures.add(struct);
        numberOfStructures++;
        pref.putInteger("NumberOfStructures", numberOfStructures);
        flushStructures();
    }
    public void removeStructure(Structure struct) {
        // TODO
    }
    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
        pref.putInteger("SummonerLevel", summonerLevel);
        pref.flush();
    }
    public void setGoddessStatueLevel(int goddessStatueLevel) {
        this.goddessStatueLevel = goddessStatueLevel;
        pref.putInteger("GoddessStatueLevel", goddessStatueLevel);
        pref.flush();
    }

    // GETTERS
    public DelayedRemovalArray<Structure> getStructures() { return structures; }
    public int getSummonerExp() { return summonerExp; }
    public int getSummonerLevel() { return summonerLevel; }
    public int getGoddessStatueLevel() { return goddessStatueLevel; }

}
