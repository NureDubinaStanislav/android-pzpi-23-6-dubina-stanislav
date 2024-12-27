package nure.dubina.stanislav.pract_task4;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter adapter = new Adapter();
        DBHelper dbHelper = new DBHelper(this);
        Button saveIntoDb = findViewById(R.id.saveIntoDb);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        EditText nameEditText = findViewById(R.id.name);
        EditText ageEditText = findViewById(R.id.age);

        saveIntoDb.setOnClickListener(view -> {
            ContentValues values = new ContentValues();
            values.put("name", nameEditText.getText().toString());
            values.put("age", ageEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(ageEditText.getText().toString()));
            dbHelper.getWritableDatabase().insert("users", null, values);
            adapter.addUser(new User(nameEditText.getText().toString(), ageEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(ageEditText.getText().toString())));
        });

        Cursor cursor = dbHelper.getReadableDatabase().query("users", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            User user = new User(name, age);
            adapter.addUser(user);
        }
        cursor.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}