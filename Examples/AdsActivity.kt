package com.melvad.examples

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
