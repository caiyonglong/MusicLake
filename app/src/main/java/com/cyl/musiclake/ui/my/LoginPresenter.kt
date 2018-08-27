package com.cyl.musiclake.ui.my


import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.my.user.User
import com.cyl.musiclake.utils.LogUtil
import com.cyl.musiclake.utils.SPUtils
import com.cyl.musiclake.utils.ToastUtils
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants.REQUEST_LOGIN
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/3.
 */

class LoginPresenter @Inject
constructor() : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    private var userModel: UserModel? = null

    private var loginListener: IUiListener? = null

    init {
        this.userModel = UserModel()
    }

    override fun attachView(view: LoginContract.View) {
        super.attachView(view)
        userModel = UserModel()
    }

    override fun login(params: Map<String, String>) {
        mView?.showLoading()
    }

    private fun getPrivateToken() {
        ApiManager.request(
                PlaylistApiServiceImpl.login(MusicApp.mTencent.accessToken, MusicApp.mTencent.openId),
                object : RequestCallBack<User> {
                    override fun success(result: User?) {
                        if (result != null) {
                            val newUserInfo = userModel!!.userInfo
                            newUserInfo!!.token = result.token
                            userModel?.savaInfo(newUserInfo)
                        }
                        //登录成功
                        mView?.hideLoading()
                        //登录成功
                        mView?.success(userModel!!.userInfo)
                    }

                    override fun error(msg: String) {
                        //登录失败
                        mView?.hideLoading()
                        mView?.showErrorInfo(msg)
                    }
                }
        )
    }


    /**
     * 实现QQ第三方登录
     */
    override fun loginByQQ(activity: Activity) {
        mView.showLoading()
        //QQ第三方登录
        MusicApp.mTencent.login(activity, "all", loginListener)
        loginListener = object : IUiListener {
            override fun onComplete(o: Any) {
                mView?.hideLoading()
                //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show()
                val `object` = o as JSONObject
                try {
                    LogUtil.e("mTencent:" + o.toString())
                    val accessToken = `object`.getString("access_token")
                    val expires = `object`.getString("expires_in")
                    val openID = `object`.getString("openid")
                    LogUtil.e("QQ$accessToken--$expires--$openID")
                    MusicApp.mTencent.setAccessToken(accessToken, expires)
                    MusicApp.mTencent.openId = openID
                    SPUtils.putAnyCommit(SPUtils.QQ_OPEN_ID, openID)
                    SPUtils.putAnyCommit(SPUtils.QQ_ACCESS_TOKEN, accessToken)
                    SPUtils.putAnyCommit(SPUtils.QQ_ACCESS_TOKEN, accessToken)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onError(uiError: UiError) {
                mView?.hideLoading()
            }

            override fun onCancel() {
                mView?.hideLoading()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == -1 && data != null) {
                try {
                    Tencent.onActivityResultData(requestCode, resultCode, data, loginListener)
                    Tencent.handleResultData(data, loginListener)
                    val info = UserInfo(mView.context, MusicApp.mTencent.qqToken)
                    info.getUserInfo(object : IUiListener {
                        override fun onComplete(o: Any) {
                            try {
                                val info = o as JSONObject
                                val nickName = info.getString("nickname")//获取用户昵称
                                val iconUrl = info.getString("figureurl_qq_2")//获取用户头像的url
                                val gender = info.getString("gender")//获取用户性别
                                //                            Map<String, String> params = new HashMap<String, String>();
                                //                            params.put(Constants.PARAM_METHOD, "qq");
                                //                            params.put(Constants.USERNAME, nickName);
                                //                            params.put(Constants.USER_SEX, gender);
                                //                            params.put(Constants.USER_IMG, iconUrl);
                                //                            params.put(Constants.USER_ID, mTencent.getOpenId());
                                val userInfo = User()
                                userInfo.id = MusicApp.mTencent.openId
                                userInfo.avatar = iconUrl
                                userInfo.sex = gender
                                userInfo.name = nickName
                                userInfo.nick = nickName
                                //保存用户信息
                                userModel!!.savaInfo(userInfo)
                                getPrivateToken()
                            } catch (e: JSONException) {
                                ToastUtils.show("网络异常，请稍后重试！")
                                e.printStackTrace()
                            }

                        }

                        override fun onError(uiError: UiError) {
                            mView?.hideLoading()
                        }

                        override fun onCancel() {
                            mView?.hideLoading()
                        }
                    })
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            } else {
                mView?.hideLoading()
            }
        }
    }
}
