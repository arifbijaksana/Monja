package com.haerul.monja.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Base64Data {
    @PrimaryKey @NonNull
    public String data_sid;
    public String data;
    public String date_created;
    public String date_modified;
    public String post_by;
    public boolean post_status;
}
