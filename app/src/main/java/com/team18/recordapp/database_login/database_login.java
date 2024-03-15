package com.team18.recordapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team18.recordapp.models.Account;

public class database_login extends SQLiteOpenHelper {

    // Tên database
    private static final String DATABASE_NAME = "recordapp";
    // Bảng account
    private static final String TABLE_TAIKHOAN = "taikhoan";
    private static final String ID_TAI_KHOAN = "idtaikhoan";
    private static final String TEN_TAI_KHOAN = "tentaikhoan";
    private static final String MAT_KHAU = "matkhau";
    private static final String EMAIL = "email";
    // Version
    private static final int DATABASE_VERSION = 1;
    private Context context;

    // Câu lệnh tạo bảng tài khoản
    private static final String SQLQuery = "CREATE TABLE " + TABLE_TAIKHOAN + " ( "
            + ID_TAI_KHOAN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEN_TAI_KHOAN + " TEXT UNIQUE, "
            + MAT_KHAU + " TEXT, "
            + EMAIL + " TEXT)";

    // Thêm dữ liệu vào bảng
    private static final String SQLQuery1 = "INSERT INTO " + TABLE_TAIKHOAN + " VALUES (null, 'owner', 'owner', 'owner@gmail.com')";
    private static final String SQLQuery2 = "INSERT INTO " + TABLE_TAIKHOAN + " VALUES (null, 'admin', 'admin', 'admin@gmail.com')";

    public database_login(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Thực thi câu lệnh tạo bảng
        db.execSQL(SQLQuery);
        // Thêm dữ liệu mẫu vào bảng
        db.execSQL(SQLQuery1);
        db.execSQL(SQLQuery2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cập nhật database khi có phiên bản mới
    }
    // phương thức lấy tất cả tài khoản
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_TAIKHOAN, null);
        return res;
    }
    // nhận thông tin từ form đưa lên database Account trong models
    public void AddTaiKhoan(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        // thực hiện thêm dữ liệu từ form thông qua ContentValues
        ContentValues values = new ContentValues();
        values.put(TEN_TAI_KHOAN, account.getmTenTaiKhoan());
        values.put(MAT_KHAU, account.getmMatKhau());
        values.put(EMAIL, account.getmEmail());
        db.insert(TABLE_TAIKHOAN, null, values);

        // đóng lại khi không dùng
        db.close();
        Log.e("ADD TK", "TC");
    }
}
