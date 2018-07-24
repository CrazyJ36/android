package crazyj36.com.openwebpage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTxt1 = (EditText) findViewById(R.id.editTxt1);
                String url = editTxt1.getText().toString();
                Intent webPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webPage);
            }
        });
    }
}
