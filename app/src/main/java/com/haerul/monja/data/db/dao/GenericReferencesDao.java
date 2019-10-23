package com.haerul.monja.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.haerul.monja.data.entity.GenericReferences;

import java.util.List;

@Dao
public interface GenericReferencesDao {
    
    @Query("select * from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_category.category_name=:name order by ref_value asc")
    List<GenericReferences> getGenericRefByCategory(String name);

    @Query("select * from generic_references " +
            "left join generic_category on generic_references.category_sid = generic_category.category_sid " +
            "where generic_references.ref_sid=:sid")
    GenericReferences getRefBySID(String sid);
    
    @Query("select * from generic_references where ref_sid=:sid")
    GenericReferences getGenericRefBySID(String sid);
    
}
