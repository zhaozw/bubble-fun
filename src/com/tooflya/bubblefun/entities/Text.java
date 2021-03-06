package com.tooflya.bubblefun.entities;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;

/**
 * @author Tooflya.com
 * @since
 */
public class Text extends ChangeableText {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pX
	 * @param pY
	 * @param pFont
	 * @param pText
	 */
	public Text(float pX, float pY, Font pFont, String pText) {
		super(pX, pY, pFont, pText);

		this.setPosition(pX, pY);
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pFont
	 * @param pText
	 * @param pLeftAlign
	 */
	public Text(float pX, float pY, Font pFont, String pText, final boolean pLeftAlign) {
		super(pX, pY, pFont, pText);

		this.setPosition(pX, pY, true);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andengine.entity.Entity#setPosition(float, float)
	 */
	@Override
	public void setPosition(final float pX, final float pY) {
		this.mX = pX - this.getWidth() / 2;
		this.mY = pY - this.getHeight() / 2;
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pLeftAlign
	 */
	public void setPosition(final float pX, final float pY, final boolean pLeftAlign) {
		this.mX = pX;
		this.mY = pY;
	}
}
