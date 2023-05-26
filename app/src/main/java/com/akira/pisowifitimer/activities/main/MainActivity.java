package com.akira.pisowifitimer.activities.main;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.*;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.bottomsheet.timepicker.TimePickerBottomSheet;
import com.akira.pisowifitimer.data.wifi.WifiData;
import com.akira.pisowifitimer.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;
  private FloatingActionButton fab;
  private TimePickerBottomSheet timePickerBottomSheet;
  private MainViewModel viewModel;
  private MainViewModelFactory factory;
  private WebView webview;
  private TextView tv;
  private Disposable disposable;

  @Inject WifiData wifiData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    timePickerBottomSheet = new TimePickerBottomSheet();

    factory = new MainViewModelFactory(wifiData);
    viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

    onsetViewBinding();
    onsetViews();
    onsetObservers();
  }

  @Override
  protected void onDestroy() {
    viewModel.unregisterNetworkCallback();
    disposable.dispose();
    super.onDestroy();
  }

  private void onsetViewBinding() {
    fab = binding.fab;
    webview = binding.webview;
    tv = binding.emptyView;
  }

  private void onsetViews() {
    setContentView(binding.getRoot());

    webview.setWebViewClient(
        new WebViewClient() {
          @Override
          public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
          }

          @SuppressWarnings("deprecation")
          @Override
          public void onReceivedError(
              WebView webview, int errorCode, String description, String failingUrl) {
            if (errorCode == ERROR_CONNECT && webview.getUrl().startsWith("https://")) {
              String httpUrl = "http://" + webview.getUrl().substring(8);
              webview.loadUrl(httpUrl);
            }
          }

          @Override
          public void onReceivedError(
              WebView webview, WebResourceRequest request, WebResourceError error) {
            if (error != null
                && error.getErrorCode() == ERROR_CONNECT
                && webview.getUrl().startsWith("https://")) {
              String httpUrl = "http://" + webview.getUrl().substring(8);
              webview.loadUrl(httpUrl);
            }
          }
        });

    webview.setWebChromeClient(
        new WebChromeClient() {
          @Override
          public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new MaterialAlertDialogBuilder(view.getContext())
                .setTitle(url + " says")
                .setMessage(message)
                .setPositiveButton(
                    android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                      }
                    })
                .setCancelable(false)
                .create()
                .show();

            return true;
          }
        });

    webview.getSettings().setJavaScriptEnabled(true);

    fab.setOnClickListener(
        v -> {
          timePickerBottomSheet.show(getSupportFragmentManager(), null);
        });
  }

  private void onsetObservers() {
    disposable =
        viewModel
            .observableWifiGateway()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                gateway -> {
                  webview.setVisibility(View.VISIBLE);
                  tv.setVisibility(View.GONE);
                  webview.loadUrl(gateway);
                });

    viewModel.registerNetworkCallback();
  }
}
