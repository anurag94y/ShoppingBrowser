package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anurag.yadav on 8/1/16.
 */
public class DatabaseOperations extends SQLiteOpenHelper {


    public String CREATE_QUERY = "CREATE TABLE" + Bookmarks.TABLE_NAME + "(" + Bookmarks.PAGE_URL + " TEXT," + Bookmarks.PAGE_TIME + " TEXT);";

    public DatabaseOperations(Context context) {
        super(context, Bookmarks.DATABASE_NAME, null, Bookmarks.Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertInfo(DatabaseOperations dop, String url, String time) {
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Bookmarks.PAGE_URL , url);
        cv.put(Bookmarks.PAGE_TIME, time);
        sqLiteDatabase.insert(Bookmarks.TABLE_NAME, null, cv);
    }
}
