package com.haerul.monja.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.haerul.monja.R;
import com.haerul.monja.base.BaseActivity;
import com.haerul.monja.data.api.ConnectionServer;
import com.haerul.monja.data.db.repository.MasterRepository;
import com.haerul.monja.data.entity.User;
import com.haerul.monja.databinding.ActivityMainBinding;
import com.haerul.monja.ui.add.AddFragment;
import com.haerul.monja.ui.login.LoginActivity;
import com.haerul.monja.utils.Constants;
import com.haerul.monja.utils.Util;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> 
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject
    MasterRepository repository;
    @Inject
    ConnectionServer server;
    MainViewModel viewModel;
    
    public static final int LIST_ID = 0x101;
    public static final int ADD_ID = 0x102;
    public static final int SYNC_ID = 0x103;
    public static final int PROFILE_ID = 0x104;
    private static ActivityMainBinding binding;
    private User user = new User();
    
    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new MainViewModel.ModelFactory(this, server, repository)).get(MainViewModel.class);
        
        checkLogin();
        
        requestPermission();
        setSupportActionBar(binding.toolbar);
        setupBottomNavMenu();
        
        binding.bnMain.setOnNavigationItemSelectedListener(this);
        loadFragment(new AddFragment(), "ADD INSPEKSI");
    }

    private void setupBottomNavMenu() {
        Menu menu = binding.bnMain.getMenu();
        menu.add(Menu.NONE, LIST_ID, Menu.NONE, "INSPEKSI").setIcon(R.drawable.ic_list);
        menu.add(Menu.NONE, ADD_ID, Menu.NONE, "ADD").setIcon(R.drawable.ic_add_circle);
        menu.add(Menu.NONE, SYNC_ID, Menu.NONE, "SYNC").setIcon(R.drawable.ic_sync);
        menu.add(Menu.NONE, PROFILE_ID, Menu.NONE, "PROFILE").setIcon(R.drawable.ic_profile);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case LIST_ID:
                fragment = new AddFragment();
                break;
            case ADD_ID:
                fragment = new AddFragment();
                break;
            case SYNC_ID:
                fragment = new AddFragment();
                break;
            case PROFILE_ID:
                fragment = new AddFragment();
                break;
        }
        setTag(menuItem.getTitle().toString());
        return loadFragment(fragment, getTag());
    }

    private void logout() {
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm_logout, null);
        Button delete = view.findViewById(R.id.delete_dialog);
        Button close = view.findViewById(R.id.close_dialog);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.DialogStyle);

        delete.setOnClickListener(v -> {
            showProgress();
            new Handler().postDelayed(() -> {
                hideProgress();
                Util.putPreference(this, Constants.IS_LOGIN, false);
                LoginActivity.navigateToLogin(this);
                finish();
                System.exit(0);
                Snackbar.make(binding.getRoot(), "Logging out!", Snackbar.LENGTH_SHORT).show();
            }, 200);

            dialog.dismiss();
        });

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void requestPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            TedPermission.with(MainActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA)
                    .check();
        }
    };

    public static void navigateToMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public boolean loadFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            Log.w("TAG", tag);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        }
        return true;
    }
    
    public static void setNavigationSelectedItemId(int id) {
        binding.bnMain.setSelectedItemId(id);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
