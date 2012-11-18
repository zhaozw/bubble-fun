package com.tooflya.bubblefun.modifiers;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.IModifier;

/**
 * @author Tooflya.com
 * @since
 */
public class AlphaModifier extends org.anddev.andengine.entity.modifier.AlphaModifier {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaModifier(float pDuration, float pFromAlpha, float pToAlpha) {
		super(pDuration, pFromAlpha, pToAlpha);

		this.addModifierListener(new IEntityModifierListener() {

			@Override
			public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
				onFinished();
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
				onStarted();
			}

		});

		this.setRemoveWhenFinished(false);

		this.mFinished = true;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void stop() {
		this.mFinished = true;
	}

	public void onStarted() {

	}

	public void onFinished() {

	}

}