# Android - Floating Action Button (FAB)
<img src="http://kmdev.se/img/gh-fab.png" width="300" height="200" />

Simple android view representing a floating action button. It's very basic at the moment and I will update it with proper animations as soon as possible. 

###How to use

Add it to your XML like this (or programmatically by code). It will automatically default to the size recommended in android documentation, 56x56.

```
<se.kmdev.android_fab.fab.FloatingActionButton
        android:id="@+id/floatingActionButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```

You can add a touch listener by using..
```
public void addOnFloatingActionButtonPressedListener(OnFloatingActionButtonPressedListener onFloatingActionButtonPressedListener)
```

Attach to a current ABSListView (ListView or GridView) for a sweet hide/reveal animation by calling..
```
public void attachToListView(AbsListView listView)
```

.. and change the center icon by using
```
public void setCenterIconResource(int resourceID)
public void setCenterIconBitmap(Bitmap bitmap)
```

[![Demo CountPages alpha](http://share.gifyoutube.com/y0ROMV.gif)](https://www.youtube.com/watch?v=Y0ujkwBY318)

Thats it for now.
