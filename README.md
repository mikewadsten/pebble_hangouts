Pebble Hangouts Fix
===================

In which I resolve [this issue][pebble_issue].


How?
----

Using the [Android NotificationListenerService API][nlsapi], and a little help from
[Ketan Parmar's example code][kpbird], I got the basic proof-of-concept running.

Then I had a bit more work to do, because of some typecasting issues.

Should-be-signed APK available [here][apk]. (Note: This may be out of date.)

[pebble_issue]:  http://forums.getpebble.com/discussion/9916/colon-in-hangouts-notifications-cutting-off-message
[nlsapi]: https://developer.android.com/reference/android/service/notification/NotificationListenerService.html
[kpbird]: http://www.kpbird.com/2013/07/android-notificationlistenerservice.html
[apk]: https://dl.dropboxusercontent.com/u/1179152/pebble_hangouts.apk


License
=======

Made available under the MIT License. Do whatever you want with this.
