package com.example.jokesphone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TerminosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminos, container, false);

        WebView webView = view.findViewById(R.id.webView);

        // Permite navegar sin salir de la app
        webView.setWebViewClient(new WebViewClient());

        // Activamos JavaScript para que pueda funcionar correctamente
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Cargamos la URL
        webView.loadUrl("https://imagine800.com");

        return view;
    }
}
