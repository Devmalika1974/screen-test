package com.test.screenmirroring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.test.screenmirroring.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private int doubleClickCounter = 0; // Counter for double click detection
    private InterstitialAd mInterstitialAd;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Initialize AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "AdMob Initialized.");
                loadNativeAd();
                loadInterstitialAd();
            }
        });

        setupButtonClickListeners();
    }

    private void loadNativeAd() {
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.admob_native_test_id))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd loadedNativeAd) {
                        // Ad successfully loaded.
                        Log.d(TAG, "Native Ad Loaded.");
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        if (isDestroyed()) {
                            loadedNativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = loadedNativeAd;
                        FrameLayout frameLayout =
                                binding.admobNativeAdContainer;
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.ad_unified, frameLayout, false);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, etc.
                        Log.e(TAG, "Native Ad failed to load: " + adError.getMessage());
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren\'t guaranteed to be in every NativeAd, so it\'s important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
    }


    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.admob_interstitial_test_id),
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "Interstitial Ad loaded");
                        mInterstitialAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d(TAG, "The ad was dismissed.");
                                // Reload the ad
                                loadInterstitialAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d(TAG, "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don\'t
                                // show the ad a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, "Interstitial Ad failed to load: " + loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    private void showInterstitialAd(Runnable onAdDismissedAction) {
        if (mInterstitialAd != null) {
             mInterstitialAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d(TAG, "The ad was dismissed.");
                    loadInterstitialAd(); // Preload the next ad
                    onAdDismissedAction.run(); // Execute the original action
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                    Log.d(TAG, "The ad failed to show.");
                    onAdDismissedAction.run(); // Execute the original action even if ad fails
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d(TAG, "The ad was shown.");
                    mInterstitialAd = null;
                }
            });
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d(TAG, "The interstitial ad wasn\'t ready yet.");
            onAdDismissedAction.run(); // Execute the action if ad is not ready
        }
    }

    private void setupButtonClickListeners() {
        binding.buttonMirroring.setOnClickListener(v -> handleButtonClick(() -> {
            try {
                startActivity(new Intent(Settings.ACTION_CAST_SETTINGS));
            } catch (Exception e) {
                Toast.makeText(this, "Could not open Cast Settings", Toast.LENGTH_SHORT).show();
            }
        }));

        binding.buttonVideoPlayer.setOnClickListener(v -> handleButtonClick(() -> {
            startActivity(new Intent(this, VideoPlayerActivity.class));
        }));

        binding.buttonWhatsWeb.setOnClickListener(v -> handleButtonClick(() -> {
             startActivity(new Intent(this, WhatsWebActivity.class));
        }));

        binding.buttonRateUs.setOnClickListener(v -> handleButtonClick(() -> {
            try {
                Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.rate_us_link)));
                startActivity(rateIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open Play Store", Toast.LENGTH_SHORT).show();
            }
        }));

        binding.buttonShare.setOnClickListener(v -> handleButtonClick(() -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (Exception e) {
                Toast.makeText(this, "Could not share app", Toast.LENGTH_SHORT).show();
            }
        }));

        binding.buttonPrivacy.setOnClickListener(v -> handleButtonClick(() -> {
             startActivity(new Intent(this, PrivacyPolicyActivity.class));
        }));
    }

    private void handleButtonClick(Runnable action) {
        doubleClickCounter++;
        // Use a Handler to check for double click within a short time frame
        Handler handler = new Handler(Looper.getMainLooper());
        final int currentClickCount = doubleClickCounter;

        handler.postDelayed(() -> {
            if (currentClickCount == doubleClickCounter) { // Check if it's the last click in the sequence
                if (doubleClickCounter >= 2) {
                    Log.d(TAG, "Double click detected, showing Interstitial Ad.");
                    showInterstitialAd(action); // Show ad, then run action
                } else {
                    Log.d(TAG, "Single click detected, running action directly.");
                    action.run(); // Run action directly for single click
                }
                doubleClickCounter = 0; // Reset counter after handling the click(s)
            }
        }, 300); // 300ms window for double click
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }
}

