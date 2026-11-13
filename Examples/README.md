# Kotlin integration example

Add [AdsActivity.kt](AdsActivity.kt) to an Android app using
`com.melvad:android-sdk:1.0.3`, with minimum API 29. Follow the root README for
the Maven repository and dependency setup. Declare the Activity in your app
manifest if you use it as a separate screen.

The example creates an ad host and waits for your app's consent flow. From your
CMP callback, call `showBanner(placementKey, privacy)` on the Activity's main
thread with the assigned `inventory_id#placement_id#client_id` key and an actual
`MelvadPrivacy` value. Do not start a request merely because the screen opened.

The Activity handles pause, resume, replacement and final destruction of its ad
view. It is the banner example from the root README, available as a Kotlin file
for use in an application. For other formats and rewarded callbacks, see the
[integration guide](https://melvad.com/developers?lang=en#android-sdk).

This source is an integration example. The SDK implementation remains in the
published AAR.
