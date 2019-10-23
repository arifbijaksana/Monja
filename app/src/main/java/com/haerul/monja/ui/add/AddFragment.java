package com.haerul.monja.ui.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.haerul.monja.R;
import com.haerul.monja.base.BaseFragment;
import com.haerul.monja.data.api.ConnectionServer;
import com.haerul.monja.data.db.repository.MasterRepository;
import com.haerul.monja.data.entity.Base64Data;
import com.haerul.monja.data.entity.Inspeksi;
import com.haerul.monja.databinding.FragmentAddBinding;
import com.haerul.monja.ui.MainActivity;
import com.haerul.monja.ui.map.MapActivity;
import com.haerul.monja.utils.CameraXActivity;
import com.haerul.monja.utils.Constants;
import com.haerul.monja.utils.Util;

import org.json.JSONObject;

import java.io.File;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.haerul.monja.base.BaseActivity.TAG;

public class AddFragment extends BaseFragment<FragmentAddBinding, AddViewModel> implements AddViewModel.Navigator {

    private static final int REQUEST_CODE_LOC = 0x291;
    private static final int REQUEST_CAMERA = 0x218;
    @Inject
    ConnectionServer connectionServer;
    @Inject
    MasterRepository repository;
    
    private FragmentAddBinding binding;
    private AddViewModel viewModel;
    private String fileUri = null;
    private String imageBase64 = null;
    
    @Override
    public int getBindingVariable() {
        return com.haerul.monja.BR._all;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add;
    }

    @Override
    public AddViewModel getViewModel() {
        return viewModel;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new AddViewModel.ModelFactory(getBaseActivity(), connectionServer, repository)).get(AddViewModel.class);
        viewModel.setNavigator(this);
        setFragmentTitle("MONJA - ADD");
        
        binding.tingkatEmergency.setupRadioGroup(repository, Constants.KONDISI);
        binding.pemadaman.setupRadioGroup(repository, Constants.PEMADAMAN);
        binding.lokasiInspeksi.setOnClickListener(view1 -> pickLocation());
        binding.setLokasi.setOnClickListener(view1 -> pickLocation());
        binding.photoText.setOnClickListener(view12 -> takePhoto());
        binding.photo.setOnClickListener(view12 -> takePhoto());
        binding.photoPreview.setOnClickListener(view12 -> takePhoto());
        binding.rayon.setupSpinnerGeneric(repository, Constants.ULP);
        binding.jenisTemuan.setupSpinnerGeneric(repository, Constants.JENIS_TEMUAN);
        
        binding.cancel.setOnClickListener(v->{
            loadFragment(new AddFragment(), getTag());
            MainActivity.setNavigationSelectedItemId(MainActivity.ADD_ID);
        });
        
        binding.save.setOnClickListener(v -> {
            hideKeyboard();
            Inspeksi inspeksi = new Inspeksi();
            inspeksi.inspeksi_sid = Util.createSID();
            inspeksi.rayon_sid = binding.rayon.getSpinnerValueSID(repository, Constants.ULP);
            inspeksi.jenis_temuan_sid = binding.jenisTemuan.getSpinnerValueSID(repository, Constants.JENIS_TEMUAN);
            inspeksi.tingkat_emergency_sid = binding.tingkatEmergency.getRadioGroupValueSID(repository, Constants.KONDISI);
            inspeksi.pemadaman_sid = binding.pemadaman.getRadioGroupValueSID(repository, Constants.PEMADAMAN);
            inspeksi.lokasi_inspeksi_x = viewModel.lon;
            inspeksi.lokasi_inspeksi_y = viewModel.lat;
            inspeksi.tanggal_inspeksi = binding.tanggalInspeksi.getText().toString();
            Base64Data dataBase64 = Util.insertBase64(repository, imageBase64, Util.getUserSID(getBaseActivity()));
            inspeksi.foto_inspeksi = dataBase64.data_sid;
            inspeksi.post_by = Util.getUserSID(getBaseActivity());
            viewModel.postInspeksi(inspeksi);
            viewModel.postBase64Data(dataBase64);
        });
    }

    private void pickLocation() {
        Log.d("TAG", "addLocation");
        try {
            JSONObject LonLat = new JSONObject();

            LonLat.put("LONGITUDE", Double.parseDouble(viewModel.lon.equals("") ? "0" : viewModel.lon));
            LonLat.put("LATITUDE", Double.parseDouble(viewModel.lat.equals("") ? "0" : viewModel.lat));

            Log.w("TAG", LonLat.toString());

            Intent intent = new Intent(getBaseActivity(), MapActivity.class);
            intent.putExtra("JSON", String.valueOf(LonLat));
            Log.w("TAG pick", String.valueOf(LonLat));
            startActivityForResult(intent, REQUEST_CODE_LOC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOC) {
            if (resultCode == RESULT_OK) {
                try {
                    Log.wtf("LOCATION", "EDIT");
                    JSONObject jsons = new JSONObject(data.getStringExtra("JSON"));
                    viewModel.lon = String.valueOf(jsons.getDouble("LONGITUDE"));
                    viewModel.lat = String.valueOf(jsons.getDouble("LATITUDE"));
                    binding.lokasiInspeksi.setText(viewModel.lon + ", " + viewModel.lat);
                    Log.w("TAG 2", String.valueOf(jsons));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                try {
                    Log.w("Test", data.toString());
                    fileUri = Uri.fromFile(new File(data.getStringExtra("data"))).toString();
                    imageBase64 = viewModel.previewCapturedImage(binding.photoPreview, fileUri);
                    Log.i(TAG, "onActivityResult:" + imageBase64);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    void takePhoto() {
        Intent intent = new Intent(getBaseActivity(), CameraXActivity.class);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void showProgress() {
        getBaseActivity().showProgress();
    }

    @Override
    public void hideProgress() {
        getBaseActivity().hideProgress();
    }

    @Override
    public void result(boolean status, String message) {
        hideKeyboard();
        if (status) {
            loadFragment(new AddFragment(), getTag());
            MainActivity.setNavigationSelectedItemId(MainActivity.ADD_ID);
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .addToBackStack(tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            Log.w("TAG", tag);
        }
    }
}
