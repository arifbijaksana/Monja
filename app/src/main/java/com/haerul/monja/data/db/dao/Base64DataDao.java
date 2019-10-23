package com.haerul.monja.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.haerul.monja.data.entity.Base64Data;

@Dao
public interface Base64DataDao {
    
    @Insert
    void insertBase64Data(Base64Data data);
    
    @Query("update base64data set post_status =:status where data_sid =:sid")
    void updateStatus(String sid, boolean status);
}
