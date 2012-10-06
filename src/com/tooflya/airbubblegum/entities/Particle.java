package com.tooflya.airbubblegum.entities;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.airbubblegum.Game;

/**
 * @author Tooflya.com
 * @since
 */
public class Particle extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float stepX = 0;
	private float stepY = 0;
	private float stepRotation = 0;
	private float stepScale = 0;
	private float stepAlpha = 0;
	private float time = 0;
	private float maxTime = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTiledTextureRegion
	 */
	public Particle(TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);
		this.setWidth(16);
		this.setHeight(16);
		this.setVisible(false);		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Particle Init() {
		stepX = Game.random.nextFloat() * 2 - 1;
		stepY = Game.random.nextFloat() * 2 - 1;

		stepRotation = Game.random.nextFloat() * 10;

		time = 0;
		maxTime = Game.random.nextInt(90);

		this.setScale(1);
		stepScale = -Game.random.nextFloat() / maxTime;

		this.setAlpha(1);
		stepAlpha = -Game.random.nextFloat() / maxTime;

		maxTime += 30;

		return this;
	}

	// ===========================================================
	// Setters
	// ===========================================================

	// ===========================================================
	// Getters
	// ===========================================================

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.setPosition(this.getX() + this.stepX, this.getY() + this.stepY);

		if (this.getScaleX() + this.stepScale < 0) {
			this.destroy();
			return;
		}
		this.setScale(this.getScaleX() + this.stepScale);

		this.setRotation(this.getRotation() + this.stepRotation);

		if (this.getAlpha() + this.stepAlpha < 0 && this.getAlpha() + this.stepAlpha > 1) {
			this.destroy();
			return;
		}
		this.setAlpha(this.getAlpha() + this.stepAlpha);

		time++;
		if (this.time > this.maxTime) {
			this.destroy();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Particle(getTextureRegion());
	}
}