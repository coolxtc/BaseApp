package com.coolxtc.answer.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.coolxtc.answer.R
import com.coolxtc.answer.viewmodel.AnswerModel
import com.coolxtc.common.App
import com.coolxtc.common.fragment.BaseFragment
import com.coolxtc.common.router.RouterPath
import com.coolxtc.common.util.ToastUtil
import com.coolxtc.common.util.observeEventSticky
import com.coolxtc.dev.export.event.DevTestEvent
import kotlinx.android.synthetic.main.fragment_answer.*

/**
 * Desc:
 * AnswerFragment
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
@Route(path = RouterPath.ANSWER_FRAGMENT)
class AnswerFragment : BaseFragment(R.layout.fragment_answer, true) {
    private val answerModel by lazy { AnswerModel.getInstant(this) }

    override fun initData() {
        answerModel.start()
        answerModel.startStatus.observe(this, {

        })
        observeEventSticky<DevTestEvent> {
            ToastUtil.toast("$simpleName:${it.msg}")
        }
    }

    override fun initView() {
        bt_create.setOnClickListener {
            answerModel.info1 = et_position1.text.toString()
            answerModel.info2 = et_position2.text.toString()
            answerModel.info3 = et_position3.text.toString()
            if (TextUtils.isEmpty(answerModel.info1) ||
                    TextUtils.isEmpty(answerModel.info2) ||
                    TextUtils.isEmpty(answerModel.info3)) {
                ToastUtil.toast("请检查输入项完整后再点击生成按钮")
                return@setOnClickListener
            }
            tv_result.text = answerModel.result
            bt_copy.visibility = View.VISIBLE
        }
        bt_copy.setOnClickListener {
            val cmb = App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.setPrimaryClip(ClipData.newPlainText(null, answerModel.result))
            ToastUtil.toast("复制成功！")
            bt_copy.visibility = View.GONE
        }
    }
}