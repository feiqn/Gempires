package com.feiqn.gempires.logic.stages.match3.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.enemies.water.WaterWizard;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.ElementalType;
import com.feiqn.gempires.models.Formation;

import java.util.ArrayList;

public class Water_1 extends MatchScreen {

    public Water_1(GempiresGame game) {
        super(game.castle.game, CampaignLevelID.WATER_1);
    }

    @Override
    public void show() {
        super.show();

        neededEnemies.add(Bestiary.WATER_WIZARD);
        initAdventureMode(neededEnemies);

        calculateGemPower(team);
        deployHeroes(team);

        setEnemyDifficulty(2);

        final ArrayList<Enemy> wave = new ArrayList<>();
        final WaterWizard w = new WaterWizard(game);
        wave.add(w);

        deployWave(wave, Formation.ONE_CENTER);

    }

    @Override
    public void clearWave() {
        if(firstWaveClear && secondWaveClear && thirdWaveClear) {
            // stage clear
            for(ItemList i : loot) {
                Gdx.app.log("You got", "" + i);
                game.castle.playerInventory.addItem(i);
            }
            allowUserInput = false;

            game.castle.castleStats.setStageCleared(ElementalType.WATER, 1);

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    game.setScreen(game.castle);
                }
            }, 3);

        } else if(firstWaveClear && secondWaveClear) {
            thirdWaveClear = true;
            // fourth wave?
            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(game);
            final WaterWizard w2 = new WaterWizard(game);

            wave.add(w3);
            wave.add(w2);

            deployWave(wave, Formation.TWO_STAGGERED_LEFT);

        } else if(firstWaveClear && !thirdWaveClear) {
            secondWaveClear = true;
            // deploy wave 3
            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(game);
            final WaterWizard w2 = new WaterWizard(game);

            wave.add(w3);
            wave.add(w2);

            deployWave(wave, Formation.TWO_IN_FRONT);

        } else if(!firstWaveClear && !secondWaveClear && !thirdWaveClear) {
            firstWaveClear = true;
            // deploy wave2

            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3  = new WaterWizard(game);
            final WaterWizard w2  = new WaterWizard(game);
            final WaterWizard w   = new WaterWizard(game);
            final WaterWizard w4  = new WaterWizard(game);
            final WaterWizard w5  = new WaterWizard(game);

            wave.add(w5);
            wave.add(w4);
            wave.add(w3);
            wave.add(w2);
            wave.add(w);

            deployWave(wave, Formation.FIVE_ASCENDING);
        }
    }
}
