package com.airmh.soundllysdktest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 결과 리스트 중 Key가 Url인 경우에만 호출되는 WebView
 * 넘어온 URL를 WebView로 노출한다. 
 * @author AirMH
 *
 */
public class WebViewDialogActivity extends Activity {

	private WebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.webview_layout);
	    
	    webView = (WebView)findViewById(R.id.webView);
	    webView.setWebViewClient(new WebClient()); 
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.loadUrl(getIntent().getStringExtra("url"));
	    
	    findViewById(R.id.webViewCloseButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	
	}
	
	class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
