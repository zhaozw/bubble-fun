package com.tooflya.bubblefun.screens;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;

import android.util.FloatMath;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.Resources;
import com.tooflya.bubblefun.entities.Acceleration;
import com.tooflya.bubblefun.entities.AwesomeText;
import com.tooflya.bubblefun.entities.BlueBird;
import com.tooflya.bubblefun.entities.Bonus;
import com.tooflya.bubblefun.entities.BonusText;
import com.tooflya.bubblefun.entities.BubbleGum;
import com.tooflya.bubblefun.entities.BubbleSnow;
import com.tooflya.bubblefun.entities.ButtonScaleable;
import com.tooflya.bubblefun.entities.Chiky;
import com.tooflya.bubblefun.entities.ChikySnow;
import com.tooflya.bubblefun.entities.Cloud;
import com.tooflya.bubblefun.entities.Coin;
import com.tooflya.bubblefun.entities.CristmasHeat;
import com.tooflya.bubblefun.entities.Entity;
import com.tooflya.bubblefun.entities.Feather;
import com.tooflya.bubblefun.entities.Glint;
import com.tooflya.bubblefun.entities.Gradient;
import com.tooflya.bubblefun.entities.Mark;
import com.tooflya.bubblefun.entities.Snowflake;
import com.tooflya.bubblefun.entities.Sprike;
import com.tooflya.bubblefun.entities.Sprite;
import com.tooflya.bubblefun.entities.TutorialText;
import com.tooflya.bubblefun.managers.CloudsManager;
import com.tooflya.bubblefun.managers.EntityManager;
import com.tooflya.bubblefun.managers.LevelsManager;
import com.tooflya.bubblefun.managers.SnowManager;

/**
 * @author Tooflya.com
 * @since
 */
public class LevelScreen extends Screen implements IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================

	protected static final EntityManager<Cloud> birds = null;

	public static boolean isTutorialNeeded = false;

	public static int mBubblesCount;
	public static int AIR;
	public static boolean running;
	public static int deadBirds;
	private static boolean isResetAnimationRunning;
	private boolean mLevelEndRunning;

	public static int Score;

	private Gradient mBackground;

	private final CloudsManager<Cloud> mClouds;
	private final SnowManager<Snowflake> mSnowflakes;

	public EntityManager<AwesomeText> mAwesomeKillText;
	public EntityManager<AwesomeText> mDoubleKillText;
	public EntityManager<AwesomeText> mTripleKillText;
	public EntityManager<BonusText> mBonusesText;

	private final Rectangle mRectangle;

	private final Sprite mSolidLine;
	private final Sprite mSolidLineAir;

	private final Rectangle shape;

	private final Sprite mLevelWord;
	private final EntityManager<Sprite> numbers;

	private Sprite mResetText;

	public final MoveModifier restartMove1;

	private final AlphaModifier rectangleAlphaModifierOn;
	private final AlphaModifier rectangleAlphaModifierOff;

	private final MoveModifier restartMove2;

	private final MoveModifier restartMove3;

	public final EntityManager<Bonus> bonuses;
	public final EntityManager<AwesomeText> splashBonusesPicture;

	public final EntityManager<Acceleration> accelerators;

	public EntityManager<Mark> mMarks;

	// ===========================================================
	// Fields
	// ===========================================================

	private BubbleGum lastAirgum = null;

	public EntityManager<Coin> coins;

	public EntityManager<Chiky> chikies;
	public EntityManager<BubbleGum> airgums;
	public EntityManager<Feather> feathers;
	public EntityManager<Glint> glints;

	public BlueBird mBlueBird;

	private final AlphaModifier mAllFallDownModifier;
	private final AlphaModifier mAllFallUpModifier;

	private final AlphaModifier mDotterAirLineOn;
	private final AlphaModifier mDotterAirLineOff;

	//private Sprite mSpecialButton = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas3, Game.context, "bb-btn.png", 0, 100, 2, 1), this.mBackground);

	public EntityManager<Sprike> sprikes;

	private Sprite mPanel;

	private final ButtonScaleable mMenuButton;

	private final ButtonScaleable mResetButton;

	private final Sprite mScoreText;

	private final EntityManager<Sprite> numbersSmall;

	public EntityManager<CristmasHeat> mCristmasHats;
	public EntityManager<Sprite> mSnowBallSpeed;

	// ===========================================================
	// Tutorial
	// ===========================================================

	public final TiledTextureRegion[] mTutorialTextureRegion = new TiledTextureRegion[10];

	public final ArrayList<TutorialText> mTutorialSprites = new ArrayList<TutorialText>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelScreen() {
		this.mBackground = new Gradient(0, 0, Options.cameraWidth, Options.cameraHeight, Resources.mLevelBackgroundGradientTextureRegion, this);

		this.mSnowflakes = new SnowManager<Snowflake>(100, new Snowflake(Resources.mSnowFlakesTextureRegion, this.mBackground));
		this.mClouds = new CloudsManager<Cloud>(10, new Cloud(Resources.mBackgroundCloudTextureRegion, this.mBackground));

		this.mAwesomeKillText = new EntityManager<AwesomeText>(20, new AwesomeText(Resources.mAwesomeText1TextureRegion, this.mBackground));
		this.mDoubleKillText = new EntityManager<AwesomeText>(20, new AwesomeText(Resources.mAwesomeText2TextureRegion, this.mBackground));
		this.mTripleKillText = new EntityManager<AwesomeText>(20, new AwesomeText(Resources.mAwesomeText3TextureRegion, this.mBackground));
		this.mBonusesText = new EntityManager<BonusText>(20, new BonusText(Resources.mScoreBonusesTextTextureRegion, this.mBackground));

		this.mRectangle = this.makeColoredRectangle(0, 0, 1f, 1f, 1f);
		this.mSolidLine = new Sprite(Resources.mAirOneTextureRegion, this.mBackground);
		this.mSolidLineAir = new Sprite(Resources.mAirTwoTextureRegion, this.mBackground);

		shape = new Rectangle(0, 0, Options.cameraWidth, Options.cameraHeight) {

			private float s = 0.015f;

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

				for (int i = 0; i < this.getChildCount(); i++) {
					this.getChild(i).setAlpha(1f);
				}
				modifier = false;
			}
		};

		mLevelWord = new Sprite(Resources.mLevelWordTextureRegion, this.shape);
		numbers = new EntityManager<Sprite>(4, new Sprite(Resources.mNumbersTextureRegion, this.shape));

		mResetText = new Sprite(Resources.mRestartTextTextureRegion, this.mRectangle);

		bonuses = new EntityManager<Bonus>(10, new Bonus(Resources.mBonusTextureRegion, this.mBackground));

		splashBonusesPicture = new EntityManager<AwesomeText>(10, new AwesomeText(Resources.mBonusSplasheTextureRegion, this.mBackground));

		accelerators = new EntityManager<Acceleration>(10, new Acceleration(Resources.mAcceleratorsTextureRegion, this.mBackground));

		mMarks = new EntityManager<Mark>(100, new Mark(Resources.mMarkTextureRegion, this.mBackground));

		coins = new EntityManager<Coin>(100, new Coin(Resources.mCoinsTextureRegion, this.mBackground));

		airgums = new EntityManager<BubbleGum>(100, new BubbleGum(Resources.mBubbleTextureRegion, this.mBackground));
		feathers = new EntityManager<Feather>(100, new Feather(Resources.mFeathersTextureRegion, this.mBackground));
		glints = new EntityManager<Glint>(100, new Glint(Resources.mGlintsTextureRegion, this.mBackground));
		this.chikies = new EntityManager<Chiky>(100, new Chiky(Resources.mRegularBirdsTextureRegion, this.mBackground));

		mSnowBallSpeed = new EntityManager<Sprite>(100, new Sprite(Resources.mSnowyBallSpeedTextureRegion, this.mBackground));

		mCristmasHats = new EntityManager<CristmasHeat>(100, new CristmasHeat(Resources.mSnowyBirdsHatTextureRegion, this.mBackground));
		mBlueBird = new BlueBird(Resources.mBlueBirdTextureRegion, new EntityManager<Feather>(100, new Feather(Resources.mBlueFeathersTextureRegion, this.mBackground)), this.mBackground);

		sprikes = new EntityManager<Sprike>(100, new Sprike(Resources.mSprikeTextureRegion, this.mBackground));

		mPanel = new Sprite(Resources.mTopGamePanelTextureRegion, this.mBackground);

		mScoreText = new Sprite(Resources.mScoreTextTextureRegion, this.mBackground);

		numbersSmall = new EntityManager<Sprite>(4, new Sprite(Resources.mSmallNumbersTextureRegion, this.mBackground));

		mMenuButton = new ButtonScaleable(Resources.mMenuButtonTextureRegion, this.mBackground) {

			@Override
			public void onClick() {
				Game.screens.setChildScreen(Game.screens.get(Screen.PAUSE), false, true, true);
			}
		};

		mResetButton = new ButtonScaleable(Resources.mRestartButtonTextureRegion, this.mBackground) {

			@Override
			public void onClick() {
				if (!isResetAnimationRunning) {
					reInit();
				}
			}
		};

		restartMove1 = new MoveModifier(0.5f, -mResetText.getWidth(), Options.cameraWidth / 8, Options.cameraCenterY,
				Options.cameraCenterY) {
			@Override
			public void onStarted() {
				rectangleAlphaModifierOn.reset();
			}

			@Override
			public void onFinished() {
				restartMove2.reset();
			}
		};

		rectangleAlphaModifierOn = new AlphaModifier(1f, 0f, 0.7f);
		rectangleAlphaModifierOff = new AlphaModifier(1f, 0.7f, 0f);

		restartMove2 = new MoveModifier(1f, Options.cameraWidth / 8, Options.cameraWidth / 8 * 2, Options.cameraCenterY,
				Options.cameraCenterY) {
			@Override
			public void onFinished() {
				restartMove3.reset();
			}
		};

		restartMove3 = new MoveModifier(0.5f, Options.cameraWidth / 8 * 2, Options.cameraWidth, Options.cameraCenterY,
				Options.cameraCenterY) {
			@Override
			public void onFinished() {
				reInit();
			}
		};

		mAllFallDownModifier = new AlphaModifier(13.4f, 1f, 0f);
		mAllFallUpModifier = new AlphaModifier(13.4f, 0f, 1f);

		mDotterAirLineOn = new AlphaModifier(1f, 0f, 1f) {
			@Override
			public void onFinished() {
				mDotterAirLineOff.reset();
			}
		};
		mDotterAirLineOff = new AlphaModifier(1f, 1f, 0f) {
			@Override
			public void onFinished() {
				mDotterAirLineOn.reset();
			}
		};

		this.mBackground.setBackgroundCenterPosition();

		// ===========================================================
		// Tutorial

		//this.mTutorialTextureRegion[0] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTutorialTextureAtlas, Game.context, "tutorial/tutorial-text-1.png", 0, 0, 1, 1);
		//this.mTutorialTextureRegion[1] = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mTutorialTextureAtlas, Game.context, "tutorial/tutorial-text-2.png", 0, 40, 1, 1);

		// ===========================================================

		this.mPanel.create().setPosition(0, 0);

		this.setOnSceneTouchListener(this);

		mSolidLine.create().setCenterPosition(Options.cameraWidth - this.mSolidLine.getWidth() / 2, Options.cameraHeight - Options.touchHeight);
		mSolidLineAir.create().setCenterPosition(Options.cameraWidth - this.mSolidLine.getWidth() / 2, Options.cameraHeight - Options.touchHeight - 8f);
		mSolidLine.enableFullBlendFunction();

		this.attachChild(shape);
		this.shape.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.shape.setAlpha(0f);

		this.mLevelWord.create().setCenterPosition(Options.cameraCenterX, Options.cameraCenterY);
		this.numbers.create().setCenterPosition(Options.cameraCenterX - 38f, Options.cameraCenterY);
		this.numbers.create().setCenterPosition(Options.cameraCenterX - 10f, Options.cameraCenterY);
		this.numbers.create().setCenterPosition(Options.cameraCenterX + 18f, Options.cameraCenterY);
		this.numbers.create().setCenterPosition(Options.cameraCenterX + 43f, Options.cameraCenterY);

		this.numbersSmall.create().setPosition(97, 8);
		this.numbersSmall.create().setPosition(111, 8);
		this.numbersSmall.create().setPosition(125, 8);
		this.numbersSmall.create().setPosition(138, 8);

		this.numbersSmall.getByIndex(0).setCurrentTileIndex(0);
		this.numbersSmall.getByIndex(1).setCurrentTileIndex(0);
		this.numbersSmall.getByIndex(2).setCurrentTileIndex(0);
		this.numbersSmall.getByIndex(3).setCurrentTileIndex(0);

		for (int i = 0; i < shape.getChildCount(); i++) {
			((Shape) shape.getChild(i)).setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}

		this.mMenuButton.create().setPosition(Options.cameraWidth - (0 + this.mMenuButton.getWidth()), 3f);
		this.mResetButton.create().setPosition(Options.cameraWidth - (5 + this.mMenuButton.getWidth() + this.mResetButton.getWidth()), 3f);

		this.mResetText.create().setPosition(-mResetText.getWidth(), Options.cameraCenterY - mResetText.getHeight() / 2f);
		this.mResetText.registerEntityModifier(restartMove1);
		this.mResetText.registerEntityModifier(restartMove2);
		this.mResetText.registerEntityModifier(restartMove3);

		this.mRectangle.registerEntityModifier(rectangleAlphaModifierOn);
		this.mRectangle.registerEntityModifier(rectangleAlphaModifierOff);

		mScoreText.create().setPosition(10, 5);

		this.mSolidLine.enableBlendFunction();
		this.mSolidLineAir.enableBlendFunction();
		this.mResetButton.enableBlendFunction();
		this.mMenuButton.enableBlendFunction();
		this.mScoreText.enableBlendFunction();
		this.mPanel.enableBlendFunction();

		for (int i = 0; i < this.numbersSmall.getCount(); i++) {
			this.numbersSmall.getByIndex(i).enableBlendFunction();
		}

		this.mSolidLine.registerEntityModifier(this.mAllFallUpModifier);
		this.mSolidLineAir.registerEntityModifier(this.mAllFallUpModifier);
		this.mResetButton.registerEntityModifier(this.mAllFallUpModifier);
		this.mMenuButton.registerEntityModifier(this.mAllFallUpModifier);
		this.mScoreText.registerEntityModifier(this.mAllFallUpModifier);
		this.mPanel.registerEntityModifier(this.mAllFallUpModifier);
		for (int i = 0; i < this.numbersSmall.getCount(); i++) {
			this.numbersSmall.getByIndex(i).registerEntityModifier(this.mAllFallUpModifier);
		}

		this.mSolidLine.registerEntityModifier(this.mAllFallDownModifier);
		this.mSolidLineAir.registerEntityModifier(this.mAllFallDownModifier);
		this.mResetButton.registerEntityModifier(this.mAllFallDownModifier);
		this.mMenuButton.registerEntityModifier(this.mAllFallDownModifier);
		this.mScoreText.registerEntityModifier(this.mAllFallDownModifier);
		this.mPanel.registerEntityModifier(this.mAllFallDownModifier);

		for (int i = 0; i < this.numbersSmall.getCount(); i++) {
			this.numbersSmall.getByIndex(i).registerEntityModifier(this.mAllFallDownModifier);
		}

		this.mBonusPanel = new Sprite(Resources.mRegularBonusPanelTextureRegion, this.mBackground);
		this.mBonusPanel.create();
		this.mBonusPanel.setCenterPosition(Options.cameraWidth - this.mBonusPanel.getWidth() / 2, Options.cameraHeight - this.mBonusPanel.getHeight() / 2);
		this.mBonusPanel.registerEntityModifier(this.mAllFallUpModifier);
		this.mBonusPanel.registerEntityModifier(this.mAllFallDownModifier);

		this.mBonusButton1 = new ButtonScaleable(Resources.mBonusButton1TextureRegion, this.mBonusPanel) {
			@Override
			public void onClick() {
				for (int i = 0; i < chikies.getCount(); i++) {
					Chiky bird = chikies.getByIndex(i);
					BubbleGum bubble = airgums.create();
					bubble.setPosition(bird.getX(), Options.cameraHeight);
					bubble.initFinishPosition(bubble.getX(), 0);
				}
			}
		};
		this.mBonusButton1.create().setPosition(2f, 61f);

	}

	private final Sprite mBonusPanel;
	private final ButtonScaleable mBonusButton1;

	// ===========================================================
	// Virtual methods
	// ===========================================================

	public void reInit() {
		this.mAllFallUpModifier.reset();

		Score = 0;
		rectangleAlphaModifierOff.reset();

		running = true;
		deadBirds = 0;
		isResetAnimationRunning = false;
		this.mLevelEndRunning = false;

		mAwesomeKillText.clear();
		mDoubleKillText.clear();
		mTripleKillText.clear();
		mBonusesText.clear();
		bonuses.clear();

		mBlueBird.create();
		mBlueBird.clear();
		airgums.clear();
		chikies.clear();
		this.mCristmasHats.clear();
		glints.clear();
		accelerators.clear();
		sprikes.clear();
		mMarks.clear();
		coins.clear();

		generateChikies();

		feathers.clear();

		for (TutorialText sprite : mTutorialSprites) {
			if (sprite.getAlpha() > 0)
				sprite.destroy();
		}

		Options.bubbleSizePower = Options.bubbleMaxSizePower;

		AIR = 100;

		mBubblesCount = 0;

		shape.reset();

		numbers.getByIndex(0).setCurrentTileIndex(Options.boxNumber + 1);

		if (Options.levelNumber < 10) {
			mLevelWord.setCenterPosition(Options.cameraCenterX - 10f, Options.cameraCenterY - 40f);
			numbers.getByIndex(3).setVisible(false);

			numbers.getByIndex(2).setCurrentTileIndex(Options.levelNumber);
		} else {
			mLevelWord.setCenterPosition(Options.cameraCenterX, Options.cameraCenterY - 40f);
			numbers.getByIndex(3).setVisible(true);

			numbers.getByIndex(2).setCurrentTileIndex((int) Math.floor(Options.levelNumber / 10));
			numbers.getByIndex(3).setCurrentTileIndex(Options.levelNumber % 10);
		}

		numbers.getByIndex(1).setCurrentTileIndex(10);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void generateChikies() {
		LevelsManager.generateLevel(Options.levelNumber);
	}

	private void checkCollision() {
		Chiky chiky;
		BubbleGum airgum;
		for (int i = chikies.getCount() - 1; i >= 0; --i) {
			chiky = chikies.getByIndex(i);
			if (chiky.isCanCollide()) {
				for (int j = airgums.getCount() - 1; j >= 0; --j) {
					airgum = (BubbleGum) airgums.getByIndex(j);
					if (airgum.isCanCollide() && this.isCollide(chiky, airgum)) {
						airgum.addBirdsKills();
						chiky.setCollide(airgum);
						deadBirds++;
					}
				}
			}
		}
		for (int i = airgums.getCount() - 1; i >= 0; --i) {
			airgum = airgums.getByIndex(i);
			if (airgum.isCanCollide()) {
				for (int j = sprikes.getCount() - 1; j >= 0; --j) {
					final Sprike sprike = sprikes.getByIndex(j);
					if (this.isCollide(airgum, sprike, true)) {
						airgum.isCollide();
					}
				}
				for (int j = bonuses.getCount() - 1; j >= 0; --j) {
					final Bonus bonus = bonuses.getByIndex(j);
					if (this.isCollide(airgum, bonus, true)) {
						final AwesomeText boom = splashBonusesPicture.create();
						boom.setCenterPosition(bonus.getCenterX(), bonus.getCenterY());
						boom.setReverse();

						airgum.isCollide();
						bonus.isCollide();

						//this.mSpecialButton.setCurrentTileIndex(1);
					}
				}

				for (int k = 0; k < this.coins.getCount(); k++) {
					final Coin air = this.coins.getByIndex(k);

					if (air.getX() <= (airgum.getX() + airgum.getWidthScaled()) &&
							airgum.getX() <= (air.getX() + air.getWidthScaled()) &&
							air.getY() <= (airgum.getY() + airgum.getHeightScaled()) &&
							airgum.getY() <= (air.getY() + air.getHeightScaled())) {
						air.remove();
					}

					if (!air.mIsAlreadyFollow) {
						if (air.getX() <= (airgum.getX() + airgum.getWidthScaled() * 1.1f) &&
								(airgum.getX() - airgum.getWidthScaled()) <= (air.getX() + air.getWidthScaled()) &&
								air.getY() <= (airgum.getY() + airgum.getHeightScaled() * 1.1f) &&
								airgum.getY() - airgum.getHeightScaled() <= (air.getY() + air.getHeightScaled())) {
							air.follow(airgum);
						}
					}
				}

				if (!mBlueBird.isSleep() && this.isCollide(mBlueBird, airgum)) {
					mBlueBird.particles();
					if (!airgum.isAnimationRunning()) {
						airgum.isCollide();
					}
				}
			}
		}
	}

	private boolean isCollide(Entity entity1, Entity entity2) {
		final float x = entity2.getCenterX() - entity1.getCenterX();
		final float y = entity2.getCenterY() - entity1.getCenterY();
		final float d = entity2.getWidthScaled() / 2 + entity1.getWidthScaled() / 2;
		return x * x + y * y < d * d;
	}

	private boolean isCollide(Entity entity1, Entity entity2, final boolean rectangle) {
		return !((entity1.getX() + entity1.getWidth() <= entity2.getX()) ||
				(entity2.getX() + entity2.getWidth() <= entity1.getX()) ||
				(entity2.getY() + entity2.getHeight() <= entity1.getY()) || (entity1.getY() + entity1.getHeight() <= entity2.getY()));
		// TODO: (R) What to do with scaledSize and various rotationCenter?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();

		switch (Options.boxNumber) {
		case 0:
			this.onWOCBoxAttached();
			break;
		case 1:
			this.onSDBoxAttached();
			break;
		case 2:
			this.onSTBoxAttached();
			break;
		}

		this.reInit();

		if (!Options.mLevelSound.isPlaying() && Options.isMusicEnabled) {
			Options.mLevelSound.play();
		}
	}

	/* (non-Javadoc)
	 * @see com.tooflya.bubblefun.screens.Screen#onPostAttached()
	 */
	@Override
	public void onPostAttached() {
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

		switch (Options.boxNumber) {
		case 0:
			this.onManagedUpdateWOCBox();
			break;
		case 1:
			this.onManagedUpdateSDBox();
			break;
		case 2:
			this.onManagedUpdateSTBox();
			break;
		}

		this.checkCollision();

		if (chikies.getCount() == 0 && mAwesomeKillText.getCount() == 0 && mDoubleKillText.getCount() == 0 && mTripleKillText.getCount() == 0 && !this.mLevelEndRunning) {
			Game.screens.setChildScreen(Game.screens.get(Screen.LEVELEND), false, false, true);
			this.mLevelEndRunning = true;

			airgums.clear();
			coins.clear();
			feathers.clear();

			this.lastAirgum = null;

			for (TutorialText sprite : mTutorialSprites) {
				sprite.destroy();
			}

			AIR = 0;
			this.mAllFallDownModifier.reset();
		} else {

			if (AIR <= 0 && running) {
				running = false;
			}

			if (airgums.getCount() <= 0 && deadBirds <= 0 && !running && !isResetAnimationRunning && !this.mLevelEndRunning) {
				restartMove1.reset();
				isResetAnimationRunning = true;
			}
		}

		/* Score */
		if (Score < 10) {
			numbersSmall.getByIndex(0).setCurrentTileIndex(Score);
			numbersSmall.getByIndex(0).setVisible(true);
			numbersSmall.getByIndex(1).setVisible(false);
			numbersSmall.getByIndex(2).setVisible(false);
			numbersSmall.getByIndex(3).setVisible(false);
		} else if (Score < 100) {
			numbersSmall.getByIndex(0).setCurrentTileIndex((int) FloatMath.floor(Score / 10));
			numbersSmall.getByIndex(1).setCurrentTileIndex((int) FloatMath.floor(Score % 10));
			numbersSmall.getByIndex(0).setVisible(true);
			numbersSmall.getByIndex(1).setVisible(true);
			numbersSmall.getByIndex(2).setVisible(false);
			numbersSmall.getByIndex(3).setVisible(false);
		} else if (Score < 1000) {
			numbersSmall.getByIndex(0).setCurrentTileIndex((int) FloatMath.floor(Score / 100));
			numbersSmall.getByIndex(1).setCurrentTileIndex((int) FloatMath.floor((Score - FloatMath.floor(Score / 100) * 100) / 10));
			numbersSmall.getByIndex(2).setCurrentTileIndex((int) FloatMath.floor(Score % 10));
			numbersSmall.getByIndex(0).setVisible(true);
			numbersSmall.getByIndex(1).setVisible(true);
			numbersSmall.getByIndex(2).setVisible(true);
			numbersSmall.getByIndex(3).setVisible(false);
		} else {
			numbersSmall.getByIndex(0).setCurrentTileIndex((int) FloatMath.floor(Score / 1000));
			numbersSmall.getByIndex(1).setCurrentTileIndex((int) FloatMath.floor((Score - FloatMath.floor(Score / 1000) * 1000) / 100));
			numbersSmall.getByIndex(2).setCurrentTileIndex((int) FloatMath.floor((Score - FloatMath.floor(Score / 100) * 100) / 10));
			numbersSmall.getByIndex(3).setCurrentTileIndex((int) FloatMath.floor(Score % 10));
			numbersSmall.getByIndex(0).setVisible(true);
			numbersSmall.getByIndex(1).setVisible(true);
			numbersSmall.getByIndex(2).setVisible(true);
			numbersSmall.getByIndex(3).setVisible(true);
		}

		/* AIR LINE */
		final float baseWidth = this.mSolidLineAir.getBaseWidth();
		this.mSolidLineAir.setWidth((int) (baseWidth / 100 * AIR));
		this.mSolidLineAir.getTextureRegion().setWidth((int) (baseWidth / 100 * AIR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (this.hasChildScene()) {
			if (this.getChildScene() != Game.screens.get(Screen.LEVELEND)) {
				Game.screens.clearChildScreens();
			}
		} else {
			Game.screens.setChildScreen(Game.screens.get(Screen.PAUSE), false, true, true);
		}
	}

	/* (non-Javadoc)
	 * @see org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener#onSceneTouchEvent(org.anddev.andengine.entity.scene.Scene, org.anddev.andengine.input.touch.TouchEvent)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent pTouchEvent) {
		final float pTouchX = pTouchEvent.getX() / Options.cameraRatioFactor;
		final float pTouchY = pTouchEvent.getY() / Options.cameraRatioFactor;

		switch (pTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			if (AIR > 0 && chikies.getCount() > 0 && this.lastAirgum == null && pTouchY > Options.cameraHeight - Options.touchHeight) {
				this.lastAirgum = (BubbleGum) airgums.create();
				this.lastAirgum.initStartPosition(pTouchX, pTouchY);
				this.lastAirgum.setScaleCenter(this.lastAirgum.getWidth() / 2, this.lastAirgum.getHeight() / 2);
			}
			break;
		case TouchEvent.ACTION_UP:
			if (this.lastAirgum != null) {
				this.lastAirgum.initFinishPosition(pTouchX, pTouchY);

				this.lastAirgum = null;

				mBubblesCount++;
			}
			break;
		case TouchEvent.ACTION_MOVE:
			if (this.lastAirgum != null) {
				mMarks.clear();

				float xr = pTouchX, yr = pTouchY;

				float mSpeedX, mSpeedY;

				float angle = (float) Math.atan2(yr - this.lastAirgum.getCenterY(), xr - this.lastAirgum.getCenterX());
				float distance = MathUtils.distance(this.lastAirgum.getCenterX(), this.lastAirgum.getCenterY(), xr, yr);
				if (distance < Options.eps) {
					mSpeedX = 0;
					mSpeedY = (Options.bubbleMinSpeed + Options.bubbleMaxSpeed) / 2 - this.lastAirgum.mLostedSpeed;
					mSpeedY = mSpeedY < Options.bubbleMinSpeed ? Options.bubbleMinSpeed : mSpeedY;
					mSpeedY = -mSpeedY;
				}
				else {
					distance -= this.lastAirgum.mLostedSpeed;
					distance = distance < Options.bubbleMinSpeed ? Options.bubbleMinSpeed : distance;
					distance = distance > Options.bubbleMaxSpeed ? Options.bubbleMaxSpeed : distance;

					if (0 < angle) {
						angle -= Options.PI;
					}

					mSpeedX = distance * FloatMath.cos(angle);
					mSpeedY = distance * FloatMath.sin(angle);

					float dx = mSpeedX / FloatMath.sqrt((float) (Math.pow(mSpeedX, 2) + Math.pow(mSpeedY, 2)));
					float dy = mSpeedY / FloatMath.sqrt((float) (Math.pow(mSpeedX, 2) + Math.pow(mSpeedY, 2)));

					float x = this.lastAirgum.getX() + this.lastAirgum.getWidthScaled() / 2, y = this.lastAirgum.getY() + this.lastAirgum.getHeightScaled() / 2;
					while (0 < x && x < Options.cameraWidth && 0 < y && y < Options.cameraHeight) {
						Entity w = mMarks.create();
						w.setSpeed(dx, dy);
						if (w != null)
							w.setCenterPosition(x, y);

						x += dx * 35;
						y += dy * 35;
					}

					x = this.lastAirgum.getX() + this.lastAirgum.getWidthScaled() / 2;
					y = this.lastAirgum.getY() + this.lastAirgum.getHeightScaled() / 2;
					while (0 < x && x < Options.cameraWidth && 0 < y && y < Options.cameraHeight) {
						Entity w = mMarks.create();
						if (w != null)
							w.setCenterPosition(x, y);
						w.setSpeed(dx, dy);
						x -= dx * 35;
						y -= dy * 35;
					}

				}
			}
			break;
		}

		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void onWOCBoxAttached() {
		this.mPanel.changeTextureRegion(Resources.mTopGamePanelTextureRegion);
		this.mLevelWord.changeTextureRegion(Resources.mLevelWordTextureRegion);
		this.numbers.changeTextureRegion(Resources.mSpecialNumbers1TextureRegion);

		this.mClouds.clear();
		this.mClouds.generateStartClouds();

		this.airgums.clear();
		this.airgums = new EntityManager<BubbleGum>(100, new BubbleGum(Resources.mBubbleTextureRegion, this.mBackground));

		this.chikies.clear();
		this.chikies = new EntityManager<Chiky>(100, new Chiky(Resources.mRegularBirdsTextureRegion, this.mBackground));

		this.mBonusPanel.changeTextureRegion(Resources.mRegularBonusPanelTextureRegion);
		this.mBonusButton1.changeTextureRegion(Resources.mBonusButton1TextureRegion);
	}

	private void onSDBoxAttached() {
		this.mPanel.changeTextureRegion(Resources.mSnowyTopGamePanelTextureRegion);
		this.mLevelWord.changeTextureRegion(Resources.mLevelSnowyWordTextureRegion);
		this.numbers.changeTextureRegion(Resources.mSpecialNumbers2TextureRegion);

		this.mClouds.clear();
		this.mSnowflakes.generateStartSnow();

		this.airgums.clear();
		this.airgums = new EntityManager<BubbleGum>(100, new BubbleSnow(Resources.mSnowyBubbleTextureRegion, this.mBackground));

		this.chikies.clear();
		this.chikies = new EntityManager<Chiky>(100, new ChikySnow(Resources.mSnowyBirdsTextureRegion, this.mBackground));

		this.mCristmasHats.clear();
		this.mCristmasHats = new EntityManager<CristmasHeat>(100, new CristmasHeat(Resources.mSnowyBirdsHatTextureRegion, this.mBackground));

		this.mBonusPanel.changeTextureRegion(Resources.mSnowBonusPanelTextureRegion);
		this.mBonusButton1.changeTextureRegion(Resources.mBonusButton1SnowTextureRegion);
	}

	private void onSTBoxAttached() {
	}

	private void onManagedUpdateWOCBox() {
		this.mClouds.update();
	}

	private void onManagedUpdateSDBox() {
		this.mClouds.update();
		this.mSnowflakes.update();
	}

	private void onManagedUpdateSTBox() {

	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, Options.cameraWidth, Options.cameraHeight);
		coloredRect.setColor(pRed, pGreen, pBlue);
		coloredRect.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		coloredRect.setAlpha(0f);

		this.attachChild(coloredRect);

		return coloredRect;
	}
}
