package com.cyl.musiclake.ui.timing

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.cyl.musiclake.R
import com.cyl.musiclake.ui.base.BaseActivity
import com.cyl.musiclake.ui.base.BaseContract
import com.cyl.musiclake.ui.base.BasePresenter
import com.cyl.musiclake.utils.CountDownUtils
import com.cyl.musiclake.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_sleep_timer.*

/**
 * 定时关闭
 */
class SleepTimerActivity : BaseActivity<BasePresenter<BaseContract.BaseView>>() {
    private val shiwuTv by lazy { findViewById<TextView>(R.id.sleepTimeShiWuTv) }
    private val sanshiTv by lazy { findViewById<TextView>(R.id.sleepTimeSanShiTv) }
    private val siwuTv by lazy { findViewById<TextView>(R.id.sleepTimeSiWuTv) }
    private val liushiTv by lazy { findViewById<TextView>(R.id.sleepTimeLiuShiTv) }
    private val customTv by lazy { findViewById<TextView>(R.id.sleepTimeCustomTv) }
    private val shiwuIv by lazy { findViewById<ImageView>(R.id.tagShiWuIv) }
    private val sanshiIv by lazy { findViewById<ImageView>(R.id.tagSanShiIv) }
    private val siwuIv by lazy { findViewById<ImageView>(R.id.tagSiWuIv) }
    private val liushiIv by lazy { findViewById<ImageView>(R.id.tagLiuShiIv) }
    private val customIv by lazy { findViewById<ImageView>(R.id.tagCustomIv) }
    private val cancelIv by lazy { findViewById<ImageView>(R.id.tagCancelIv) }

    override fun getLayoutResID(): Int {
        return R.layout.activity_sleep_timer
    }

    override fun initView() {
    }

    override fun initData() {
        initStatus()
        initTime()

        playControlSwitch.isChecked = CountDownUtils.isOpenSleepSwitch
        //显示提示文字
        closeAppTipsTv.setText(if (playControlSwitch.isChecked) R.string.switch_minutes_open_desc else R.string.switch_minutes_close_desc)

        playControlSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            CountDownUtils.isOpenSleepSwitch = isChecked
            //显示提示文字
            closeAppTipsTv.setText(if (isChecked) R.string.switch_minutes_open_desc else R.string.switch_minutes_close_desc)
        }
    }

    override fun initInjector() {
    }

    private fun initTime() {
        CountDownUtils.addTextView(shiwuTv)
        CountDownUtils.addTextView(sanshiTv)
        CountDownUtils.addTextView(siwuTv)
        CountDownUtils.addTextView(liushiTv)
        CountDownUtils.addTextView(customTv)
        when (CountDownUtils.type) {
            1 -> clickShiWuItem(null)
            2 -> clickSanShiItem(null)
            3 -> clickSiWuItem(null)
            4 -> clickLiuShiItem(null)
            5 -> clickCustomItem(null)
            else -> {
                clickCancelItem(null)
            }
        }
    }

    fun clickShiWuItem(view: View?) {
        initStatus()
        shiwuIv.visibility = View.VISIBLE
        shiwuTv.visibility = View.VISIBLE
        if (view != null) {
            CountDownUtils.start(15 * 60 * 1000, 1)
        }

    }

    fun clickSanShiItem(view: View?) {
        initStatus()
        sanshiIv.visibility = View.VISIBLE
        sanshiTv.visibility = View.VISIBLE
        if (view != null) {
            CountDownUtils.start(30 * 60 * 1000, 2)
        }

    }

    fun clickSiWuItem(view: View?) {
        initStatus()
        siwuIv.visibility = View.VISIBLE
        siwuTv.visibility = View.VISIBLE
        if (view != null) {
            CountDownUtils.start(45 * 60 * 1000, 3)
        }

    }

    fun clickLiuShiItem(view: View?) {
        initStatus()
        liushiIv.visibility = View.VISIBLE
        liushiTv.visibility = View.VISIBLE
        if (view != null) {
            CountDownUtils.start(60 * 60 * 1000, 4)
        }
    }

    fun clickCustomItem(view: View?) {
        initStatus()
        customIv.visibility = View.VISIBLE
        customTv.visibility = View.VISIBLE
        view?.let {
            MaterialDialog(this).show {
                title(R.string.custom_count_down_time)
                input(inputType = InputType.TYPE_CLASS_NUMBER,
                        maxLength = 4, hintRes = R.string.count_down_minutes) { dialog, input ->
                    val isValid = (input ?: 0).toString().toInt() <= 24 * 60
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                }
                positiveButton {
                    val time = (it.getInputField().text
                            ?: 0).toString().toInt()
                    if (time == 0 || time > 24 * 60) {
                        ToastUtils.show(getString(R.string.down_time_more))
                    } else {
                        CountDownUtils.starCountDownByTime(time)
                    }
                }
                positiveButton(R.string.sure)
            }
            //            BaseSystemDialog().apply {
//                val view = View.inflate(this@SleepTimerActivity, R.layout.dialog_select_sleep_time, null)
//                val selectHour = view.findViewById<NumberPicker>(R.id.selectHour)
//                setNumberPickerDividerColor(selectHour)
//                selectHour.maxValue = 23
//                selectHour.minValue = 0
//                val selectM = view.findViewById<NumberPicker>(R.id.selectMin)
//                setNumberPickerDividerColor(selectM)
//                selectM.maxValue = 59
//                selectM.minValue = 1
//                noText = this@SleepTimerActivity.getString(R.string.close_up)
//                yesText = this@SleepTimerActivity.getString(R.string.start_up)
//                contentView = view
//                onYesClickListener = {
//                    CountDownUtils.start((selectHour.value * 60 * 60 * 1000 + selectM.value * 60 * 1000).toLong()
//                            , 5)
//                }
//                onNoClickListener = {
//                    clickCancelItem(null)
//                }
//            }.showIt(this)
        }

    }

    fun clickCancelItem(view: View?) {
        CountDownUtils.cancel()
        initStatus()
        cancelIv.visibility = View.VISIBLE
    }

    fun clickTimerSwitch(view: View?) {
        playControlSwitch.isChecked = !playControlSwitch.isChecked
        CountDownUtils.isOpenSleepSwitch = playControlSwitch.isChecked
    }

//    private fun setNumberPickerDividerColor(numberPicker: NumberPicker) {
//        val pickerFields = NumberPicker::class.java.declaredFields
//        for (pf in pickerFields) {
//            if (pf.name == "mSelectionDivider") {
//                pf.isAccessible = true
//                try {
//                    //设置分割线的颜色值 透明
//                    pf.set(numberPicker, ColorDrawable(this.resources.getColor(R.color.whiteAlpha54)))
//                } catch (e: IllegalArgumentException) {
//                    e.printStackTrace()
//                } catch (e: Resources.NotFoundException) {
//                    e.printStackTrace()
//                } catch (e: IllegalAccessException) {
//                    e.printStackTrace()
//                }
//                break
//            }
//        }

//        // 分割线高度
//        for (pf2 in pickerFields) {
//            if (pf2.name == "mSelectionDividerHeight") {
//                pf2.isAccessible = true
//                try {
//                    val result = 3
//                    pf2.set(numberPicker, result)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//                break
//            }
//        }

//    }

    private fun initStatus() {
        shiwuIv.visibility = View.GONE
        sanshiIv.visibility = View.GONE
        siwuIv.visibility = View.GONE
        liushiIv.visibility = View.GONE
        customIv.visibility = View.GONE
        shiwuTv.visibility = View.GONE
        sanshiTv.visibility = View.GONE
        siwuTv.visibility = View.GONE
        liushiTv.visibility = View.GONE
        customTv.visibility = View.GONE
        cancelIv.visibility = View.GONE
    }
}
