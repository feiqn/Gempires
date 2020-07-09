package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.heroes.HeroCard;

public class BackButton extends Image {

    private enum ParentType {
        GROUP,
        HEROCARD,
        HEROROSTERPOPUP,
        STRUCTUREPOPUPMENU
    }

    private ParentType parentType;
    
    public BackButton(TextureRegion region) {
        super(region);
    }

    public void setParent(HeroCard heroCard) {
        parentType = ParentType.HEROCARD;
    }

    public void setParent(Group group) {
        parentType = ParentType.GROUP;
    }

    public void setParent(HeroRosterPopup heroRosterPopup) {
        parentType = ParentType.HEROROSTERPOPUP;
    }

    public void setParent(StructurePopupMenu structurePopupMenu) {
        parentType = ParentType.STRUCTUREPOPUPMENU;
    }

    public void closeParent() {
        switch (parentType){
            case GROUP:
            case HEROCARD:
            case HEROROSTERPOPUP:
            case STRUCTUREPOPUPMENU:
                break;
        }
    }
}
