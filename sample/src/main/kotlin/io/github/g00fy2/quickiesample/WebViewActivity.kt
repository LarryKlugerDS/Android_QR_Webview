package io.github.g00fy2.quickiesample
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat


public class WebViewActivity : AppCompatActivity() {

  class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
      return false
    }
  }

  public class WebJavaScriptInterface internal constructor(context: Context, activity: Activity) {
    var _activity: Activity = activity
    var _context: Context = context

    // Message from Android to JavaScript
    @JavascriptInterface
    public fun msg(): String {
      return "From Android!"
    }

    // post message to Android app from JavaScript
    @JavascriptInterface
    public fun postMsg(msg: String) {
      _activity.runOnUiThread(Runnable {
        val resultActivityIntent = Intent(_activity, ResultActivity::class.java)
        resultActivityIntent.putExtra("postMsg", msg);
        startActivity(_context, resultActivityIntent, null)
      })
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
        val url = bundle!!.getString("url")

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.webViewClient = WebViewClient()

        val wvSettings = myWebView.getSettings()
        @SuppressLint("SetJavaScriptEnabled")
        wvSettings.javaScriptEnabled = true
        wvSettings.domStorageEnabled = true

        myWebView.addJavascriptInterface(WebJavaScriptInterface(this@WebViewActivity, this), "WebView")

        //myWebView.loadUrl("https://docusign.github.io/examples/androidTest.html")
        myWebView.loadUrl(url!!)

        // REMOVE Uses url to stop lint errors if url is not used above
        val resultActivityIntent = Intent(this, ResultActivity::class.java)
        resultActivityIntent.putExtra("foo", url);
  }
}