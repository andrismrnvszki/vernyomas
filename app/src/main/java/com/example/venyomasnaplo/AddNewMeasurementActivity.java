package com.example.venyomasnaplo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;

public class AddNewMeasurementActivity extends AppCompatActivity {

    private final static String LOG_TAG = AddNewMeasurementActivity.class.getName();

    private FirebaseAuth mAuth;

    EditText sysEditText;
    EditText diasEditText;
    EditText pulseEditText;

    private CollectionReference mItems;
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private int SYS;
    private int DIAS;
    private int PULSE;
    private String DATE;
    private String ID = "";
    private MeasurementItem mItem;

    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_measurement);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Measurements");

        Log.i(LOG_TAG, "El se hiszem h működik 67");

        ID = getIntent().getStringExtra("ID");
        SYS = getIntent().getIntExtra("SYS",-1);
        DIAS = getIntent().getIntExtra("DIAS",-1);
        DATE = getIntent().getStringExtra("DATE");
        PULSE = getIntent().getIntExtra("PULSE",-1);


        Log.i(LOG_TAG, "El se hiszem h működik 68");
        sysEditText = findViewById(R.id.SysA);
        diasEditText = findViewById(R.id.DiasA);
        pulseEditText = findViewById(R.id.PulseA);
        Log.i(LOG_TAG, "El se hiszem h működik 68.5");
        Log.i(LOG_TAG, "ID: "+ID+" SYS: "+ SYS+ " DIAS: "+DIAS+" DATE: "+DATE);
        Log.i(LOG_TAG,"-----------------------------------------------------");
        if(ID!=null){
            sysEditText.setText(String.valueOf(SYS), TextView.BufferType.EDITABLE);
            diasEditText.setText(String.valueOf(DIAS), TextView.BufferType.EDITABLE);
            pulseEditText.setText(String.valueOf(PULSE), TextView.BufferType.EDITABLE);
        }



        Log.i(LOG_TAG, "El se hiszem h működik 69");
        //queryData();

        mNotificationHandler = new NotificationHandler(this);
        //mNotificationHandler.Send("Pénisz");

    }

    public void Back(View view) {
        Intent intent = new Intent(this, MeasurementsListActivity.class);
        startActivity(intent);
    }

    public void SaveMeasurement(View view){


        //Log.d(LOG_TAG, "sysStr = " + sysStr + "------------------------------------------------------------------------------------------"  );

        if(TextUtils.isEmpty(sysEditText.getText().toString()) || TextUtils.isEmpty(diasEditText.getText().toString()) ||
                TextUtils.isEmpty(pulseEditText.getText().toString())){
            Toast.makeText(getApplicationContext(),
                    "Kérem ellenőrizze, hogy a beírt adatok helyesek-e!",
                    Toast.LENGTH_LONG)
                    .show();
        }else{
            int sysStr = Integer. parseInt(sysEditText.getText().toString());
            int diasStr = Integer. parseInt(diasEditText.getText().toString());
            int pulseStr = Integer. parseInt(pulseEditText.getText().toString());
            String dateStr = DATE==null ?  LocalDate.now().toString() : DATE;


            mItems.add(new MeasurementItem(sysStr , diasStr, pulseStr, dateStr, user.getEmail()));
            if(ID != null){
                mItems.document(ID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(LOG_TAG, "Error deleting document", e);
                            }
                        });
            }
            mNotificationHandler.Send("Mérés hozzáadva!");
            Back(view);
        }

    }

}