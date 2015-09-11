package com.example.groupic.groupicbetter;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.example.groupic.groupicbetter.resources.Event;
import com.example.groupic.groupicbetter.resources.Photo;
import com.example.groupic.groupicbetter.resources.ServerHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if (resultCode == RESULT_OK) {
                Log.i("App", "worked");

                String contents = intent.getStringExtra("SCAN_RESULT");

                Log.d("Barcode", contents);
                // Handle successful scan
                String event_id = String.valueOf(Event.join(this, contents, "asd"));
                if (-1 != Integer.parseInt(event_id)) {
                    Intent i = new Intent(this, Barcode.class);
                    i.putExtra("event_id", event_id);
                    startActivity(i);
                }
                else
                {
                    CharSequence text = "Event not found!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Log.i("App", "Scan unsuccessful");
            }
        }
    }

    public void openBarcode(View view)
    {
        (new IntentIntegrator(this)).initiateScan();
    }

    public void openPublicEvents(View view)
    {
        Intent intent = new Intent(this, PublicEvents.class);
        startActivity(intent);
    }
}
