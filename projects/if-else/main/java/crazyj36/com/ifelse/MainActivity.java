package crazyj36.com.ifelse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set on click listenter for "button"
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Setting the EditText field to work with, and getting its' content by getText
                EditText editText = (EditText) findViewById(R.id.editText);
                String text = editText.getText().toString();

                //If it is true that the content in editText matches "(Nothing)" show it
                if (text.matches("")) {
                    Toast.makeText(MainActivity.this, "Text Field is Empty", Toast.LENGTH_SHORT).show();
                }

                // If it was false that in the above "if" statement editText was empty show it
                else Toast.makeText(MainActivity.this, "There is Text in the Text Field", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
