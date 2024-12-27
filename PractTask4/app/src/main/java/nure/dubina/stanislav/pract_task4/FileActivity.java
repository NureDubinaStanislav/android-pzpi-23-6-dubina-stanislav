package nure.dubina.stanislav.pract_task4;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText editText = findViewById(R.id.editText);
        Button writeFile = findViewById(R.id.writeFile);
        Button readFile = findViewById(R.id.readFile);

        writeFile.setOnClickListener(view -> {

            FileOutputStream fos = null;
            try {
                fos = openFileOutput("myfile.txt", Context.MODE_PRIVATE);

                fos.write(editText.getText().toString().getBytes());
                fos.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        readFile.setOnClickListener(view -> {
            FileInputStream fis = null;
            try {
                fis = openFileInput("myfile.txt");

                int c;
                String temp = "";
                while ((c = fis.read()) != -1) {
                    temp = temp + Character.toString((char) c);
                }
                fis.close();
                editText.setText(temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}