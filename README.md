# inputdemo
该项目演示了输入驱动的基本使用

##  硬件准备
在运行该项目之前，我们要准备如下元器件：
* 树莓派3开发板 1块
* 面板板 1块
* 按钮开关 2个
* 电阻 2个
* 杜邦线（母对公，公对公） 若干

>广告时间咯：如果你还没有自己的开发板和元器件，到我们的“1024工场微店”来逛逛一逛吧（文章底部二维码），这里能一次性有买到你想要的！

## 电路图
![电路图](img/circuit.png)

## 构建运行
1. 在Android Studio中，点击Run即可。
2. 如果你通过gradle命令进行构建，则：
```bash
gradle installDebug
adb shell am start com.chengxiang.inputdemo/.MainActivity
```

## 运行效果
点击开关按钮，在输入框中会输入对应的A和B字母。

![结果1](img/result1.jpg)

![结果2](img/result2.jpg)

* * * * *
*1.新技术，新未来！欢迎大家关注“1024工场”微信服务号，时刻关注我们的最新的技术讯息。*

![服务号](img/fuwuhao.jpg)     

*2.抛弃各种找元器件的烦恼，来“1024工场”微店，一次性买到你所想要的。*

![微店](img/weidian.jpg) 

*3.加入“Android Things开发”QQ讨论群，一起学习一起Hi。（甭客气！尽情的扫描或者长按！）*

![qq群](img/qq.png)         