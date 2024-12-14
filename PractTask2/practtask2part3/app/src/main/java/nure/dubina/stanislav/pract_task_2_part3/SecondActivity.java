package nure.dubina.stanislav.pract_task_2_part3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SecondActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
    }

    public void onFinishClick(View v) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SecondActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SecondActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SecondActivity", "onPause");
    }

    @Override
    protected void onStop() {
        Log.d("SecondActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("SecondActivity", "onDestroy");
        super.onDestroy();
    }
}