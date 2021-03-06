package com.tooflya.bubblefun.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tooflya.bubblefun.Game;
import com.tooflya.bubblefun.Options;
import com.tooflya.bubblefun.screens.Screen;
import com.tooflya.bubblefun.screens.StoreScreen;

/**
 * @author Tooflya.com
 * @since
 */
public class DataStorage extends SQLiteOpenHelper {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "storage";

	private static final String LEVEL_TABLE = "levels";
	private static final String BOX_TABLE = "boxes";
	private static final String MORE_TABLE = "more";

	private static final String LEVEL_ID = "id";
	private static final String LEVEL_STATE = "state";
	private static final String LEVEL_STARS = "stars";
	private static final String LEVEL_SCORE = "score";

	private static final String BOX_ID = "id";
	private static final String BOX_STATE = "state";

	private static final String MORE_ADS = "ads";
	private static final String MORE_COINS = "coins";
	private static final String MORE_RATING = "rating";
	private static final String MORE_NAME = "name";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * 
	 */
	public DataStorage() {
		super(Game.mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Boxes
	// ===========================================================

	/**
	 * @param db
	 */
	public void addBox(final SQLiteDatabase db, final boolean pIsOpen) {
		final ContentValues values = new ContentValues();

		values.put(BOX_STATE, pIsOpen);

		db.insert(BOX_TABLE, null, values);
	}

	/**
	 * @param id
	 * @return
	 */
	public Box getBox(final int id) {
		final Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + BOX_STATE + " FROM " + BOX_TABLE + " WHERE " + BOX_ID + " = " + id, null);
		cursor.moveToFirst();

		final Box box = new Box(cursor.getInt(0) > 0);

		cursor.close();

		return box;
	}

	/**
	 * @param id
	 * @param pOpen
	 * @return
	 */
	public int updateBox(final int id, final int pOpen) {
		final ContentValues values = new ContentValues();

		values.put(BOX_STATE, pOpen);

		return this.getReadableDatabase().update(BOX_TABLE, values, LEVEL_ID + " = ?", new String[] { String.valueOf(id) });
	}

	// ===========================================================
	// Levels
	// ===========================================================

	/**
	 * 
	 */
	public void addLevel(final SQLiteDatabase db) {
		final ContentValues values = new ContentValues();

		values.put(LEVEL_STATE, 0);
		values.put(LEVEL_STARS, 0);

		db.insert(LEVEL_TABLE, null, values);
	}

	/**
	 * @param id
	 * @param pOpen
	 * @param pStars
	 * @param pScore
	 * @return
	 */
	public int updateLevel(final int id, final int pOpen, int pStars, int pScore) {
		final Level existLevel = this.getLevel(id);

		pStars = pStars < existLevel.getStarsCount() ? existLevel.getStarsCount() : pStars;
		pScore = pScore < existLevel.getScoreCount() ? existLevel.getScoreCount() : pScore;

		final ContentValues values = new ContentValues();

		values.put(LEVEL_STATE, pOpen);
		values.put(LEVEL_STARS, pStars);
		values.put(LEVEL_SCORE, pScore);

		return this.getReadableDatabase().update(LEVEL_TABLE, values, LEVEL_ID + " = ?", new String[] { String.valueOf(id + 25 * Options.boxNumber) });
	}

	/**
	 * @param id
	 * @return
	 */
	public Level getLevel(int id) {
		final Cursor cursor = this.getReadableDatabase().query(LEVEL_TABLE, new String[] { LEVEL_STATE, LEVEL_STARS, LEVEL_SCORE }, LEVEL_ID + "=?", new String[] { String.valueOf(id + 25 * Options.boxNumber) }, null, null, null, null);
		cursor.moveToFirst();

		final Level level = new Level(id, cursor.getInt(0) > 0, cursor.getInt(1), cursor.getInt(2));

		cursor.close();

		return level;
	}

	// ===========================================================
	// Score
	// ===========================================================

	/**
	 * @return
	 */
	public int getTotalCore() {
		final Cursor cursor = this.getReadableDatabase().rawQuery("SELECT SUM(score) FROM levels", null);
		cursor.moveToFirst();

		final int value = cursor.getInt(0);

		cursor.close();

		return value;
	}

	/**
	 * @return
	 */
	public int getTotalStars() {
		final Cursor cursor = this.getReadableDatabase().rawQuery("SELECT SUM(stars) FROM " + LEVEL_TABLE, null);
		cursor.moveToFirst();

		final int value = cursor.getInt(0);

		cursor.close();

		return value;
	}

	/**
	 * @return
	 */
	public int getTotalCoins() {
		final Cursor cursor = this.getReadableDatabase().rawQuery("SELECT coins FROM " + MORE_TABLE, null);
		cursor.moveToFirst();

		final int value = cursor.getInt(0);

		cursor.close();

		return value;
	}

	/**
	 * @return
	 */
	public boolean isAdvertisimentDisabled() {
		final Cursor cursor = this.getReadableDatabase().query(MORE_TABLE, new String[] { MORE_ADS }, null, null, null, null, null, null);
		cursor.moveToFirst();

		final boolean result = cursor.getInt(0) > 0;

		cursor.close();

		return result;
	}

	/**
	 * @param pCoins
	 */
	public void addCoins(final int pCoins) {
		final ContentValues values = new ContentValues();

		values.put(MORE_COINS, this.getTotalCoins() + pCoins);

		this.getReadableDatabase().update(MORE_TABLE, values, null, null);

		((StoreScreen) Game.mScreens.get(Screen.STORE)).updateCoinsText();
	}

	/**
	 * @param pCoins
	 */
	public void removeCoins(final int pCoins) {
		final ContentValues values = new ContentValues();

		values.put(MORE_COINS, this.getTotalCoins() - pCoins);

		this.getReadableDatabase().update(MORE_TABLE, values, null, null);

		((StoreScreen) Game.mScreens.get(Screen.STORE)).updateCoinsText();
	}

	/**
	 * @return
	 */
	public boolean isRatingDisabled() {
		final Cursor cursor = this.getReadableDatabase().query(MORE_TABLE, new String[] { MORE_RATING }, null, null, null, null, null, null);
		cursor.moveToFirst();

		final boolean result = cursor.getInt(0) > 0;

		cursor.close();

		return result;
	}

	/**
	 * 
	 */
	public void disableRating() {
		final ContentValues values = new ContentValues();

		values.put(MORE_RATING, 1);

		this.getReadableDatabase().update(MORE_TABLE, values, null, null);
	}

	/**
	 * @param pName
	 */
	public void setRatingName(final String pName) {
		final ContentValues values = new ContentValues();

		values.put(MORE_NAME, pName);

		this.getReadableDatabase().update(MORE_TABLE, values, null, null);
	}

	/**
	 * 
	 */
	public String getRatingName() {
		final Cursor cursor = this.getReadableDatabase().query(MORE_TABLE, new String[] { MORE_NAME }, null, null, null, null, null, null);
		cursor.moveToFirst();

		final String result = cursor.getString(0);

		cursor.close();

		return result;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + LEVEL_TABLE + "(" + LEVEL_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + LEVEL_STARS + " INTEGER DEFAULT 0,  " + LEVEL_SCORE + " INTEGER DEFAULT 0,  " + LEVEL_STATE + " INTEGER DEFAULT 0" + ")");
		db.execSQL("CREATE TABLE " + BOX_TABLE + "(" + BOX_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," + BOX_STATE + " INTEGER DEFAULT 0" + ")");
		db.execSQL("CREATE TABLE " + MORE_TABLE + "(" + MORE_ADS + " INTEGER DEFAULT 0" + ", " + MORE_NAME + " STRING" + ", " + MORE_RATING + " INTEGER DEFAULT 0" + ", " + MORE_COINS + " INTEGER DEFAULT 0)");

		for (int i = 1; i <= 25 * 3; i++) {
			this.addLevel(db);
		}

		this.addBox(db, true);
		for (int i = 1; i <= 3; i++) {
			this.addBox(db, false);
		}

		final ContentValues values = new ContentValues();

		values.put(MORE_ADS, 0);
		values.put(MORE_COINS, 0);
		values.put(MORE_RATING, 0);
		values.put(MORE_NAME, "");

		db.insert(MORE_TABLE, null, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + LEVEL_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + BOX_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MORE_TABLE);

		this.onCreate(db);
	}
}
