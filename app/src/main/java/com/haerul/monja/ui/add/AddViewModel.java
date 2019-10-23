package com.haerul.monja.ui.add;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haerul.monja.base.BaseViewModel;
import com.haerul.monja.data.api.ConnectionServer;
import com.haerul.monja.data.db.repository.MasterRepository;
import com.haerul.monja.data.entity.Base64Data;
import com.haerul.monja.data.entity.Inspeksi;
import com.haerul.monja.utils.Constants;
import com.haerul.monja.utils.Util;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddViewModel extends BaseViewModel<AddViewModel.Navigator> {
    
    public String lon = "";
    public String lat = "";
    
    public AddViewModel(Context context, ConnectionServer connectionServer, MasterRepository repository) {
        super(context, connectionServer, repository);
    }

    public String previewCapturedImage(ImageView imageView, String uri) {
        String image = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            final Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(uri).getPath(), options);
            image = encodeImage(bitmap);
            imageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return image;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    
    public void postInspeksi(Inspeksi inspeksi) {
        getNavigator().showProgress();
        JsonObject object = new JsonParser().parse(new Gson().toJson(inspeksi)).getAsJsonObject();
        getConnectionServer().postInspeksi(Util.getToken(getContext()), object)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().get(Constants.STATUS).getAsBoolean()) {
                                getRepository().insertInspeksi(inspeksi);
                                getNavigator().result(true, response.body().get(Constants.MESSAGE).getAsString());
                            }
                            else {
                                getNavigator().result(false, response.body().get(Constants.MESSAGE).getAsString());
                            }
                        }
                        getNavigator().hideProgress();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        getNavigator().hideProgress();
                        getNavigator().result(false, t.getMessage());
                    }
                });
    }
    
    public void postBase64Data(Base64Data data) {
        JsonObject object = new JsonParser().parse(new Gson().toJson(data)).getAsJsonObject();
        getConnectionServer().postBase64Data(Util.getToken(getContext()), object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(Constants.STATUS).getAsBoolean()) {
                        getRepository().updateStatus(data.data_sid, true);
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) { }
        });
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
            return (T) new AddViewModel(context, server, repository);
        }
    }
    
    public interface Navigator {
        void showProgress();
        void hideProgress();
        void result(boolean status, String message);
    }
}
