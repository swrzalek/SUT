package pl.edu.ug.sut;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String KEY_ID = "id";
    private static final String COL_STATE = "state";
    private static final String COL_DATE = "date";
    private static final String COL_DURATION = "duration";
    private static final String TABLE_LAPS = "laps";



    private static final String CREATE_TABLE_LAPS = "CREATE TABLE "
            + TABLE_LAPS + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_STATE + " TEXT," + COL_DATE + " NUMERIC," + COL_DURATION + " REAL )";

    public DatabaseHelper(Context context) {
        super( context, "laps.db", null, 1 );
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( CREATE_TABLE_LAPS );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addPeriod(DataModel datamodel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("state", datamodel.getState());
        values.put("date",datamodel.getDate());
        values.put("duration", datamodel.getDuration());
        db.insertOrThrow(TABLE_LAPS,null, values);
    }

    public void detelePeriod(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguemnts={""+id};
        db.delete(TABLE_LAPS, "id=?",arguemnts );

    }
    public DataModel getPeriod(int id){
        DataModel dataModel = new DataModel();
            SQLiteDatabase db = getReadableDatabase();
            String[] columns={"id","state","date","duration"};
            String args[]={id+""};
            Cursor cursor = db.query(TABLE_LAPS, columns, "id=?", args,null,null,null,null);
            if (cursor!=null){
                cursor.moveToFirst();
                dataModel.setId( cursor.getLong( 0 ) );
                dataModel.setState( cursor.getString( 1 ) );
                dataModel.setDate( cursor.getString( 2 ) );
                dataModel.setDuration( cursor.getString( 3 ) );
            }
            return dataModel;

    }

    public List<DataModel> getAll(){
        List<DataModel> dataModels = new LinkedList<>(  );
        String[] columns={"id","state","date","duration"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query( TABLE_LAPS, columns,null,null,null,null,null );
        while(cursor.moveToNext()) {
            DataModel dataModel = new DataModel();
            dataModel.setId( cursor.getLong( 0 ) );
            dataModel.setState( cursor.getString( 1 ) );
            dataModel.setDate( cursor.getString( 2 ) );
            dataModel.setDuration( cursor.getString( 3 ) );
            dataModels.add( dataModel );
        }
         return dataModels;

        }

    public int dataCounter() {
        String query = "SELECT * FROM " + TABLE_LAPS + " WHERE " + COL_DATE + "> datetime('now','-1 hours')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(query, null);
        int count = (data.getCount() / 2);
        data.close();
        return count;
    }
    public double longestOnCounter() {
            String query = "SELECT " + "MAX(" + COL_DURATION + ") FROM " + TABLE_LAPS + " WHERE " + COL_STATE + " = 'ON'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(query ,null );
        data.moveToFirst();
        double count = data.getInt( 0 );
        data.close();
        return count;

    }
    public double longestOffCounter() {
        String query = "SELECT " + "MAX(" + COL_DURATION + ") FROM " + TABLE_LAPS + " WHERE " + COL_STATE + " = 'OFF'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(query ,null );
        data.moveToFirst();
        double count = data.getInt( 0 );
        data.close();
        return count;
    }
    public double sumOn() {
        String query = "SELECT * FROM " + TABLE_LAPS + " WHERE " + COL_STATE + " = OFF";
        String query1=   "(SELECT SUM(duration) from laps) total WHERE state = 'OFF'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery( "SELECT SUM(duration) from laps total WHERE state = 'ON'",null );
        data.moveToFirst();
        double sum = data.getDouble( 0 );
        data.close();
        return sum;

    }
    public double sumOff() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery( "SELECT SUM(duration) from laps total WHERE state = 'OFF'",null );
        data.moveToFirst();
        double sum = data.getDouble( 0 );
        data.close();
        return sum;

    }
}
