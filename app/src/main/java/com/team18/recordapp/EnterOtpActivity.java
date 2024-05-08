package com.team18.recordapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class EnterOtpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtOtp;
    private Button btnVerifyOtp;
    private String mPhoneNumber;
    private String mVerificationId;
    TextView reSendOtp;
    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_log_in);

        edtOtp = findViewById(R.id.edt_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        reSendOtp = (TextView) findViewById(R.id.reSendOtp);

        mAuth = FirebaseAuth.getInstance();
        getPhone();
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOtp = edtOtp.getText().toString().trim();
                if(strOtp.length() == 6) {
                    onClickSendOtp(strOtp);
                } else {
                    Toast.makeText(EnterOtpActivity.this, "Enter Opt code.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnterOtpActivity.this, "Opt code have been resend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhone() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference parentRef = FirebaseDatabase.getInstance().getReference().child("list_users");

        String targetKey = firebaseUser.getUid();

        DatabaseReference childRef = parentRef.child(targetKey);

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mPhoneNumber = user.getPhone();
                String phone = "+84"+mPhoneNumber.substring(1);
                verifyPhone(phone);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void onClickSendOtp(String strOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, strOtp);
        signInWithPhoneAuthCredential(credential);
    }

    private void verifyPhone(String strPhoneNum) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strPhoneNum)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtpActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mProgress = new ProgressDialog(EnterOtpActivity.this);
        mProgress.setMessage("");
        mProgress.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            logIn();
                        } else {
                            Toast.makeText(EnterOtpActivity.this, "Double check Opt code or your code is time out.", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.getInstance().signOut();
        finish();
    }

//    private void goToAudioSharingActivity() {
//        Intent intent = new Intent(EnterOtpActivity.this, AudioSharingActivity.class);
//        startActivity(intent);
//    }

    private void logIn() {
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                            Toast.makeText(EnterOtpActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
//                            goToAudioSharingActivity();
                        } else {
                            Toast.makeText(EnterOtpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}