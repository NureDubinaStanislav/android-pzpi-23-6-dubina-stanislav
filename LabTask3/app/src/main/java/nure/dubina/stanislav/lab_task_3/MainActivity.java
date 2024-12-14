package nure.dubina.stanislav.lab_task_3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private String input = "";
    private String operator = "";
    private double num1 = 0, num2 = 0;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        result.setFocusable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int[] numberButtons = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        View.OnClickListener numberClickListener = v -> {
            if (isResultDisplayed) {
                input = "";
                isResultDisplayed = false;
            }
            Button button = (Button) v;
            input += button.getText().toString();
            result.setText(input);
        };

        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(numberClickListener);
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!input.contains(".")) {
                if (input.isEmpty()) {
                    input = "0.";
                } else {
                    input += ".";
                }
                result.setText(input);
            }
        });

        int[] operatorButtons = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide};
        View.OnClickListener operatorClickListener = v -> {
            if (!input.isEmpty()) {
                num1 = Double.parseDouble(input);
                input = "";
                Button button = (Button) v;
                operator = button.getText().toString();
            }
        };

        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(operatorClickListener);
        }

        findViewById(R.id.btnEquals).setOnClickListener(v -> {
            if (!input.isEmpty() && !operator.isEmpty()) {
                try {
                    num2 = Double.parseDouble(input);
                    double resultValue = 0;

                    switch (operator) {
                        case "+":
                            resultValue = num1 + num2;
                            break;
                        case "-":
                            resultValue = num1 - num2;
                            break;
                        case "×":
                            resultValue = num1 * num2;
                            break;
                        case "÷":
                            if (num2 == 0) {
                                result.setText("Помилка");
                                return;
                            }
                            resultValue = num1 / num2;
                            break;
                        default:
                            result.setText("Помилка");
                            return;
                    }

                    if (resultValue == (long) resultValue) {
                        result.setText(String.valueOf((long) resultValue));
                    } else {
                        result.setText(String.valueOf(resultValue));
                    }
                    input = String.valueOf(resultValue);
                    operator = "";
                    isResultDisplayed = true;

                } catch (Exception e) {
                    result.setText("Помилка");
                }
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            input = "";
            operator = "";
            num1 = num2 = 0;
            result.setText("0");
            isResultDisplayed = false;
        });
    }
}