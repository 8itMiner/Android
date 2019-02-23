<img src="https://github.com/MyNSB/Android/blob/master/app/src/main/res/mipmap-xxxhdpi/mynsb_logo.png" width="200"/> &nbsp; 
# &nbsp;&nbsp; MyNSB Android

[![Code Climate](https://img.shields.io/codeclimate/maintainability/MyNSB/Android.svg?style=flat-square)](https://codeclimate.com/github/MyNSB/Android)
[![license](https://img.shields.io/github/license/MyNSB/Android.svg?style=flat-square)]()
[![GitHub release](https://img.shields.io/github/release/MyNSB/Android.svg?style=flat-square)]()
[![GitHub contributors](https://img.shields.io/github/contributors/MyNSB/Android.svg?style=flat-square)](https://github.com/MyNSB/API)



This is the Android version of the myNSB application. It features all the features supported by all other myNSB apps.

## Setup
Setup of the Application requires opening this project in Android studio and executing all the Gradle Scripts (Note: If using Android Studio this is done for you automatically). After the environment has been setup ensure that you set up an emulated device to test the application on, for more information about this please click [here](https://developer.android.com/studio/run/device)



## Developing
Contribution is simple, all files are located in the `app/src/main` folder    
 - <b>IDE Setup</b>
    - The perfered IDE for development is Android Studio and can be downloaded from [here](https://developer.android.com/studio)
    - Do also note that the Firebase API key has been excluded from the build so in order to contribute code you must either test the application on your personal Firebase Account or disable all Firebase realted features in the application. This can be easilly acheived by editing the `App.java` file.
 - <b>Testing</b>
    - Before creating a pull request ensure that you have tested your new feature/improvements on a variety of devices, both emulated and physical. 
 - <b>Contributing</b>
    - When developing ensre you fork the `public` branch and all pull requests are made to the public branch. Each pull request will be reviewed and after review accepted and rebased with the master branch
 
 
## Current Features
The full list of implemented features are:
 - Notifications
 - Reminders
 - Timetables
 - Events
 - Calendars
 - 4U Paper
