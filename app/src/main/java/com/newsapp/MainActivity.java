package com.newsapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.InterstitialAd;
import com.yandex.mobile.ads.InterstitialAdEventListener;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    private Button showAdButton;
    private TextView statusText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAdButton = findViewById(R.id.button_show_ad);
        statusText = findViewById(R.id.text_status);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("R-M-19685327-1");

        showAdButton.setOnClickListener(v -> showAd());

        setupInterstitialAd();
        handler.postDelayed(() -> showAd(), 5000);
    }

    private void setupInterstitialAd() {
        interstitialAd.setInterstitialAdEventListener(new InterstitialAdEventListener() {
            @Override
            public void onAdLoaded() {
                statusText.setText("Реклама готова");
                showAdButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(String error) {
                statusText.setText("Ошибка: " + error);
                handler.postDelayed(() -> loadAd(), 5000);
            }

            @Override
            public void onAdShown() {
                statusText.setText("Реклама показана");
            }

            @Override
            public void onAdDismissed() {
                statusText.setText("Реклама закрыта");
                loadAd();
            }
        });
        loadAd();
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        showAdButton.setEnabled(false);
        statusText.setText("Загрузка рекламы...");
    }

    private void showAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Реклама не загружена", Toast.LENGTH_SHORT).show();
            loadAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
