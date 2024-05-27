package io.github.g00fy2.quickiesample
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import io.github.g00fy2.quickiesample.databinding.ActivityMainBinding
import io.github.g00fy2.quickiesample.databinding.ActivityResultBinding

public class ResultActivity : AppCompatActivity() {
  private lateinit var binding: ActivityResultBinding

  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val postMsg = bundle!!.getString("postMsg")

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myTextView: TextView = findViewById(R.id.textViewResult)
        myTextView.text = postMsg

        binding.continueToMain.setOnClickListener {
          continueToMainClick
        }

  }

  val continueToMainClick = View.OnClickListener {
    val resultActivityIntent = Intent(this, MainActivity::class.java)
    startActivity(resultActivityIntent)
  }
}