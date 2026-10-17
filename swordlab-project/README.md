# SwordLab (Fabric 1.21.11, Mojang mappings)

Client-side test modules for validating a server anti-cheat: Trigger Bot, Silent Aim Assist, Auto Jump Reset.
Open the glass GUI with Right Shift (rebindable). Left-click a module to toggle, right-click to expand settings.

## Build on GitHub (no local setup)
1. Upload the files in this folder to the ROOT of a repo (build.gradle must be at the top level).
2. Actions tab -> build -> Run workflow.
3. Download the `swordlab-jar` artifact. (`gradle-wrapper-files` artifact = gradlew + gradle/wrapper, optional.)

## Build locally
JDK 21 + Gradle installed:  gradle build --no-daemon   ->  build/libs/swordlab-1.0.0.jar
