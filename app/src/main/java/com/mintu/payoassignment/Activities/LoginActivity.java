package com.mintu.payoassignment.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mintu.payoassignment.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditext;
    private EditText passwordEditext;
    private Button buttonLogin;
    private TextView signupTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views Init
        init();

        //Setting On click
        clickListener();
    }

    private void clickListener() {
        buttonLogin.setOnClickListener(this);
        signupTextview.setOnClickListener(this);
    }

    private void init() {
        emailEditext = (EditText) findViewById(R.id.email_editext);
        passwordEditext = (EditText) findViewById(R.id.password_editext);
        buttonLogin = (Button) findViewById(R.id.button_login);
        signupTextview = (TextView) findViewById(R.id.signup_textview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_login:
                login();
                break;
            case R.id.signup_textview:
                signUp();
                break;
        }
    }

    private void signUp() {
        startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
        finish();
    }

    private void login() {

        String email = emailEditext.getText().toString();
        String password = passwordEditext.getText().toString();

        if (email.isEmpty()) {
            emailEditext.setError("Required!");
            emailEditext.requestFocus();
        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            emailEditext.setError("Invalid Email!");
            emailEditext.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditext.setError("Required!");
            passwordEditext.requestFocus();
        } else if (password.length() < 6) {
            passwordEditext.setError("Password too short!");
            passwordEditext.requestFocus();
        } else {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Signing In...Please Wait!");
            dialog.show();

            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(LoginActivity.this,
                                            MainActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Please Verify Your Email", Toast.LENGTH_LONG)
                                            .show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException()
                                        .getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
