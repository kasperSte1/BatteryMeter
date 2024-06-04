package com.example.batterymetter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyListView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private List<BatteryData> batteryDataList;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.HistoryRecyclerListView);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        batteryDataList = new ArrayList<>();

        adapter = new HistoryAdapter(this, batteryDataList);
        historyListView.setAdapter(adapter);

        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            BatteryData selectedData = (BatteryData) parent.getItemAtPosition(position);
            Intent intent = new Intent(HistoryActivity.this, MeasurementDetailActivity.class);  
            intent.putExtra("timestamp", selectedData.getTimestamp());
            intent.putExtra("appName", selectedData.getAppName());
            intent.putExtra("values", new ArrayList<>(selectedData.getValues()));
            startActivity(intent);
        });

        fetchMeasurements();
    }

    private void fetchMeasurements() {
        if (user != null) {
            db.collection("users")
                    .document(user.getUid())
                    .collection("measurements")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limitToLast(200)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            batteryDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BatteryData data = document.toObject(BatteryData.class);
                                batteryDataList.add(data);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(HistoryActivity.this, "Błąd pobierania danych: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
