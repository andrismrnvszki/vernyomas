package com.example.venyomasnaplo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;

public class Register extends AppCompatActivity {

    private final static String LOG_TAG = Register.class.getName();

    private FirebaseAuth mAuth;

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText password2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Back(View view) {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        finish();
    }

    public void Register(View view) {
        usernameEditText = findViewById(R.id.loginUsernameEditText);
        emailEditText = findViewById(R.id.registEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        password2EditText = findViewById(R.id.loginPassword2EditText);


        if(TextUtils.isEmpty(usernameEditText.getText().toString()) || TextUtils.isEmpty(emailEditText.getText().toString()) ||
                TextUtils.isEmpty(passwordEditText.getText().toString()) || TextUtils.isEmpty(password2EditText.getText().toString())){
                        Toast.makeText(getApplicationContext(),
                                "Kérem ellenőrizze, hogy a beírt adatok helyesek-e! A jelszónak legalább 6 karakter hosszúnak kell lennie!",
                                Toast.LENGTH_LONG)
                                .show();
        }else{
            String usernameStr = usernameEditText.getText().toString();
            String emailStr = emailEditText.getText().toString();
            String passwordStr = passwordEditText.getText().toString();
            String password2Str = password2EditText.getText().toString();

            if(!passwordStr.equals(password2Str) || passwordStr.length()<6){
                Toast.makeText(getApplicationContext(),
                        "A jelszavak nem egyeznek, vagy a jelszó túl rövid! (min 6 karakter)",
                        Toast.LENGTH_LONG)
                        .show();
            }else{
                mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(LOG_TAG, "User created succesfully!!!!!!!!!!!!!-----------------------------------------------");
                            GetToMeasurements();
                        }else{
                            Log.d(LOG_TAG, emailStr+passwordStr);
                            Log.d(LOG_TAG, "Nem sikerült a regisztráció!!!!");
                            Toast.makeText(Register.this, "Unsuccesfull: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }

        //Log.i(LOG_TAG, "Regisztrált: " + usernameStr + " jelszóval: " + passwordStr);
    }

    public void GetToMeasurements(){
        Intent intent = new Intent(this, MeasurementsListActivity.class);
        startActivity(intent);
    }
}