package com.practice.andr_networking_asm.ui.user;

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
import com.practice.andr_networking_asm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button btnReg, btnLogin;
    private EditText edtUsername, edtName, edtEmail, edtPass1, edtPass2;
    String registerUrl = "http://192.168.1.7/nguyenduchai_pd03241/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

    }

    private void initViews() {
        btnReg = findViewById(R.id.btn_reg);
        btnLogin = findViewById(R.id.btn_reg);
        edtUsername = findViewById(R.id.username);
        edtName = findViewById(R.id.fullname);
        edtEmail = findViewById(R.id.email);
        edtPass1 = findViewById(R.id.password1);
        edtPass2 = findViewById(R.id.password2);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPass1.getText().toString().equals(edtPass2.getText().toString())){
                    handlerRegister();
                }else {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không giống nhau!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handlerRegister() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handlerAfterRegister(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Volley error", Toast.LENGTH_SHORT).show();
                Log.d("Volley Error",error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String,String>();

                param.put("username",edtUsername.getText().toString());
                param.put("fullname",edtName.getText().toString());
                param.put("email",edtEmail.getText().toString());
                param.put("password",edtPass1.getText().toString());
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
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                alert = jsonObject.getString("thongbaoloi");
                Toast.makeText(this, alert, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}