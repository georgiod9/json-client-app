package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    Button submitRequest;
    Button get;
    TextView displayText;
    TextView dataField;
    TextView responseField;
    String url;
    String url_home;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitRequest = findViewById(R.id.button_submit);
        submitRequest.setOnClickListener(submitListener);

        get = findViewById(R.id.button_get);
        get.setOnClickListener(getListener);

        displayText = findViewById(R.id.textView1);
        dataField = findViewById(R.id.editTextName);
        responseField = findViewById(R.id.textViewResponse);

        queue = Volley.newRequestQueue(this);
        url = "http://192.168.8.190:3000/postdata";
        url_home = "http://192.168.8.190:3000";

        queue.start();
    }

    //Submit request button listener method
    private final View.OnClickListener submitListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            HashMap<String, String> params = new HashMap<String,String>();
            params.put("data", dataField.getText().toString()); // the entered data as the body.

            JsonObjectRequest jsObjRequest =
                    new JsonObjectRequest(Request.Method.POST,
                    url,
                    new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                displayText.setText(response.getString("message"));
                                responseField.setText(response.getString("status"));
                                Log.i("Main", "Server status: " + response.getString("status"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            displayText.setText("That didn't work!");
                        }
                    });

            queue.add(jsObjRequest);
        }
    };

    private final View.OnClickListener getListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_home, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONArray jsonArray = response;
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String data = jsonObject.getString("data");
                            Log.i("Main", "Server response: " + data);
                        }
                    } catch (Exception w) {
                        Toast.makeText(MainActivity.this, w.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            queue.add(jsonArrayRequest);
        }
    };
}