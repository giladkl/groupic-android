package com.example.groupic.groupicbetter.resources;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

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

            JSONArray respone = server.get(context, baseUrl + "photos", params);

            Log.d("Fuck java", respone.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
