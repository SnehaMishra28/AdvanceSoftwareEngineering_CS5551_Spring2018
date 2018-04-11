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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TwitterLoginButton twitterLoginButton;
    private TextView registerUser;
    private Button loginUser;
    private EditText userName, userPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);

        //assign the widgets to instances
        assignWidgets();

       // twitterLoginButton = findViewById(R.id.twitterLogin_btn);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                login(session);


            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Authentication failed!!", Toast.LENGTH_LONG).show();
            }
        });

        // Set on click listener to buttons
        registerUser.setOnClickListener(this);
        loginUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerTextView:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.login_btn:
                userLogin();
                //startActivity(new Intent(LoginActivity.this,ImageActivity.class));
                finish();
                break;
        }
    }

    // user login method
    private void userLogin() {
        String userEmailString = userName.getText().toString().trim();
        String userPassString = userPassword.getText().toString().trim();

        //if user does not enter email ID
        if(userEmailString.isEmpty()){
            userName.setError("Email is required");
            userName.requestFocus();
            return;
        }

        // check if user enters a valid email address
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmailString).matches()){
            userName.setError("Please enter a valid Email.");
            userName.requestFocus();
            return;
        }

        //check the password length
        if(userPassString.length()<6){
            userPassword.setError("Password length is less than 6! Try again!");
            userPassword.requestFocus();
            return;
        }

        //if user does not enter password
        if(userPassString.isEmpty()){
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }

        //Log user with entered user email and password
        auth.signInWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //on successful login user is navigated to image activity
                   // startActivity(new Intent(LoginActivity.this,ImageActivity.class));
                    Intent intent = new Intent(LoginActivity.this, ImageActivity.class);
                    // so when the user press back button from the Image Activity, it wont come to Login activity
                    // clears all the activity on top of stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //assign the widgets to instances
    public void assignWidgets(){
        auth = FirebaseAuth.getInstance();
        twitterLoginButton = findViewById(R.id.twitterLogin_btn);
         registerUser = findViewById(R.id.registerTextView);
         loginUser = findViewById(R.id.login_btn);
         userName = findViewById(R.id.userNameEditText);
         userPassword = findViewById(R.id.passwordEditText);
    }

    //twitter login method
    public void login(TwitterSession session){
        String userName = session.getUserName();
        Intent intent = new Intent(LoginActivity.this, ImageActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        twitterLoginButton.onActivityResult(requestCode,resultCode,data);
    }
}
