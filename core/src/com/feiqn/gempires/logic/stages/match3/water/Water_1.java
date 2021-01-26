package com.feiqn.gempires.logic.stages.match3.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.enemies.water.WaterWizard;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.Formation;
import com.feiqn.gempires.models.stats.PlayerInventory;

import java.util.ArrayList;

public class Water_1 extends MatchScreen {

    ArrayList<Bestiary> neededEnemies;
    private Boolean firstWaveClear;
    private Boolean secondWaveClear;
    private Boolean thirdWaveClear;
    private PlayerInventory playerInventory;
    private GempiresGame game;

    public Water_1(GempiresGame game, PlayerInventory inventory) {
        super(game, CampaignLevelID.WATER_1);
        this.game = game;
        firstWaveClear = false;
        secondWaveClear = false;
        thirdWaveClear = false;
        neededEnemies = new ArrayList<>();
        playerInventory = inventory;
    }

    @Override
    public void show() {
        super.show();

        neededEnemies.add(Bestiary.WATER_WIZARD);
        initAdventureMode(neededEnemies);

        setEnemyDifficulty(2);

        final ArrayList<Enemy> wave = new ArrayList<>();
        final WaterWizard w = new WaterWizard(waterWizardTextureRegion);
        wave.add(w);

        deployWave(wave, Formation.ONE_CENTER);

    }

    @Override
    public void clearWave() {
        if(firstWaveClear && secondWaveClear && thirdWaveClear) {
            // stage clear
            for(ItemList i : loot) {
                Gdx.app.log("You got", "" + i);
                playerInventory.addItem(i);
            }
            allowUserInput = false;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    game.setScreen(new CastleScreen(game));
                }
            }, 3);

        } else if(firstWaveClear && secondWaveClear) {
            thirdWaveClear = true;
            // fourth wave?
            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w2 = new WaterWizard(waterWizardTextureRegion);

            wave.add(w3);
            wave.add(w2);
// TODO
            deployWave(wave, Formation.TWO_IN_FRONT);


        } else if(firstWaveClear && !thirdWaveClear) {
            secondWaveClear = true;
            // deploy wave 3
            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w2 = new WaterWizard(waterWizardTextureRegion);
// TODO
            wave.add(w3);
            wave.add(w2);

            deployWave(wave, Formation.TWO_IN_FRONT);


        } else if(!firstWaveClear && !secondWaveClear && !thirdWaveClear) {
            firstWaveClear = true;
            // deploy wave2

            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w2 = new WaterWizard(waterWizardTextureRegion);

            wave.add(w3);
            wave.add(w2);

            deployWave(wave, Formation.TWO_IN_FRONT);

        }
    }
}
