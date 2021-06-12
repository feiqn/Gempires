package com.feiqn.gempires.logic.characters.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.characters.Combatant;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;
import com.feiqn.gempires.logic.ui.BackButton;
import com.feiqn.gempires.models.ElementalType;

public class HeroCard extends Combatant {

    public static class HeroCardThumbnail extends Image {
        private final HeroCard parentCard;

        public HeroCardThumbnail(Texture texture, HeroCard parent){
            super(texture);
            this.parentCard = parent;

            sharedInit();
        }
        public HeroCardThumbnail(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;

            sharedInit();
        }

        public void sharedInit() {

            this.setSize(2,3);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    setColor(1.5f, 1.5f, 1.5f, 1);

                    final Group heroInfoGroup = new Group();
                    heroInfoGroup.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                    parentCard.behindCardImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    parentCard.cardImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                    heroInfoGroup.addActor(parentCard.behindCardImage);
                    heroInfoGroup.addActor(parentCard.cardImage);

                    parentCard.parentCastle.activeMenu.addActor(heroInfoGroup);

                    final BackButton backButton = new BackButton(parentCard.game);
                    backButton.setParent(heroInfoGroup);
                    final float buttonSize = Gdx.graphics.getWidth() / 8f;
                    backButton.setSize(buttonSize, buttonSize);

                    heroInfoGroup.addActor(backButton);

                    // TODO: display stats, hero info, etc
                    Gdx.app.log("Hero Info:", "");
                    Gdx.app.log("Name: ", "" + parentCard.heroName);
                    Gdx.app.log("Title: ", "" + parentCard.heroTitle);
                    Gdx.app.log("Description: ", "" + parentCard.heroDescription);
                    Gdx.app.log("Ability: ", "" + parentCard.heroAbilityTitle);
                    Gdx.app.log("Ability Description ", "" + parentCard.heroAbilityDescription);
                    Gdx.app.log("Hero Stats: ", "----------");
                    Gdx.app.log("Strength: ", "" + parentCard.strength);
                    Gdx.app.log("Defence: ","" + parentCard.defense);
                    Gdx.app.log("Max HP: ", "" + parentCard.maxHealth);
                    Gdx.app.log("Level: ", "" + parentCard.level);
                    Gdx.app.log("Experience: ", "" + parentCard.experience);
                }
            });
        }
    }

    public class HeroBattleCard extends Image {
        private final HeroCard parentCard;

        public HeroBattleCard(Texture texture, HeroCard parent) {
            super(texture);
            this.parentCard = parent;

            sharedInit();
        }
        public HeroBattleCard(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;

//            final float w = Gdx.graphics.getWidth() * .2f;
//            this.setSize(w, w * 1.5f);

            sharedInit();
        }

        public void sharedInit() {

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    // TODO
                    heroAbility();
                }
            });
        }

    }

    private final HeroCard self = this;

    public final HeroBattleCard battleCard;
    public final HeroCardThumbnail thumbnail;

    public ElementalType elementalType;

    private int bravery,
                level,
                experience;

    private boolean isPure;

    public String heroName,
                  heroTitle,
                  heroDescription,
                  heroAbilityTitle,
                  heroAbilityDescription;

    public HeroList heroID;
    public Image behindCardImage,
                 cardImage;

    public CastleScreen parentCastle;

    public MatchScreen battleScreen;

    public HeroCard(Texture texture, CastleScreen castleScreen, Texture behindCardTexture, ElementalType element) {
        super(castleScreen.game, texture);
        this.thumbnail = new HeroCardThumbnail(texture, self);
        this.battleCard = new HeroBattleCard(texture, self); // todo: different region
        this.behindCardImage = new Image(behindCardTexture);
        parentCastle = castleScreen;
        elementalType = element;

        sharedInit();
    }
    public HeroCard(TextureRegion region, CastleScreen castleScreen, Texture behindCardTexture, ElementalType element) {
        super(castleScreen.game, region);
        this.thumbnail = new HeroCardThumbnail(region, self);
        this.battleCard = new HeroBattleCard(region, self); // todo: different region
        this.behindCardImage = new Image(behindCardTexture);
        parentCastle = castleScreen;
        elementalType = element;

        sharedInit();
    }

    private void sharedInit() {

        this.setSize(game.castle.gameCamera.viewportWidth, game.castle.gameCamera.viewportHeight);

        switch (elementalType) {
            case FIRE:
                cardImage = new Image(game.gempiresAssetHandler.fireCardTexture);
                break;
            case VOID:
                cardImage = new Image(game.gempiresAssetHandler.voidCardTexture);
                break;
            case STONE:
                cardImage = new Image(game.gempiresAssetHandler.stoneCardTexture);
                break;
            case WATER:
                cardImage = new Image(game.gempiresAssetHandler.waterCardTexture);
                break;
            case NATURE:
                cardImage = new Image(game.gempiresAssetHandler.natureCardTexture);
                break;
            case ELECTRIC:
                cardImage = new Image(game.gempiresAssetHandler.electricCardTexture);
            case PURE:
                break;
        }

        // base stats
        level = 1;
        experience = 0;
        bravery = 1;
        isPure = false;
    }

//    public void initialiseThumbnail(Element element) {
//        switch(element) {
//            case NATURE:
//                thumbnail = new HeroCardThumbnail(parentCastle.natureCardTexture, self);
//                break;
//            case WATER:
//                thumbnail = new HeroCardThumbnail(parentCastle.waterCardTexture, self);
//                break;
//            case FIRE:
//                thumbnail = new HeroCardThumbnail(parentCastle.fireCardTexture, self);
//                break;
//            case STONE:
//                thumbnail = new HeroCardThumbnail(parentCastle.stoneCardTexture, self);
//                break;
//            case ELECTRIC:
//                thumbnail = new HeroCardThumbnail(parentCastle.electricCardTexture, self);
//                break;
//            case VOID:
//                thumbnail = new HeroCardThumbnail(parentCastle.voidCardTexture, self);
//                break;
//            case PURE:
//                break;
//        }
//    }

    public void heroAbility() {}

    // SETTERS
    @Override
    public void draw(Batch batch, float parentAlpha) {
        behindCardImage.draw(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setSize(float w, float h) {
        try{

            behindCardImage.setSize(w,h);
            super.setSize(w,h);

        } catch (Exception e) {

            Gdx.app.log("catch", "image null");
            behindCardImage = new Image();
            behindCardImage.setSize(w,h);
            super.setSize(w,h);

        }
    }

    @Override
    public void setPosition(float x, float y) {
        behindCardImage.setPosition(x,y);
        super.setPosition(x,y);
    }


//    public void setBehindCardImage(TextureRegion region) {
//        behindCard = new Image(region);
//        behindCard.setSize(self.getWidth(), self.getHeight());
//    }
//    public void setBehindCardImage(Texture texture) {
//        behindCard = new Image(texture);
//        behindCard.setSize(self.getWidth(), self.getHeight());
//    }
    public void manuallySetStats(float strength, float defense, float maxHealth) {
        this.strength = strength;
        this.defense = defense;
        this.maxHealth = maxHealth;
    }

    public void scaleToTrueLevel(int goalTrueLevel) {
        while(getTrueLevel() < goalTrueLevel) {
            if(level < 99) {
                levelUp();
            } else {
                increaseBravery();
            }
        }
    }

    public void increaseBravery() {
        if(bravery < 5) {
            bravery++;
            level = 1;
            strength *= 1.25f;
            defense *= 1.25f;
            maxHealth *= 1.25f;
        }
    }

    public void addExp(int expToAdd) {
        this.experience += expToAdd;
    }

    public void manuallySetExp(int exp) {
        this.experience = exp;
    }

    public void levelUp() {
        if(level < 99) {
            level++;
            strength *= 1.25f;
            defense *= 1.25f;
            maxHealth *= 1.25f;
        }
    }

    // GETTERS
    public int getTrueLevel() {
        // TODO: isPure ?
        final int braveryBonus = (bravery * 100) - 100;
        return braveryBonus + level;
    }
    public boolean getPurity() {
        return isPure;
    }
    public int getBravery() {
        return bravery;
    }
    public int getExperience() {
        return experience;
    }
    public int getLevel() {
        return level;
    }
}