package com.haerul.monja.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
   @PrimaryKey @NonNull
   public String user_sid;
   public String user_uid;
   public String user_name;
   public String user_phone;
   public String user_email;
   public String user_position;
   public String user_region;
   public String user_address;
   public int user_type;
   public String user_password;
   public int is_active;
   public String user_role_sid;
   public String date_created;
   public String date_modified;
}
