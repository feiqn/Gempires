package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;

public class BackButton extends Image {

    private enum ParentType {
        GROUP,
        HEROCARD,
        MENUPOPUP,
    }

    private ParentType parentType;

    private HeroCard parentHeroCard;
    private Group parentGroup;
    private PopupMenu parentPopup;

    public BackButton(TextureRegion region) {
        super(region);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                closeParent();
            }
        });
    }

    public void setParent(HeroCard heroCard) {
        parentType = ParentType.HEROCARD;
        parentHeroCard = heroCard;
    }

    public void setParent(Group group) {
        parentType = ParentType.GROUP;
        parentGroup = group;
    }

    public void setParent(PopupMenu popupMenu) {
        parentType = ParentType.MENUPOPUP;
        parentPopup = popupMenu;
    }

    public void closeParent() {
        switch (parentType){
            case GROUP:
                parentGroup.remove();
                break;
            case HEROCARD:
                parentHeroCard.remove();
                break;
            case MENUPOPUP:
                parentPopup.remove();
                break;
        }
    }
}
