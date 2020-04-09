package com.awesomeproject;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.soloader.SoLoader;

import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private ReactNativeHost mReactNativeHost;
  private static final Handler handler = new Handler(Looper.getMainLooper());

  @Override
  public ReactNativeHost getReactNativeHost() {
    if (mReactNativeHost == null) {
      mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for
          // example:
          packages.add(new NativeEmitterPackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }

        @Override
        protected ReactInstanceManager createReactInstanceManager() {
          ReactInstanceManager instanceManager = super.createReactInstanceManager();
          instanceManager.addReactInstanceEventListener(context -> {
            Log.d("React", "React context created: " + context);
            context.addLifecycleEventListener(listener);
          });
          return instanceManager;
        }
      };
    }
    return mReactNativeHost;
  }

  private LifecycleEventListener listener = new LifecycleEventListener() {

    private long timestamp = System.currentTimeMillis();
    private final boolean doOnResume = false;
    private Runnable loadBundleRunnable = () -> {
      System.out.println("Lifecycle: Loading bundle on suspend");
      loadBundle();
    };

    @Override
    public void onHostResume() {
      System.out.println("Lifecycle: onHostResume");
      handler.removeCallbacks(loadBundleRunnable);
      if (doOnResume && System.currentTimeMillis() - timestamp > 10000) {
        loadBundle();
      }
    }

    @Override
    public void onHostPause() {
      System.out.println("Lifecycle: onHostPause");
      if (!doOnResume && System.currentTimeMillis() - timestamp > 10000) {
        handler.postDelayed(loadBundleRunnable, 0);
      }
    }

    @Override
    public void onHostDestroy() {
      System.out.println("Lifecycle: onHostDestroy");
    }

    private void loadBundle() {
      timestamp = System.currentTimeMillis();
      try {
        // #1) Get the ReactInstanceManager instance, which is what includes the
        //     logic to reload the current React context.
        if (mReactNativeHost == null || !mReactNativeHost.hasInstance()) {
          return;
        }
        final ReactInstanceManager instanceManager = mReactNativeHost.getReactInstanceManager();
        // #3) Get the context creation method and fire it on the UI thread (which RN enforces)
        handler.post(() -> {
          try {
            // We don't need to resetReactRootViews anymore
            // due the issue https://github.com/facebook/react-native/issues/14533
            // has been fixed in RN 0.46.0
            //resetReactRootViews(instanceManager);

            instanceManager.recreateReactContextInBackground();
          } catch (Exception e) {
            // The recreation method threw an unknown exception
            // so just simply fallback to restarting the Activity (if it exists)
          }
        });

      } catch (Exception e) {
        // Our reflection logic failed somewhere
        // so fall back to restarting the Activity (if it exists)
        System.out.println("Lifecycle: Failed to load the bundle, falling back to restarting the Activity (if it exists). " + e.getMessage());
      }
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
  }
}
