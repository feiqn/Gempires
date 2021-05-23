package com.feiqn.gempires.logic.ui.wideButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.feiqn.gempires.logic.items.ItemList;
import com.feiqn.gempires.logic.ui.popupMenus.PopupMenu;

import java.util.HashMap;


public class LevelUpWideButton extends WideButton {

    public LevelUpWideButton(final PopupMenu parentMenu) {
        super(parentMenu.getParentStructure().getParentScreen().game.gempiresAssetHandler.yellowButtonTexture, parentMenu);
        boolean canLevelUp = true;

        updateLabelText("Level up!");

        final HashMap<ItemList, Integer> neededItems = parentMenu.getParentStructure().getLevelUpItemRequirements();

        for(ItemList item : ItemList.values()) {
            if(neededItems.containsKey(item)) {
                Gdx.app.log("level up button", "You need " + neededItems.get(item) + " " + item.toString() + "s. You have: " + parentMenu.getParentStructure().getParentScreen().playerInventory.getItemCount(item));
                final int ownedAmount = parentMenu.getParentStructure().getParentScreen().playerInventory.getItemCount(item);
                final int neededAmount = neededItems.get(item);
                if(ownedAmount < neededAmount) {
                    canLevelUp = false;
                }
            }
        }

        final HashMap<String, Integer> neededResources = parentMenu.getParentStructure().getLevelUpResourceRequirements();
        final int neededFood = neededResources.get("food");
        final int neededOre = neededResources.get("ore");
        final int neededArcana = neededResources.get("arcana");
        final int ownedFood = (int) parentMenu.getParentStructure().getParentScreen().playerInventory.getFoodCount();
        final int ownedOre = (int) parentMenu.getParentStructure().getParentScreen().playerInventory.getOreCount();
        final int ownedArcana = (int) parentMenu.getParentStructure().getParentScreen().playerInventory.getFoodCount();

        Gdx.app.log("level up button", "You also need " + neededFood + " food, " + neededOre + " ore, and " + neededArcana + " arcana");

        if(neededArcana < ownedArcana
          || neededFood < ownedFood
          || neededOre  < ownedOre) {
            canLevelUp = false;
        }

        if(canLevelUp) {
            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    for(ItemList item : ItemList.values()) {
                        if(neededItems.containsKey(item)) {
                            final int count = neededItems.get(item);
                            for(int c = 0; c < count; c++) {
                                parentMenu.getParentStructure().getParentScreen().playerInventory.subtractItem(item);
                            }
                        }
                    }
                    parentMenu.getParentStructure().getParentScreen().playerInventory.subtractFood(neededFood);
                    parentMenu.getParentStructure().getParentScreen().playerInventory.subtractOre(neededOre);
                    parentMenu.getParentStructure().getParentScreen().playerInventory.subtractArcana(neededArcana);

                    parentMenu.getParentStructure().levelUp();

                }
            });
        } else {
            setColor(.3f,.3f,.3f,1);
        }
    }
}
