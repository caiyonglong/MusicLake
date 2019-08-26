# 项目编译&服务器部署

## 项目编译
  - Android Studio
  - 签名文件未上传，有需要的，可生成自己的应用签名文件（登录有用）
  - 进入软件设置界面，可设置音乐湖后台API，和NeteaseCloudMusicApi 的API，然后重启

## 服务器部署

### 音乐湖后台
- [音乐湖服务端](https://github.com/sunzongzheng/player-be)：音乐湖云歌单后台（主要是负责云歌单，音乐榜单和登录相关)


### 其他
- 网易云音乐API：[https://github.com/Binaryify/NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)。
为了提供更好的体验，目前暂时部署在leanCloud上面，因为是leanCloud的体验实例，所以有每天 6 小时的强制休眠时间，而且每天的请求次数也有
限制，所以不稳定。如果有服务器的可以自己搭建，部署一下 NeteaseCloudMusicApi。

## 应用中API使用
- 进入软件设置界面，可设置音乐湖后台API，和NeteaseCloudMusicApi 的API，然后重启

## 关于登录
创建应用签名，在对应的平台注册应用并配置，如
- QQ登录：[QQ互联](https://connect.qq.com)创建应用
- 微博登录：[微博开放平台](https://open.weibo.com/authentication/)，
- github：在项目 setting -> Developer settings -> New Github App注册。

QQ、微博创建应用都要域名备案，github不用。 创建完应用记得修改配置文件，在common/Constants.java中 修改密钥
