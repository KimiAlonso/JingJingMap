package com.zbd.jingjingmap.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amarok on 2016/12/29.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite-test.db";
    private Map<String, Dao> daos = new HashMap<String, Dao>();


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource,CustomLocation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

        try {

            TableUtils.dropTable(connectionSource,CustomLocation.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context)
    {
        context = context.getApplicationContext();
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }


    public synchronized Dao getDao(Class clazz) throws SQLException
    {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();

        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
