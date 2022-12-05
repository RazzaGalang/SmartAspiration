package com.razzagalangadzan.smartaspirations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.razzagalangadzan.smartaspirations.R;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    String username, password;
    int intId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText user = findViewById(R.id.nis);
        TextInputEditText pass = findViewById(R.id.password);

        MaterialButton buttonMasuk = findViewById(R.id.buttonMasuk);
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username    = String.valueOf(user.getText());
                    intId       = Integer.parseInt(username);}
                catch(NumberFormatException a) {
                    intId = 0;}
                try {
                    password    = String.valueOf(pass.getText());}
                catch(NumberFormatException a) {
                    password = null;}

                if(intId==0){
                    user.setError("Harap Masukkan NIS");
                }
                else if(password.equals("")){
                    pass.setError("Harap Masukkan Password");
                }
                else {
                    class Login extends AsyncTask<Void, Void, String> {
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

                            if (s.equals("Login Berhasil")){
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                HashMap<String, String> params = new HashMap<>();
                                intent.putExtra("nama", s);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                            else if (s.equals("Username atau Password Salah")){
                                LoginFailedAlert FailedProgress = new LoginFailedAlert();
                                FailedProgress.show(getSupportFragmentManager(), "test");
                            }
                            else {
                                NoConnectionAlert NoConnectionProgress = new NoConnectionAlert();
                                NoConnectionProgress.show(getSupportFragmentManager(), "test");
                            }
                        }

                        @Override
                        protected String doInBackground(Void... voids) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put(Konfigurasi.KEY_NIS, username);
                            params.put(Konfigurasi.KEY_PASS, password);

                            RequestHandler rh = new RequestHandler();
                            String res = rh.sendPostRequest(Konfigurasi.URL_LOGIN, params);
                            return res;
                        }
                    }

                    Login log = new Login();
                    log.execute();
                }
            }
        });
    }
}