# Android Surveys App

Welcome guys! This is a quick guide to help you get started.

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Project Board
Link to the project board: [Project Board](https://github.com/minhnimble/android-surveys-list/projects/1)

## Prerequisites

It's dangerous to go alone, make sure you have these! Follow the links to find out how to download and install them.

* [Android Studio](https://developer.android.com/studio) (at least Android Studio 4.0.0)
    * You'll probably want to install/update some plugins like the Simulator. Just open Android Studio and follow the prompts.
* [Bundler](https://help.dreamhost.com/hc/en-us/articles/115001070131-Using-Bundler-to-install-Ruby-gems)
    * Check that you successfully installed bundler by doing `bundle -v` anywhere in Terminal. This command should display the Bundler version. If it doesn't check the Troubleshooting guide below.
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html)
    * Select the installer that corresponds to your machine OS
* [Xcode Command Line Tools and Fastlane](https://docs.fastlane.tools/getting-started/android/setup/)
    * Just follow the **Installing fastlane** section
    * Check if you've installed Fastlane correctly by executing `fastlane -v`
    * Check if your Bundler works with Fastlane by executing `bundle exec fastlane -v`

## Installation

In your local machine, navigate to a folder you want to work from. Personally, I use `~/Documents/apps/`. So for example, I would do:

```
$ mkdir ~/Documents/apps/
$ cd ~/Documents/apps/
```

Then do a `git clone`. Remember that this command creates a folder called `android_surveys_list` so you **don't** have to create the folder first.

```
(apps) $ git clone git@github.com:minhnimble/android-surveys-list.git android_surveys_list
```

While you wait for the project to download, go make yourself a cup of coffee. ^^

## Gradle Sync

Open the android project and click on the Sync Project with Gradle Files button on the top right corner. If you can't find it, there's a useful shortcut `CMD + SHIFT + A` (use CTRL for non-Mac) to pull up the Actions search. Type in "sync" and that should be the top hit.

## Running Tests

To run tests in Android Studio, hit `CTRL + SHIFT + R`.

To run tests using Fastlane, enter this command in Terminal:

```
$ (master) bundle exec fastlane android test
```

Protip: If `bundle exec` doesn't work, you can still just run
```
$ (master) fastlane android test
```

Fastlane just recommends that you use `bundle exec` to execute fastlane commands.

If everything works out, you should see an output like this:
```
+------+------------------+-------------+
|           fastlane summary            |
+------+------------------+-------------+
| Step | Action           | Time (in s) |
+------+------------------+-------------+
| 1    | default_platform | 0           |
| 2    | clean test       | 63          |
+------+------------------+-------------+

fastlane.tools finished successfully 🎉
```

## Releasing Firebase

#### Staging & Production Builds ####

To release Staging and Production builds to **Firebase Distribution**, we create a release branch, named in the format of `release_[env_lowercase]_firebase/[version_number]`.

##### Releasing steps #####

1. Create a release branch from `master` in this format `release_[env_lowercase]_firebase/[version_number]`, and don't push the branch to `origin` yet

2. Commit the `version_number` change if needed in this branch

2. Now, push the branch to `origin`, so that Bitrise can hook and proceed
> ⚠️ **Warning** - Please note that every commit pushed to `origin` on `release_[env_lowercase]_firebase/[version_number]` will get hooked by Bitrise, which will trigger the release process again, so please make sure all the needed changes is ready in `local` git first before pushing to `origin`. Once the branch is pushed, no more commits should be allowed

3. Wait for the workflow to finish

4. Review and merge `release_[env_lowercase]_firebase/[version_number]` to `master` PR

## Final Note
If you still have issues setting up your environment, contact me for help over Slack. My name is Mikey on Slack ^^
