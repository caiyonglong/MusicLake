# MusicLake [![Build Status](https://travis-ci.org/caiyonglong/MusicLake.svg?branch=develop)](https://travis-ci.org/caiyonglong/MusicLake)

# 免责声明
- 音乐湖只用作个人学习研究，禁止用于商业及非法用途，如产生法律纠纷与本人无关
- 音乐api来自于github，非官方版api，本软件不提供任何音频存储服务，如需下载音频，请支持正版！。
- 音乐版权归各网站所有，本站不承担任何法律责任和连带责任。如果已经涉及到您的版权，请速与本站管理员联系，我们将第一时间为你处理。

# 功能版本
- Android 音乐播放器 （最低支持Android版本5.0）
- 本地音乐播放，百度音乐，QQ音乐、虾米音乐、网易云音乐等网络音乐在线播放
- 歌词播放、桌面歌词、桌面小控件
- 通知栏控制、线控播放、音频焦点控制
- QQ登录、在线歌单同步
- 百度电台列表，网易云mv排行榜，mv播放评论
- 酷狗歌词搜索、修改歌词样式
- 精力有限，需求不饱和都会逐步完善，有兴趣可赏个star静等完善与bug修复，期望的功能也可提[issues](https://github.com/caiyonglong/MusicLake/issues)
- [PC端](https://github.com/sunzongzheng/music)
- [版本计划](https://github.com/caiyonglong/MusicLake/projects)
- [下载页](https://github.com/caiyonglong/MusicLake/releases)
- [蒲公英下载](https://www.pgyer.com/ZIWH)


# 更新日志

## v4.1.6.beta
1、增加socket音乐社区（支持实时在线人数统计、聊天室、分享歌曲）
2、增加下载限制（自动过滤有版权限制的歌曲）
3、更新qq三方登录sdk,支持无qq手机扫码登录
4、增加本地歌曲删除功能
5、增加qq音乐榜单
6、增加歌词翻译（基本是翻译成中文，部分歌曲没有）
7、修复一些bugs(适配8.0悬浮窗、下载过程中退出无法继续下载)
- 增加热更新
- 优化侧边栏功能显示

## v4.1.5
- 多种机型的悬浮窗权限适配
- 修复bugly bugs

## v4.1.4
- 支持导入外部歌单（网易云、qq、虾米。分享链接格式需要注意，例如qq不支持短连接解析，有些歌单没有复制链接按钮，需要分享给qq好友后，复制分享后的链接，链接形式<http://y.qq.com/...id=1158454608&...> ）
- 支持批量添加在线歌曲到歌单
- 优化歌单操作、播放页增加歌曲操作
- 主页懒加载
- 修复一些bugs

## v4.1.3
- 修复在线歌单歌曲播放音频不一致的问
- 修复崩溃bug

## v4.1.2
- Material Design 设计优化UI
- 优化歌曲文件夹列表
- 修改网易云排行榜接口，增加热搜接口
- 分离排行榜、增加网友精选歌单
- 修复下载进度条不显示bugs，以及修改MP3标签
- 优化豆瓣专辑图片访问次数限制问题

## v4.1.1
- 优化网易云、qq、虾米专辑图片尺寸
- 修改歌单详情页
- 优化内存，修复已知bugs

## v4.1.0
- 新增mv排行榜，最新mv，支持mv播放，横竖屏切换
- 优化在线歌曲下载管理
- 新增最热歌手列表、电台列表
- 新增歌词搜索，优化歌词同步
- 新增锁屏播放，修复线控bug、
- 均衡器（仅支持系统自带均衡器）

## 更早
- 本地音乐播放，百度音乐，QQ音乐、虾米音乐、网易云音乐等网络音乐播放
- 支持在线歌曲下载
- 歌词播放、桌面歌词、桌面小控件
- 通知栏控制、线控播放、音频焦点控制
- QQ登录、在线歌单同步
- 百度电台列表，网易云mv排行榜，mv播放评论


# 相关项目|音乐API
- [音乐API](https://github.com/sunzongzheng/musicApi)
- [云歌单API](https://github.com/sunzongzheng/player-be)
- [NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)


# 第三方库

- [rxjava](https://github.com/ReactiveX/RxJava)
- [retrofit](https://github.com/square/retrofit)
- [dagger2](https://github.com/google/dagger)
- [Glide](https://github.com/bumptech/glide)
- [LitePal](https://github.com/LitePalFramework/LitePal)
- [DSBridge](https://github.com/wendux/DSBridge-Android)
- [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [More..](https://github.com/caiyonglong/MusicLake/blob/develop/app/build.gradle)

# 软件运行截图
<p>
<img src="screenshots/preview1.png" width="225" height="400"/>
<img src="screenshots/preview2.png" width="225" height="400"/>
<img src="screenshots/preview3.png" width="225" height="400"/>
<img src="screenshots/preview4.png" width="225" height="400"/>
<img src="screenshots/preview5.png" width="225" height="400"/>
<img src="screenshots/preview6.png" width="225" height="400"/>
<img src="screenshots/preview7.png" width="225" height="400"/>
<img src="screenshots/preview8.png" width="225" height="400"/>
<img src="screenshots/preview9.png" width="225" height="400"/>
<img src="screenshots/preview10.png" width="225" height="400"/>
<img src="screenshots/preview11.png" width="225" height="400"/>
<img src="screenshots/preview12.png" width="225" height="400"/>
<img src="screenshots/preview15.png" width="225" height="400"/>
</p>
