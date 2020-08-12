package com.practice.andr_networking_asm.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.practice.andr_networking_asm.DAO.DAO;
import com.practice.andr_networking_asm.MainActivity;
import com.practice.andr_networking_asm.R;
import com.practice.andr_networking_asm.controller.IntentController;
import com.practice.andr_networking_asm.controller.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView txtForgotPass;
    private Button btnLogin;
    private TextView txtReg;
    private EditText edtUsername, edtPass;
    private String TAG = "Login Activity";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        txtForgotPass = findViewById(R.id.txtForgot_password);
        edtUsername = findViewById(R.id.username);
        edtPass = findViewById(R.id.password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLogin();
            }
        });

        txtReg = findViewById(R.id.txt_reg);
        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentController.directinal(LoginActivity.this, ForgotPasswordActivity.class);
            }
        });

        checkLogin();
    }

    private void handlerLogin(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                DAO.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handlerAfterLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Volley Error: " + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String,String>();
                param.put("username",edtUsername.getText().toString());
                param.put("password",edtPass.getText().toString());
                param.put("tag","login");
                return param;
            }
        };
        Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
    }

    private void handlerAfterLogin(String res){
        String mess = "";
        String name = "";
        String email = "";
        String image = "";
        String username = "";

        try {
            JSONObject jsonObject = new JSONObject(res);
            mess = jsonObject.getString("thanhcong");

            if(Integer.parseInt(mess) == 1) {
                JSONObject user = jsonObject.getJSONObject("user_account");

                username = user.getString("username");
                name = user.getString("fullname");
                email = user.getString("email");
                image = user.getString("image");
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                sessionManager.setLogin(username, email, name, image, true);
                IntentController.directinal(LoginActivity.this, MainActivity.class);
                finish();
            }else {
                mess = jsonObject.getString("thongbaoloi");
                Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkLogin(){
        if(!sessionManager.check()){
            return;
        }else {
            IntentController.directinal(LoginActivity.this, MainActivity.class);
            finish();
        }
    }

}