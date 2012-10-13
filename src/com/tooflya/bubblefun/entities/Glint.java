package com.tooflya.bubblefun.entities;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.FloatMath;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;

public class Glint extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mAddToScreen;

	private float mStepX;
	private float mStepY;

	private float mRotationAngle;

	private int mSleep;

	private boolean isParticle;

	private Entity mFollowObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTiledTextureRegion
	 * @param pScreen
	 */
	public Glint(TiledTextureRegion pTiledTextureRegion, final int pScreen) {
		super(pTiledTextureRegion, false);

		this.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		Game.screens.get(pScreen).attachChild(this);

		this.mAddToScreen = pScreen;

		this.setScaleCenter(this.getWidthScaled() / 2, this.getHeightScaled() / 2);
	}

	@Override
	public Entity deepCopy() {
		return new Glint(getTextureRegion(), this.mAddToScreen);
	}

	@Override
	public Entity create() {
		this.isParticle = false;

		this.setCurrentTileIndex(4);

		return super.create();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Glint Init(final int i, final Entity pFollowObject) {
		this.mFollowObject = pFollowObject;

		this.mStepX = 3f * FloatMath.sin(i * 2 * Options.PI / 10);
		this.mStepY = 3f * FloatMath.cos(i * 2 * Options.PI / 10);

		final float scale = Game.random.nextFloat();
		this.mScaleX = scale;
		this.mScaleY = scale;

		this.mAlpha = 1f;

		this.mRotationAngle = Game.random.nextFloat();

		this.mSleep = Game.random.nextInt(50);

		this.isParticle = true;

		this.setVisible(false);
		//this.setIgnoreUpdate(false);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.isParticle && --this.mSleep <= 0) {
			if (this.mSleep == 0) {
				this.setCenterPosition(this.mFollowObject.getCenterX(), this.mFollowObject.getCenterY());
				System.out.println(this.getWidthScaled());
				this.show();
			}
			this.mX += this.mStepX;
			this.mY += this.mStepY;

			this.mAlpha -= 0.01f;
			this.mRotation -= this.mRotationAngle;

			if (this.mAlpha < 0) {
				this.destroy();
			}
		}
	}
}
