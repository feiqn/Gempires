package com.feiqn.gempires.logic.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.feiqn.gempires.logic.stages.match3.MatchScreen;

import java.util.Random;

public class Tornado extends Image {

    final private MatchScreen matchScreen;

    public Tornado(final TextureRegion region, final MatchScreen parentMatchScreen) {
        super(region);
        this.setSize(1,1);
        this.matchScreen = parentMatchScreen;

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(.5f, .5f, .5f, 1);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int point, int button) {
                setColor(1.5f, 1.5f, 1.5f, 1);
                shuffleWholeBoard();
            }
        });
    }

    public void shuffleWholeBoard() {

        DelayedRemovalArray<Integer> unusedSlots = new DelayedRemovalArray<>(matchScreen.gems.size);
        for(int i = 0; i < matchScreen.gems.size; i++) {
            unusedSlots.add(i);
        }

        // TODO: fix

        for(int x = 0; x < unusedSlots.size; x+=2) {
            final Random random = new Random();
            final int gemToMove = random.nextInt(unusedSlots.size);
            Gdx.app.log("tornado", "gemToMove:" + gemToMove);

            final int newDestinationSlot = random.nextInt(unusedSlots.size);
            Gdx.app.log("tornado", "newDestinationSlot:" + newDestinationSlot);

            if(gemToMove != newDestinationSlot) {
                matchScreen.swapGems(matchScreen.gems.get(gemToMove), matchScreen.gems.get(newDestinationSlot));

                unusedSlots.begin();
                unusedSlots.removeValue(gemToMove, true);
                unusedSlots.removeValue(newDestinationSlot, true);
                unusedSlots.end();
            }
        }
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                matchScreen.checkWholeBoardForMatches();
            }
        }, 1f);
    }

}
