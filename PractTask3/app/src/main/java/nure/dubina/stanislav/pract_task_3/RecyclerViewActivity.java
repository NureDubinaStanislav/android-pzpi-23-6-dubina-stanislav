package nure.dubina.stanislav.pract_task_3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] data = new String [20];
        for (int i = 0; i < data.length; i++) {
            data[i] = "Hello World!";
        }
        MyAdapter adapter = new MyAdapter(data);
        recyclerView.setAdapter((adapter));
    }

}