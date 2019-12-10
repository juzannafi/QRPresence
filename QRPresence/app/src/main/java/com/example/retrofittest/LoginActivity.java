package com.example.retrofittest;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    Context mContext;
    GetService mApiService;
    ProgressDialog loading;
    //SharedPreferences mPreferences;
    //SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button Login = findViewById(R.id.buttonLogin);
        TextView Register = findViewById(R.id.RegisterText);
        final TextView Username = findViewById(R.id.usernameLogin);
        final TextView Password = findViewById(R.id.passwordLogin);

        mContext = this;
        mApiService = UtilsAPI.getAPIService();
        //mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mEditor = mPreferences.edit();






        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Register = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(Register);

            }



        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                mApiService.loginRequest(Username.getText().toString(), Password.getText().toString())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){

                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        loading.dismiss();
                                        String nama = jsonRESULTS.getJSONObject("data").getString("username");
                                        String namaLengkap = jsonRESULTS.getJSONObject("data").getString("fullname");
                                        String NIM = jsonRESULTS.getJSONObject("data").getString("nim");
                                        Intent intent = new Intent(mContext, HomeScreen.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("result_username",nama);
                                        extras.putString("result_fullname",namaLengkap);
                                        extras.putString("result_nim",NIM);
                                        intent.putExtras(extras);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    loading.dismiss();
                                    Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                loading.dismiss();
                            }
                        });
            }



        });



    }



    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    }







