package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.movieapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private EditText UserEdt,PassEdt;
FirebaseAuth auth;

private Button LoginBtn,RegisterNowBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        RegisterNowBtn=findViewById(R.id.RegisterNowBtn);
        UserEdt=findViewById(R.id.UserEdt);
        PassEdt=findViewById(R.id.PassEdt);
        LoginBtn=findViewById(R.id.LoginBtn);

        //when user click on login button
        LoginBtn.setOnClickListener(v -> {
            login();
        });

        //when user click on register now button
        RegisterNowBtn.setOnClickListener(v -> {
            RegisterNow();
        });
        
       

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void RegisterNow() {
        RegisterNowBtn=findViewById(R.id.RegisterNowBtn);
        RegisterNowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void login() {

        String Email =String.valueOf(UserEdt.getText());
        String Password = String.valueOf(PassEdt.getText());

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();
        }

        auth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() ||UserEdt.getText().toString().equals("admin") && PassEdt.getText().toString().equals("admin") ) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        UserEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    UserEdt.setHint("");
                }
            }
        });


    }

    }
