package crazyj36.com.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tst = Toast.makeText(this,"Kotlin App", Toast.LENGTH_LONG)
        tst.show()
    }
}
