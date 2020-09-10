package com.mintu.payoassignment.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mintu.payoassignment.R;
import com.mintu.payoassignment.Models.UserModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fnameEditext;
    private EditText lnameEditext;
    private EditText emailEditext;
    private EditText passwordEditext;
    private EditText cpasswordEditext;
    private EditText mobileEditext;
    private EditText addressEditext;
    private Button buttonSignup;
    private TextView loginTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        buttonSignup.setOnClickListener(this);
        loginTextview.setOnClickListener(this);
    }

    private void init() {
        fnameEditext = (EditText) findViewById(R.id.fname_editext);
        lnameEditext = (EditText) findViewById(R.id.lname_editext);
        emailEditext = (EditText) findViewById(R.id.email_editext);
        passwordEditext = (EditText) findViewById(R.id.password_editext);
        cpasswordEditext = (EditText) findViewById(R.id.cpassword_editext);
        mobileEditext = (EditText) findViewById(R.id.mobile_editext);
        addressEditext = (EditText) findViewById(R.id.address_editext);
        buttonSignup = (Button) findViewById(R.id.button_signup);
        loginTextview = (TextView) findViewById(R.id.login_textview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_signup:
                signUp();
                break;
            case R.id.login_textview:
                login();
                break;
        }
    }

    private void login() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void signUp() {
        final String fname = fnameEditext.getText().toString().trim();
        final String lname = lnameEditext.getText().toString().trim();
        final String email = emailEditext.getText().toString().trim();
        String password = passwordEditext.getText().toString().trim();
        String cpassword = cpasswordEditext.getText().toString().trim();
        final String mobile = mobileEditext.getText().toString().trim();
        final String address = addressEditext.getText().toString().trim();

        if (fname.isEmpty()){ fnameEditext.setError("Required");}
        else if (lname.isEmpty()){ lnameEditext.setError("Required");}
        else if (email.isEmpty()){ emailEditext.setError("Required");}
        else if (password.isEmpty()){ passwordEditext.setError("Required");}
        else if (cpassword.isEmpty()){ cpasswordEditext.setError("Required");}
        else if (!password.equals(cpassword)){ cpasswordEditext.setError("Password Doesn't Matched");}
        else if (mobile.isEmpty()){ mobileEditext.setError("Required");}
        else if (address.isEmpty()){ addressEditext.setError("Required");}
        else
            {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Signing Up...Please Wait!");
                dialog.show();

                //Creating User Using Email and Password
                //Saving User Data
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                dialog.dismiss();
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Verify Your Mail", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        LoginActivity.class
                                );
                                startActivity(intent);
                                finish();

                                UserProfileChangeRequest userProfileChangeRequest
                                        = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(email)
                                        .build();

                                authResult.getUser().updateProfile(userProfileChangeRequest);

                                UserModel userModel = new UserModel();

                                userModel.setFname(fname);
                                userModel.setLname(lname);
                                userModel.setEmail(email);
                                userModel.setMobileNumber(mobile);
                                userModel.setAddress(address);

                                FirebaseFirestore.getInstance()
                                        .collection("PayOUsers")
                                        .document(authResult.getUser().getUid())
                                        .set(userModel);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
    }
}
