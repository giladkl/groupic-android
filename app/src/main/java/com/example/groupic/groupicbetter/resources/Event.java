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
public class Event extends Resource{
    public static int join(Context context, String barcode, String uniqId)
    {
        try {
            HashMap params = new HashMap<String, String>();
            params.put("barcode", barcode);
            params.put("uid", uniqId);

            JSONArray respone = server.post(context, baseUrl + "events/join", params);
            Log.d("App", respone.get(1).toString());
            if (false == (Boolean) respone.get(1))
            {
                return Integer.parseInt(respone.getJSONObject(0).optString("event_id"));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
