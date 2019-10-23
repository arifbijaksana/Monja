package com.haerul.monja.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.haerul.monja.base.BaseViewModel;
import com.haerul.monja.data.api.ConnectionServer;
import com.haerul.monja.data.db.repository.MasterRepository;
import com.haerul.monja.data.entity.User;

public class MainViewModel extends BaseViewModel {
    
    public MainViewModel(Context context, ConnectionServer connectionServer, MasterRepository repository) {
        super(context, connectionServer, repository);
    }
    
    public User getUser(String sid) {
        return getRepository().getUserBySID(sid);
    }
    
    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer server;
        private MasterRepository repository;
        public ModelFactory(Context context, ConnectionServer server, MasterRepository repository) {
            this.context = context;
            this.server = server;
            this.repository = repository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainViewModel(context, server, repository);
        }
    }
}
