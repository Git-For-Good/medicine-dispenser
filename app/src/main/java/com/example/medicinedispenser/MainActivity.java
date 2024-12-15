package com.example.medicinedispenser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button buttonSection1, buttonSection2, buttonSection3;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSection1 = findViewById(R.id.buttonSection1);
        buttonSection2 = findViewById(R.id.buttonSection2);
        buttonSection3 = findViewById(R.id.buttonSection3);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSection1.setOnClickListener(v -> toggleSection("Section1"));
        buttonSection2.setOnClickListener(v -> toggleSection("Section2"));
        buttonSection3.setOnClickListener(v -> toggleSection("Section3"));
    }

    private void toggleSection(String sectionName) {
        DatabaseReference sectionRef = databaseReference.child(sectionName);

        sectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int currentLed = snapshot.child("Led").getValue(Integer.class);
                    int currentBuzzer = snapshot.child("Buzzer").getValue(Integer.class);

                    int newValue = currentLed == 0 ? 1 : 0;

                    sectionRef.child("Led").setValue(newValue);
                    sectionRef.child("Buzzer").setValue(newValue);

                    Toast.makeText(MainActivity.this,
                            sectionName + " toggled to " + newValue, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to update: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
