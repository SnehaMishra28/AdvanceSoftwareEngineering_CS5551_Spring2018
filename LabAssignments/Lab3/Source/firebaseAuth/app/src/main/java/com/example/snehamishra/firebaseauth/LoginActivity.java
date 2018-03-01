package com.example.snehamishra.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // views and widgets
    private Button loginUser;
    private EditText editTextEmail, editTextPassword;
    private TextView  registerUser;
    private ProgressDialog progressDialog;

    //Firebase Authentication Fileds
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign IDs
        loginUser = (Button)findViewById(R.id.signin_btn);
        editTextEmail =(EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        registerUser = (TextView) findViewById(R.id.signUp_btn);

        //assign OnClick Listener
        loginUser.setOnClickListener(this);
        registerUser.setOnClickListener(this);

        //Assign Instances
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            //user already logged in
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }

    private void userLogin(){
        String userEmailString, userPassString;
        userEmailString = editTextEmail.getText().toString().trim();
        userPassString = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userPassString)) {

            Toast.makeText(LoginActivity.this, "Please enter your Password ! ", Toast.LENGTH_LONG).show();
            // stop the further execution
            return;
        }

        if (TextUtils.isEmpty(userEmailString)) {
            Toast.makeText(LoginActivity.this, "Please enter your Email ! ", Toast.LENGTH_LONG).show();
            // stop the further execution
            return;
        }

        // If validation is successful
        progressDialog.setMessage("Registering new User");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(userEmailString,userPassString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    //move to home page after login
                    finish();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v==loginUser)
        {
            //sign in user
            userLogin();
        }

        if (v==registerUser){
            //set up user registration
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else
        {
            //user could not be signed in
        }
    }
}
