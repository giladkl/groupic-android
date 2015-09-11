package com.example.groupic.groupicbetter.resources;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by giladkl on 29/08/15.
 */
public class Photo extends Resource{
    public static JSONArray getPhotos(Context context, int eventId)
    {
        try {
            HashMap params = new HashMap<String, String>();
            params.put("event_id", "" + eventId);
            JSONArray response = server.get(context, baseUrl + "photos", params);
            try {
                if (false == (Boolean) response.get(1))
                {
                    return (JSONArray) response.get(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
