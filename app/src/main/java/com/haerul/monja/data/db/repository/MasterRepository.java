package com.haerul.monja.data.db.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.haerul.monja.data.db.MasterDatabase;
import com.haerul.monja.data.entity.Base64Data;
import com.haerul.monja.data.entity.GenericReferences;
import com.haerul.monja.data.entity.Inspeksi;
import com.haerul.monja.data.entity.User;

import java.util.List;

public class MasterRepository {
    private final MasterDatabase database;

    public MasterRepository(MasterDatabase database) {
        this.database = database;
    }

    public MasterRepository(Context context) {
        this.database =  MasterDatabase.getDatabase(context);
    }

    public static MasterRepository getInstance(MasterDatabase database) {
        return new MasterRepository(database);
    }
    
    //reff generic
    public List<GenericReferences> getRefByCategory(String category) {
        return database.genericReferencesDao().getGenericRefByCategory(category);
    }
    public GenericReferences getRefBySID(String sid) {
        return database.genericReferencesDao().getRefBySID(sid);
    }
    
    public User getUserBySID(String sid) {
        return database.userDao().getUserBySID(sid);
    }
    
    
    // inspeksi
    public void insertInspeksi(Inspeksi inspeksi) {
        database.inspeksiDao().insertInspeksi(inspeksi);
    }
    
    public LiveData<List<Inspeksi>> getInspeksi() {
        return database.inspeksiDao().getInspeksi();
    }
    
    // base64data
    public void insertBase64Data(Base64Data data) {
        database.base64DataDao().insertBase64Data(data);
    }
    
    public void updateStatus(String sid, boolean status) {
        database.base64DataDao().updateStatus(sid, status);
    }
}
