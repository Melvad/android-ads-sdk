# Distributed runtime reference

`Android/` contains byte-for-byte copies of the JavaScript and HTML runtime files
already distributed inside the published Android SDK 1.0.3 AAR. The original
artifact remains in `maven/`; its packaging and checksums are unchanged.

These files let GitHub inspect the distributed text assets, alongside the public
Kotlin API signatures, as it does for the unpacked iOS SDK. The language percentages
describe the text and interfaces in this public distribution, not a source-code
composition report. TypeScript is compiled to JavaScript in the shipped runtime.

Original third-party notices remain in [`../Licenses`](../Licenses). File provenance
and SHA-256 checksums are recorded in `manifest.json`. Implementation project
sources and source maps are not included.
