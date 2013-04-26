package com.sqlhelper;

import java.util.ArrayList;
import com.entities.Event;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper {

	//private static final String LOGCAT = null;

	public DBController(Context applicationcontext) {
		super(applicationcontext, "androidsqlite.db", null, 1);
		// Log.d(LOGCAT, "Created");
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE Event (Id INTEGER PRIMARY KEY, Name TEXT, Date TEXT)";
		database.execSQL(query);
		// Log.d(LOGCAT, "Event table Created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS Event";
		database.execSQL(query);
		onCreate(database);
	}

	public long insertEvent(Event event) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Name", event.getName());
		values.put("Date", event.getDate());
		long rowId = database.insert("Event", null, values);
		database.close();
		return rowId;
	}

	public Event getEventInfo(int id) {
		Event event = new Event();
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM Event where Id='" + id + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				int eventId = cursor.getInt(0);
				String name = cursor.getString(1);
				String date = cursor.getString(2);
				event.setId(eventId);
				event.setName(name);
				event.setDate(date);
			} while (cursor.moveToNext());
		}
		return event;
	}

	public int updateEvent(Event event) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Name", event.getName());
		values.put("Date", event.getDate());

		String where = "Id=?";
		String[] whereArgs = new String[] { String.valueOf(event.getId()) };

		return database.update("Event", values, where, whereArgs);
	}

	public void deleteEvent(int id) {
		// Log.d(LOGCAT, "delete");
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM Event where Id='" + id + "'";
		// Log.d("query", deleteQuery);
		database.execSQL(deleteQuery);
	}

	public void deleteAllRows() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete("Event", null, null);
	}

	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> eventList;
		eventList = new ArrayList<Event>();
		String selectQuery = "SELECT  * FROM Event";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Event event = new Event();

				int eventId = cursor.getInt(0);
				String name = cursor.getString(1);
				String date = cursor.getString(2);
				event.setId(eventId);
				event.setName(name);
				event.setDate(date);

				eventList.add(event);
			} while (cursor.moveToNext());
		}
		return eventList;
	}

}
