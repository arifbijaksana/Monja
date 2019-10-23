package com.haerul.monja.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Inspeksi {
    @PrimaryKey
    @NonNull
    public String inspeksi_sid;
    public String inspeksi_uid;
    public String rayon_sid;
    public String penyulang_sid;
    public String jenis_temuan_sid;
    public String tingkat_emergency_sid;
    public String pemadaman_sid;
    public String jenis_wo_sid;
    public String status_tl_sid;
    public String lokasi_inspeksi_y;
    public String lokasi_inspeksi_x;
    public String tanggal_inspeksi;
    public String tanggal_tl;
    public String keterangan;
    public String foto_inspeksi;
    public String foto_tl;
    public String post_by;
    public boolean post_status;
}
