package com.feiqn.gempires.models;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;

import java.util.ArrayList;
import java.util.HashMap;

public class GempiresAssetHandler {

    private final GempiresGame game;
    private final AssetManager manager;
    private final HashMap<Bestiary, Boolean> enemyTextureIsInitialized;

    public Texture barracksTexture,
                   avatarTexture;

    public TextureRegion[] gemTextures,
                           attackTokenTextures;

    public TextureRegion // ENEMIES
                         waterWizardTextureRegion,
                         darkKnightTextureRegion,
                         fireDragonTextureRegion,

                         // UI
                         purpleButtonTexture,
                         blueButtonTexture,
                         yellowButtonTexture,

                         // STRUCTURES
                         foodTexture,
                         oreTexture,
                         arcanaTexture,
                         thymeTexture,
                         pureGemTexture,
                         farmTexture,
                         mineTexture,
                         libraryTexture,
                         siloTexture,
                         warehouseTexture,
                         archivesTexture,
                         alchemistTexture,
                         goddessStatueTexture,
                         summoningPyreTexture,

                         altarFireTexture,
                         altarWaterTexture,
                         altarVoidTexture,
                         altarElectricTexture,
                         altarNatureTexture,
                         altarStoneTexture,

                         campaignSelectorVoidTexture,
                         campaignSelectorWaterTexture,
                         campaignSelectorFireTexture,
                         campaignSelectorElectricTexture,
                         campaignSelectorNatureTexture,
                         campaignSelectorStoneTexture,

                         menuTexture,
                         stoneCardTexture,
                         waterCardTexture,
                         fireCardTexture,
                         electricCardTexture,
                         voidCardTexture,
                         natureCardTexture,
                         backButtonTexture;


    public GempiresAssetHandler(GempiresGame game) {
        this.game = game;
        manager = new AssetManager();

        enemyTextureIsInitialized = new HashMap<>(Bestiary.values().length);
        for(int i = 0; i < Bestiary.values().length; i++) {
            enemyTextureIsInitialized.put(Bestiary.values()[i], false);
        }

        loadEverything();
    }

    public void initialiseGemTextures(Boolean classicMode) {
        final Texture gemSpriteSheet = manager.get("gem_set.png", Texture.class);
        gemTextures = new TextureRegion[] {
                new TextureRegion(gemSpriteSheet, 160, 0,   32, 32),  // GREEN 0
                new TextureRegion(gemSpriteSheet, 128, 64,  32, 32),  // PURPLE 1
                new TextureRegion(gemSpriteSheet, 32,  128, 32, 32),  // RED 2
                new TextureRegion(gemSpriteSheet, 0,   192, 32, 32),  // ORANGE 3
                new TextureRegion(gemSpriteSheet, 0,   288, 32, 32),  // YELLOW 4
                new TextureRegion(gemSpriteSheet, 160, 320, 32, 32),  // BLUE 5
                new TextureRegion(gemSpriteSheet, 160, 384, 32, 32)   // CLEAR 6, not to be confused with BLANK 7
        };

        if(!classicMode) {
            attackTokenTextures = new TextureRegion[] {
                    new TextureRegion(gemSpriteSheet, 128,0,   32, 32),
                    new TextureRegion(gemSpriteSheet, 160,64,  32, 32),
                    new TextureRegion(gemSpriteSheet, 0,  128, 32, 32),
                    new TextureRegion(gemSpriteSheet, 32, 192, 32, 32),
                    new TextureRegion(gemSpriteSheet, 0,  256, 32, 32),
                    new TextureRegion(gemSpriteSheet, 128,320, 32, 32),
                    new TextureRegion(gemSpriteSheet, 128,384, 32, 32)
            };
        }
    }

    public void initialiseEnemyTextures(ArrayList<Bestiary> neededEnemies) {
        for(Bestiary beast : neededEnemies) {
            switch(beast) {
                case WATER_WIZARD:
                    if(!enemyTextureIsInitialized.get(Bestiary.WATER_WIZARD)) {
                        final Texture wizardSpriteSheet = manager.get("characters/enemies/water/wizard_spritesheet.png", Texture.class);
                        waterWizardTextureRegion = new TextureRegion(wizardSpriteSheet, 64, 288, 64, 64);

                        enemyTextureIsInitialized.put(Bestiary.WATER_WIZARD, true);
                    }
                    break;

                case DARK_KNIGHT:
                    if(!enemyTextureIsInitialized.get(Bestiary.DARK_KNIGHT)) {
                        final Texture darkKnightSprite = manager.get("characters/enemies/dark/dark_knight.png", Texture.class);
                        darkKnightTextureRegion = new TextureRegion(darkKnightSprite, 0,0, 64,64);

                        enemyTextureIsInitialized.put(Bestiary.DARK_KNIGHT, true);
                    }
                    break;

                case FIRE_DRAGON:
                    if(!enemyTextureIsInitialized.get(Bestiary.FIRE_DRAGON)) {
                        final Texture dragonSpriteSheet = manager.get("characters/enemies/DAGRONS5.png", Texture.class);
                        fireDragonTextureRegion = new TextureRegion(dragonSpriteSheet,32*5,0,96,96);

                        enemyTextureIsInitialized.put(Bestiary.FIRE_DRAGON, true);
                    }
                    break;

                case STONE_DRAKE:
                case FOREST_DRAGON:
                case ICE_FIEND:
                default:
                    break;
            }
        }
    }

    public void initialiseCastleTextures() {
        // TODO: new, isometric textures, please.

        avatarTexture = manager.get("avatars/vivian.png", Texture.class);

        final Texture gemSpriteSheet = manager.get("gem_set.png", Texture.class);
        pureGemTexture = new TextureRegion(gemSpriteSheet, 0, 384, 32,32);

        final Texture goddessSpriteSheet = manager.get("structures/goddessSpriteSheet.png", Texture.class);
        goddessStatueTexture = new TextureRegion(goddessSpriteSheet,672, 64, 32, 64);

        barracksTexture = manager.get("structures/barracks.png", Texture.class);

        final Texture menuSpriteSheet = manager.get("ui/menu.png", Texture.class);
        menuTexture = new TextureRegion(menuSpriteSheet, 0,  192,96, 96);
        backButtonTexture = new TextureRegion(menuSpriteSheet, 192,0 , 32, 32);
        yellowButtonTexture = new TextureRegion(menuSpriteSheet, 96, 192, 192,64);
        purpleButtonTexture = new TextureRegion(menuSpriteSheet, 96, 256, 192,64);
        blueButtonTexture = new TextureRegion(menuSpriteSheet,96, 320, 192, 64);

        final Texture cardSpriteSheet = manager.get("ui/heroCards.png", Texture.class);
        electricCardTexture = new TextureRegion(cardSpriteSheet, 32*5, 32*4, 96, 128);
        stoneCardTexture = new TextureRegion(cardSpriteSheet, 0, 32*8, 96, 128);
        fireCardTexture = new TextureRegion(cardSpriteSheet, 32*5, 32*8, 96, 128);
        waterCardTexture = new TextureRegion(cardSpriteSheet, 0, 32*12, 96, 128);
        voidCardTexture = new TextureRegion(cardSpriteSheet, 32*5, 32*12, 96, 128);
        natureCardTexture = new TextureRegion(cardSpriteSheet, 0, 0, 96, 128);

        final Texture iconSpriteSheet = manager.get("icon-pack.png", Texture.class);
        foodTexture = new TextureRegion(iconSpriteSheet, 256, 0, 32, 32);
        arcanaTexture = new TextureRegion(iconSpriteSheet, 320, 0, 32, 32);

        final Texture buildingSpriteSheet = manager.get("structures/buildingSpriteSheet.png", Texture.class);
        farmTexture =                       new TextureRegion(buildingSpriteSheet, 0, 128, 32, 32);
        mineTexture =                       new TextureRegion(buildingSpriteSheet, 160, 96, 32, 32);
        libraryTexture =                    new TextureRegion(buildingSpriteSheet, 64, 0, 32, 32);
        alchemistTexture =                  new TextureRegion(buildingSpriteSheet, 32*7, 0, 32,32);
        archivesTexture =                   new TextureRegion(buildingSpriteSheet, 32*5, 32*5, 32,32);
        siloTexture =                       new TextureRegion(buildingSpriteSheet, 32, 32*7, 32, 32);
        warehouseTexture =                  new TextureRegion(buildingSpriteSheet, 32, 32*5, 32,32);
        altarWaterTexture =                 new TextureRegion(buildingSpriteSheet, 32*5, 0, 32,32);

        final Texture itemSpriteSheet   = manager.get("ui/RPG_Item_Pack.png", Texture.class);
        campaignSelectorVoidTexture     = new TextureRegion(itemSpriteSheet,64, 0,  16,16);
        campaignSelectorWaterTexture    = new TextureRegion(itemSpriteSheet,64, 16, 16,16);
        campaignSelectorElectricTexture = new TextureRegion(itemSpriteSheet,64, 32, 16,16);
        campaignSelectorFireTexture     = new TextureRegion(itemSpriteSheet,64, 48, 16,16);
        campaignSelectorNatureTexture   = new TextureRegion(itemSpriteSheet,64, 64, 16,16);
        campaignSelectorStoneTexture    = new TextureRegion(itemSpriteSheet,64, 70, 16,16);
        thymeTexture                    = new TextureRegion(itemSpriteSheet,16*6, 16*14, 16, 16);
        oreTexture                      = new TextureRegion(itemSpriteSheet, 32, 16*7, 16,16);

        // TODO: summoningPyreTexture
    }

    public void loadEverything() {
        loadCastleTextures();
        loadEnemyTextures();
        loadHeroTextures();
        loadMatchScreenTextures();
    }
    public void loadEnemyTextures() {
        manager.load("characters/enemies/water/wizard_spritesheet.png", Texture.class);
        manager.load("characters/enemies/dark/dark_knight.png", Texture.class);
        manager.load("characters/enemies/DAGRONS5.png", Texture.class);
    }
    public void loadHeroTextures() {

    }
    public void loadCastleTextures() {
        manager.load("avatars/vivian.png", Texture.class);
        manager.load("gem_set.png", Texture.class);
        manager.load("structures/goddessSpriteSheet.png", Texture.class);
        manager.load("structures/barracks.png", Texture.class);
        manager.load("ui/menu.png", Texture.class);
        manager.load("ui/heroCards.png", Texture.class);
        manager.load("icon-pack.png", Texture.class);
        manager.load("structures/buildingSpriteSheet.png", Texture.class);
        manager.load("ui/RPG_Item_Pack.png", Texture.class);
    }
    public void loadMatchScreenTextures() {
        manager.load("gem_set.png", Texture.class);
    }

    // GETTERS
    public AssetManager getManager() { return manager; }
}
