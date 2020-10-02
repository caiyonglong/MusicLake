package com.cyl.musiclake.ui.settings

import android.content.Intent
import android.view.MenuItem
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract

class SettingsActivity : BaseActivity<BaseContract.BasePresenter<*>>() {
    override fun getLayoutResID(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {}
    override fun initData() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SettingsFragment())
                .commit()
    }

    override fun initInjector() {}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}