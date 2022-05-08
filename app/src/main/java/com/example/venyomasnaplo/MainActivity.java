package com.example.venyomasnaplo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getName();
    private final static int RC_SIGN_IN = 123;


    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    EditText emailEditText;
    EditText passwordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void Register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void Login(View view) {

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);


        if(TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString())){
            Toast.makeText(getApplicationContext(),
                    "Kérem ellenőrizze, hogy a beírt adatok helyesek-e!",
                    Toast.LENGTH_LONG)
                    .show();
        }else{
            String emailStr = emailEditText.getText().toString();
            String passwordStr = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(LOG_TAG, "User logged in succesfully!!!!!!!!!!!!!-----------------------------------------------");
                        GetToMeasurements();
                    }else {
                        Log.d(LOG_TAG, emailStr + passwordStr);
                        Log.d(LOG_TAG, "Nem sikerült a bejelentkezés!!!!");
                        Toast.makeText(MainActivity.this, "Unsuccesfull: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        //Log.i(LOG_TAG, "Bejelentkezett: " + usernameStr + " jelszóval: " + passwordStr);
    }

    public void LoginGoogle(View view) {
        Log.i(LOG_TAG, "Google Log");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void GetToMeasurements(){
        Intent intent = new Intent(this, MeasurementsListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_activity, R.anim.slide_out_activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "firebaseAuthWithGoogle: "+ account.getId());

                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.w(LOG_TAG, "Google sign in failed!");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Google login was succesfully!!!!!!!!!!!!!-----------------------------------------------");
                    GetToMeasurements();
                }else {
                    Log.d(LOG_TAG, "Nem sikerült a bejelentkezés!!!!");
                    Toast.makeText(MainActivity.this, "Unsuccesfull: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void KamuLogin(View view) {
        //TODO
        String emailStr = "kamukamu69420@gmail.com";
        String passwordStr = "pénisz123";

        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Guest logged in succesfully!!!!!!!!!!!!!-----------------------------------------------");
                    GetToMeasurements();
                }else {
                    Log.d(LOG_TAG, emailStr + passwordStr);
                    Log.d(LOG_TAG, "Nem sikerült a bejelentkezés!!!!");
                    Toast.makeText(MainActivity.this, "Unsuccesfull: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}