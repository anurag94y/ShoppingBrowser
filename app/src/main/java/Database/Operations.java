package Database;

/**
 * Created by anurag.yadav on 9/15/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Operations extends SQLiteOpenHelper implements BaseColumns {
    public static final String PAGE_URL = "Page_URL";
    public static final String PAGE_TITLE = "Page_Title";
    public static final String PAGE_IMAGE = "Page_Image";
    public static final String DATABASE_NAME = "USER_INFO";
    public static final String HISTORY_TABLE_NAME = "history";
    public static final String BOOKMARK_TABLE_NAME = "bookmark";
    public static final int Database_Version = 1;

    private static final String HISTORY_CREATE_QUERY = "CREATE TABLE " + HISTORY_TABLE_NAME + "(" + PAGE_URL + " TEXT," + PAGE_TITLE + " TEXT," +  PAGE_IMAGE + " BLOB);";
    private static final String BOOKMARK_CREATE_QUERY = "CREATE TABLE " + BOOKMARK_TABLE_NAME + "(" + PAGE_URL + " TEXT," + PAGE_TITLE + " TEXT," +  PAGE_IMAGE + " BLOB);";



    public Operations(Context context) {
        super(context, DATABASE_NAME, null, Database_Version);
        Log.d("History Operations", "Database Created");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HISTORY_CREATE_QUERY);
        sqLiteDatabase.execSQL(BOOKMARK_CREATE_QUERY);
        Log.d("History Operations", "Table Created");
        Log.d("Bookmark Operations", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertHistory(Operations dop, String url, String title, Bitmap image) {
        byte[] data = getBitmapAsByteArray(image);
        url = resize(url);
        title = resize(title);
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        System.out.println("Bitmap Image " + image + " data " + data);
        ContentValues cv = new ContentValues();
        cv.put(PAGE_URL , url);
        cv.put(PAGE_TITLE, title);
        cv.put(PAGE_IMAGE, data);
        try {
            sqLiteDatabase.insert(HISTORY_TABLE_NAME, null, cv);
            Log.d("History", "One row inserted in History");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor getHistory(Operations historyOperations) {
        SQLiteDatabase database = historyOperations.getReadableDatabase();
        String[] columns = {PAGE_URL, PAGE_TITLE, PAGE_IMAGE};
        Cursor result = database.query(HISTORY_TABLE_NAME, columns, null, null, null, null, null);
        return result;

    }

    public void deleteHistory(Operations historyOperations) {
        SQLiteDatabase database = historyOperations.getReadableDatabase();
        database.delete(HISTORY_TABLE_NAME, null, null);
    }

    public void insertBookmark(Operations dop, String url, String title, Bitmap image) {
        byte[] data = getBitmapAsByteArray(image);
        url = resize(url);
        title = resize(title);
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        System.out.println("Bitmap Image " + image + " data " + data);
        ContentValues cv = new ContentValues();
        cv.put(PAGE_URL , url);
        cv.put(PAGE_TITLE, title);
        cv.put(PAGE_IMAGE, data);
        try {
            sqLiteDatabase.insert(BOOKMARK_TABLE_NAME, null, cv);
            Log.d("bookmark Operations", "One row inserted in BookMark : " + BOOKMARK_TABLE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String resize(String text) {
        if(text != null && text.length() > 38) {
            text = text.substring(0, 38);
        }
        return text;
    }

    public Cursor getBookmark(Operations bookmarkOperations) {
        SQLiteDatabase database = bookmarkOperations.getReadableDatabase();
        String[] columns = {PAGE_URL, PAGE_TITLE, PAGE_IMAGE};
        Cursor result = database.query(BOOKMARK_TABLE_NAME, columns, null, null, null, null, null);
        return result;

    }

    public void deleteBookmark(Operations bookmarkOperations) {
        SQLiteDatabase database = bookmarkOperations.getReadableDatabase();
        database.delete(BOOKMARK_TABLE_NAME, null, null);
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream);
            return outputStream.toByteArray();
        }
        else
            return new byte[0];
    }


}
