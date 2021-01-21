package com.feiqn.gempires.logic.castle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.ui.PopupMenu;
import com.feiqn.gempires.logic.ui.ItemNotifierBubble;

import java.util.HashMap;

public class Structure extends Image {

    public enum StructureType {
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
        GODDESS_STATUE      // base structure at the center of castle. everybody always has only one.
    }

    public Rectangle bounds;

    private int level;

    final private Structure self = this;

    public StructureType structureType;

    private float productionRate;
    private float resourceCapacity;
    private float storedResource;

    // This needs to remain public for HeroCard
    public PopupMenu heroRosterPopup;

    private boolean readyToCollect;

    public ItemNotifierBubble itemNotifierBubble; // must be initialised by child class if used

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
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);

                switch(structureType) {
                    case FARM:
                    case MINE:
                    case LIBRARY:
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
            case FARM:
                map.put(food, level * 100);
                map.put(ore, level * 200);
                map.put(arcana, level * 25);
                break;
            case MINE:
                map.put(food, level * 200);
                map.put(ore, level * 100);
                map.put(arcana, level * 25);
                break;
            case LIBRARY:
                map.put(food, level * 300);
                map.put(ore, level * 300);
                map.put(arcana, level * 100);
                break;
            case GODDESS_STATUE:
                map.put(food, level * 2000);
                map.put(ore, level * 2000);
                map.put(arcana, level * 1000);
                break;
        }

        return map;
    }

    public HashMap<ItemList, Integer> getLevelUpItemRequirements() {
        final HashMap<ItemList, Integer> map = new HashMap<>();

        switch(level) {
            case 1:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        map.put(ItemList.MINT, 1);
                        break;
                }
            case 2:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 3:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 4:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 5:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 6:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 7:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 8:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
            case 9:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }

            // 10..98

            case 99:
                switch(structureType) {
                    case GODDESS_STATUE:
                    case FARM:
                    case SUMMONING_CIRCLE:
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
                    case ALTAR:
                    case SILO:
                    case MINE:
                    default:
                        break;
                }
                break;
        }

        return map;
    }

    public void setXY(float pX, float pY) {
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
