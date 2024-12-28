package nure.dubina.stanislav.lab.lab.task5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMPORTANCE = "importance";
    public static final String COLUMN_DATE_TIME = "dateTime";
    public static final String COLUMN_IMAGE_URI = "imageUri";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_DATE_TIME + " TEXT, " +
                    COLUMN_IMPORTANCE + " INTEGER, " +
                    COLUMN_IMAGE_URI + " TEXT);";

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public int addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_DATE_TIME, note.getDateTime());
        values.put(COLUMN_IMPORTANCE, note.getImportance());
        values.put(COLUMN_IMAGE_URI, note.getImageUri() != null ? note.getImageUri().toString() : null);

        int id = (int) db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_DATE_TIME, note.getDateTime());
        values.put(COLUMN_IMPORTANCE, note.getImportance());
        values.put(COLUMN_IMAGE_URI, (note.getImageUri() != null) ? note.getImageUri().toString() : null);

        int rows = db.update(TABLE_NOTES, values, COLUMN_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
        Log.d("Database", "Rows updated: " + rows);
        return rows;
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NOTES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        Log.d("Database", "Rows deleted: " + rowsDeleted);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, COLUMN_DATE_TIME + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String imageUriString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));
                Uri imageUri = (imageUriString != null && !imageUriString.isEmpty()) ? Uri.parse(imageUriString) : null;
                Log.d("Database", "Loaded Image URI: " + imageUriString);

                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMPORTANCE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME)),
                        imageUri
                );
                notes.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return notes;
    }
}