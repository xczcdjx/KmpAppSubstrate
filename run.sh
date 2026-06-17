# desktop 热重载
./gradlew :desktopApp:hotRun --auto
# desktop run
./gradlew :desktopApp:run
# 静态资源生成
./gradlew :shared:genComposeRes
# Android Debug
./gradlew :androidApp:installDebug && \
adb shell am start -n com.djx.rerainkmp/.MainActivity
# Ios
#./gradlew :iosApp:embedAndSignAppleFrameworkForXcode
#open iosApp/iosApp.xcodeproj