package com.example.retrofittest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofittest.API.GetService;
import com.example.retrofittest.API.UtilsAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {



    Context mContext;
    GetService mApiService;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mApiService = UtilsAPI.getAPIService();

        Button registerButton = findViewById(R.id.registerButton);
        final EditText username = findViewById(R.id.usernameText);
        final EditText password = findViewById(R.id.passwordText);
        final EditText fullname = findViewById(R.id.fullname);
        final EditText nim = findViewById(R.id.nimText);

        final String sUser = username.getText().toString();
        final String sPass = password.getText().toString();
        final String sFullname = fullname.getText().toString();
        final String sNim = nim.getText().toString();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                mApiService.registerRequest(username.getText().toString(), password.getText().toString(), fullname.getText().toString(), nim.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    Log.i("debug", "onResponse: BERHASIL");
                                    loading.dismiss();
                                    Toast.makeText(mContext, "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
                                    SQLiteDatabase HistoryDB = openOrCreateDatabase(username.getText().toString(),MODE_PRIVATE,null);
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("debug", "onResponse: GA BERHASIL");
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();

    }
}

