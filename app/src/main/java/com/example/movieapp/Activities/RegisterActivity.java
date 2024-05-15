package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {
    Button registerButton;
    EditText RegEmailEdt,RegPassEdt;
    TextView goToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        RegEmailEdt = findViewById(R.id.RegEmailEdt);
        RegPassEdt = findViewById(R.id.RegPasswordEdt);
        registerButton = findViewById(R.id.RegisterBtn);
        mAuth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(v -> {
        Register();
        });
        goToLogin = findViewById(R.id.GotoLogin);
        goToLogin.setOnClickListener(v -> {
           GoToLogin();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void GoToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void Register() {

            String Email =String.valueOf(RegEmailEdt.getText());
            String Password = String.valueOf(RegPassEdt.getText());

            if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
                Toast.makeText(this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();
            }


            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                if(Password.length()<6){
                                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters",
                                            Toast.LENGTH_SHORT).show();
                                }else if(!Email.contains("@gmail.com")){
                                    Toast.makeText(RegisterActivity.this, "Please enter a valid email address",
                                            Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            }
                        }
                    });


    }
}