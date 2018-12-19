package org.toktakprogramming.cuetconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText id_t, email_t, password_t, district_t;
    Button submit;
    Spinner bloodSpin;
    CheckBox checkBox;

    AlertDialog.Builder builder;

    int id;
    String blood;
    String email, password, district;
    SharedPreferences login;
    ArrayAdapter<CharSequence> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initialize the Variables with layout

        builder = new AlertDialog.Builder(Register.this);
        id_t = (EditText) findViewById(R.id.Cuet_id);
        email_t = (EditText) findViewById(R.id.Email_id);
        password_t = (EditText) findViewById(R.id.Passwrod_editText);
        district_t = (EditText) findViewById(R.id.district_id);
        submit = (Button) findViewById(R.id.submit);
        bloodSpin = (Spinner) findViewById(R.id.bloodSpinnerid);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.bloodGroup, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpin.setAdapter(arrayAdapter);
        bloodSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blood = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                blood = "Not Interested";
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_t.getText().toString().equals("") || email_t.getText().toString().equals("") || password_t.getText().toString().equals("") || district_t.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Information!!", Toast.LENGTH_SHORT).show();

                } else {
                    id = Integer.parseInt(id_t.getText().toString());
                    email = email_t.getText().toString();
                    password = password_t.getText().toString();
                    district = district_t.getText().toString();
                    login = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = login.edit();
                    editor.putBoolean("loged", true);
                    editor.commit();


                    if (!checkBox.isChecked()) {
                        Toast.makeText(Register.this, "tick the checkbox", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pushData();
                    }




                }
            }
        });

    }

    //pushing Data to the database;

    void pushData() {
        String url="http://rakib.ourcuet.com/reg.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                builder.setTitle("Registration info:");
                builder.setMessage(response);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> regPack = new HashMap<String, String>();
                regPack.put("id", Integer.toString(id));
                regPack.put("email", email);
                regPack.put("password", password);
                regPack.put("district", district);
                regPack.put("blood", blood);

                return regPack;
            }

        };
        MySingleton.getmInstance(Register.this).AddToRequestQueue(stringRequest);

    }
}