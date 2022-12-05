package com.razzagalangadzan.smartaspirations.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.razzagalangadzan.smartaspirations.CharaAlert;
import com.razzagalangadzan.smartaspirations.ErrorAlert;
import com.razzagalangadzan.smartaspirations.Konfigurasi;
import com.razzagalangadzan.smartaspirations.LoadingAlert;
import com.razzagalangadzan.smartaspirations.R;
import com.razzagalangadzan.smartaspirations.RequestHandler;
import com.razzagalangadzan.smartaspirations.SuccessAlert;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AspirasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AspirasiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AspirasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AspirasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AspirasiFragment newInstance(String param1, String param2) {
        AspirasiFragment fragment = new AspirasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aspirasi,container,false);

        TextInputEditText getAspirasi = root.findViewById(R.id.add_aspirasi);
        MaterialButton buttonKirim = root.findViewById(R.id.buttonKirim);

        buttonKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAspirasi.length() < 20){
                    CharaAlert CharaProgress = new CharaAlert();
                    CharaProgress.show(getChildFragmentManager(), "test") ;
                } else {
                    final String aspirasi = getAspirasi.getText().toString().replaceAll("\n", "<br>");
                    class AddSuhu extends AsyncTask<Void, Void, String> {

                        LoadingAlert LoadingProgress = new LoadingAlert();
                        SuccessAlert SuccessProgress = new SuccessAlert();
                        ErrorAlert ErrorProgress = new ErrorAlert();

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            LoadingProgress.show(getChildFragmentManager(), "test");
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            LoadingProgress.dismiss();

                            if (s.equals("Berhasil Menambahkan Aspirasi")){
                                SuccessProgress.show(getChildFragmentManager(), "test");
                                getAspirasi.setText("");
                            } else if (s.length() < 20){
                                ErrorProgress.show(getChildFragmentManager(), "test");
                            }

                        }

                        @Override
                        protected String doInBackground(Void... v) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put(Konfigurasi.KEY_ASPIRASI, aspirasi);
                            RequestHandler rh = new RequestHandler();
                            String res = rh.sendPostRequest(Konfigurasi.URL_ADD, params);
                            return res;
                        }
                    }

                    AddSuhu ae = new AddSuhu();
                    ae.execute();
                }
            }
        });

        return root;
    }
}