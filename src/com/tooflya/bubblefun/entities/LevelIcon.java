package com.tooflya.bubblefun.entities;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.Screen;
import com.tooflya.bubblefun.screens.LevelScreen1;

public class LevelIcon extends ButtonScaleable {

	public int id;

	public boolean blocked = false;

	public LevelIcon(TiledTextureRegion pTiledTextureRegion, final org.anddev.andengine.entity.Entity pParentScreen) {
		super(pTiledTextureRegion, pParentScreen);
	}

	@Override
	public void onClick() {
		if (!this.blocked) {
			Options.levelNumber = this.id;
			((LevelScreen1)Game.screens.get(Screen.LEVEL)).reInit();
			Game.screens.set(Screen.LOAD);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.airbubblegum.entities.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new LevelIcon(getTextureRegion(), this.mParentScreen);
	}
}
