package com.android.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteUtil {
    final private Context context;

    public SqliteUtil(Context context) {
        this.context = context;
    }

    /**
     * 返回SQLiteDatabase实例
     * 
     * @param dbName
     * @return
     */
    private SQLiteDatabase getSqLiteDatabase(String dbName) {
        return context.openOrCreateDatabase(dbName, Context.MODE_APPEND, null);
    }

    /**
     * 返回指定数据库中的指定内容
     * 
     * @param dbName
     * @param sql
     * @param strings
     * @return
     */
    public Cursor getAllCursor(String dbName, String sql, String[] strings) {
        SQLiteDatabase db = getSqLiteDatabase(dbName);
        return db.rawQuery(sql, strings);
    }

    /**
     * 删除指定数据库下的指定表中的所有行
     * 
     * @param dbName
     * @param table
     */
    public void deleteAllRow(String dbName, String table) {
        SQLiteDatabase db = getSqLiteDatabase(dbName);
        db.delete(table, null, null);
    }
}