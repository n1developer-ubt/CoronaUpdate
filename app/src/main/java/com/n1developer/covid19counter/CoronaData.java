package com.n1developer.covid19counter;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class CoronaData {
    public static String getText(String urlx) throws Exception {
        String fullString = "";
        URL url = new URL(urlx);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            fullString += line;
        }
        reader.close();

        return fullString;
    }

    private static boolean Loaded = false;

    public static AllData getNewData(Context c)
    {
        AllData newData = null;
        try{
            String newText = getText("http://pixtopost.com:9070/api/v1/newdata");
            TemporaryData.writeToFile(c,"data.txt",newText);
            newData = new AllData();
            newData.loadFromJson(new JSONObject(newText));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return newData;
    }
}