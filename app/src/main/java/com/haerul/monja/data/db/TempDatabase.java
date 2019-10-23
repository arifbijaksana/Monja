package com.haerul.monja.data.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.haerul.monja.data.db.dao.UserDao;
import com.haerul.monja.data.entity.User;
import com.haerul.monja.utils.Constants;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class TempDatabase extends RoomDatabase {

    abstract UserDao userDao();
    
    private static TempDatabase INSTANCE;
    public  static TempDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TempDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, TempDatabase.class, Constants.TEMP_DB)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
