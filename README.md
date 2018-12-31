# InstagramAuth

An Android library that makes it easy to authenticate your app users with Instagram.

[![](https://jitpack.io/v/nikolajakshic/instagramauth.svg)](https://jitpack.io/#nikolajakshic/instagramauth)

## Download

In your project's top-level `build.gradle` file, ensure that JitPack's Maven repository is included:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Then, in your app-level `build.gradle` file, declare `InstagramAuth` as a dependency:

```groovy
dependencies {
    implementation 'com.github.nikolajakshic:instagramauth:1.1.1'
}
```

If you are not using AndroidX, [migrate to it](https://developer.android.com/jetpack/androidx/migrate).

## Usage

Add `CLIENT_ID` and `REDIRECT_URI` to your `AndroidManifest.xml` file:

```xml
        . . .

        <meta-data
            android:name="com.nikola.jakshic.instagramauth.ClientId"
            android:value="YOUR_CLIENT_ID"/>

        <meta-data
            android:name="com.nikola.jakshic.instagramauth.RedirectUri"
            android:value="YOUR_REDIRECT_URI"/>
    </application>
</manifest>
```

Call `AuthManager.getInstance().login()` to launch  an `Activity` that will, using `WebView`, prompt the User
to log in with Instagram:

```java
AuthManager.getInstance().login(this, new AuthManager.LoginCallback() {
    @Override
    public void onSuccess() {
        // User successfully logged in, get him to the Home screen or something...
    }

    @Override
    public void onError(InstagramAuthException e) {
        if (e instanceof InstagramAuthAccessDeniedException) {
            // User denied access, do something...
        }

        if (e instanceof InstagramAuthNetworkOperationException) {
            // Network problem, do something...
        }
    }
});
```

Pass the `Activity`'s result back to the `AuthManager`:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    AuthManager.getInstance().onActivityResult(requestCode, resultCode, data);
}
```

Use the `AuthManager.getInstance().getUserInfoAsync()` (for synchronous execution, use `getUserInfo()`) method to request basic profile information for the currently signed in User:

```java
AuthManager.getInstance().getUserInfoAsync(new AuthManager.Callback<UserInfo>() {
    @Override
    public void onSuccess(UserInfo userInfo) {
        String id = userInfo.getId();
        String userName = userInfo.getUserName();
        String fullName = userInfo.getFullName();
        String photoUrl = userInfo.getPhotoUrl();

        . . .
    }

    @Override
    public void onError(InstagramAuthException e) {
        if (e instanceof InstagramAuthTokenException) {
            // Instagram expires the token after some period of time,
            // so we need to re-authenticate the User. When this happens,
            // library is automatically logging User out, i.e. the old token is removed
            // and AuthManager.getInstance().isLoggedIn() returns false.
            //
            // Prompt the User to log in again.
        }

        if (e instanceof InstagramAuthNetworkOperationException) {
            // Network problem, do something...
        }
    }
});
```

Optionally, customize library's `Activity` appearance by overriding the `InstagramAuthTheme` style in your `styles.xml` file:

```xml
    . . .

    <style name="InstagramAuthTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- set toolbar color -->
        <item name="colorPrimary">#f44336</item>
        <!-- set status bar color-->
        <item name="colorPrimaryDark">#d32f2f</item>
        <!-- set title -->
        <item name="instagram_auth_title">Login with Instagram</item>
    </style>
</resources>
```

## FAQ

Q: I get the error message `Implicit authentication is disabled`. What should I do?  
A: https://www.instagram.com/developer/clients/manage/ -> Manage -> Security -> Uncheck `Disable implicit OAuth`

## License

```
MIT License

Copyright (c) 2018 Nikola Jakšić

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
