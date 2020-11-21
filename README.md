DanmakuFlameMaster
==================

# 已弃用
使用 [DanmakuView](https://github.com/duzhaokun123/DanmakuView)

android上开源弹幕解析绘制引擎项目。

### DFM Inside: 
[![bili](https://raw.github.com/ctiao/ctiao.github.io/master/images/apps/bili.png?raw=true)](https://play.google.com/store/apps/details?id=tv.danmaku.bili)

- libndkbitmap.so(ndk)源码：https://github.com/Bilibili/NativeBitmapFactory
- 开发交流群：314468823 (加入请注明DFM开发交流) (不由我(duzhaokun123)维护)

### Features

- 使用多种方式(View/SurfaceView/TextureView)实现高效绘制
- B站xml弹幕格式解析
- 基础弹幕精确还原绘制
- 支持mode7特殊弹幕
- 多核机型优化，高效的预缓存机制
- 支持多种显示效果选项实时切换
- 实时弹幕显示支持
- 换行弹幕支持/运动弹幕支持
- 支持自定义字体
- 支持多种弹幕参数设置
- 支持多种方式的弹幕屏蔽

### TODO:

- 增加OpenGL ES绘制方式
- ~~64位原生库~~ 因为 https://github.com/bilibili/DanmakuFlameMaster/blob/e2846461a09e33720a049f628f09c653f55531f0/DanmakuFlameMaster/src/main/java/tv/cjump/jni/NativeBitmapFactory.java#L38
在 API >= 23 的设备上没有必要

### Download (不建议)

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.github.ctiao:DanmakuFlameMaster:0.9.25'

    //只在 API < 23 的设备上可用
    implementation 'com.github.ctiao:ndkbitmap-armv7a:0.9.25'
    implementation 'com.github.ctiao:ndkbitmap-armv5:0.9.25'
    implementation 'com.github.ctiao:ndkbitmap-x86:0.9.25'
}
```

### License
    Copyright (C) 2013-2015 Chen Hui <calmer91@gmail.com>
    Licensed under the Apache License, Version 2.0 (the "License");

