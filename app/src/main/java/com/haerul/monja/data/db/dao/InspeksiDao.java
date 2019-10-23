package com.haerul.monja.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.haerul.monja.data.entity.Inspeksi;

import java.util.List;

@Dao
public interface InspeksiDao {
    
    @Insert
    void insertInspeksi(Inspeksi inspeksi);
    
    @Query("select * from inspeksi")
    LiveData<List<Inspeksi>> getInspeksi();
}
