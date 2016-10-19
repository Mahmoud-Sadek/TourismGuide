package com.example.sadokey.tourism_guide.Classes;

import android.webkit.WebView;
import android.webkit.WebViewClient;
public class fekraWebViewClient extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
