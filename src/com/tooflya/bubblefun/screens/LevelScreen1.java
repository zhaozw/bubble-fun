package com.tooflya.bubblefun.screens;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.modifier.IModifier;

import android.util.FloatMath;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.Screen;
import com.tooflya.bubblefun.entities.BlueBird;
import com.tooflya.bubblefun.entities.Bubble;
import com.tooflya.bubblefun.entities.ButtonScaleable;
import com.tooflya.bubblefun.entities.Chiky;
import com.tooflya.bubblefun.entities.Cloud;
import com.tooflya.bubblefun.entities.Entity;
import com.tooflya.bubblefun.entities.Glint;
import com.tooflya.bubblefun.entities.Particle;
import com.tooflya.bubblefun.entities.Sprite;
import com.tooflya.bubblefun.entities.Wind;
import com.tooflya.bubblefun.managers.CloudsManager;
import com.tooflya.bubblefun.managers.EntityManager;
import com.tooflya.bubblefun.modifiers.MoveModifier;

/**
 * @author Tooflya.com
 * @since
 */
public class LevelScreen1 extends Screen implements IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	public static int mBubblesCount;
	public static int AIR;
	public static boolean running;
	public static int deadBirds;
	private static boolean isResetAnimationRunning;

	private final BitmapTextureAtlas mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	private final Sprite mBackground = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/bg-game.png", 0, 0, 1, 1), this);

	private CloudsManager clouds = new CloudsManager(10, new Cloud(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/cloud.png", 0, 615, 1, 3), this.mBackground));

	private final Sprite mDottedLine = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/dash-line.png", 0, 1020, 1, 1), this.mBackground);

	private final ButtonScaleable mMenuButton = new ButtonScaleable(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/menu-btn.png", 0, 805, 1, 2), this.mBackground) {

		@Override
		public void onClick() {
			LevelScreen1.this.setChildScene(Game.screens.get(Screen.PAUSE), false, true, true);
		}
	};

	private final ButtonScaleable mResetButton = new ButtonScaleable(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/replay-btn.png", 0, 880, 1, 2), this.mBackground) {

		@Override
		public void onClick() {
			reInit();
		}
	};

	private final Sprite mTextTapHere = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/text-tap.png", 0, 950, 1, 1), this.mBackground) {
		public boolean shake = false;
		private float minS = -15, maxS = 15;
		private boolean reverse = false;
		private float step = 5f;
		private int mi = 100, i = 0;

		@Override
		public Entity create() {
			this.setRotationCenter(this.getWidth() / 2, this.getHeight() / 2);

			return super.create();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate
		 * (float)
		 */
		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);

			if (shake) {
				i++;
				if (reverse) {
					this.mRotation += step;
					if (this.mRotation >= maxS) {
						this.reverse = false;
					}
				}
				else {
					this.mRotation -= step;
					if (this.mRotation <= minS) {
						this.reverse = true;
					}
				}
				if (i >= mi) {
					this.i = 0;
					this.shake = false;
					this.reverse = false;
					this.mRotation = -15;
				}
			}
		}

		@Override
		public void reset() {
			this.shake = true;
		}
	};

	private final Rectangle shape = new Rectangle(0, 0, Options.cameraOriginRatioX, Options.cameraOriginRatioY) {

		private float s = 0.005f;

		private boolean modifier = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate
		 * (float)
		 */
		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			super.onManagedUpdate(pSecondsElapsed);

			boolean update = false;
			for (int i = 0; i < this.getChildCount(); i++) {
				if (this.getChild(i).getAlpha() > 0) {
					this.getChild(i).setAlpha(this.getChild(i).getAlpha() - s);
					update = true;
				}
			}

			if (!update && !modifier) {
				modifier = true;
			}
		}

		public void reset() {
			move.reset();

			for (int i = 0; i < this.getChildCount(); i++) {
				this.getChild(i).setAlpha(1f);
			}
			modifier = false;
		}
	};

	private final Sprite mLevelWord = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/text-level.png", 0, 980, 1, 1), this.shape);
	private final EntityManager numbers = new EntityManager(4, new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/numbers-sprite.png", 385, 0, 1, 11), this.shape));

	private final MoveModifier move = new MoveModifier(0.5f, Options.cameraOriginRatioCenterX - mTextTapHere.getWidth() / 2, Options.cameraOriginRatioCenterX
			- mTextTapHere.getWidth() / 2, (Options.cameraOriginRatioY / 3 * 2) + Options.cameraOriginRatioY / 3 / 2 + Options.cameraOriginRatioY / 3,
			(Options.cameraOriginRatioY / 3 * 2) + Options.cameraOriginRatioY / 3 / 2) {
		@Override
		public void onFinished() {
			mTextTapHere.reset();
		}
	};

	private final MoveModifier moveDown = new MoveModifier(0.5f, Options.cameraOriginRatioCenterX - mTextTapHere.getWidth() / 2, Options.cameraOriginRatioCenterX
			- mTextTapHere.getWidth() / 2, (Options.cameraOriginRatioY / 3 * 2) + Options.cameraOriginRatioY / 3 / 2, (Options.cameraOriginRatioY / 3 * 2)
			+ Options.cameraOriginRatioY / 3 / 2 + Options.cameraOriginRatioY / 3);

	private final Sprite mResetText = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context,
			Options.CR + "/text-restart.png", 430, 0, 1, 1), this.mBackground);

	private final MoveModifier restartMove1 = new MoveModifier(0.5f, -mResetText.getWidth(), Options.cameraOriginRatioX / 8, Options.cameraOriginRatioCenterY,
			Options.cameraOriginRatioCenterY) {
		@Override
		public void onFinished() {
			restartMove2.reset();
		}
	};

	private final MoveModifier restartMove2 = new MoveModifier(1f, Options.cameraOriginRatioX / 8, Options.cameraOriginRatioX / 8 * 2, Options.cameraOriginRatioCenterY,
			Options.cameraOriginRatioCenterY) {
		@Override
		public void onFinished() {
			restartMove3.reset();
		}
	};

	private final MoveModifier restartMove3 = new MoveModifier(0.5f, Options.cameraOriginRatioX / 8 * 2, Options.cameraOriginRatioX, Options.cameraOriginRatioCenterY,
			Options.cameraOriginRatioCenterY) {
		@Override
		public void onFinished() {
			reInit();
		}
	};

	private final EntityManager thorns = new EntityManager(10, new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/thorn.png", 430, 40, 1, 1), this.mBackground));

	public final EntityManager winds = new EntityManager(10, new Wind(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/speed-wind.png", 430, 65, 6, 1), this.mBackground));

	private Sound mYeahSound;

	// ===========================================================
	// Fields
	// ===========================================================

	private Bubble lastAirgum = null;

	public static EntityManager chikies;
	public static EntityManager airgums;
	public static EntityManager feathers;
	public static EntityManager glints;

	private static BlueBird mBlueBird;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelScreen1() {
		this.setOnSceneTouchListener(this);

		mBackground.create();
		this.clouds.generateStartClouds();

		mDottedLine.create().setPosition(0, Options.cameraOriginRatioY / 3 * 2);

		this.attachChild(shape);
		this.shape.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.shape.setAlpha(0f);

		mLevelWord.create().setCenterPosition(Options.cameraOriginRatioCenterX, Options.cameraOriginRatioCenterY);
		numbers.create().setCenterPosition(Options.cameraOriginRatioCenterX - 30f, Options.cameraOriginRatioCenterY);
		numbers.create().setCenterPosition(Options.cameraOriginRatioCenterX - 10f, Options.cameraOriginRatioCenterY);
		numbers.create().setCenterPosition(Options.cameraOriginRatioCenterX + 10f, Options.cameraOriginRatioCenterY);
		numbers.create().setCenterPosition(Options.cameraOriginRatioCenterX + 30f, Options.cameraOriginRatioCenterY);

		for (int i = 0; i < shape.getChildCount(); i++) {
			((Shape) shape.getChild(i)).setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		numbers.getByIndex(0).setCurrentTileIndex(1);
		numbers.getByIndex(1).setCurrentTileIndex(10);

		mTextTapHere.setRotation(-15f);
		mTextTapHere.registerEntityModifier(move);
		mTextTapHere.registerEntityModifier(moveDown);

		chikies = new EntityManager(31, new Chiky(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/small-bird.png", 430, 100, 6, 4), this.mBackground));
		airgums = new EntityManager(100, new Bubble(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/gum-animation.png", 430, 280, 1, 6), this.mBackground));
		feathers = new EntityManager(100, new Particle(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/feather.png", 730, 585, 1, 2), this.mBackground));

		mBlueBird = new BlueBird(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR
				+ "/blue-bird.png", 430, 600, 6, 1), new EntityManager(100, new Particle(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				mBackgroundTextureAtlas, Game.context, Options.CR + "/feather_new_blue.png", 430, 890, 1, 2), this.mBackground)), this.mBackground);

		glints = new EntityManager(100, new Glint(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas,
				Game.context, Options.CR + "/blesk.png", 730, 900, 1, 3), this.mBackground));

		this.mMenuButton.create().setPosition(Options.cameraOriginRatioX - (10 + this.mMenuButton.getWidth()), 10);
		this.mResetButton.create().setPosition(
				Options.cameraOriginRatioX - (15 + this.mMenuButton.getWidth() + this.mResetButton.getWidth()),
				10);

		mResetText.create().setPosition(-mResetText.getWidth(), Options.cameraOriginRatioCenterY - mResetText.getHeight() / 2);
		mResetText.registerEntityModifier(restartMove1);
		mResetText.registerEntityModifier(restartMove2);
		mResetText.registerEntityModifier(restartMove3);

		try {
			this.mYeahSound = SoundFactory.createSoundFromAsset(Game.engine.getSoundManager(), Game.instance, "yeah.mp3");
		} catch (final IOException e) {
		}
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	public void reInit() {
		running = true;
		deadBirds = 0;
		isResetAnimationRunning = false;

		mBlueBird.create();
		mBlueBird.clear();
		airgums.clear();
		chikies.clear();
		glints.clear();
		winds.clear();
		thorns.clear();
		generateChikies(30); // TODO: Change count depending to level number.

		feathers.clear();

		Options.scalePower = 20; // TODO: Change count of scale power.

		AIR = 10;// 100;

		mBubblesCount = 0;

		mTextTapHere.create().setCenterPosition(Options.cameraOriginRatioCenterX, (Options.cameraOriginRatioY / 3 * 2) + Options.cameraOriginRatioY / 3 / 2 + Options.cameraOriginRatioY / 3);

		shape.reset();

		if (Options.levelNumber < 10) {
			mLevelWord.setCenterPosition(Options.cameraOriginRatioCenterX - 10f, Options.cameraOriginRatioCenterY - 40f);
			numbers.getByIndex(3).setVisible(false);

			numbers.getByIndex(2).setCurrentTileIndex(Options.levelNumber);
		} else {
			mLevelWord.setCenterPosition(Options.cameraOriginRatioCenterX, Options.cameraOriginRatioCenterY - 40f);
			numbers.getByIndex(3).setVisible(true);

			numbers.getByIndex(2).setCurrentTileIndex((int) Math.floor(Options.levelNumber / 10));
			numbers.getByIndex(3).setCurrentTileIndex(Options.levelNumber % 10);
		}

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void generateChikies(final int count) {
		Chiky chiky;

		final float minHeight = Options.chikySize / 2 + Options.ellipseHeight;
		final float sizeHeight2 = (Options.cameraOriginRatioY - Options.touchHeight - Options.chikySize - 2 * Options.ellipseHeight) / 2;

		final int stepSign = 1;
		switch (Options.levelNumber) {
		case 1:
			chiky = (Chiky) chikies.create();
			chiky.init(0, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 2:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * 0.5f, -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(0, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			return;
		case 3:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(0, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);

			thorns.create().setCenterPosition(80, Options.cameraCenterY);
			return;
		case 4:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(0, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			return;
		case 5:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(0, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 6:
			chiky = (Chiky) chikies.create();
			chiky.init(2, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 7:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * 0.5f, -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 8:
			chiky = (Chiky) chikies.create();
			chiky.init(2, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * 0.5f, -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 9:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			return;
		case 10:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(0, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 11:
			chiky = (Chiky) chikies.create();
			chiky.init(3, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 12:
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			return;
		case 13:
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			return;
		case 14:
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 15:
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			return;
		case 16:
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * 0.5f, -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, 0, minHeight + sizeHeight2, stepSign);
			return;
		case 17:
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 18:
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 19:
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		case 20:
			chiky = (Chiky) chikies.create();
			chiky.init(1, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(2, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * Game.random.nextFloat(), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(3, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2, stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(4, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), -stepSign);
			chiky = (Chiky) chikies.create();
			chiky.init(5, Options.cameraOriginRatioX / 2, minHeight + sizeHeight2 * (Game.random.nextFloat() + 1), stepSign);
			return;
		}
	}

	private void checkCollision() {
		Chiky chiky;
		Bubble airgum;
		for (int i = this.chikies.getCount() - 1; i >= 0; --i) {
			chiky = (Chiky) this.chikies.getByIndex(i);

			if (chiky.getIsFly()) {
				for (int j = this.airgums.getCount() - 1; j >= 0; --j) {
					airgum = (Bubble) this.airgums.getByIndex(j);
					if (this.isCollide(chiky, airgum)) {
						chiky.setIsNeedToFlyAway(airgum.getScaleX() * 0.75f);
						this.mYeahSound.play();
					}
					for (int a = thorns.getCount() - 1; a >= 0; --a) {
						if (this.isCollide(airgum, thorns.getByIndex(a))) {
							if (!airgum.isAnimationRunning()) {
								airgum.animate(40, 0, airgum);
							}
						}
					}
				}
			}
		}

		for (int j = this.airgums.getCount() - 1; j >= 0; --j) {
			airgum = (Bubble) this.airgums.getByIndex(j);
			if (this.isCollide(this.mBlueBird, airgum)) {
				if (!this.mBlueBird.isSleep()) {
					this.mBlueBird.particles();
					if (!airgum.isAnimationRunning()) {
						airgum.animate(40, 0, airgum);
					}
				}
				break;
			}
		}
	}

	private boolean isCollide(Entity entity1, Entity entity2) {
		final float x = entity2.getCenterX() - entity1.getCenterX();
		final float y = entity2.getCenterY() - entity1.getCenterY();
		final float d = entity2.getWidthScaled() / 2 + entity1.getWidthScaled() / 2;
		return x * x + y * y < d * d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();

		Game.screens.get(Screen.LEVEL).clearChildScene();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate
	 * (float)
	 */
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.checkCollision();

		this.clouds.update();

		if (chikies.getCount() == 0) {
			Game.screens.set(Screen.LEVELEND);
		} else {

			if (AIR <= 0 && running) {
				moveDown.reset();
				running = false;
			}

			if (airgums.getCount() <= 0 && deadBirds <= 0 && !running && !isResetAnimationRunning) {
				restartMove1.reset();
				isResetAnimationRunning = true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.airbubblegum.Screen#loadResources()
	 */
	@Override
	public void loadResources() {
		Game.loadTextures(mBackgroundTextureAtlas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.airbubblegum.Screen#unloadResources()
	 */
	@Override
	public void unloadResources() {
		Game.unloadTextures(mBackgroundTextureAtlas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public boolean onBackPressed() {
		PreloaderScreen.mChangeAction = 1; // TODO: WTF? LOL d:

		if (this.hasChildScene()) {
			this.clearChildScene();
		} else {
			this.setChildScene(Game.screens.get(Screen.PAUSE), false, true, true);
		}

		return true;
	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent pTouchEvent) {
		final float pTouchX = pTouchEvent.getX() / Options.cameraRatioFactor;
		final float pTouchY = pTouchEvent.getY() / Options.cameraRatioFactor;

		switch (pTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			if (AIR > 0) {
				if (this.lastAirgum == null && pTouchY > Options.cameraOriginRatioY - Options.cameraOriginRatioY / 3)
				{
					this.lastAirgum = (Bubble) airgums.create();
					this.lastAirgum.setCenterPosition(pTouchX, pTouchY);
					this.lastAirgum.setScaleCenterY(0);
				}
				if (this.lastAirgum != null) {
					this.lastAirgum.setIsScale(true);
				}
			}
			break;
		case TouchEvent.ACTION_UP:
			if (this.lastAirgum != null) {
				if (this.lastAirgum.getCenterY() - pTouchY > 40f) {

					this.lastAirgum.setIsScale(false);

					float angle = (float) Math.atan2(pTouchY - this.lastAirgum.getCenterY(), pTouchX - this.lastAirgum.getCenterX());

					float distance = MathUtils.distance(this.lastAirgum.getCenterX(), this.lastAirgum.getCenterY(), pTouchX, pTouchY) / Options.SPEED;

					distance = distance > 6 ? 6 : distance;

					this.lastAirgum.setSpeedXB(-(distance * FloatMath.cos(angle)));
					this.lastAirgum.setSpeedYB(-(distance * FloatMath.sin(angle)));

					Glint particle;
					for (int i = 0; i < 15; i++) {
						particle = ((Glint) glints.create());
						if (particle != null) {
							particle.Init(i, this.lastAirgum);
						}
					}
				} else {

					this.lastAirgum.setIsScale(false);
				}
				this.lastAirgum = null;

				mBubblesCount++;
			}
			break;
		}

		return false;
	}
	// ===========================================================
	// Methods
	// ===========================================================

}
