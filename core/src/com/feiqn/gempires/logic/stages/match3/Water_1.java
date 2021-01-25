package com.feiqn.gempires.logic.stages.match3;

import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.Gem;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.enemies.water.WaterWizard;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.Formation;

import java.util.ArrayList;

public class Water_1 extends MatchScreen{

    ArrayList<Bestiary> neededEnemies;
    private Boolean firstWaveClear;
    private Boolean secondWaveClear;
    private Boolean thirdWaveClear;

    public Water_1(GempiresGame game) {
        super(game, CampaignLevelID.WATER_1);
        firstWaveClear = false;
        secondWaveClear = false;
        thirdWaveClear = false;
        neededEnemies = new ArrayList<>();
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

        } else if(firstWaveClear && secondWaveClear) {
            thirdWaveClear = true;
            // fourth wave?

        } else if(firstWaveClear && !thirdWaveClear) {
            secondWaveClear = true;
            // deploy wave 3

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
