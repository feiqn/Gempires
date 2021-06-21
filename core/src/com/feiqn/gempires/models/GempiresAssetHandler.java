package com.feiqn.gempires.models;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.enemies.Bestiary;
import com.feiqn.gempires.logic.characters.heroes.HeroList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class GempiresAssetHandler {

    private final GempiresGame game;
    private final AssetManager manager;
    private final HashMap<Bestiary, Boolean> enemyTextureIsInitialized;
    private final HashMap<HeroList, Boolean> heroTextureIsInitialized;

    // TODO: y'all know we need to use that TexturePacker boi ye ye

    public Texture // STRUCTURES
                   barracksTexture,
                   mineTexture,
                   mineTexture2,

                   // SPECIAL GEMS
                   circleElectricTexture,
                   circleFireTexture,
                   circleNatureTexture,
                   circlePureTexture,
                   circleStoneTexture,
                   circleVoidTexture,
                   circleWaterTexture,

                   flowerElectricTexture,
                   flowerFireTexture,
                   flowerNatureTexture,
                   flowerPureTexture,
                   flowerStoneTexture,
                   flowerVoidTexture,
                   flowerWaterTexture,

                   heartElectricTexture,
                   heartFireTexture,
                   heartNatureTexture,
                   heartPureTexture,
                   heartStoneTexture,
                   heartVoidTexture,
                   heartWaterTexture,

                   hexagonElectricTexture,
                   hexagonFireTexture,
                   hexagonNatureTexture,
                   hexagonPureTexture,
                   hexagonStoneTexture,
                   hexagonVoidTexture,
                   hexagonWaterTexture,

                   starElectricTexture,
                   starFireTexture,
                   starNatureTexture,
                   starPureTexture,
                   starStoneTexture,
                   starVoidTexture,
                   starWaterTexture,

                   // UI
                   avatarTexture,

                   // HEROES
                   vivainTexture,
                   vivainCardTexture;

    public TextureRegion[] gemTextures,
                           attackTokenTextures;

    public TextureRegion // ENEMIES
                         waterWizardTextureRegion,
                         darkKnightTextureRegion,
                         fireDragonTextureRegion,

                         // HEROES

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

        heroTextureIsInitialized = new HashMap<>(HeroList.values().length);
        for(int i = 0; i < HeroList.values().length; i++) {
            heroTextureIsInitialized.put(HeroList.values()[i], false);
        }

        enemyTextureIsInitialized = new HashMap<>(Bestiary.values().length);
        for(int i = 0; i < Bestiary.values().length; i++) {
            enemyTextureIsInitialized.put(Bestiary.values()[i], false);
        }

        loadEverything();
    }

    public void initialiseGemTextures(Boolean classicMode) {
        final Texture gemSpriteSheet = manager.get("gem_set.png", Texture.class);
        gemTextures = new TextureRegion[] {
                new TextureRegion(gemSpriteSheet, 160, 0  , 32, 32),  // GREEN  NATURE - 0
                new TextureRegion(gemSpriteSheet, 128, 64 , 32, 32),  // PURPLE VOID --- 1
                new TextureRegion(gemSpriteSheet, 32 , 128, 32, 32),  // RED -- FIRE --- 2
                new TextureRegion(gemSpriteSheet, 0  , 192, 32, 32),  // ORANGE STONE -- 3
                new TextureRegion(gemSpriteSheet, 0  , 288, 32, 32),  // YELLOW ELECTRIC 4
                new TextureRegion(gemSpriteSheet, 160, 320, 32, 32),  // BLUE - WATER -- 5
                new TextureRegion(gemSpriteSheet, 160, 384, 32, 32)   // CLEAR  PURE --- 6 (not to be confused with BLANK 7)
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

        circleElectricTexture = manager.get("gems/Circle_electric.png", Texture.class);
        circleFireTexture     = manager.get("gems/Circle_fire.png"    , Texture.class);
        circleNatureTexture   = manager.get("gems/Circle_nature.png"  , Texture.class);
        circlePureTexture     = manager.get("gems/Circle_pure.png"    , Texture.class);
        circleStoneTexture    = manager.get("gems/Circle_stone.png"   , Texture.class);
        circleVoidTexture     = manager.get("gems/Circle_void.png"    , Texture.class);
        circleWaterTexture    = manager.get("gems/Circle_water.png"   , Texture.class);

        flowerElectricTexture = manager.get("gems/Flower_electric.png", Texture.class);
        flowerFireTexture     = manager.get("gems/Flower_fire.png"    , Texture.class);
        flowerNatureTexture   = manager.get("gems/Flower_nature.png"  , Texture.class);
        flowerPureTexture     = manager.get("gems/Flower_pure.png"    , Texture.class);
        flowerStoneTexture    = manager.get("gems/Flower_stone.png"   , Texture.class);
        flowerVoidTexture     = manager.get("gems/Flower_void.png"    , Texture.class);
        flowerWaterTexture    = manager.get("gems/Flower_water.png"   , Texture.class);

        heartElectricTexture = manager.get("gems/Heart_electric.png", Texture.class);
        heartFireTexture     = manager.get("gems/Heart_fire.png"    , Texture.class);
        heartNatureTexture   = manager.get("gems/Heart_nature.png"  , Texture.class);
        heartPureTexture     = manager.get("gems/Heart_pure.png"    , Texture.class);
        heartStoneTexture    = manager.get("gems/Heart_stone.png"   , Texture.class);
        heartVoidTexture     = manager.get("gems/Heart_void.png"    , Texture.class);
        heartWaterTexture    = manager.get("gems/Heart_water.png"   , Texture.class);

        heartElectricTexture = manager.get("gems/Hexagon_electric.png", Texture.class);
        heartFireTexture     = manager.get("gems/Hexagon_fire.png"    , Texture.class);
        heartNatureTexture   = manager.get("gems/Hexagon_nature.png"  , Texture.class);
        heartPureTexture     = manager.get("gems/Hexagon_pure.png"    , Texture.class);
        heartStoneTexture    = manager.get("gems/Hexagon_stone.png"   , Texture.class);
        heartVoidTexture     = manager.get("gems/Hexagon_void.png"    , Texture.class);
        heartWaterTexture    = manager.get("gems/Hexagon_water.png"   , Texture.class);

        starElectricTexture = manager.get("gems/Star_electric.png", Texture.class);
        starFireTexture     = manager.get("gems/Star_fire.png"    , Texture.class);
        starNatureTexture   = manager.get("gems/Star_nature.png"  , Texture.class);
        starPureTexture     = manager.get("gems/Star_pure.png"    , Texture.class);
        starStoneTexture    = manager.get("gems/Star_stone.png"   , Texture.class);
        starVoidTexture     = manager.get("gems/Star_void.png"    , Texture.class);
        starWaterTexture    = manager.get("gems/Star_water.png"   , Texture.class);
    }

    public void initialiseHeroTextures(ArrayList<HeroList> neededHeroes) {
        vivainTexture = manager.get("characters/heroes/dark/vivain.png", Texture.class);
        vivainCardTexture = manager.get("characters/heroes/dark/vivainCARD.png", Texture.class);
//        for(HeroList list : neededHeroes) {
//            switch (list) {
//                case VIVAIN:
//                    if(!heroTextureIsInitialized.get(HeroList.VIVAIN)) {
//                        vivainTexture = manager.get("characters/heroes/dark/vivain.png", Texture.class);
//                    }
//                    break;
//
//                case LEIF:
//                default:
//                    break;
//            }
//        }
    }

    public void initialiseEnemyTextures(@NotNull ArrayList<Bestiary> neededEnemies) {
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
        stoneCardTexture    = new TextureRegion(cardSpriteSheet, 0, 32*8, 96, 128);
        fireCardTexture     = new TextureRegion(cardSpriteSheet, 32*5, 32*8, 96, 128);
        waterCardTexture    = new TextureRegion(cardSpriteSheet, 0, 32*12, 96, 128);
        voidCardTexture     = new TextureRegion(cardSpriteSheet, 160, 256, 96, 128);
        natureCardTexture   = new TextureRegion(cardSpriteSheet, 0, 0, 96, 128);

        final Texture iconSpriteSheet = manager.get("icon-pack.png", Texture.class);
        foodTexture = new TextureRegion(iconSpriteSheet, 256, 0, 32, 32);
        arcanaTexture = new TextureRegion(iconSpriteSheet, 320, 0, 32, 32);

        final Texture alphaFarmSpriteSheet = manager.get("structures/picopicofarm.png", Texture.class);
        farmTexture = new TextureRegion(alphaFarmSpriteSheet,0,0,64,32);

        mineTexture = manager.get("structures/goldmine.png", Texture.class);
        mineTexture2 = manager.get("structures/goldmine2.png", Texture.class);

        final Texture buildingSpriteSheet = manager.get("structures/buildingSpriteSheet.png", Texture.class);
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
        manager.load("characters/heroes/dark/vivain.png", Texture.class);
        manager.load("characters/heroes/dark/vivainCARD.png", Texture.class);
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
        manager.load("structures/goldmine.png", Texture.class);
        manager.load("structures/goldmine2.png", Texture.class);
        manager.load("structures/picopicofarm.png", Texture.class);
    }
    public void loadMatchScreenTextures() {
        manager.load("gem_set.png", Texture.class);

        manager.load("gems/Circle_electric.png", Texture.class);
        manager.load("gems/Circle_fire.png"    , Texture.class);
        manager.load("gems/Circle_nature.png"  , Texture.class);
        manager.load("gems/Circle_pure.png"    , Texture.class);
        manager.load("gems/Circle_stone.png"   , Texture.class);
        manager.load("gems/Circle_void.png"    , Texture.class);
        manager.load("gems/Circle_water.png"   , Texture.class);

        manager.load("gems/Flower_electric.png", Texture.class);
        manager.load("gems/Flower_fire.png"    , Texture.class);
        manager.load("gems/Flower_nature.png"  , Texture.class);
        manager.load("gems/Flower_pure.png"    , Texture.class);
        manager.load("gems/Flower_stone.png"   , Texture.class);
        manager.load("gems/Flower_void.png"    , Texture.class);
        manager.load("gems/Flower_water.png"   , Texture.class);

        manager.load("gems/Heart_electric.png", Texture.class);
        manager.load("gems/Heart_fire.png"    , Texture.class);
        manager.load("gems/Heart_nature.png"  , Texture.class);
        manager.load("gems/Heart_pure.png"    , Texture.class);
        manager.load("gems/Heart_stone.png"   , Texture.class);
        manager.load("gems/Heart_void.png"    , Texture.class);
        manager.load("gems/Heart_water.png"   , Texture.class);

        manager.load("gems/Hexagon_electric.png", Texture.class);
        manager.load("gems/Hexagon_fire.png"    , Texture.class);
        manager.load("gems/Hexagon_nature.png"  , Texture.class);
        manager.load("gems/Hexagon_pure.png"    , Texture.class);
        manager.load("gems/Hexagon_stone.png"   , Texture.class);
        manager.load("gems/Hexagon_void.png"    , Texture.class);
        manager.load("gems/Hexagon_water.png"   , Texture.class);

        manager.load("gems/Star_electric.png", Texture.class);
        manager.load("gems/Star_fire.png"    , Texture.class);
        manager.load("gems/Star_nature.png"  , Texture.class);
        manager.load("gems/Star_pure.png"    , Texture.class);
        manager.load("gems/Star_stone.png"   , Texture.class);
        manager.load("gems/Star_void.png"    , Texture.class);
        manager.load("gems/Star_water.png"   , Texture.class);
    }

    // GETTERS
    public AssetManager getManager() { return manager; }
}
