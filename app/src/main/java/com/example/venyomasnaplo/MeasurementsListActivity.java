package com.example.venyomasnaplo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class MeasurementsListActivity extends AppCompatActivity {
    private final static String LOG_TAG = MeasurementsListActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<MeasurementItem> mItemList;
    private MeasurementItemAdapter mAdapter;
    int gridNumber = 1;


    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.measurements_list);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView_list);

        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        mItemList = new ArrayList<>();

        mAdapter = new MeasurementItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Measurements");
        queryData();


    }

    private void queryData() {
        mItemList.clear();
        mItems.orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    MeasurementItem item = document.toObject(MeasurementItem.class);
                    if(item.getUser().equals(user.getEmail())){
                        item.setId(document.getId());
                        mItemList.add(item);
                        Log.d(LOG_TAG, "ELEM ID" + item.getId());
                    }

                }
                if (mItemList.size()==0 && user.getEmail().equals("kamukamu69420@gmail.com")){
                    initializeData();
                    queryData();
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    public void deleteItem(MeasurementItem item){
        DocumentReference ref= mItems.document(item.getId());

        ref.delete().addOnSuccessListener(succes -> {
            Log.d(LOG_TAG, "Törölt elem" + item.getId());
        }).addOnFailureListener(failure -> {
            Toast.makeText(this, "Nem sikerült a törlés" + item.getId(), Toast.LENGTH_LONG).show();
        });

        queryData();
    }

    private void initializeData() {
        int[] itemSys = getResources().getIntArray(R.array.mesItemSys);
        int[] itemDias = getResources().getIntArray(R.array.mesItemDias);
        int[] itemsPulse = getResources().getIntArray(R.array.mesItemPulse);

        String[] itemDate = getResources().getStringArray(R.array.mesItemDate);

        // Clear the existing data (to avoid duplication).
        //mItemList.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemSys.length; i++) {
            mItems.add(new MeasurementItem(itemSys[i],
                                            itemDias[i],
                                            itemsPulse[i],
                                            itemDate[i],
                                            user.getEmail()));
        }


        // Notify the adapter of the change.

        //mAdapter.notifyDataSetChanged();

    }

    //public void deleteItem(MeasurementItem item){
    //    DocumentReference ref= mItems.document(item._getId());
    //
    //    ref.delete().addOnSuccessListener(succes -> {
    //        Log.d(LOG_TAG, "Törölt elem" + item._getId());
    //    }).addOnFailureListener(failure -> {
    //        Toast.makeText(this, "Nem sikerült a törlés" + item._getId(), Toast.LENGTH_LONG).show();
    //    });
    //
    //    queryData();
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                BackToLogin();
                return true;
            case R.id.addNewMeasurement:
                hozzaAd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public void hozzaAd() {
        Intent intent = new Intent(this, AddNewMeasurementActivity.class);
        startActivity(intent);

    }

    public void BackToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void editItem(MeasurementItem currentItem) {
        Intent intent = new Intent(this, AddNewMeasurementActivity.class);
        intent.putExtra("ID", currentItem.getId());
        intent.putExtra("SYS", currentItem.getSystole());
        intent.putExtra("DIAS", currentItem.getDiastole());
        intent.putExtra("PULSE", currentItem.getPulse());
        intent.putExtra("DATE", currentItem.getDate());

        startActivity(intent);
    }
}
