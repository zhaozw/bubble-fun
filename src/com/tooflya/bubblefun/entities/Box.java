package com.tooflya.bubblefun.entities;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bubblefun.modifiers.AlphaModifier;
import com.tooflya.bubblefun.modifiers.ScaleModifier;

public class Box extends Button/*Scaleable*/{

	private ScaleModifier modifier1 = new ScaleModifier(0.2f, 1f, 0.9f, 1f, 1.1f) {
		@Override
		public void onFinished() {
			modifier2.reset();
		}
	};

	private ScaleModifier modifier2 = new ScaleModifier(0.2f, 0.9f, 1.1f, 1.1f, 0.9f) {
		@Override
		public void onFinished() {
			modifier3.reset();
		}
	};

	private ScaleModifier modifier3 = new ScaleModifier(0.2f, 1.1f, 1f, 0.9f, 1f);

	private AlphaModifier modifier4 = new AlphaModifier(1f, 0.5f, 1f);

	public AlphaModifier modifier5 = new AlphaModifier(1f, 1f, 0.5f);

	public Box(TiledTextureRegion pTiledTextureRegion, Entity pParentScreen) {
		super(pTiledTextureRegion, pParentScreen);

		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		this.registerEntityModifier(modifier1);
		this.registerEntityModifier(modifier2);
		this.registerEntityModifier(modifier3);
		this.registerEntityModifier(modifier4);
		this.registerEntityModifier(modifier5);
	}

	public void animation() {
		modifier1.reset();

		if (this.getAlpha() < 1f) {
			modifier4.reset();
		}
	}

	@Override
	public void onClick() {
	}
}