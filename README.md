# Module Health Dashboard

This is a hack week project showing important metrics about our codebase. The code uses Jetpack Compose for Desktop. 

Let me be clear: it was a hack week project and the code looks like that or even worse: lots of duplication, bad algorithms, confusing naming, no proper encapsulation, bad to no patterns, etc. The code was written for a 5 minute demo.

Highlights from the demo:
* Custom table with sticky headers and a sticky first row
* Rows are lazily rendered
* Scrolling in cells if the content doesn't fit
* Fading edges for a better scroll experience
* Custom sorting for columns
* Scrollbars (well, because everything is custom)

You can run the project with
```kotlin
./gradlew run
```

![Screenshot](screenshot.png?raw=true "Screenshot")