APP_ABI := all
APP_STL := gnustl_static
APP_CPPFLAGS := -std=gnu++11 -fexceptions -D__STDC_LIMIT_MACROS
NDK_TOOLCHAIN_VERSION = 4.9
APP_PLATFORM := android-15
#指定支持的ABI平台。上面所示为armeabi-v7a, 可选的值有all (代表全平台)、arm64-v8a、x86、x86_64
#APP_ABI = armeabi-v7a x86 arm64-v8a
APP_ABI = armeabi-v7a