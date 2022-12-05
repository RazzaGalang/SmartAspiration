package com.razzagalangadzan.smartaspirations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.razzagalangadzan.smartaspirations.R;
import com.razzagalangadzan.smartaspirations.databinding.ActivityMainBinding;
import com.razzagalangadzan.smartaspirations.fragment.AkunFragment;
import com.razzagalangadzan.smartaspirations.fragment.AspirasiFragment;
import com.razzagalangadzan.smartaspirations.fragment.HomeFragment;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        AddSuhu ae = new AddSuhu();
        ae.execute();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.Home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.Aspirasi:
                    replaceFragment(new AspirasiFragment());
                    break;

                case R.id.Akun:
                    replaceFragment(new AkunFragment());
                    break;

            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();

    }

    class AddSuhu extends AsyncTask<Void, Void, String> {

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");

        LoadingAlert dialogProgres = new LoadingAlert();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgres.show(getSupportFragmentManager(), "test");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogProgres.dismiss();

            Toast.makeText(MainActivity.this, "nama" + s, Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("nama", s);
            AkunFragment myFrag = new AkunFragment();
            myFrag.setArguments(bundle);

        }

        @Override
        protected String doInBackground(Void... v) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Konfigurasi.KEY_NIS, username);
            params.put(Konfigurasi.KEY_PASS, password);

            RequestHandler rh = new RequestHandler();
            String res = rh.sendPostRequest(Konfigurasi.URL_NAMA, params);
            return res;

        }
    }
}