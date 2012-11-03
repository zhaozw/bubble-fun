package com.tooflya.bubblefun;

/**
 * @author Tooflya.com
 * @since
 */
public class Options {
	public static final boolean DEBUG = true;

	public static float PI = (float) (2 * Math.asin(1));

	/** Camera parameters */
	public final static float cameraRatioCenter = 520f;
	public static float cameraRatioFactor;

	public static int cameraWidth;
	public static int cameraHeight;
	public static int cameraCenterX;
	public static int cameraCenterY;

	public static float DPI;
	public final static float targetDPI = 300f;

	public static float SPEED;

	public static String CR;

	public static float cameraOriginRatioX;
	public static float cameraOriginRatioY;

	public static float cameraOriginRatioCenterX;
	public static float cameraOriginRatioCenterY;

	public static int levelNumber = 1;

	public static float touchHeight;
	public static float ellipseHeight;

	public static float scalePower;

	public final static int particlesCount = 7;

	public final static int chikySize = 64;

	
	public final static float minChikyStepX = 1f; // TODO: Correct value.
	public final static float maxChikyStepX = 2f; // TODO: Correct value.

	public final static float chikyOffsetX = 3 * Options.chikySize; // TODO: Correct value.
}
