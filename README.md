# DripWire-style Android UI Rebuild

A clean Android project with a dark login/dashboard UI and a GitHub Actions workflow that builds a debug APK.

## Build on GitHub from a phone
1. Upload the contents of this folder to the root of a GitHub repository (keep `.github/workflows/build-apk.yml`).
2. Open Actions and run **Build DripWire Rebuild APK**.
3. Open the successful run and download artifact **DripWire-Rebuild-debug-apk**.
4. Extract the artifact ZIP and install `app-debug.apk`.

## Scope
This is an independent UI/demo recreation based on visible resources. It does not include the original protected native code, proxy/VPN tunnel, server endpoints, licensing, or floating overlay functionality. The Start/Stop button only changes local demo status.
