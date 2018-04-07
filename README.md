# MusicLake
Android 音乐播放器，基于MVP + Retrofit + Rxjava2 框架。代码重构了一次，还是发现还有很多不足的地方。

# 已完成功能

- [x] 本地听歌。按专辑、歌手、文件夹分类
- [x] 在线听歌。百度音乐，QQ音乐、虾米音乐、网易云音乐
- [x] 本地歌单
- [x] 播放历史
- [x] 播放队列
- [x] 网络歌曲下载
- [x] 播放歌词、桌面歌词
- [x] 通知栏控制，线控播放
- [x] 音频焦点控制
- [x] QQ登录、在线歌单同步

# 已知bug
- [ ] 歌单中的虾米音乐暂时无法播放（java获取单首虾米音乐的api还未解析出来）
- [ ] 缺少本地歌曲的专辑图片，使用豆瓣音乐api,获取专辑图片（不知道是不是请求次数太多的原因，有时报400 Bad Request）

# 计划
- [ ] 优化项目架构
- [ ] 验证，修复bug
- [ ] 完善百度音乐，QQ音乐、虾米音乐、网易云音乐的api

# 音乐来源
- 绝大部分来源于[Binaryify/NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)和[LIU9293/musicAPI](https://github.com/LIU9293/musicAPI)等
- [sunzongzheng/musicApi](https://github.com/sunzongzheng/musicApi)

# 项目运行截图
<img src="screenshots/preview1.png" width="100px"/>
<img src="screenshots/preview2.png" width="100px"/>
<img src="screenshots/preview3.png" width="100px"/>
<img src="screenshots/preview4.png" width="100px"/>
<img src="screenshots/preview5.png" width="100px"/>
<img src="screenshots/preview6.png" width="100px"/>
<img src="screenshots/preview7.png" width="100px"/> 
