Android app that acts as a shim for data uri handling when trying to launch the browser via an intent.

This app is a proof of concept only, I don't expect anyone to actually use it in production.

It would be nice if browsers registered for data URIs, then you could launch content without needing to be online.

![QR Code](https://raw.githubusercontent.com/madmaw/android-data-uri-service/master/qrcode.png)

If you scan the above QR code before you have installed the app, the device will probably complain. Afterward, it will launch a website, even if you aren't connected to the Internet.

You can download the latest build of the app from https://raw.githubusercontent.com/madmaw/android-data-uri-service/master/target/adus-1.0.apk

I didn't make the demo, but took it from here http://js1k.com/2014-dragons/demo/1664

I've logged a bug with Google to add data URI handling to Chrome for Android https://code.google.com/p/chromium/issues/detail?id=399814