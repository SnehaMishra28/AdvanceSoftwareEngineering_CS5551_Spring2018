package com.example.snehamishra.smartshopping;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView backSignIn;
    private EditText registerUserEmail, registerUserPassword;
    private Button registerUser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        defineWidgets();

        registerUser.setOnClickListener(this);
        backSignIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                registerUser();
                break;
            case R.id.registerBackTextView:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;

        }
    }

    private void registerUser(){
        String userEmail = registerUserEmail.getText().toString().trim();
        String userPass = registerUserPassword.getText().toString().trim();

        //if user does not enter email ID
        if(userEmail.isEmpty()){
            registerUserEmail.setError("Email is required");
            registerUserEmail.requestFocus();
            return;
        }

        // check if user enters a valid email address
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            registerUserEmail.setError("Please enter a valid Email.");
            registerUserEmail.requestFocus();
            return;
        }

        //check the password length
        if(userPass.length()<6){
            registerUserPassword.setError("Password length is less than 6! Try again!");
            registerUserPassword.requestFocus();
            return;
        }

        //if user does not enter password
        if(userPass.isEmpty()){
            registerUserPassword.setError("Password is required");
            registerUserPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User registered Successfully!", Toast.LENGTH_SHORT).show();

                    //on successful login user is navigated to image activity
                    // startActivity(new Intent(LoginActivity.this,ImageActivity.class));
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    // so when the user press back button from the Image Activity, it wont come to Login activity
                    // clears all the activity on top of stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // or we can simply log user in and start the image activity

                }
                else {
                    //check if user is already registered
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        // If registration fails, display a Exception message to the user.
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void defineWidgets(){
        backSignIn = findViewById(R.id.registerBackTextView);
        registerUser = findViewById(R.id.register_btn);
        registerUserEmail = findViewById(R.id.registerEmailEditText);
        registerUserPassword = findViewById(R.id.registerPasswordEditText);
    }
}
