package com.coolxtc.common.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.coolxtc.common.R


/**
 * 对话框工具
 *
 * @author xiajia
 *
 *
 *
 *
 * $Id$
 *
 *
 */
object DialogUtil {

    open class BaseDialog : Dialog {

        var okButton: Button? = null
        var otherButton: Button? = null
        var allButtons: Array<Button>? = null
        var listView: ListView? = null
        var gridView: GridView? = null
        var contextTextView: TextView? = null
        var imageView: ImageView? = null
        var input: EditText? = null
        var mIsMatchWidth: Boolean = false
        var view: View? = null
        var view1: View? = null

        interface DialogDismissListener {
            fun onDialogDismiss()
        }

        constructor(context: Context?, theme: Int) : super(context!!, theme) {}

        constructor(context: Context, theme: Int, gravity: Int, isMatchWidth: Boolean) : super(
                context,
                theme
        ) {
            this.setCanceledOnTouchOutside(true)
            this.mIsMatchWidth = isMatchWidth
            // this.getWindow().setWindowAnimations(R.style.AnimBottom);
            this.window!!.setGravity(gravity)
        }

        override fun show() {
//            //隐藏虚拟键
//            if (NavBarUtils.hasNavBar(context)) {
//                NavBarUtils.hideBottomUIMenu(this)
//            }
            if (mIsMatchWidth) {
                val lp = window!!.attributes
                lp.width = ScreenTools.instance(context).screenWidth // 设置宽度
                lp.height =
                        ScreenTools.instance(context).screenHeight - ScreenTools.getStatusHeight(context) // 设置宽度
                window!!.attributes = lp
//                window?.setLayout(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
            }
            if (!isShowing) {
                super.show()
            }
        }

        fun setGravity(gravity: Int) {
            this.window!!.setGravity(gravity)
        }

        override fun setCanceledOnTouchOutside(cancel: Boolean) {
            super.setCanceledOnTouchOutside(cancel)
        }

        fun setOkClickListener(listener: View.OnClickListener?) {
            if (listener != null && okButton != null) {
                okButton!!.setOnClickListener(listener)
            }
        }

        fun setOtherClickListener(listener: View.OnClickListener?) {
            if (listener != null && otherButton != null) {
                otherButton!!.setOnClickListener(listener)
            }
        }

        fun setLvItemClickListener(listener: OnItemClickListener?) {
            if (listener != null && listView != null) {
                listView!!.onItemClickListener = listener
            }
        }

        fun setGvItemClickListener(listener: OnItemClickListener?) {
            if (listener != null && gridView != null) {
                gridView!!.onItemClickListener = listener
            }
        }

        fun setContextText(text: String) {
            if (contextTextView != null) {
                contextTextView!!.text = text
            }
        }
    }

    /****************************
     * 自定义升级弹出框
     */
    fun createMineUpdateAppDialog(
            context: Activity,
            title: String,
            version: String,
            content: String,
            btnOKCaption: String,
            contentCenter: Boolean = true,
    ): BaseDialog {
        val dialog = BaseDialog(context, R.style.CustomConfirmDialog)
        dialog.setContentView(R.layout.dialog_confirm_update_app)
        dialog.window!!.attributes.gravity = Gravity.CENTER
        val titleTv = dialog.findViewById(R.id.confirm_title) as TextView
        if (!TextUtils.isEmpty(title)) {
            titleTv.text = title
        }
        val versionTv = dialog.findViewById(R.id.tv_version) as TextView
        if (!TextUtils.isEmpty(version)) {
            versionTv.text = "版本号:$version"
        }
        val mesTv = dialog.findViewById(R.id.confirm_msg) as TextView
        if (!TextUtils.isEmpty(content)) {
            val msg = "更新说明:" +
                    content.replace("<p align=\"left\">", "").replace("</p>", "<br>").replace("<p>", "")
            if (contentCenter) {
                mesTv.gravity = Gravity.START
            } else {
                mesTv.gravity = Gravity.END
            }
            LogUtils.i("hhh---,msg:$msg")
            mesTv.text = Html.fromHtml(msg)
        } else {
            mesTv.visibility = View.INVISIBLE
        }
        val btnOK = dialog.findViewById(R.id.btnConfirm) as Button
        if (btnOKCaption.isNotEmpty()) {
            btnOK.text = btnOKCaption
        }
        dialog.okButton = btnOK
        val btnCancel = dialog.findViewById(R.id.btnCancelConfirm) as Button
        dialog.otherButton = btnCancel
        btnCancel.setOnClickListener { dialog.dismiss() }
        return dialog
    }

    fun createProgressDialog(activity: Activity, content: String = "加载中..."): BaseDialog {
        val mDialog = BaseDialog(activity, R.style.DialogCommonStyle, Gravity.CENTER, true)
        mDialog.setContentView(R.layout.qbb_progress_dialog)
        mDialog.setCanceledOnTouchOutside(false)
        val msgTv = mDialog.findViewById(R.id.tvMsg) as TextView
        if (!TextUtils.isEmpty(content)) {
            msgTv.text = content
        }
        mDialog.contextTextView = msgTv
        return mDialog
    }
}



