# React-Native-Bug
Example project to reproduce the bug

##  Description
When app is started and navigates to new `ReactFragment` or `ReactActivity` the call to `recreateReactContextInBackground()` method of `ReactInstanceManager` results in appearance of two copies of new screen at the same time.
But if I make a 200+ ms delay before calling `recreateReactContextInBackground()` then I see appearance of the new screen and then it's reload removing initially created view.
This behavior was tested on react native versions: `0.61.3` and `0.62.2`
I suspect there is a race condition between the processes of new screen initialization and react context recreation.

## React Native version:
System:
    OS: macOS 10.15.3
    CPU: (12) x64 Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz
    Memory: 2.32 GB / 16.00 GB
    Shell: 3.2.57 - /bin/bash
  Binaries:
    Node: 12.12.0 - /usr/local/bin/node
    Yarn: 1.19.1 - /usr/local/bin/yarn
    npm: 6.11.3 - /usr/local/bin/npm
    Watchman: 4.9.0 - /usr/local/bin/watchman
  SDKs:
    iOS SDK:
      Platforms: iOS 13.2, DriverKit 19.0, macOS 10.15, tvOS 13.2, watchOS 6.1
    Android SDK:
      API Levels: 28, 29
      Build Tools: 28.0.3, 29.0.2
  IDEs:
    Android Studio: 3.6 AI-192.7142.36.36.6241897
    Xcode: 11.3.1/11C504 - /usr/bin/xcodebuild
  npmPackages:
    react: 16.11.0 => 16.11.0 
    react-native: 0.61.3 => 0.61.3 
  npmGlobalPackages:
    react-native-cli: 2.0.1
    react-native-git-upgrade: 0.2.7

## Steps To Reproduce
1. Create new project with `react-native init ExampleProject`
2. Copy `MainActivity` to `SecondActivity` changing main component name to `ExampleProject2`. Add corresponding record to `AndroidManifest.xml`
3a. Option 1: Override `onResume` method of `SecondActivity ` and add `getReactInstanceManager().recreateReactContextInBackground()` in it. This is the same thing CodePush does when loading new bundle with rule "on next resume".
3b.  Option 2: Override `onPause` method of `MainActivity ` and add `getReactInstanceManager().recreateReactContextInBackground()` in it. This is the same thing CodePush does when loading new bundle with rule "on next suspend".
4. Copy with renaming rename `App.js` to `App2.js`.
5. Make background colors in `App.js` and `App2.js` semitransparent with alpha = 0.4 (to witness old screen in front on the new one)
6. Register new component in `instance.js` with `AppRegistry.registerComponent(appName + '2', () => App2);`
7. Add button to `App.js` component in any place to navigate to `SecondActivity`
8. Add native module as usual that will handle button click and call `getCurrentActivity().startActivity(new Intent(currentActivity, SecondActivity.class));`
9. Build and start the app
10. Click the button to navigate to `SecondActivity`
11. You will see 2 copies of `App2` component one in front of another. And only the one in front (old one) is scrollable.

## Expected Results
Only one instance of `App2` will appear on the screen and be fully intractable. And ideally reloaded seamlessly (without blank screen).

## Snack, code example, screenshot, or link to a repository:
Example source code: https://github.com/G0retZ/React-Native-Bug
Screenshot of the bug:
![photo_2020-04-09 18 51 07](https://user-images.githubusercontent.com/13471216/78914481-1b003100-7a93-11ea-968a-9f916e639c86.jpeg)
