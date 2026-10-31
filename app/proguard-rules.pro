# Add project specific ProGuard rules here.
-keep class com.hograna.liga.data.local.entity.** { *; }
-keep class com.hograna.liga.domain.model.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# Hilt
-dontwarn com.google.dagger.**
-keep class dagger.hilt.** { *; }
