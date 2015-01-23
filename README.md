# Welcome to the World of Airports!

A simple app that pulls data about airports from a [Cloudant](https://cloudant.com) database and displays their location as map pins in a map view.

![](app.png?raw=true)

## Requirements

1. Install the [Android SDK](https://developer.android.com/sdk/index.html) and [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
2. Download and configure the [Google Play services SDK](https://developer.android.com/sdk/installing/adding-packages.html).

#### Device Requirements
To run the app you'll need either:
 - A compatible Android device that runs Android 2.3 or higher and includes Google Play Store.
 - The Android emulator with an AVD that runs the Google APIs platform based on Android 4.2.2 or higher.

## Google Maps API

Using the Google Maps Android API v2 requires you to register a signing key to Google. When doing so, Google generates an API key you must add to the Android manifest.

You can obtain your Google Maps API key [here](https://developers.google.com/maps/documentation/android/start#obtain_a_google_maps_api_key).

In order to run this app correctly you must change the appropriate `meta-data` in the `AndroidManifest.xml`:

	<meta-data
	    android:name="com.google.android.maps.v2.API_KEY"
	    android:value="your_key_here" />

## Building with Gradle

If you're using [Android Studio](http://developer.android.com/sdk/index.html) then simply follow these steps:

1. Open `File`
2. Select `Import Project`
3. Select `build.gradle` under the project directory
4. Click `OK` and Gradle will do everything for you.

Otherwise, you'll want to download [Gradle](https://www.gradle.org/downloads) and execute ``gradle build`` from the root of the project.

You can see results of the build process in the build folder. Here you see several folders related to various parts of the app. The assembled Android package resides in the apk folder. The APK file here is ready to be deployed to a device or emulator.
