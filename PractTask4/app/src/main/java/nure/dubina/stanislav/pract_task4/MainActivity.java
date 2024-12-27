package nure.dubina.stanislav.pract_task4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Button sharedPreference = findViewById(R.id.sharedPreference);
        Button goIntoDb = findViewById(R.id.goIntoDb);
        Button goIntoFile = findViewById(R.id.goIntoFile);
        EditText nameEditText = findViewById(R.id.name);
        EditText ageEditText = findViewById(R.id.age);
        sharedPreference.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Name", nameEditText.getText().toString());
            editor.putInt("Age", ageEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(ageEditText.getText().toString()));
            editor.apply();
        });

        String name = sharedPreferences.getString("Name", "");
        int age = sharedPreferences.getInt("Age", -1);
        nameEditText.setText(name);
        if (age >= 0) {
            ageEditText.setText(Integer.toString(age));
        }

        goIntoDb.setOnClickListener(view -> {
            startActivity(new Intent(this, DBActivity.class));
        });

        goIntoFile.setOnClickListener(view -> {
            startActivity(new Intent(this, FileActivity.class));
        });
    }
}