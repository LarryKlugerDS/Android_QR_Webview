package io.github.g00fy2.quickiesample
import android.annotation.SuppressLint
import android.icu.text.DisplayContext
import android.os.Bundle
import android.view.WindowInsets
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class WebViewActivity : AppCompatActivity() {

  public class JsObject {
    @JavascriptInterface
    public fun msg(): String {
      return "From Android!"
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
           val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
           v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val windowInsetsController =
          WindowCompat.getInsetsController(window, window.decorView)
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // Hide top app name bar
        supportActionBar?.hide()

        val bundle = intent.extras
        val url = bundle?.getString("url")

        val myWebView: WebView = findViewById(R.id.webview)
        @SuppressLint("SetJavaScriptEnabled")
        myWebView.getSettings().javaScriptEnabled = true
        myWebView.addJavascriptInterface(JsObject(), "Android")

        myWebView.loadUrl("https://docusign.github.io/examples/androidTest.html?$url")
        //myWebView.loadUrl("https://xerox.com?$url")
        //myWebView.loadUrl(url!!)
  }
}