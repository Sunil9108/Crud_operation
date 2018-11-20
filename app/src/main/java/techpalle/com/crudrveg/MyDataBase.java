package techpalle.com.crudrveg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase {

    MyHelper mh;
    SQLiteDatabase sdb;

    public MyDataBase(Context c){
        mh = new MyHelper(c, "techpalle.db", null, 1);
    }

    public void open(){
        sdb = mh.getWritableDatabase();
    }

    public void close() {
        if (sdb != null) {
            sdb.close();
        }
    }

    public void insertEmp(String ename, int esal){
        ContentValues cv = new ContentValues();
        cv.put("ename", ename);
        cv.put("esal", esal);
        sdb.insert("emp", null, cv);
    }

    public Cursor getEmp(){
        Cursor c = sdb.query("Emp", null,null,null,null,null,null);
        return c;
    }

    public void updateEmp(int eno, String ename, int esal){
        ContentValues cv = new ContentValues();
        cv.put("ename", ename);
        cv.put("esal", esal);
        sdb.update("emp", cv, "_id = ?", new String[]{""+eno});
    }

    public void deleteEmp(int eno){
        sdb.delete("emp", "_id = ?", new String[]{""+eno});
    }

    public class MyHelper extends SQLiteOpenHelper {


        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table emp(_id integer primary key, ename text, esal integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
