package duc.example.com.phonebookfinal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import duc.example.com.phonebookfinal.model.UserModel;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_GROUPS = "groups";
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String GROUP = "mgroup";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " integer primary key autoincrement, "
            + NAME + " text not null, "
            + NUMBER + " integer );";

    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE " + TABLE_GROUPS + "("
            + KEY_ID + " integer primary key autoincrement, "
            + GROUP + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_GROUPS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USERS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_GROUPS + "'");

        onCreate(db);
    }
    public void addUser(String name,String number, String group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER,number);
        long id = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);


        ContentValues valuesGroup = new ContentValues();
        valuesGroup.put(KEY_ID, id);
        valuesGroup.put(GROUP, group);
        db.insert(TABLE_GROUPS, null, valuesGroup);

    }
    public ArrayList<UserModel> getAllUsers() {
        ArrayList<UserModel> userModelArrayList = new ArrayList<UserModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                userModel.setName(c.getString(c.getColumnIndex(NAME)));
                userModel.setNumber(c.getString(c.getColumnIndex(NUMBER)));

                String selectGroupQuery = "SELECT  * FROM " + TABLE_GROUPS +" WHERE "+KEY_ID+" = "+ userModel.getId();


                Cursor cGroup = db.rawQuery(selectGroupQuery, null);

                if (cGroup.moveToFirst()) {
                    do {
                        userModel.setGroup(cGroup.getString(cGroup.getColumnIndex(GROUP)));
                    } while (cGroup.moveToNext());
                }




                userModelArrayList.add(userModel);
            } while (c.moveToNext());
        }
        return userModelArrayList;



}
    public void updateUser(int id, String name, String number, String group) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER,number);
        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        ContentValues valuesGroup = new ContentValues();
        valuesGroup.put(GROUP, group);
        db.update(TABLE_GROUPS, valuesGroup, KEY_ID + " = ?", new String[]{String.valueOf(id)});


    }
    public void deleteUSer(int id) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_USERS, KEY_ID + " = ?",new String[]{String.valueOf(id)});

        db.delete(TABLE_GROUPS, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }


}