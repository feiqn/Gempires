package com.feiqn.gempires.logic.castle.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.ui.popupMenus.PopupMenu;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

import java.util.HashMap;

public class Structure extends Image {

    // I really should have put all the code for these into their respective child classes,
    // but now it's a sunk-cost and I'm just going to keep making the mess worse.
    public enum StructureType {
        FARM,               // grow food to feed troops
        SILO,               // store food
        MINE,               // mine ore to build things
        WAREHOUSE,          // store ore
        LIBRARY,            // research arcana to level up goddessStatue and others
        ARCHIVE,            // store arcana
        ALTAR_WATER,        // power up heroes of corresponding element
        ALTAR_FIRE,
        ALTAR_STONE,
        ALTAR_ELECTRIC,
        ALTAR_VOID,
        ALTAR_NATURE,
        ALCHEMIST,          // create items like healing potions or ascent stones
        SUMMONING_PYRE,     // use items to play special stages TODO
        CLAN_TOWER,         // interact with multiplayer guild TODO during Multiplayer implementation
        ACADEMY,            // train lower tier heroes into higher tier ones TODO
        BARRACKS,           // access hero roster
        TURRET,             // attack nearby enemies TODO
        GARRISON,           // station heroes to attack nearby enemies TODO
        BARRICADE,          // block enemies from passing through TODO
        GODDESS_STATUE,     // base structure at the center of castle. everybody always has only one.
        CAMPAIGN_SELECTOR   // handler for campaign levels
    }

    public Rectangle bounds;

    private int level;

    final private Structure self = this;

    public StructureType structureType;

    /*  Resources are stored as generic "resources" in this and
     *  respective child classes. The StructureType can then
     *  be switched upon to determine whether the resource should be
     *  food, iron, arcana, etc.
     */
    private float productionRate;
    private float resourceCapacity;
    private float storedResource;

    // This needs to remain public for HeroCard
    public PopupMenu heroRosterPopup;

    private boolean readyToCollect;

    public ItemNotifierBubble itemNotifierBubble; // must be initialised by child class if used

    private CastleScreen parentScreen;

    public Structure(Texture texture, GempiresGame game) {
        super(texture);
        sharedInit(game.castle);
    }

    public Structure(TextureRegion region, GempiresGame game) {
        super(region);
        sharedInit(game.castle);
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
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);

                switch(structureType) {
                    case FARM:
                    case MINE:
                    case LIBRARY:
                    case SILO:
                    case ARCHIVE:
                    case WAREHOUSE:
                        final PopupMenu resourcePopupMenu = new PopupMenu(self, PopupMenu.MenuType.RESOURCE_STRUCTURE);
                        parentScreen.uiGroup.addActor(resourcePopupMenu);
                        break;
                    case BARRACKS:
                        heroRosterPopup = new PopupMenu(self, PopupMenu.MenuType.HERO_ROSTER);
                        parentScreen.uiGroup.addActor(heroRosterPopup);
                        break;
                    case GODDESS_STATUE:
                        final PopupMenu goddessPopUpMenu = new PopupMenu(self, PopupMenu.MenuType.GODDESS_STATUE);
                        parentScreen.uiGroup.addActor(goddessPopUpMenu);
                        break;
                    case CAMPAIGN_SELECTOR:
                        final PopupMenu campaignMenu = new PopupMenu(self, PopupMenu.MenuType.CAMPAIGN_SELECTOR);
                        parentScreen.uiGroup.addActor(campaignMenu);
                        break;
                    case ALCHEMIST:
                        final PopupMenu menu = new PopupMenu(self, PopupMenu.MenuType.ALCHEMIST);
                        parentScreen.uiGroup.addActor(menu);
                        break;
                    case ACADEMY:
                        Gdx.app.log("Academy","");
                        break;
                    case SUMMONING_PYRE:
                        Gdx.app.log("Summoning Pyre","");
                        break;
                    case TURRET:
                        Gdx.app.log("Turret","");
                        break;
                    case GARRISON:
                        Gdx.app.log("Garrison","");
                        break;
                    case BARRICADE:
                        Gdx.app.log("Barricade","");
                        break;
                    case ALTAR_FIRE:
                        Gdx.app.log("Fire Altar","");
                        break;
                    case ALTAR_VOID:
                        Gdx.app.log("Void Alter","");
                        break;
                    case ALTAR_STONE:
                        Gdx.app.log("Stone Altar","");
                        break;
                    case ALTAR_WATER:
                        Gdx.app.log("Water Altar","");
                        break;
                    case ALTAR_NATURE:
                        Gdx.app.log("Nature Altar","");
                        break;
                    case ALTAR_ELECTRIC:
                        Gdx.app.log("Electric Altar","");
                        break;
                    case CLAN_TOWER:
                        Gdx.app.log("Clan Tower", "");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public HashMap<String, Integer> getLevelUpResourceRequirements() {
        final HashMap<String, Integer> map = new HashMap<>();

        final String food = "food";
        final String ore = "ore";
        final String arcana = "arcana";

        switch(structureType) {
            case SILO:
            case FARM:
                map.put(food, level * 100);
                map.put(ore, level * 200);
                map.put(arcana, level * 25);
                break;
            case MINE:
            case WAREHOUSE:
                map.put(food, level * 200);
                map.put(ore, level * 100);
                map.put(arcana, level * 25);
                break;
            case LIBRARY:
            case ARCHIVE:
                map.put(food, level * 300);
                map.put(ore, level * 300);
                map.put(arcana, level * 100);
                break;
            case GODDESS_STATUE:
                map.put(food, level * 2000);
                map.put(ore, level * 2000);
                map.put(arcana, level * 1000);
                break;
            default:
                map.put(food, level * 100);
                map.put(ore, level * 100);
                map.put(arcana, level * 50);
                break;
        }

        return map;
    }

    public HashMap<ItemList, Integer> getLevelUpItemRequirements() {
        final HashMap<ItemList, Integer> loot = new HashMap<>();

        switch(level) {
            case 1:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        loot.put(ItemList.MINT, 1);
                        break;
                }
                break;
            case 2:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 3:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 4:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 5:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 6:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 7:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 8:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
            case 9:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;

            // 10..98

            case 99:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_PYRE:
                    case CLAN_TOWER:
                    case WAREHOUSE:
                    case BARRICADE:
                    case ALCHEMIST:
                    case GARRISON:
                    case BARRACKS:
                    case LIBRARY:
                    case ARCHIVE:
                    case ACADEMY:
                    case TURRET:
                    case ALTAR_WATER:
                    case ALTAR_FIRE:
                    case ALTAR_NATURE:
                    case ALTAR_VOID:
                    case ALTAR_ELECTRIC:
                    case ALTAR_STONE:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
        }

        return loot;
    }

    @Override
    public void setPosition(float pX, float pY) {
        super.setPosition(pX, pY);
        bounds.setX(pX);
        bounds.setY(pY);
    }

    public void selectLevel() {
        // child class CampaignSelector only
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

    // this.is really unnecessary /)_.
    public void updateResource(float delta) {
        if(this.storedResource + (this.productionRate*delta) < this.resourceCapacity) {
            this.storedResource += (this.productionRate * delta);
        } else {
            this.storedResource = this.resourceCapacity;
        }
        if(this.structureType == StructureType.FARM || this.structureType == StructureType.MINE || this.structureType == StructureType.LIBRARY) {
            if(!this.readyToCollect && this.storedResource > (this.resourceCapacity / 10)) {
                this.parentScreen.getGameStage().addActor(this.itemNotifierBubble);
                this.parentScreen.rootGroup.addActor(this.itemNotifierBubble);
                this.itemNotifierBubble.setXY(this.getX() + .25f, this.getY() + .5f);
                this.readyToCollect = true;
            }
        }
    }

    //SETTERS
    public void levelUp() {
        level++;
        this.increaseProductionRate(productionRate * .25f);
        this.increaseResourceCapacity(resourceCapacity * .25f);
        parentScreen.playerInventory.calculateMaximums();
    }
    public void increaseProductionRate(float productionRate) {
        this.productionRate += productionRate;
    }
    public void increaseResourceCapacity(float resourceCapacity) {
        this.resourceCapacity += resourceCapacity;
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