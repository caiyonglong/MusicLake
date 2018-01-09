1、ViewPager中fragment A，点击跳转切换到另外一个Fragment B(addFragment 添加B),
然后返回到A(回退栈顶的A)。A中的数据不刷新。尝试了在onResume中,添加更新,但是效果还是一样。

解决办法,引入事件总线RxBus.

2、适配Android 8.0 出现
java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation

只有全屏不透明的活动才能请求方向.主要是manifest中定义了android:screenOrientation="portrait" 和设置半透明主题导致。
            
            
  

