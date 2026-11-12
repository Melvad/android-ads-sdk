# Melvad Android Ads SDK

Binary distribution of Melvad Android Ads SDK **1.0.3** for Android 10 (API 29) and later.

The repository contains the prebuilt AAR, public Maven metadata, documentation and
licenses. SDK implementation sources and source maps are not distributed.

Native Open Measurement 1.6.12 is included for display and video ads. No additional OMID integration is required in the publisher app.

Rewarded video supports explicit `prepareAd` / `isReady` / `show` and native once-only reward callbacks. Optional supplier-VAST SSV requires a separately configured backend; local reward is not server credit. See the integration guide for the contract and examples.

## Installation

Use an Android project with a Kotlin 2.2.10-compatible compiler, compile SDK 37 or later, Java 11 or
later bytecode support, and Android System WebView with WebMessageListener support.
Build your app with a JDK supported by your Android Gradle Plugin.

Add the version-pinned public Maven repository to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://raw.githubusercontent.com/Melvad/android-ads-sdk/1.0.3/maven")
            content { includeGroup("com.melvad") }
        }
    }
}
```

Add the SDK to your app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.melvad:android-sdk:1.0.3")
}
```

Set `minSdk = 29` or later. Merge these entries into your existing configuration.
No GitLab account, access token, Node.js installation or SDK source build is needed.
Gradle resolves the native Prebid Mobile dependency and the other runtime dependencies
from Maven Central and Google's Maven repository. Do not add a second standalone
Prebid binary or substitute a JavaScript Prebid build.

## Display a banner

Use the placement key from Melvad in the format
`inventory_id#placement_id#client_id`. Match the placement's configured format.
The following Activity owns one banner and does not make automatic ad requests:

```kotlin
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.melvad.sdk.*

class AdsActivity : Activity() {
    private lateinit var sdk: MelvadSdk
    private lateinit var host: FrameLayout
    private var ad: MelvadAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sdk = MelvadSdk(this)
        host = FrameLayout(this)
        setContentView(host)
    }

    // Call from your CMP callback after the app permits an ad request.
    fun showBanner(placementKey: String, privacy: MelvadPrivacy) {
        ad?.destroy()
        host.removeAllViews()
        val view = sdk.createAdView(this)
        ad = view
        host.addView(view, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        view.listener = object : MelvadAdListener {
            override fun onLoaded(view: MelvadAdView) {
                Log.d("Melvad", "Creative loaded")
            }
            override fun onImpression(view: MelvadAdView) {
                Log.d("Melvad", "Visible impression")
            }
            override fun onError(view: MelvadAdView, error: MelvadAdError) {
                Log.e("Melvad", "Ad result: " + error.code)
            }
        }
        view.post {
            if (ad === view) view.loadAd(MelvadAdRequest(
                placement = MelvadPlacement.parse(placementKey),
                format = MelvadAdFormat.BANNER,
                privacy = privacy
            ))
        }
    }

    override fun onPause() { ad?.pause(); super.onPause() }
    override fun onResume() { super.onResume(); ad?.resume() }
    override fun onDestroy() { ad?.destroy(); ad = null; super.onDestroy() }
}
```

From your app's resolved consent flow, call `showBanner(placementKey, privacy)` on
the main thread. Create `MelvadPrivacy` from the actual CMP values:
`gdprApplies`, `tcString` and `limitAdTracking`. The last flag controls use of the
advertising ID and does not decide whether an ad request is permitted. The default
limits advertising ID use. The SDK supplies the app package name and its Google Play URL.

## Formats and lifecycle

Supported formats include fixed-size banners, interstitial banners, adhesion,
expandable banners, native image/video cards, inline video, inread, inslide,
infeed, instream, fullscreen video and rewarded video. Banners and native cards
use show.js; video uses vpaid.js. Native cards are rendered HTML cards.

Keep a reference to each ad view. Replacing a load cancels its previous request.
Use `pause()` / `resume()` for hosts without a ViewTreeLifecycleOwner and call
`destroy()` when removing the slot permanently. A view with a lifecycle owner
also follows that lifecycle. In a Fragment, release the view in `onDestroyView()`.
Do not load on every Compose recomposition or reuse a destroyed view.

`onLoaded` and `onImpression` are separate events. Apply a reward only in
`onRewardEarned`; early close, error and no-fill do not grant a reward.
Use an Activity context and a window-sized host for fullscreen formats.
Test your placements and app lifecycle on physical devices before publishing.

## Documentation

[Integration guide and examples for all supported formats](https://melvad.com/developers?lang=en#android-sdk).

## Downloads and verification

[Release 1.0.3](https://github.com/Melvad/android-ads-sdk/releases/tag/1.0.3)
contains the binary distribution and checksums. The Maven layout is also available
in [maven/com/melvad/android-sdk/1.0.3](maven/com/melvad/android-sdk/1.0.3).
Use the Maven dependency for normal integration: adding only a local AAR does not
install its transitive dependencies. `SHA256SUMS` verifies the repository distribution.

## License

[Melvad SDK License](LICENSE), identical to the license supplied with the Melvad iOS SDK.
Included third-party components retain their [own licenses](Licenses/NOTICE).
