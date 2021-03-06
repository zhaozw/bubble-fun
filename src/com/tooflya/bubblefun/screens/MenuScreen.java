package com.tooflya.bubblefun.screens;

import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.R;
import com.tooflya.bubblefun.Resources;
import com.tooflya.bubblefun.entities.ButtonScaleable;
import com.tooflya.bubblefun.entities.Cloud;
import com.tooflya.bubblefun.entities.Entity;
import com.tooflya.bubblefun.entities.PlayIcon;
import com.tooflya.bubblefun.entities.ShopIndicator;
import com.tooflya.bubblefun.managers.CloudsManager;
import com.tooflya.bubblefun.managers.ScreenManager;

/**
 * @author Tooflya.com
 * @since
 */
public class MenuScreen extends ReflectionScreen {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final float ICONS_SIZE = 44f;
	private static final float ICONS_PADDING = 10f;
	private static final float ICONS_PADDING_BETWEEN = 10f;

	// ===========================================================
	// Fields
	// ===========================================================

	private final Entity mLogoBackground;
	private final Entity mSettingsIcon;
	private final Entity mBlueBird;
	private final Entity mParachuteBird1;
	private final Entity mParachuteBird2;
	private final Entity mBird1;
	private final Entity mBird2;

	private final ButtonScaleable mTwitterIcon;
	private final ButtonScaleable mFacebookIcon;
	private final ButtonScaleable mMoreIcon;
	private final ButtonScaleable mSoundIcon;
	private final ButtonScaleable mMusicIcon;
	private final ButtonScaleable mBuyButton;
	private final PlayIcon mPlayIcon;

	private final ShopIndicator mShopIndicator;

	private final RotationModifier mRotateOn = new RotationModifier(0.3f, 0f, 405f);
	private final RotationModifier mRotateOff = new RotationModifier(0.3f, 405f, 0f);

	private final MoveModifier mMoreMoveOn = new MoveModifier(0.3f, ICONS_PADDING * 2, 53f + ICONS_PADDING * 2, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			registerTouchArea(mMoreIcon);
		}
	};

	private final MoveModifier mMoreMoveOff = new MoveModifier(0.3f, ICONS_PADDING * 2 + 53f, ICONS_PADDING, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			unregisterTouchArea(mMoreIcon);
		}
	};

	private final MoveModifier mMusicMoveOn = new MoveModifier(0.3f, ICONS_PADDING * 2, 90f + ICONS_PADDING * 2, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			registerTouchArea(mSoundIcon);
		}
	};
	private final MoveModifier mMusicMoveOff = new MoveModifier(0.3f, ICONS_PADDING * 2 + 90f, ICONS_PADDING, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			unregisterTouchArea(mSoundIcon);
		}
	};

	private final MoveModifier mSoundMoveOn = new MoveModifier(0.3f, ICONS_PADDING * 2, 127f + ICONS_PADDING * 2, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			registerTouchArea(mMusicIcon);
		}
	};
	private final MoveModifier mSoundMoveOff = new MoveModifier(0.3f, ICONS_PADDING * 2 + 90f, ICONS_PADDING, Options.cameraHeight - 50f, Options.cameraHeight - 50f) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.modifiers.MoveModifier#onFinished()
		 */
		@Override
		public void onFinished() {
			unregisterTouchArea(mMusicIcon);
		}
	};

	private final Runnable mShowRatingDialog = new Runnable() {
		@Override
		public void run() {
			AlertDialog.Builder builder = new AlertDialog.Builder(Game.mInstance);
			builder.setTitle("Participation in the rating")
					.setMessage("Do you want to participate in the game rating?")
					.setNeutralButton("Later", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mDontShopRating = true;
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mDontShopRating = true;
						}
					})
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Game.mInstance.runOnUiThread(mShowNameDialog);
						}
					});
			builder.create().show();
		}
	};

	private final Runnable mShowNameDialog = new Runnable() {
		@Override
		public void run() {
			final LayoutInflater in = LayoutInflater.from(Game.mInstance);
			final View inf = in.inflate(R.layout.rating_name, null);

			AlertDialog.Builder builder = new AlertDialog.Builder(Game.mInstance);
			builder.setView(inf);
			builder.setTitle("Participation in the rating")
					.setMessage("Please enter your user name. It will appear in the table of records.")
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mDontShopRating = true;
						}
					})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Game.mDatabase.setRatingName(((EditText) inf.findViewById(R.id.username)).getText().toString());
							System.out.println(Game.mDatabase.getRatingName());
							Game.mDatabase.disableRating();
						}
					});
			builder.create().show();
		}
	};

	private boolean mDontShopRating = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuScreen() {
		this.mBackground = Resources.mBackgroundGradient.deepCopy(this);
		this.mBackgroundHouses = Resources.mBackgroundHouses1.deepCopy(this.mBackground);
		this.mBackgroundGrass = Resources.mBackgroundGrass3.deepCopy(this.mBackground);
		this.mBackgroundWater = Resources.mBackgroundWater.deepCopy(this.mBackground);

		this.mBlueBird = new Entity(Resources.mBackgroundBlueBirdTextureRegion, this.mBackground);
		this.mParachuteBird1 = new Entity(Resources.mBackgroundParachuteBirdTextureRegion, this.mBackground);
		this.mParachuteBird2 = new Entity(Resources.mBackgroundParachuteBirdTextureRegion, this.mBackground);
		this.mBird1 = new Entity(Resources.mBackgroundBirdTextureRegion, this.mBackground);
		this.mBird2 = new Entity(Resources.mBackgroundBirdTextureRegion, this.mBackground);

		this.mClouds = new CloudsManager<Cloud>(10, new Cloud(Resources.mBackgroundCloudTextureRegion, this.mBackground));

		this.mLogoBackground = new Entity(Resources.mBackgroundLogoNameTextureRegion, this.mBackground);

		this.mTwitterIcon = new ButtonScaleable(Resources.mTwitterTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse("twitter://user?screen_name=tooflya"));
					Game.mInstance.startActivity(intent);

				} catch (Exception e) {
					Game.mInstance.startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("https://twitter.com/#!/tooflya")));
				}
			}

		};

		this.mFacebookIcon = new ButtonScaleable(Resources.mFacebookTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				try {
					Game.mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0);
					Game.mInstance.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/386292514777918")));
				} catch (Exception e) {
					Game.mInstance.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/386292514777918")));
				}
			}
		};

		this.mPlayIcon = new PlayIcon(Resources.mPlayIconTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				super.onClick();

				Game.mScreens.set(Screen.BOX);
			}
		};

		this.mBuyButton = new ButtonScaleable(Resources.mBuyButtonTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				StoreScreen.ATTACH_TYPE = 0;
				Game.mScreens.set(Screen.STORE);
			}
		};

		this.mMoreIcon = new ButtonScaleable(Resources.mMoreIconTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				Game.mScreens.set(Screen.MORE);
			}
		};

		this.mSoundIcon = new ButtonScaleable(Resources.mSoundIconTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				this.setCurrentTileIndex(Options.isSoundEnabled ? 1 : 0);
				Options.isSoundEnabled = !Options.isSoundEnabled;
			}
		};

		this.mMusicIcon = new ButtonScaleable(Resources.mMusicIconTextureRegion, this.mBackground) {

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				Options.isMusicEnabled = !Options.isMusicEnabled;

				if (Options.isMusicEnabled) {
					this.setCurrentTileIndex(0);
					Options.mMainSound.play();
				} else {
					this.setCurrentTileIndex(1);
					Options.mMainSound.pause();
				}
			}
		};

		this.mSettingsIcon = new ButtonScaleable(Resources.mSettingsIconTextureRegion, this.mBackground, true) {

			private boolean rotation = false;

			/* (non-Javadoc)
			 * @see com.tooflya.bubblefun.entities.Button#onClick()
			 */
			@Override
			public void onClick() {
				if (this.rotation) {
					MenuScreen.this.mRotateOff.reset();
					MenuScreen.this.mMoreMoveOff.reset();
					MenuScreen.this.mMusicMoveOff.reset();
					MenuScreen.this.mSoundMoveOff.reset();
				} else {
					MenuScreen.this.mRotateOn.reset();
					MenuScreen.this.mMoreMoveOn.reset();
					MenuScreen.this.mMusicMoveOn.reset();
					MenuScreen.this.mSoundMoveOn.reset();
				}
				this.rotation = !this.rotation;
			}
		};

		this.mShopIndicator = new ShopIndicator(Resources.mShopAvailableTextureRegion, this.mBuyButton);
		this.mShopIndicator.setCenterPosition(105f, 10f);

		this.mClouds.generateStartClouds();

		this.mBackground.create().setBackgroundCenterPosition();
		this.mBackgroundHouses.create().setPosition(0, Options.cameraHeight - this.mBackgroundHouses.getHeight());
		this.mBackgroundGrass.create().setPosition(0, Options.cameraHeight - this.mBackgroundGrass.getHeight());
		this.mBackgroundWater.create().setPosition(0, Options.cameraHeight - this.mBackgroundWater.getHeight());

		this.mLogoBackground.create().setCenterPosition(Options.cameraCenterX, mBackground.getY() + 170f);

		this.mBlueBird.create().setPosition(Options.cameraWidth - ICONS_PADDING_BETWEEN - ICONS_PADDING - ICONS_SIZE * 2, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE * 3);
		this.mParachuteBird2.create().setPosition(Options.cameraCenterX + 100f, Options.cameraCenterY + 50f);
		this.mBird1.create().setPosition(50f, Options.cameraCenterY);
		this.mBird2.create().setPosition(30f, Options.cameraCenterY + 50f);

		this.mBird1.setScale(1f);
		this.mBird2.setScale(0.8f);

		this.mParachuteBird1.setRotation(-5f);
		this.mParachuteBird2.setRotation(15f);

		this.mParachuteBird2.getTextureRegion().setFlippedHorizontal(true);

		this.mTwitterIcon.create().setPosition(Options.cameraWidth - ICONS_PADDING - ICONS_SIZE, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);
		this.mFacebookIcon.create().setPosition(Options.cameraWidth - ICONS_PADDING_BETWEEN - ICONS_PADDING - ICONS_SIZE * 2, Options.cameraHeight - ICONS_PADDING - ICONS_SIZE);

		this.mPlayIcon.create().setCenterPosition(Options.cameraCenterX, Options.cameraCenterY + 50f);
		this.mBuyButton.create().setCenterPosition(Options.cameraCenterX - 100f, Options.cameraCenterY + 180f);

		this.mSettingsIcon.create().setPosition(8f, Options.cameraHeight - 60f);
		this.mMoreIcon.create().setPosition(ICONS_PADDING, Options.cameraHeight - 50f);
		this.mSoundIcon.create().setPosition(ICONS_PADDING, Options.cameraHeight - 50f);
		this.mMusicIcon.create().setPosition(ICONS_PADDING, Options.cameraHeight - 50f);

		this.mSettingsIcon.setRotationCenter(this.mSettingsIcon.getWidthScaled() / 2, this.mSettingsIcon.getHeightScaled() / 2);

		this.mRotateOn.setRemoveWhenFinished(false);
		this.mRotateOff.setRemoveWhenFinished(false);

		this.mMoreMoveOn.setRemoveWhenFinished(false);
		this.mMoreMoveOff.setRemoveWhenFinished(false);

		this.mMusicMoveOn.setRemoveWhenFinished(false);
		this.mMusicMoveOff.setRemoveWhenFinished(false);

		this.mSettingsIcon.registerEntityModifier(this.mRotateOn);
		this.mSettingsIcon.registerEntityModifier(this.mRotateOff);

		this.mMoreIcon.registerEntityModifier(this.mMoreMoveOn);
		this.mMoreIcon.registerEntityModifier(this.mMoreMoveOff);

		this.mSoundIcon.registerEntityModifier(this.mMusicMoveOn);
		this.mSoundIcon.registerEntityModifier(this.mMusicMoveOff);

		this.mMusicIcon.registerEntityModifier(this.mSoundMoveOn);
		this.mMusicIcon.registerEntityModifier(this.mSoundMoveOff);

		this.unregisterTouchArea(this.mMoreIcon);
		this.unregisterTouchArea(this.mSoundIcon);
		this.unregisterTouchArea(this.mMusicIcon);
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();

		ScreenManager.mChangeAction = 0;

		this.mShopIndicator.update();

		if (Options.isMusicEnabled) {
			if (!Options.mMainSound.isPlaying() && Options.isMusicEnabled) {
				Options.mMainSound.play();
			}
		}

		Game.mAdvertisementManager.hideSmall();
	}

	/* (non-Javadoc)
	 * @see com.tooflya.bubblefun.screens.Screen#onPostAttached()
	 */
	@Override
	public void onPostAttached() {
		if (Game.mIsAlreadyPlayed) {
			Game.mScreens.setChildScreen(Game.mScreens.get(Screen.RATE), false, false, true);
		} else {
			if (!Game.mDatabase.isRatingDisabled() && !this.mDontShopRating) {
				Game.mInstance.runOnUiThread(this.mShowRatingDialog);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.Screen#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (this.hasChildScene()) {
			Game.mScreens.clearChildScreens();
		} else {
			Game.mScreens.setChildScreen(Game.mScreens.get(Screen.EXIT), false, false, true);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
}