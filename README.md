[![](https://jitpack.io/v/AndroidCrafts-man/RenderSpy.svg)](https://jitpack.io/#AndroidCrafts-man/RenderSpy)

## Render Spy

Lightweight library designed to measure and collect performance metrics for Android applications in production.

 it focuses only on collecting pure metrics and not enforces you to use specific reporting channel and monitoring infrastructure, so you're flexible with re-using the monitoring approaches already existing in your product.

## Gradle Integration

### Step A: Add Maven Repository
In your project-level build.gradle or settings.gradle file, add the JitPack repository:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step B: Add Dependencies [![](https://jitpack.io/v/AndroidCrafts-man/RenderSpy.svg)](https://jitpack.io/#AndroidCrafts-man/RenderSpy)
Next, include the library in your app-level build.gradle file. Replace x.x.x with the latest version

```groovy
dependencies {
  implementation 'com.github.AndroidCrafts-man:RenderSpy:Tag'
}
```

### Getting started
The library supports collecting the following performance metrics:

* App Cold Startup Time
* Rendering performance per Activity
* Time to Interactive & Time to First Render per screen

### Collecting Startup Times
Implement the callback invoked once Startup Time is collected:

```
class MyStartupTimeListener : AppStartupTimeTracker.Listener {

    override fun onColdStartupTimeIsReady(
        startupTime: Long,
        firstActivity: Activity,
        isActualColdStart: Boolean
    ) {
        // Log or report Startup Time metric in a preferable way
    }
}
```
Then register your listener as early in Application#onCreate as possible:

```
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppStartupTimeTracker.register(this, MyStartupTimeListener())
    }
}
```

### Collecting Frame Metrics
Implement the callback invoked every time when the foreground Activity is paused (we can call it "the end of the screen session") and use RenderingMetricsMapper to represent rendering performance metrics in a convenient aggregated format:
```
class MyFrameMetricsListener : ActivityFrameMetricsTracker.Listener {

    override fun onFramesMetricsReady(
        activity: Activity,
        frameMetrics: Array<SparseIntArray>,
        foregroundTime: Long?
    ) {
        val data = RenderingMetricsMapper.toRenderingMetrics(frameMetrics, foregroundTime) ?: return
        // Log or report Frame Metrics for current Activity's "session" in a preferable way
    }
}
```
Then register your listener in Application#onCreate before any activity is created:
```
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityFrameMetricsTracker.register(this, MyFrameMetricsListener)
    }
}
```
As shown in the code sample above, you can use RenderingMetricsMapper to collect frames metrics in the aggregated format which is convenient for reporting to the backend. Then metrics will be represented as RenderingMetrics instance, which will provide data on:

totalFrames - total amount of frames rendered during the screen session
totalFreezeTimeMs - total accumulated time of the UI being frozen during the screen session
slowFrames - amount of slow frames per screens session
frozenFrames - amount of frozen frames per screens session
Even though we support collecting widely used slow & frozen frames we strongly recommend relying on totalFreezeTimeMs as the main rendering metric

### Collecting Screen Time to Interactive (TTI)
Implement the callbacks invoked every time when screen's Time To Interactive (TTI) & Time To First Render (TTFR) metrics are collected:

```
object MyTtiListener : BaseTtiTracker.Listener {

    override fun onScreenCreated(screen: String) {}

    override fun onFirstFrameIsDrawn(screen: String, duration: Long) {
        // Log or report TTFR metrics for specific screen in a preferable way
    }
    override fun onFirstUsableFrameIsDrawn(screen: String, duration: Long) {
        // Log or report TTI metrics for specific screen in a preferable way
    }
}

```

Then instantiate TTI tracker in Application#onCreate before any activity is created and using this listener:

// keep instances globally accessible or inject as singletons using any preferable DI framework
val ttiTracker = BaseTtiTracker(AppTtiListener)
val viewTtiTracker = ViewTtiTracker(ttiTracker)

```
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityTtfrHelper.register(this, viewTtiTracker)
    }
}
```

That will enable automatic TTFR collection for every Activity in the app. For TTI collection you'll need to call viewTtiTracker.onScreenIsUsable(..) manually from the Activity, when the meaningful data is visible to the user e.g.:

// call this e.g. when the data is received from the backend,
// progress bar stops spinning and screen is fully ready for the user

viewTtiTracker.onScreenIsUsable(activity.componentName, rootContentView)
See the SampleApp for a full working example

Collecting TTI/TTFR for Fragment-based screens in single-Activity apps
The example above works for Activity-based screens, however if you use the "Single-Activity" approach you also need to enable TTI/TTFR tracking for the Fragments inside you main Activity:

```
class MyMainActivity : Activity() {

    override fun onCreate() {
        super.onCreate()
        val fragmentHelper = FragmentTtfrHelper(viewTtiTracker)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentHelper, true)
    }
}
```
Then you can call viewTtiTracker.onScreenIsUsable(..) in Fragments the same way as described above.


## Acknowledgements
<img src="https://github.com/user-attachments/assets/a2d52d25-4607-42d0-86a1-7976d5b94bf0" alt="WhatsApp Image" width="300"/>
