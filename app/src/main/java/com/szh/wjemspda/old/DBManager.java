//package cn.wjems.szh.wjemspda;
//
///**
// * Created by Administrator on 2017/10/23.
// */
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
////参考：http://blog.csdn.net/liuhe688/article/details/6715983
//public class DBManager
//{
//    private DatabaseHelper helper;
//    private SQLiteDatabase db;
//
//    public DBManager(Context context)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> Constructor");
//        helper = new DatabaseHelper(context);
//        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
//        // mFactory);
//        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
//        db = helper.getWritableDatabase();
//    }
//
//    /**
//     * add persons
//     *
//     * @param persons
//     */
//    public void add(List<Maildb> persons)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> add");
//        // 采用事务处理，确保数据完整性
//        db.beginTransaction(); // 开始事务
//        try
//        {
//            for (Maildb person : persons)
//            {
//                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
//                        + " VALUES(null, ?, ?, ?)", new Object[] { person.mailno,
//                        person.chepai, person.pici,person.saomiaoshijian });
//                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
//                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
//                // 使用占位符有效区分了这种情况
//            }
//            db.setTransactionSuccessful(); // 设置事务成功完成
//        }
//        finally
//        {
//            db.endTransaction(); // 结束事务
//        }
//    }
//
//    public void add(Maildb person)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> add");
//        // 采用事务处理，确保数据完整性
//        db.beginTransaction(); // 开始事务
//        try
//        {
//
//                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
//                        + " VALUES(null, ?, ?, ?,?)", new Object[] { person.mailno,
//                        person.chepai, person.pici,person.saomiaoshijian });
//                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
//                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
//                // 使用占位符有效区分了这种情况
//
//            db.setTransactionSuccessful(); // 设置事务成功完成
//        }
//        finally
//        {
//            db.endTransaction(); // 结束事务
//        }
//    }
//    /**
//     * update person's age
//     *
//     * @param person
//     */
//
//
//    /**
//     * delete old person
//     *
//     * @param person
//     */
//    public void deletePerson(Maildb person)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> deleteOldPerson");
//        db.delete(DatabaseHelper.TABLE_NAME, "mailno = ? and chepai = ? and pici = ?",
//                new String[] { String.valueOf(person.mailno),String.valueOf(person.chepai),String.valueOf(person.pici) });
//    }
//
//    /**
//     * query all persons, return list
//     *
//     * @return List<Person>
//     */
//    public ArrayList<Maildb> query()
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> query");
//        ArrayList<Maildb> persons = new ArrayList<Maildb>();
//        Cursor c = queryTheCursor();
//        while (c.moveToNext())
//        {
//            Maildb person = new Maildb();
//            person._id = c.getInt(c.getColumnIndex("_id"));
//            person.mailno = c.getString(c.getColumnIndex("mailno"));
//            person.chepai = c.getString(c.getColumnIndex("chepai"));
//            person.pici  = c.getInt(c.getColumnIndex("pici"));
//            person.saomiaoshijian = c.getInt(c.getColumnIndex("saomiaoshijian"));
//            persons.add(person);
//        }
//        c.close();
//        return persons;
//    }
//
//    public ArrayList<Integer> querybychepai(String chepai){
//        ArrayList<Integer> persons = new ArrayList<Integer>();
//        Cursor c = queryTheCursorbychepaigroup(chepai);
//        while (c.moveToNext())
//        {
//            persons.add(c.getInt(c.getColumnIndex("pici")));
//        }
//        c.close();
//        return persons;
//    }
//
//
//    public Cursor queryTheCursorbychepaigroup(String chepai){
//        Cursor c = db.rawQuery("SELECT pici FROM " + DatabaseHelper.TABLE_NAME +" where chepai = '"+chepai+"' group by pici order by pici",
//                null);
//        return c;
//    }
//
//    public ArrayList<Maildb> querybychepaipici(String chepai,int pici)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> query");
//        ArrayList<Maildb> persons = new ArrayList<Maildb>();
//        Cursor c = queryTheCursor();
//        while (c.moveToNext())
//        {
//            Maildb person = new Maildb();
//            person._id = c.getInt(c.getColumnIndex("_id"));
//            person.mailno = c.getString(c.getColumnIndex("mailno"));
//            person.chepai = c.getString(c.getColumnIndex("chepai"));
//            person.pici  = c.getInt(c.getColumnIndex("pici"));
//            person.saomiaoshijian = c.getLong(c.getColumnIndex("saomiaoshijian"));
//            persons.add(person);
//        }
//        c.close();
//        return persons;
//    }
//
//    public Cursor queryTheCursorbychepaipici(String chepai,int pici)
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> queryTheCursor");
//        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME+" where chepai = '"+chepai+"' and pici = '"+pici+"'",
//                null);
//        return c;
//    }
//    /**
//     * query all persons, return cursor
//     *
//     * @return Cursor
//     */
//    public Cursor queryTheCursor()
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> queryTheCursor");
//        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
//                null);
//        return c;
//    }
//
//    /**
//     * close database
//     */
//    public void closeDB()
//    {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> closeDB");
//        // 释放数据库资源
//        db.close();
//    }
//
//}