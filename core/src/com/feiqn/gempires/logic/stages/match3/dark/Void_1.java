package com.feiqn.gempires.logic.stages.match3.dark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.enemies.Enemy;
import com.feiqn.gempires.logic.characters.enemies.dark.DarkKnight;
import com.feiqn.gempires.logic.characters.enemies.water.WaterWizard;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.models.CampaignLevelID;
import com.feiqn.gempires.models.ElementalType;
import com.feiqn.gempires.models.Formation;
import com.feiqn.gempires.models.stats.PlayerInventory;

import java.util.ArrayList;

public class Void_1 extends MatchScreen {

    final ArrayList<Bestiary> neededEnemies;
    private Boolean firstWaveClear;
    private Boolean secondWaveClear;
    private Boolean thirdWaveClear;
    final private PlayerInventory playerInventory;
    final private GempiresGame game;
    final private CastleScreen parent;
    final private ArrayList<HeroCard> team;

    public Void_1(CastleScreen castle) {
        super(castle.game, CampaignLevelID.VOID_1);
        parent = castle;
        this.game = castle.game;
        firstWaveClear = false;
        secondWaveClear = false;
        thirdWaveClear = false;
        neededEnemies = new ArrayList<>();
        playerInventory = parent.playerInventory;
        this.team = parent.heroRoster.getTeam(parent.heroRoster.defaultTeam);
    }

    @Override
    public void show() {
        super.show();

        neededEnemies.add(Bestiary.DARK_KNIGHT);
        initAdventureMode(neededEnemies);

        calculateGemPower(team);
        deployHeroes(team);

        setEnemyDifficulty(2);

        final ArrayList<Enemy> wave = new ArrayList<>();

        final DarkKnight d = new DarkKnight(darkKnightTextureRegion);
        wave.add(d);

        deployWave(wave, Formation.ONE_CENTER);
    }

    @Override
    public void clearWave() { // TODO: change these to void enemies instead of water
        if(firstWaveClear && secondWaveClear && thirdWaveClear) {
            // stage clear
            for(ItemList i : loot) {
                Gdx.app.log("You got", "" + i);
                playerInventory.addItem(i);
            }
            allowUserInput = false;

            parent.castleStats.setStageCleared(ElementalType.VOID, 1);

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    game.setScreen(parent);
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

            deployWave(wave, Formation.TWO_STAGGERED_LEFT);

        } else if(firstWaveClear && !thirdWaveClear) {
            secondWaveClear = true;
            // deploy wave 3
            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w2 = new WaterWizard(waterWizardTextureRegion);

            wave.add(w3);
            wave.add(w2);

            deployWave(wave, Formation.TWO_IN_FRONT);

        } else if(!firstWaveClear && !secondWaveClear && !thirdWaveClear) {
            firstWaveClear = true;
            // deploy wave2

            final ArrayList<Enemy> wave = new ArrayList<>();
            final WaterWizard w3 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w2 = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w  = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w4  = new WaterWizard(waterWizardTextureRegion);
            final WaterWizard w5  = new WaterWizard(waterWizardTextureRegion);

            wave.add(w5);
            wave.add(w4);
            wave.add(w3);
            wave.add(w2);
            wave.add(w);

            deployWave(wave, Formation.FIVE_ASCENDING);

        }
    }
}
