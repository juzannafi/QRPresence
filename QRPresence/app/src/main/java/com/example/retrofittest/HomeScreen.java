package com.example.retrofittest;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.retrofittest.API.GetService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.Date;
import java.text.SimpleDateFormat;

public class HomeScreen extends AppCompatActivity {


    private Button buttonScan, buttonGetUser, historyButton;
    TextView textViewUsername, textViewFullname, textViewNIM;

    ImageView ivBgContent;
    CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;


    Context mContext;
    GetService mApiService;
    ProgressDialog loading;
    private Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());

        final Context mContext = this;

        // initialize object
        buttonScan = (Button) findViewById(R.id.buttonScan);
        historyButton = (Button) findViewById(R.id.HistoryButton);
        final TextView textViewUsername = findViewById(R.id.UsernameTextView);
        final TextView textViewFullname = findViewById(R.id.FullnameTextView);
        final TextView textViewNIM = findViewById(R.id.NIMTextView);
        final TextView textViewClassID = findViewById(R.id.ClassIDTextView);
        textViewFullname.bringToFront();
        textViewNIM.bringToFront();
        textViewUsername.bringToFront();
        textViewClassID.bringToFront();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final String result_username = extras.getString("result_username");
        final String result_fullname = extras.getString("result_fullname");
        final String result_nim = extras.getString("result_nim");
        textViewUsername.setText(result_username);
        textViewFullname.setText(result_fullname);
        textViewNIM.setText(result_nim);
        final String URLKelas = "http://qrpresence.herokuapp.com/api/class/presensi";


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewClassID.setText(result.getText());
                        String URL = URLKelas.toString();
                        String Fullname = result_fullname.toString();
                        String NIM = result_nim.toString();
                        String CLASS = result.getText().toString();

                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl("http://qrpresence.herokuapp.com/api/class/")
                                .addConverterFactory(GsonConverterFactory.create());

                        Retrofit retrofit = builder.build();

                        String DynamicURL = "http://qrpresence.herokuapp.com/api/class/presensi";

                        GetService sendPresensi = retrofit.create(GetService.class);
                        Call<ResponseBody> call = sendPresensi.postPresensi(DynamicURL, Fullname, NIM, CLASS);

                        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){

                                    try {
                                        loading.dismiss();
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        String status = jsonRESULTS.getJSONObject("data").getString("classid");
                                        Toast.makeText(mContext, "ID Kelas :" + status + "\n Presensi berhasil", Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    loading.dismiss();
                                    Toast.makeText(mContext, "GAGAL Presensi", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                loading.dismiss();
                                Toast.makeText(mContext, "Connection Error", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });
            }
        });


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCodeScanner.startPreview();

            }

        });

        historyButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {

                Intent intent = new Intent(mContext, History.class);
                startActivity(intent);

            }

        });




    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        showDialog();

    }



    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        HomeScreen.this.finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }



}










