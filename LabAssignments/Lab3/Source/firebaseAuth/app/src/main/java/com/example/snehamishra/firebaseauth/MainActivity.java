package com.example.snehamishra.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private ProgressDialog progressDialog;

    // views and widgets
    private Button registerUser;
    private EditText editTextEmail, editTextPassword;
    private TextView  loginUser;

    //Firebase Authentication Fileds
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign IDs to the buttons and texts
        registerUser = (Button)findViewById(R.id.register_btn);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        loginUser = (TextView) findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);
        Log.i("my Tag","checking if it reaches here");
        loginUser.setOnClickListener(this);
        registerUser.setOnClickListener(this);

        //Assign instances
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){
            //user already logged in
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
    }


    private void registerUser() {
        String userEmailString, userPassString;
        userEmailString = editTextEmail.getText().toString().trim();
        userPassString = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userPassString)) {
            Toast.makeText(MainActivity.this, "Please enter your Password ! ", Toast.LENGTH_LONG).show();
            // stop the further execution
            //return;
        }

        if (TextUtils.isEmpty(userEmailString)) {

            Toast.makeText(MainActivity.this, "Please enter your Email ! ", Toast.LENGTH_LONG).show();
            // stop the further execution
           // return;
        }

        // If validation is successful
        progressDialog.setMessage("Registering user, Please wait.");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //user is successfully registered
                    //move to home activity
                    Toast.makeText(MainActivity.this, "User registered successfully!. ", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));


                } else {
                    Toast.makeText(MainActivity.this, "User could not be registered!. ", Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == registerUser)
        {
            registerUser();

        }else if(v==loginUser)
        {
            //open Login activity
            startActivity(new Intent(this,LoginActivity.class));

        }
    }
}
