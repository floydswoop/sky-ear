# Building SkyEar

This guide assumes no Android experience. Allow about an hour the first time, mostly waiting for downloads.

SkyEar is a small Android app that shows a web page (the whole interface) and adds the phone features a web page cannot reach: microphone, location, aircraft data, Bluetooth and Wi-Fi scanning, and motion sensors.

## What you need

- A computer (Windows, macOS or Linux) with about 8 GB of RAM (16 GB is better) and 8 GB of free disk space.
- [Android Studio](https://developer.android.com/studio), free from Google. Install it with the default options; on first launch it downloads the Android tools.
- An Android phone and a USB cable that carries data (some cables only charge).

## 1. Prepare your phone

1. Open **Settings > About phone** and tap **Build number** 7 times. (On some phones it is under Software information.)
2. Open **Settings > Developer options** and switch on **USB debugging**.
3. Connect the phone to the computer. When the phone asks "Allow USB debugging?", tap **Allow**.

## 2. Get the project

The repository is a complete Android Studio project, so there is nothing to copy by hand.

1. In Android Studio choose **File > New > Project from Version Control**.
2. Set **URL** to `https://github.com/floydswoop/sky-ear.git` and pick a folder for it.
3. Click **Clone**, then wait for the bottom progress bar to stop. The first sync downloads the build tools and can take several minutes.

If you don't have Git, download the repository as a ZIP from GitHub, unpack it, and use **File > Open** on the unpacked folder.

## 3. Build and run

1. Choose your phone from the device list at the top and press the green **Run** button.
2. The first start asks for permissions as you use each feature: microphone, location, and nearby devices.

To get an APK file instead, choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**. It is written to `app/build/outputs/apk/debug/app-debug.apk`. From a command line, `gradlew assembleDebug` does the same.

To check everything works: open the **Synth** tab and tap Start (use headphones), then try the **Field** tab's **Start scan** button. To try the widget, long-press the home screen, choose Widgets and add SkyEar.

The app's ID is `io.github.floydswoop.skyear`. Builds made before version 0.3 used a different ID, so Android treats them as a separate app with its own clips and settings.

## Updating

Choose **Git > Pull** in Android Studio (or download a fresh ZIP), then Run. Your saved clips live inside the app and are kept across updates.

## Troubleshooting

| What you see | Likely cause and fix |
|---|---|
| Red error `... overrides nothing` or `Unresolved reference` | A changed or mixed file. Pull again or re-download the project. If it persists, open an issue with every line starting `e:` from the Build panel. |
| App shows "SkyEar could not start" | `index.html` is not at `app/src/main/assets/index.html`, or has a different name such as `index (1).html`. |
| Overhead or Library say "Android app only" | The page is old or the Kotlin file is old. Pull again and run **Build > Clean Project**. |
| Microphone message `NotAllowedError` | The manifest lacks `RECORD_AUDIO` / `MODIFY_AUDIO_SETTINGS`, or the permission was denied: Settings > Apps > SkyEar > Permissions. |
| No location prompt | The manifest lacks the location permissions. Rebuild after fixing it. |
| Remote ID finds nothing | Turn on Bluetooth, Wi-Fi and Location. Only drones that broadcast Remote ID appear. Wi-Fi updates are slow (about every 30 seconds). |
| Compass reading looks wrong | Wave the phone in a figure 8 to calibrate; keep it away from metal and magnets. |
| "Export needs Android 10 or newer" | Exporting to Downloads uses a newer storage method. Use the clip files from the app's private storage instead. |

## Making a shareable APK

1. **Build > Generate Signed App Bundle / APK > APK**.
2. Create a keystore when asked. **Keep the keystore file and its passwords safe and private; never commit them to the repository.** You need the same keystore for every future update.
3. Copy the APK to a phone and open it. Android will ask you to allow installs from that source. Publishing on Google Play is a separate process with its own account requirements.

## Changing the interface

Everything you see is in `app/src/main/assets/index.html`. Colors are the `--bg`, `--fg`, `--ac` and `--card` values at the top. Text and buttons are in the middle. The long `<script>` section at the bottom holds the behavior. Keep a backup before editing, and you can preview the page in a desktop browser (phone-only features will say they need the app).
