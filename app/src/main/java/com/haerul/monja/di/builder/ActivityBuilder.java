package com.haerul.monja.di.builder;

import com.haerul.monja.ui.MainActivity;
import com.haerul.monja.ui.add.AddFragment;
import com.haerul.monja.ui.login.LoginActivity;
import com.haerul.monja.ui.map.MapActivity;
import com.haerul.monja.utils.CameraActivity;
import com.haerul.monja.utils.CameraXActivity;
import com.haerul.monja.utils.SQLiteDownloaderActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    
    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();
    
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
    
    @ContributesAndroidInjector
    abstract SQLiteDownloaderActivity sqLiteDownloaderActivity();
    
    @ContributesAndroidInjector
    abstract AddFragment addFragment();
    
    @ContributesAndroidInjector
    abstract MapActivity mapActivity();
    
    @ContributesAndroidInjector
    abstract CameraActivity cameraActivity();
    
    @ContributesAndroidInjector
    abstract CameraXActivity cameraXActivity();
}
