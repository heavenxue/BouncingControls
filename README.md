### Bouncing

自定义动画控件

>**弹跳菜单控件**是一款专门脱离于Activity的一个自定义控件。特点概述：
 
 ####BoucingMemu
- **动态加载** ：不用再XML布局文件中生命定义；
- **框架化** ：可以直接像`Toast.makeText(getBaseContext(),"",Toast.LENGTH_SHORT).show()`，利用了建造者模式来调用；
- **动画美感** ：利用了属性动画。

#### BouceProgressBar
- **动画美感** ：利用了属性动画。见下面图

>**上下跳动的小球控件**令小球在绳子上面跳动自定义控件。特点概述：
- **动画美感** ：利用了属性动画。
- **性能** ：利用surfaceView性能更好(高效绘制，具有缓冲性能)


### 效果图：
![github](https://github.com/heavenxue/BouncingMenu/raw/master/doc/shoot.png "github")
![BouceProgressBar.gif](http://upload-images.jianshu.io/upload_images/1628151-9a7141647fcf97ea.gif?imageMogr2/auto-orient/strip)