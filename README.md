Android-SwitchIcon
================

### This library is Java version of https://github.com/zagum/Android-SwitchIcon

[![](https://jitpack.io/v/zagum/Android-SwitchIcon.svg)](https://jitpack.io/#zagum/Android-SwitchIcon)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)

Google launcher-style implementation of switch (enable/disable) icon

![image](https://github.com/zagum/Android-SwitchIcon/blob/master/art/sample.gif)

Compatibility
-------------

This library is compatible from API 15 (Android 4.0.3).

Usage
-----

SwitchIconView extends from AppCompatImageView so you can set icon with  ```android:src```

Set any icon (vector or image) to SwitchIconView and enjoy switchable icon in your app :)

Use ```app:si_tint_color``` to set color to icon. Default color is black;

Use ```app:si_disabled_color``` to set color when icon disabled. Default color is equals with ```app:si_tint_color```;

Use ```app:si_disabled_alpha``` to set alpha when icon disabled. Default alpha is ```.5```;

Use ```app:si_no_dash``` if you don't want to draw dash, when icon disabled;

Use ```app:si_animation_duration``` if you want to change switching state animation duration;

Use ```app:si_enabled``` to set initial icon state;

Fully customized implementation:

```xml
    <com.liner.switchicon.SwitchIcon
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="8dp"
        app:si_animation_duration="500"
        app:si_disabled_alpha=".3"
        app:si_disabled_color="#b7b7b7"
        app:si_tint_color="#ff3c00"
        app:si_enabled="false"
        app:si_no_dash="true"
        app:srcCompat="@drawable/ic_cloud"/>
```

Public methods and fields: 

```java

public boolean isIconEnabled()  // Reurn icon state
public void setIconEnabled(boolean iconEnabled, boolean animate) // change icon state
public void switchState(boolean animate) // switch icon state
  
```

See [sample](https://github.com/LinerSRT/SwitchIcon/tree/master/app) project for more information.