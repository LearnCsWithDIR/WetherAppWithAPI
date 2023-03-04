package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.NetworkUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Context context;
    TextView SearchInput,output;
    String TempValue = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchInput = findViewById(R.id.search);
        output = findViewById(R.id.temp);

    }
    public void search(View view){

        String cityName = SearchInput.getText().toString();

        if (cityName.isEmpty()){
            output.setText("Not Found");
        }
        else {

            getData(cityName);
        }

    }
    public void getData(String cityName) {
        Uri uri = Uri.parse("https://api.tomorrow.io/v4/weather/realtime?location="+cityName+"&apikey=DB6YxZcw7tOnmKduEyEkZcFDFkVV0uZm")
                .buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
            new CallApi().execute(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }
    class CallApi extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
                return data;

            } catch (IOException e) {
               e.printStackTrace();
               return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            parseJson(s);
        }
        public void parseJson(String data) {
            String result;

            JSONObject cityo = null;
            try {
                cityo = new JSONObject(data);
                result = cityo.getJSONObject("data")
                        .getJSONObject("values")
                        .getString("temperature");
                output.setText(result);

            } catch (JSONException e) {
               result = "Not Found";
            }

        }
    }




}