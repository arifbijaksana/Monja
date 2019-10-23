package com.haerul.monja.ui.login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.haerul.monja.base.BaseViewModel;
import com.haerul.monja.data.api.ConnectionServer;
import com.haerul.monja.utils.Constants;
import com.haerul.monja.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseViewModel<LoginViewModel.LoginNavigator> {
    
    public LoginViewModel(Context context, ConnectionServer connectionServer) {
        super(context, connectionServer);
    }
        
    public void onLoginClick() {
        getNavigator().onLogin();
    }
    
    public void postLogin(JsonObject object, String email, String password) {
        setIsLoading(true);
        
        try {

            getConnectionServer().loginCall(object).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get(Constants.STATUS).getAsBoolean()) {
                            JsonObject data = response.body().get("data").getAsJsonObject();

                            Util.putPreference(getContext(), Constants.USER_SID, data.get(Constants.USER_SID).getAsString());
                            Util.putPreference(getContext(), Constants.USER_UID , data.get(Constants.USER_UID ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_NAME , data.get(Constants.USER_NAME ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_PHONE , data.get(Constants.USER_PHONE ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_EMAIL , email);
                            Util.putPreference(getContext(), Constants.USER_POSITION , data.get(Constants.USER_POSITION ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_REGION , data.get(Constants.USER_REGION ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_ADDRESS , data.get(Constants.USER_ADDRESS ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_TYPE , data.get(Constants.USER_TYPE ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_PASSWORD , password);
                            Util.putPreference(getContext(), Constants.IS_ACTIVE , data.get(Constants.IS_ACTIVE ).getAsString());
                            Util.putPreference(getContext(), Constants.USER_ROLE_SID , data.get(Constants.USER_ROLE_SID ).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_CREATED , data.get(Constants.DATE_CREATED ).getAsString());
                            Util.putPreference(getContext(), Constants.DATE_MODIFIED , data.get(Constants.DATE_MODIFIED ).getAsString());
                            Util.putPreference(getContext(), Constants.TOKEN_AUTH , data.get(Constants.TOKEN_AUTH ).getAsString());
                            
                            getNavigator().loginResult(true, response.body().get(Constants.MESSAGE).getAsString());
                        }
                        else {
                            getNavigator().loginResult(false, response.body().get(Constants.MESSAGE).getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    t.getLocalizedMessage();
                    setIsLoading(false);
                    getNavigator().loginResult(false, "Login Failed, Network Problem...");
                }
            });
        
        }
        catch (Exception e) {
            setIsLoading(false);
            getNavigator().loginResult(false, "Login Failed, Network Problem...");
            e.printStackTrace();
        }
    }

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer connectionServer;
        public ModelFactory(Context context, ConnectionServer connectionServer) {
            this.context = context;
            this.connectionServer = connectionServer;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LoginViewModel(context, connectionServer);
        }
    }
    
    public interface LoginNavigator {
        void onLogin();
        void loginResult(boolean status, String message);
    }
}
