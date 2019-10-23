package com.haerul.monja.data.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.haerul.monja.data.db.dao.Base64DataDao;
import com.haerul.monja.data.db.dao.GenericCategoryDao;
import com.haerul.monja.data.db.dao.GenericReferencesDao;
import com.haerul.monja.data.db.dao.InspeksiDao;
import com.haerul.monja.data.db.dao.UserDao;
import com.haerul.monja.data.entity.Base64Data;
import com.haerul.monja.data.entity.GenericCategory;
import com.haerul.monja.data.entity.GenericReferences;
import com.haerul.monja.data.entity.Inspeksi;
import com.haerul.monja.data.entity.User;
import com.haerul.monja.utils.Constants;

@Database(entities = {
        User.class, 
        Inspeksi.class,
        GenericCategory.class,
        GenericReferences.class,
        Base64Data.class
}, version = 1, exportSchema = false)
public abstract class MasterDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract InspeksiDao inspeksiDao(); 
    public abstract GenericCategoryDao genericCategoryDao();
    public abstract GenericReferencesDao genericReferencesDao();
    public abstract Base64DataDao base64DataDao();
    
    private static MasterDatabase INSTANCE;
    public  static MasterDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (MasterDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MasterDatabase.class, Constants.MASTER_DB)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
