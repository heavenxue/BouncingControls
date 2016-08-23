### BouncingMenu

一款弹跳菜单控件

>**弹跳菜单控件**是一款专门脱离于Activity的一个自定义控件。特点概述：
 
- **动态加载** ：不用再XML布局文件中生命定义；
- **框架化** ：可以直接像*Toast.makeText(getBaseContext(),"",Toast.LENGTH_SHORT).show()*，利用了建造者模式来调用；
- **动画美感** ：利用了属性动画。

### 效果图：
![github](https://github.com/heavenxue/BouncingMenu/raw/master/doc/shoot.png "github")

### 使用
``` java
BoucingMenu.make(getBaseContext(),main_layout,R.layout.menu_boucing).show();
```

### 引申
本例最主要的是如何得到`DecorView`，通过android源码我们知道`DecorView`是`Activity`布局中最外层的一个布局，而我们真正意义上的xml中书写的布局都是它的Child<\br>
    
画出的曲线用到的是贝塞尔曲线
    