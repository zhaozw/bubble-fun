package com.tooflya.bubblefun.screens;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.Screen;
import com.tooflya.bubblefun.background.AsyncTaskLoader;
import com.tooflya.bubblefun.background.IAsyncCallback;
import com.tooflya.bubblefun.entities.Entity;
import com.tooflya.bubblefun.entities.Sprite;

public class PreloaderScreen extends Screen implements IAsyncCallback {

	// ===========================================================
	// Constants
	// ===========================================================

	public static int mChangeAction = 0;

	private final static BitmapTextureAtlas mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	public final Sprite mBackground = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/preload-bg.png", 0, 0, 1, 1), this);
	public final Sprite mBalon = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/preload-fill.png", 0, 612, 1, 1), this) {

		/* (non-Javadoc)
		 * @see com.tooflya.bubblefun.entities.Entity#create()
		 */
		@Override
		public Entity create() {
			this.setHeight(1);
			this.getTextureRegion().setHeight(1);

			return super.create();
		}
	};
	public final Sprite mBar = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/preload-bar.png", 100, 612, 1, 1), this);
	public final Sprite mTextBar = new Sprite(BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBackgroundTextureAtlas, Game.context, Options.CR + "/preload-text.png", 0, 712, 1, 1), this);

	public int updates = 0;
	private final TimerHandler mTimer = new TimerHandler(1f / 15.0f, true, new ITimerCallback() {

		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {

			updates++;

			/** Changing size of progressbar */
			if (mBalon.getHeight() < mBalon.getBaseHeight()) {
				mBalon.getTextureRegion().setHeight(mBalon.getTextureRegion().getHeight() + 3);
				mBalon.setHeight(mBalon.getTextureRegion().getHeight());

				if (updates >= 15) {
					/** Start background loader */
					new AsyncTaskLoader().execute(PreloaderScreen.this);
				}

			} else {
				switch (mChangeAction) {
				case 0:
					Game.screens.set(Screen.LEVEL);
					break;
				case 1:
					Game.screens.set(Screen.MENU);
					break;
				default:
					Game.screens.set(Screen.CHOISE);
				}
				/** Register timer of loading progressbar changes */
				PreloaderScreen.this.unregisterUpdateHandler(mTimer);
			}
		}
	});

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PreloaderScreen() {
		this.loadResources();

		mBackground.create().setCenterPosition(Options.cameraCenterX, Options.cameraCenterY);

		mBalon.setRotationCenter(mBalon.getWidthScaled() / 2, mBalon.getHeightScaled() / 2);
		mBalon.setRotation(180);
		mBalon.setCenterPosition(Options.cameraCenterX, Options.cameraCenterY);
		mBalon.create();

		mBar.create().setCenterPosition(Options.cameraCenterX, Options.cameraCenterY);
		mTextBar.create().setCenterPosition(Options.cameraCenterX, Options.cameraCenterY + 100 * Options.cameraRatioFactor); // TODO: (R) Magic number.
	}

	@Override
	public void onAttached() {
		super.onAttached();

		updates = 0;
		mBalon.create();

		/** Register timer of loading progressbar changes */
		this.registerUpdateHandler(mTimer);
	}

	@Override
	public void onDetached() {
		super.onDetached();

		/** Register timer of loading progressbar changes */
		this.unregisterUpdateHandler(mTimer);
	}

	@Override
	public void loadResources() {
		Game.loadTextures(mBackgroundTextureAtlas);
	}

	@Override
	public void unloadResources() {
		Game.unloadTextures(mBackgroundTextureAtlas);
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void workToDo() {
		if (mChangeAction == 0) {
			Game.screens.get(Screen.EXIT).unloadResources();
			Game.screens.get(Screen.MENU).unloadResources();
			Game.screens.get(Screen.CHOISE).unloadResources();
			Game.screens.get(Screen.LEVEL).loadResources();
			Game.screens.get(Screen.LEVELEND).loadResources();
			Game.screens.get(Screen.PAUSE).loadResources();
		} else {
			Game.screens.get(Screen.PAUSE).unloadResources();
			Game.screens.get(Screen.LEVEL).unloadResources();
			Game.screens.get(Screen.LEVELEND).unloadResources();
			Game.screens.get(Screen.MENU).loadResources();
			Game.screens.get(Screen.CHOISE).loadResources();
			Game.screens.get(Screen.EXIT).loadResources();
		}
	}

	@Override
	public void onComplete() {
	}

}
