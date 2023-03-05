package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.NetworkErrorException;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;

public class MainActivity extends AppCompatActivity {

    Context context;
    TextView SearchInput,output,Erro;
    String TempValue = null;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchInput = findViewById(R.id.search);
        output = findViewById(R.id.temp);
        Erro = findViewById(R.id.erro);

    }
    public void search(View view) throws IOException {

        String cityName = SearchInput.getText().toString();

        if (cityName.isEmpty()){
            Erro.setText("Please Enter Location");
        }
        else {

            getData(cityName);
        }

    }
    public void getData(String cityName) throws IOException  {
        Uri uri = Uri.parse("https://api.tomorrow.io/v4/weather/realtime?location="+cityName+"&apikey=DB6YxZcw7tOnmKduEyEkZcFDFkVV0uZm")
                .buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
            new CallApi().execute(url);

        } catch (MalformedURLException e) {
            System.out.println("error"+ e);
        }


    }
    class CallApi extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
                return data;

            } catch (IOException e) {
                System.out.println("error"+ e);

                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void parseJson(String data)throws IOException  {
            String result,symbolWith;

            JSONObject cityo = null;

            try {
                cityo = new JSONObject(data);

                result = cityo.getJSONObject("data")
                        .getJSONObject("values")
                        .getString("temperature");
                symbolWith = result + " \u2103";
                output.setText(symbolWith);

            }
            catch (JSONException e) {
               System.out.println("error"+ e);
             //  output.setText("Error");


            }
            if (data.equals("Error")){
                Erro.setText("Not Found");
            }

        }
    }




}