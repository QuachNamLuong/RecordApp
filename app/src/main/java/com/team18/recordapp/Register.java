package com.team18.recordapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.team18.recordapp.database.database_login;
import com.team18.recordapp.models.Account;

public class Register extends AppCompatActivity {

    EditText edtDKTaiKhoan, edtDKMatKhau, edtEmail, edtOTP, edtPhone;
    Button btnDKDangKy, btnDKDangNhap, btnDKOTP;
    database_login database_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database_login = new database_login(this);
        AnhXa();
        // button đăng ký
        btnDKDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taikhoan = edtDKTaiKhoan.getText().toString();
                String matkhau = edtDKMatKhau.getText().toString();
                String email = edtEmail.getText().toString();
                String Phone = edtPhone.getText().toString();
                Account taiKhoan1 = CreateTaiKhoan();
                if (taikhoan.equals("") || matkhau.equals("") || email.equals("")) {
                    Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
                else if (!isValidEmail(email)) {
                    Toast.makeText(Register.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
                }
                else if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(Register.this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_LONG).show();
                }
                // nếu đầy đủ thông tin nhập vào thì add tài khoản vào database
                else {
                    database_login.AddTaiKhoan(taiKhoan1);
                    Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            // Phương thức kiểm tra email có hợp lệ
            private boolean isValidEmail(String email) {
                // Biểu thức chính quy để kiểm tra định dạng email
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                return email.matches(emailPattern);
            }
        });

        // trả về trang đăng nhập
        btnDKDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Phương thức tạo tài khoản
    private Account CreateTaiKhoan () {
        String taikhoan = edtDKTaiKhoan.getText().toString();
        String matkhau = edtDKMatKhau.getText().toString();
        String email = edtEmail.getText().toString();
        Account tk = new Account(taikhoan, matkhau, email);
        return  tk;
    }
    private void AnhXa() {
        edtEmail = findViewById(R.id.email);
        edtDKTaiKhoan = findViewById(R.id.dktaikhoan);
        edtDKMatKhau = findViewById(R.id.dkmatkhau);
        edtPhone = findViewById(R.id.phone);
        edtOTP = findViewById(R.id.otp);
        btnDKDangKy = findViewById(R.id.dkdky);
        btnDKDangNhap = findViewById(R.id.dkdangnhap);
        btnDKOTP = findViewById(R.id.sendOTP);
    }
}