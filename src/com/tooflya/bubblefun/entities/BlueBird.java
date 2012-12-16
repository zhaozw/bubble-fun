package com.tooflya.bubblefun.entities;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.os.Vibrator;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.Resources;
import com.tooflya.bubblefun.managers.EntityManager;
import com.tooflya.bubblefun.screens.LevelScreen;
import com.tooflya.bubblefun.screens.Screen;

/**
 * @author Tooflya.com
 * @since
 */
public class BlueBird extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private final static float mSpeed = 2f;

	private static final long[] pFrameDuration = new long[] { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
	private static final int[] pNormalMoveFrames = new int[] { 0, 1, 2, 3, 4, 5, 4, 3, 2, 1 };

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSleepTime = 0f;
	private float mSleepTimeOrigin = 300f;

	private boolean mIsSleep;

	private EntityManager<?> mFeathersManager;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTiledTextureRegion
	 * @param pFeathersManager
	 * @param pParentScreen
	 */
	public BlueBird(final TiledTextureRegion pTiledTextureRegion, final EntityManager<?> pFeathersManager, final org.anddev.andengine.entity.Entity pParentScreen) {
		super(pTiledTextureRegion, pParentScreen);

		this.mFeathersManager = pFeathersManager;

		this.animate(pFrameDuration, pNormalMoveFrames, 9999);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 
	 */
	public void particles() {
		if (!this.isSleep()) {
			((Vibrator) Game.instance.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(10);

			if (this.mTextureRegion.e(Resources.mBlueBirdTextureRegion)) {
				for (int i = 0; i < Options.particlesCount; i++) {
					Feather particle;
					particle = ((Feather) mFeathersManager.create());
					if (particle != null) {
						particle.Init().setCenterPosition(this.getCenterX(), this.getCenterY());
					}
				}
			} else if (this.mTextureRegion.e(Resources.mSpaceBlueBirdTextureRegion)) {
				Glass particle;
				for (int i = 0; i < Options.particlesCount; i++) {
					particle = ((LevelScreen) Game.screens.get(Screen.LEVEL)).glasses.create();
					if (particle != null) {
						particle.Init().setCenterPosition(this.getCenterX(), this.getCenterY());
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	public boolean isSleep() {
		return this.mIsSleep;
	}

	/**
	 * 
	 */
	public void clear() {
		this.mFeathersManager.clear();
	}

	/**
	 * @return
	 */
	private float generateNewHeight() {
		return Game.random.nextFloat() * (Options.cameraHeight - Options.touchHeight - this.mHeight);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.airbubblegum.entities.Entity#create()
	 */
	@Override
	public Entity create() {
		this.setSpeedX(mSpeed);

		this.mX = Game.random.nextInt(2) == 0 ? 0 - this.mWidth : Options.cameraWidth + this.mWidth;
		this.mY = this.generateNewHeight();

		return super.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.mSleepTime <= 0) {
			this.mIsSleep = false;
		} else {
			this.mIsSleep = true;
		}

		if (this.mIsSleep) {
			if (LevelScreen.running) {
				this.mSleepTime--;
			}
		} else {
			if (this.getTextureRegion().isFlippedHorizontal()) {
				if (this.mX + this.mWidth < 0) {
					this.getTextureRegion().setFlippedHorizontal(false);
					this.mSleepTime = this.mSleepTimeOrigin;
					this.mY = this.generateNewHeight();
				} else {
					this.mX -= this.getSpeedX();
				}
			} else {
				if (this.mX - this.mWidth > Options.cameraWidth) {
					this.getTextureRegion().setFlippedHorizontal(true);
					this.mSleepTime = this.mSleepTimeOrigin;
					this.mY = this.generateNewHeight();
				} else {
					this.mX += this.getSpeedX();
				}
			}
		}
	}
}
