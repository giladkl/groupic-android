package com.example.groupic.groupicbetter.resources;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by giladkl on 29/08/15.
 */
public class ServerHandler {
    public JSONArray get(Context context, String url, HashMap<String, String> params) throws ExecutionException, InterruptedException {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            DownloadWebpageTask task = new DownloadWebpageTask(params, "GET");
            task.execute(url);
            try {
                return new JSONArray(task.get().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // display error
        }
        return null;
    }

    public JSONArray post(Context context, String url, HashMap<String, String> params) throws ExecutionException, InterruptedException {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            DownloadWebpageTask task = new DownloadWebpageTask(params, "POST");
            task.execute(url);
            try {
                return new JSONArray(task.get().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // display error
        }
        return null;
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> params;
        String httpType;

        public DownloadWebpageTask(HashMap<String, String> params, String httpType)
        {
            this.params = params;
            this.httpType = httpType;
        }
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0], this.httpType, this.params);
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // On post do
        }
    }

    private String downloadUrl(String myurl, String httpType, HashMap<String, String> params) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            if (httpType.equals("GET")) {
                myurl += "?" + getParamsUrlString(params);
            }
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(httpType);
            if (httpType.equals("POST"))
            {
                conn.setDoInput(true);
                conn.getOutputStream().write(getParamsUrlString(params).getBytes());
            }
            else {
                if (httpType.equals("GET")) {
                    conn.setDoInput(true);
                }
            }
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("App", "The response is: " + response);
            // Convert the InputStream into a string
            String contentAsString = getResponse(conn);

            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        InputStream inStream = connection.getInputStream();
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader reader = new BufferedReader(inStreamReader);

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            response.append(currentLine);
        }
        reader.close();

        return response.toString();
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private String getParamsUrlString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        if (params != null) {
            for (String keySet : params.keySet())
            {
                if (!first)
                    result.append("&");
                else
                    first = false;

                result.append(URLEncoder.encode(keySet, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(params.get(keySet), "UTF-8"));

            }
        }
        return result.toString();
    }
}
