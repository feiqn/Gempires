package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.ui.HeroRosterPopup;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;
import com.feiqn.gempires.logic.ui.StructurePopupMenu;

public class Structure extends Image {
    public enum Type {
        FARM,               // grow food to feed troops
        SILO,               // store food
        MINE,               // mine ore to build things
        WAREHOUSE,          // store ore
        LIBRARY,            // research arcana to level up goddessStatue and others
        ARCHIVE,            // store arcana
        ALTAR,              // power up heroes of corresponding element
        ALCHEMIST,          // create items like healing potions or ascent stones
        SUMMONING_CIRCLE,   // do pulls for more heroes
        CLAN_TOWER,         // interact with multiplayer guild
        ACADEMY,            // train lower tier heroes into higher tier ones
        BARRACKS,           // access hero roster
        TURRET,             // attack nearby enemies
        GARRISON,           // station heroes to attack nearby enemies
        BARRICADE,          // block enemies from passing through
        GODDESS_STATUE      // base structure at the center of base. everybody always has only one.
    }

    public Rectangle bounds;

    private int level;

    final private Structure self = this;

    public Type structureType;

    private float productionRate;
    private float resourceCapacity;
    private float storedResource;

    private boolean readyToCollect;

    private HeroRosterPopup heroRosterPopup;

    public ItemNotifierBubble itemNotifierBubble; // must be initialised by child class

    private CastleScreen parentScreen;

    public Structure(Texture texture, CastleScreen parent) {
        super(texture);
        sharedInit(parent);
    }

    public Structure(TextureRegion region, CastleScreen parent) {
        super(region);
        sharedInit(parent);
    }

    private void sharedInit(CastleScreen parent) {
        this.readyToCollect = false;

        // base stats at level 1
        this.productionRate = 1;
        this.resourceCapacity = 100;
        this.storedResource = 0;

        this.parentScreen = parent;
        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        this.level = 1;

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                Gdx.app.log("Structure", "stored: " + getStoredResource());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);

                switch(structureType) {
                    case FARM:
                    case MINE:
                    case LIBRARY:
                        final StructurePopupMenu popUpMenu = new StructurePopupMenu(self);
                        parentScreen.getStage().addActor(popUpMenu);
                        break;
                    case BARRACKS:
                        heroRosterPopup = new HeroRosterPopup(self);
                        parentScreen.getStage().addActor(heroRosterPopup);
                        break;
                }
            }
        });
    }

    void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }

    public void collectResource() {
        switch(this.structureType) {
            case FARM:
                this.parentScreen.playerInventory.addFood(this.storedResource);
                break;
            case MINE:
                this.parentScreen.playerInventory.addOre(this.storedResource);
                break;
            case LIBRARY:
                this.parentScreen.playerInventory.addArcana(this.storedResource);
                break;
        }

        this.storedResource = 0;
        this.readyToCollect = false;
    }

    public void updateResource(float delta) {
        if(this.storedResource +(this.productionRate*delta) < this.resourceCapacity) {
            this.storedResource += (this.productionRate * delta);
        } else {
            this.storedResource = this.resourceCapacity;
        }
        if(this.structureType == Type.FARM || this.structureType == Type.MINE || this.structureType == Type.LIBRARY) {
            if(!this.readyToCollect && this.storedResource > (this.resourceCapacity / 10)) {
                this.parentScreen.getStage().addActor(this.itemNotifierBubble);
                this.itemNotifierBubble.setXY(this.getX(), this.getY() + 10f);
                this.readyToCollect = true;
            }
        }
    }

    //SETTERS
    public void levelUp() {
        level++;
        this.setProductionRate(getLevel() * 1.25f);
        this.setResourceCapacity(getLevel() * 1.25f);
    }

    public void setProductionRate(float productionRate) {
        this.productionRate = productionRate;
    }

    public void setResourceCapacity(float resourceCapacity) {
        this.resourceCapacity = resourceCapacity;
    }

    public void switchReadyToCollect() {
        this.readyToCollect = !readyToCollect;
    }


    // GETTERS
    public int getLevel() {
        return level;
    }
    public Structure getSelf() {
        return self;
    }
    public CastleScreen getParentScreen() {
        return parentScreen;
    }
    public float getProductionRate() {
        return productionRate;
    }
    public float getResourceCapacity() {
        return resourceCapacity;
    }
    public float getStoredResource() {
        return storedResource;
    }
}
