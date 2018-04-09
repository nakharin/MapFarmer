package com.zqg.kotlin

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.nakharin.mapfarmer.R

class LoadingDialog(context: Context, msg: String) {
    private var mLoadingView: LVCircularRing
    private var mLoadingDialog: Dialog? = null

    init {
        val view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view, null)
        val layout = view.findViewById(R.id.dialog_view) as LinearLayout
        mLoadingView = view.findViewById(R.id.lv_circularring) as LVCircularRing
        val loadingText = view.findViewById(R.id.loading_text) as TextView
        loadingText.text = msg
        mLoadingDialog = Dialog(context, R.style.loading_dialog)
        mLoadingDialog!!.setContentView(layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT))
    }

    fun cancelable(isCancel: Boolean) {
        if (mLoadingDialog != null) {
            mLoadingDialog!!.setCancelable(isCancel)
        }
    }

    fun show() {
        if (mLoadingDialog != null) {
            mLoadingDialog!!.show()
            mLoadingView.startAnim()
        }
    }

    fun close() {
        if (mLoadingDialog != null) {
            mLoadingView.stopAnim()
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }
}
