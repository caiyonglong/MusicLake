package com.cyl.musiclake.ui.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.my.user.User
import com.just.agentweb.AgentWeb
import com.just.agentweb.IAgentWebSettings
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_web.*
import wendu.dsbridge.CompletionHandler
import wendu.dsbridge.DWebView

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BindLoginFragment : BaseFragment<LoginPresenter>(), LoginContract.View {

    override fun getLayoutId(): Int {
        return R.layout.fragment_login_web
    }

    override fun initInjector() {
        mFragmentComponent.inject(this)
    }


    override fun initViews() {
        super.initViews()
//        usernameWrapper.hint = getString(R.string.bind_username)
//        passwordWrapper.hint = getString(R.string.prompt_password)
//        mPresenter?.attachView(this)
//        bindBtn.setOnClickListener {
//            val username = usernameWrapper.editText?.text.toString()
//            val password = passwordWrapper.editText?.text.toString()
//            // TODO: 检查　
//            if (!validatePassword(username)) {
//                usernameWrapper.isErrorEnabled = false
//                passwordWrapper.isErrorEnabled = false
//
//                usernameWrapper.error = "邮箱或者手机号"
//            } else if (!validatePassword(password)) {
//                usernameWrapper.isErrorEnabled = false
//                passwordWrapper.isErrorEnabled = false
//
//                passwordWrapper.error = "密码错误"
//            } else {
//                usernameWrapper.isErrorEnabled = false
//                passwordWrapper.isErrorEnabled = false
//                //TODO:登录
//                progressBar.visibility = View.VISIBLE
//
//                mPresenter?.bindNetease(username, password)
//            }
//        }
        var isQQ = true

        btn.setOnClickListener {
            if (isQQ){
                initWebView("https://y.qq.com")
            }else{
                initWebView("https://music.163.com")
            }
            isQQ=!isQQ
        }
    }


    fun initWebView(url: String) {
        val mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webContent, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
//                .setAgentWebWebSettings(object : IAgentWebSettings<WebSettings> {
//                    override fun getWebSettings(): WebSettings {
//                        return this.webSettings
//
//                    }
//
//                    override fun toSetting(webView: WebView?): IAgentWebSettings<WebSettings> {
////                        webView?.settings?.userAgentString = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36"
//                        return this
//                    }
//                })
                .createAgentWeb()
                .ready()
                .go(url)
    }


    //判断密码是否合法
    private fun validatePassword(password: String): Boolean {
        return password.length in 5..18
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (mPresenter != null) {
            mPresenter?.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorInfo(msg: String) {
    }

    override fun success(user: User) {
    }

}
