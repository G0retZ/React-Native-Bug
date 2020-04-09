package com.awesomeproject;

import com.facebook.react.ReactActivity;

public class SecondActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "AwesomeProject2";
  }


  @Override
  protected void onResume() {
      super.onResume();
      getReactInstanceManager().recreateReactContextInBackground();
    }
}
