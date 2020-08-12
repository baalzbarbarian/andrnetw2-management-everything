package com.practice.andr_networking_asm.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.practice.andr_networking_asm.model.mUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "Reg Activity";
    private Button btnReg, btnLogin;
    private EditText edtUsername, edtName, edtEmail, edtPass;
    mUser m;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);

        initViews();

    }

    private void initViews() {
        btnReg = findViewById(R.id.reg_btnReg);
        btnLogin = findViewById(R.id.reg_btnBack);
        edtUsername = findViewById(R.id.reg_edtUsername);
        edtName = findViewById(R.id.reg_edtName);
        edtEmail = findViewById(R.id.reg_edtMail);
        edtPass = findViewById(R.id.reg_edtPass);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m = new mUser();
                m.setName(edtName.getText().toString());
                m.setUsername(edtUsername.getText().toString());
                m.setMail(edtEmail.getText().toString());
                m.setPass(edtPass.getText().toString());

                validateData(m);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void validateData(mUser m) {
        int isValid = m.validationData();
        if (isValid == 0){
            Toasty.error(this, "Không được để trống!", Toast.LENGTH_SHORT).show();
        }else if(isValid == -1){
            Toasty.error(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        }else if(isValid == -2){
            Toasty.error(this, "Tài khoản và mật khẩu phải hơn 6 ký tự!", Toast.LENGTH_LONG).show();
        }else {
            handlerRegister();
        }
    }

    private void handlerRegister() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DAO.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handlerAfterRegister(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegisterActivity.this, "Volley Error!", Toast.LENGTH_SHORT).show();
                Log.d("Volley Error",error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String,String>();

                param.put("username", m.getUsername());
                param.put("fullname",m.getName());
                param.put("email", m.getMail());
                param.put("password", m.getPass());
                param.put("tag","register");

                return param;
            }
        };
        Volley.newRequestQueue(RegisterActivity.this).add(stringRequest);
    }

    private void handlerAfterRegister(String res) {
        String alert = "";
        try {
            JSONObject jsonObject = new JSONObject(res);
            alert = jsonObject.getString("thanhcong");
            if (Integer.parseInt(alert) == 1) {
                Toasty.success(this, "Đăng ký thành công!", Toast.LENGTH_SHORT, true).show();
                sessionManager.setLogin(m.getUsername(), m.getMail(), m.getName(), "", true);
                IntentController.directinal(RegisterActivity.this, MainActivity.class);
                finish();
            } else {
                Toast.makeText(this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}