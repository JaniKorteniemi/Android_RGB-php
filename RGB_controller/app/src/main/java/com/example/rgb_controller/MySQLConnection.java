package com.example.rgb_controller;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class MySQLConnection extends AsyncTask<Integer, Void, String> {
    final String serverName ="-amazonaws.com";
    final String username = "-";
    final String password = "-";
    final String DB = "ledDB";
    final String strUrl = serverName +"/jotain.php";

    AlertDialog alertDialog;

    String records="", errors="";

    Context context;

    MySQLConnection(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(Integer... voids) {

        try {
            int R = voids[0];
            int G = voids[1];
            int B = voids[2];
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_Data = URLEncoder.encode("R","UTF-8")+"="+URLEncoder.encode(String.valueOf(R),"UTF-8")+"&"
                    +URLEncoder.encode("G","UTF-8")+"="+URLEncoder.encode(String.valueOf(G),"UTF-8")+"&"
                    +URLEncoder.encode("B","UTF-8")+"="+URLEncoder.encode(String.valueOf(B),"UTF-8");
            bufferedWriter.write(post_Data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
        e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("RGB Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

