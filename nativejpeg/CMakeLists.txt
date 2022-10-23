# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)


project("nativejpeg")

#set(CMAKE_VERBOSE_MAKEFILE  on)
set(ANDROID_ABI "arm64-v8a")
# 设置so输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/libs/${ANDROID_ABI})

# 初始化目录变量
set(NATIVE_SOURCE_DIR ${CMAKE_SOURCE_DIR}/src/main)
# so库文件目录
set(libjpegPath ${NATIVE_SOURCE_DIR}/jniLibs)
# 头文件目录
set(INC_DIR ${NATIVE_SOURCE_DIR}/cpp/include )

message("头文件目录：${INC_DIR}")

# 添加头文件目录
include_directories( ${INC_DIR})

# 设置编译标志
set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")
# Declares and names the project.


# 配置、链接动态库
add_library(
        nativejpeg
        SHARED
        src/main/cpp/nativejpeg.cpp)

#导入第三方库 libjpeg.so
add_library(libjpeg SHARED IMPORTED)
set_target_properties(libjpeg PROPERTIES IMPORTED_LOCATION ${libjpegPath}/${ANDROID_ABI}/libjpeg.a)

message("三方库地址：${libjpegPath}/${ANDROID_ABI}/libjpeg.a")


# 查找NDK原生库log，android
find_library( # Sets the name of the path variable.
        log-lib
        log)
find_library(android-lib android)
find_library(graphics jnigraphics)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
# 链接所有库到nativejpeg
target_link_libraries( # Specifies the target library.
        nativejpeg
        # Links the target library to the log library
        # included in the NDK.
        libjpeg
        ${graphics}
        ${log-lib}
        ${android-lib})