1、ViewPager中fragment A，点击跳转切换到另外一个Fragment B(addFragment 添加B),
然后返回到A(回退栈顶的A)。A中的数据不刷新。尝试了在onResume中,添加更新,但是效果还是一样。

解决方案,1、引入事件总线RxBus.2、方法回调

2、适配Android 8.0 出现
java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation

只有全屏不透明的活动才能请求方向.主要是manifest中定义了android:screenOrientation="portrait" 和设置半透明主题导致。
            
3、使用Rxbus时，退出Activity。然后从通知栏进入程序崩溃。

4、Retrofit gson解析数据QQ音乐歌词数据时，抛出异常，因为返回的数据不是一个完整的json数据。
```
  io.reactivex.exceptions.OnErrorNotImplementedException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
  ....
```
返回数据格式jsonp格式：MusicJsonCallback({...})。
通过下面的解决方法，不会抛出异常。但是获得的String 数据只有MusicJsonCallback( .这部分
重要部分不知道去哪了...
```
Gson gson = new GsonBuilder()
        .setLenient()
        .create();

Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
```

不得已，最后尝试重写GSON转换器。遇到一个大坑那就是ResponseBody //ResponseData中的流只能使用一次，我们先将流中的数据读出在byte数组中。这个方法中已经关闭了ResponseBody,所以不需要再关闭了  

5、Observable<T> 转换成 Observable<S> 

```
//lamba表达式
Observable.flatMap(T -> {
    return Observable.fromArray(S);
}
//Java表达式
Observable.flatMap(new Function<T, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(T tt) throws Exception {
                        return null;
                    }
                });
```

6、Retrofit 多个网络请求合并使用
```
 Observable.merge(QQApiServiceImpl.search(key, limit, page), XiamiServiceImpl.search(key, limit, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> results) {
                        mView.showSearchResult(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showEmptyView();
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
               
```
7、封装MediaPlayer，prepareAsync()装载异常，还有getDuration会出现数据异常
```
    //异步装载数据
    player.prepareAsync();
    //设置异步装载完毕监听事件
    player.setOnPreparedListener(this);
    //设置异步装载进度
    player.setOnBufferingUpdateListener(this);
```
8、ViewPager中 多点触碰导致
```
java.lang.IllegalArgumentException: pointerIndex out of range
    at android.view.MotionEvent.nativeGetAxisValue(Native Method)
                      
```
原因分析：
在LrcView中,因为调用了requestDisallowInterceptTouchEvent来区分左右滑动和上下滑动，
所以会导致父类的view多点触摸有些情况下会出现数组溢出的情况.
解决办法
- 利用try{}catch(){}抛出异常
- 重写ViewPager中的onTouchEvent 和onInterceptTouchEvent。
```
public class MultiTouchViewPager extends ViewPager {

    public MultiTouchViewPager(Context context) {
        super(context);
    }

    public MultiTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
```

9、InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.


10、重构数据库，当插入十几条数据或更多时，其中重复的数据更新数据库，不重复的添加进去，能不能用一条语句实现？

11、再次设计数据库，播放队列，播放历史设计成一种特殊的歌单数据。

12、增加setNextMediaPlayer方法时，抛出异常
E/MediaPlayer: next player is not prepared

13、Observable.flatmap 操作符的使用

14、Attempt to invoke interface method 接口
下载服务接口更新下载状态。

15、AudioManager控制播放

16、播放暂停恢复，音量变化

17、线控

18、对于ViewPager+Fragment+Tablayout中 懒加载优化。

19、getwidth()和getheight()已经过时使用point
//屏幕宽高
   
         Displaydisplay = getWindowManager().getDefaultDisplay();
   
         Pointsize = newPoint();
   
         display.getSize(size);
   
         width = size.x;
   
         height = size.y;
   
 20、Warning: com.tencent.smtt.export.external.DexLoader: can't find referenced class dalvik.system.VMStack
 混淆问题
 ```
 -dontwarn com.tencent.smtt.**
 -keep class com.tencent.smtt.** { *; }
```