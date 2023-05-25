package com.akira.pisowifitimer.activities.main;

import android.net.http.SslError;
import android.os.*;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.bottomsheet.timepicker.TimePickerBottomSheet;
import com.akira.pisowifitimer.data.wifi.WifiData;
import com.akira.pisowifitimer.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
  private CompositeDisposable disposables;
  @Inject WifiData wifiData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    timePickerBottomSheet = new TimePickerBottomSheet();

    factory = new MainViewModelFactory(wifiData);
    viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

    disposables = new CompositeDisposable();

    onsetViewBinding();
    onsetViews();
    onsetObservers();
  }

  @Override
  protected void onDestroy() {
    viewModel.unregisterNetworkCallback();
    disposables.clear();
    super.onDestroy();
    
  }

  private void onsetViewBinding() {
    fab = binding.fab;
    this.webview = binding.webview;
  }

  private void onsetViews() {
    setContentView(binding.getRoot());

    webview.setWebViewClient(
        new WebViewClient() {
          @Override
          public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
          }
        });
    webview.getSettings().setJavaScriptEnabled(true);

    fab.setOnClickListener(
        v -> {
          timePickerBottomSheet.show(getSupportFragmentManager(), null);
        });
  }

  private void onsetObservers() {
    Disposable disposable =
        viewModel
            .observableWifiGateway()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(webview::loadUrl);

    disposables.add(disposable);
    
    viewModel.registerNetworkCallback();
  }
}
