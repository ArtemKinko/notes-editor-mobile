package com.artemkinko.lab4_8;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RequestTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            URL weatherEndpoint = new URL(strings[0]);
            HttpsURLConnection connection = (HttpsURLConnection) weatherEndpoint.openConnection();
            InputStream responseBody = connection.getInputStream();
            Scanner s = new Scanner(responseBody).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
