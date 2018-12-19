package j.edu.wasp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

//public abstract class DataBaseHelper extends SQLiteOpenHelper {
//
//    private static final String TAG ="DataBaseHelper";
//    private static final String TABLE_NAME = "family";
//    private static final String COL1 = "ID";
//    private static final String COL2 = "name";
//
//    public DataBaseHelper(@Nullable Context context) {
//        super(context, TABLE_NAME, 1);
//    }
////
////    public DataBaseHelper(@Nullable Context context) {
////        super(context, TABLE_NAME, 1);
////    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " +  TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                COL2 + " TEXT)";
//        db.execSQL(createTable);
//    }
//
////    @Override
////    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
////        onCreate(db);
////    }
//
//    public boolean addData(String item) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL2, item);
//
//        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
//        long result = db.insert(TABLE_NAME, null, contentValues);
//
//        if(result == -1) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public Cursor getItemID(String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME +
//                " WHERE " + COL2 + " = " + name + "";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }
//}
