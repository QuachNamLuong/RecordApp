package com.team18.recordapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
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

public class Login extends AppCompatActivity {

    // biến ẩn hiện mật khẩu
    boolean hidePassword;

    // tạo biến cho màn đăng nhập
    EditText edtTaiKhoan, edtMatKhau;
    Button btnDangNhap, btnDangKy;
    // tạo đối tượng cho db
    database_login database_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnhXa();
        // đối tượng database_login
        database_login = new database_login(this);

        // tạo sự kiện khi click button Đăng Nhập sẽ chuyển sang page Đăng ký với intent
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gán cho các biến được nhập từ EditText
                String tentaikhoan = edtTaiKhoan.getText().toString();
                String matkhau = edtMatKhau.getText().toString();
                // sử dụng trỏ để lấy dữ liệu, gọi getData() để lấy tất cả tài khoản ở database
                Cursor cursor = database_login.getData();
                // Biến để theo dõi trạng thái đăng nhập
                boolean isLogged = false;
                // thực hiện vòng lặp để lấy dữ liệu từ cursor với moveToNext() di chuyển tiếp
                while (cursor.moveToNext()) {
                    // lấy dữ liệu và gán vào biến, dữ liệu tài khoản ô 1, mật khẩu ô 2 và email ô 3, ô 0 là id
                    String datatentaikhoan = cursor.getString(1);
                    String datamatkhau = cursor.getString(2);
                    //nếu tài khoản đăng nhập khớp với database
                    if(datatentaikhoan.equals(tentaikhoan) && datamatkhau.equals(matkhau)) {
                        int idd = cursor.getInt(0);
                        String email = cursor.getString(3);
                        String tentk = cursor.getString(1);
                        // chuyển qua màn hình MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        // gửi dữ liệu qua MainActivity
                        intent.putExtra("id", idd);
                        intent.putExtra("email", email);
                        intent.putExtra("tentaikhoan", tentk);
                        startActivity(intent);
                        isLogged = true;
                    }
                }
                if (!isLogged) {
                    // Kiểm tra mật khẩu và tài khoản rỗng
                    if (tentaikhoan.equals("") && matkhau.equals("")) {
                        Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    }
                    // Kiểm tra tên tài khoản rỗng
                    else if (tentaikhoan.equals("")) {
                        Toast.makeText(Login.this, "Tên tài khoản không được bỏ trống", Toast.LENGTH_LONG).show();
                    }
                    // Kiểm tra mật khẩu rỗng
                    else if (matkhau.equals("")) {
                        Toast.makeText(Login.this, "Mật khẩu không được bỏ trống", Toast.LENGTH_LONG).show();
                    }
                    // Thông báo lỗi nếu tài khoản hoặc mật khẩu không khớp
                    else {
                        Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu sai. Vui lòng nhập lại", Toast.LENGTH_LONG).show();
                    }
                }
                // thực hiện trả cursor về đầu
                cursor.moveToFirst();
                // đóng khi không dùng
                cursor.close();
            }
        });

        // ẩn hiển mật khẩu
        edtMatKhau.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= edtMatKhau.getRight() - edtMatKhau.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = edtMatKhau.getSelectionEnd();
                        if(hidePassword) {
                            // chuyển icon
                            edtMatKhau.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.password_off, 0);
                            // ẩn mật khẩu
                            edtMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            hidePassword = false;
                        }
                        else {
                            // chuyển icon
                            edtMatKhau.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.password_on, 0);
                            // hiện mật khẩu
                            edtMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            hidePassword = true;
                        }
                        edtMatKhau.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void AnhXa() {
        edtTaiKhoan = findViewById(R.id.taikhoan);
        edtMatKhau = findViewById(R.id.matkhau);
        btnDangNhap = findViewById(R.id.dangnhap);
        btnDangKy = findViewById(R.id.dangky);
    }
}