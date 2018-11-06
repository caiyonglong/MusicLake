package com.cyl.musiclake.ui.my

import android.content.Intent
import android.view.View
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseFragment
import com.cyl.musiclake.ui.my.user.User
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * 作者：yonglong on 2016/8/11 18:17
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class BindLoginActivity : BaseFragment<LoginPresenter>(), LoginContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }


    override fun initInjector() {
        mFragmentComponent.inject(this)
    }

    override fun initViews() {
        super.initViews()
        usernameWrapper.hint = getString(R.string.bind_username)
        passwordWrapper.hint = getString(R.string.prompt_password)
        mPresenter?.attachView(this)
        bindBtn.setOnClickListener {
            val username = usernameWrapper.editText?.text.toString()
            val password = passwordWrapper.editText?.text.toString()
            // TODO: 检查　
            if (!validatePassword(username)) {
                usernameWrapper.isErrorEnabled = false
                passwordWrapper.isErrorEnabled = false

                usernameWrapper.error = "邮箱或者学号"
            } else if (!validatePassword(password)) {
                usernameWrapper.isErrorEnabled = false
                passwordWrapper.isErrorEnabled = false

                passwordWrapper.error = "密码错误"
            } else {
                usernameWrapper.isErrorEnabled = false
                passwordWrapper.isErrorEnabled = false
                //TODO:登录
                progressBar.visibility = View.VISIBLE

                mPresenter?.bindNetease(username,password)
            }
        }
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
