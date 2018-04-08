package com.nakharin.mapfarmer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by nakarin on 8/22/2017 AD.
 */

class DialogConfirmFragment : DialogFragment() {

    private var tag = 0
    private var single = true
    private var cancel = true
    private var state = State.NORMAL
    private var title: String? = ""
    private var message: String? = ""
    private var strOk: String? = ""
    private var strCancel: String? = ""

    private var onOneDialogListener: OnOneDialogListener? = null
    private var onTwoDialogListener: OnTwoDialogListener? = null

    private var tvTitle: TextView? = null
    private var tvMessage: TextView? = null

    private var rootLayout: LinearLayout? = null

    private var vLine_0: View? = null

    private var btnOK: Button? = null
    private var vLine: View? = null
    private var btnCancel: Button? = null

    /********************************************************************************
     * Listener ***********************************
     */

    private val onClickListener = View.OnClickListener { v ->
        if (v === btnOK) {

            if (onOneDialogListener != null) {
                onOneDialogListener!!.onPositiveButtonClick(tag, dialog)
            }

            if (onTwoDialogListener != null) {
                onTwoDialogListener!!.onPositiveButtonClick(tag, dialog)
            }
        }

        if (v === btnCancel) {
            if (onTwoDialogListener != null) {
                onTwoDialogListener!!.onNegativeButtonClick(tag, dialog)
            }
        }
    }

    enum class State private constructor(private val id: Int) {
        NORMAL(0), SUCCESS(1), FAILED(2);


        companion object {

            internal fun fromId(id: Int): State {
                for (state in values()) {
                    if (state.id == id) {
                        return state
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            restoreArguments(arguments!!)
        } else {
            restoreInstanceState(savedInstanceState)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(cancel)
        dialog.setCanceledOnTouchOutside(cancel)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_confirm, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Method from this class
        bindView(view)
        // Method from this class
        setupView()
    }

    private fun bindView(v: View) {

        rootLayout = v.findViewById<View>(R.id.root_layout) as LinearLayout

        tvTitle = v.findViewById<View>(R.id.tv_title) as TextView
        tvMessage = v.findViewById<View>(R.id.tv_message) as TextView

        vLine_0 = v.findViewById(R.id.v_line_0)

        btnOK = v.findViewById<View>(R.id.btn_ok) as Button
        vLine = v.findViewById(R.id.v_line)
        btnCancel = v.findViewById<View>(R.id.btn_cancel) as Button
    }

    private fun setupView() {

        tvTitle!!.text = title
        tvMessage!!.text = Html.fromHtml(message)
        btnOK!!.text = strOk
        btnCancel!!.text = strCancel

        if (single) {
            vLine!!.visibility = View.GONE
            btnCancel!!.visibility = View.GONE
        } else {
            vLine!!.visibility = View.VISIBLE
            btnCancel!!.visibility = View.VISIBLE
        }

        when (state) {
            DialogConfirmFragment.State.NORMAL -> {
                rootLayout!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_background)
                tvTitle!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_title_background)
                vLine_0!!.background = ContextCompat.getDrawable(context!!, R.color.colorPrimary)
                vLine!!.background = ContextCompat.getDrawable(context!!, R.color.colorPrimary)
            }
            DialogConfirmFragment.State.SUCCESS -> {
                rootLayout!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_background_success)
                tvTitle!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_title_background_success)
                vLine_0!!.background = ContextCompat.getDrawable(context!!, R.color.colorGreen)
                vLine!!.background = ContextCompat.getDrawable(context!!, R.color.colorGreen)
            }
            DialogConfirmFragment.State.FAILED -> {
                rootLayout!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_background_fail)
                tvTitle!!.background = ContextCompat.getDrawable(context!!, R.drawable.dialog_title_background_fail)
                vLine_0!!.background = ContextCompat.getDrawable(context!!, R.color.colorPrimary)
                vLine!!.background = ContextCompat.getDrawable(context!!, R.color.colorPrimary)
            }
        }

        btnOK!!.setOnClickListener(onClickListener)
        btnCancel!!.setOnClickListener(onClickListener)
    }

    fun setOnOneDialogListener(oneDialogListener: OnOneDialogListener?) {
        this.onOneDialogListener = oneDialogListener
    }

    fun setOnTwoDialogListener(onTwoDialogListener: OnTwoDialogListener?) {
        this.onTwoDialogListener = onTwoDialogListener
    }

    //    private OnDialogListener getOnDialogListener() {
    //        Fragment fragment = getParentFragment();
    //        try {
    //            if (fragment != null) {
    //                return (OnDialogListener) fragment;
    //            } else {
    //                return (OnDialogListener) getActivity();
    //            }
    //        } catch (ClassCastException e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TAG, tag)
        outState.putBoolean(KEY_CANCELABLE, cancel)
        outState.putInt(KEY_STATE, state.ordinal)
        outState.putBoolean(KEY_SINGLE, single)
        outState.putString(KEY_TITLE, title)
        outState.putString(KEY_MESSAGE, message)
        outState.putString(KEY_STR_OK, strOk)
        outState.putString(KEY_STR_CANCEL, strCancel)
    }

    private fun restoreInstanceState(bundle: Bundle) {
        tag = bundle.getInt(KEY_TAG)
        cancel = bundle.getBoolean(KEY_CANCELABLE)
        state = State.fromId(bundle.getInt(KEY_STATE))
        single = bundle.getBoolean(KEY_SINGLE)
        title = bundle.getString(KEY_TITLE)
        message = bundle.getString(KEY_MESSAGE)
        strOk = bundle.getString(KEY_STR_OK)
        strCancel = bundle.getString(KEY_STR_CANCEL)
    }

    private fun restoreArguments(bundle: Bundle) {
        tag = bundle.getInt(KEY_TAG)
        cancel = bundle.getBoolean(KEY_CANCELABLE)
        state = State.fromId(bundle.getInt(KEY_STATE))
        single = bundle.getBoolean(KEY_SINGLE)
        title = bundle.getString(KEY_TITLE)
        message = bundle.getString(KEY_MESSAGE)
        strOk = bundle.getString(KEY_STR_OK)
        strCancel = bundle.getString(KEY_STR_CANCEL)
    }

    /********************************************************************************
     * Interface ***********************************
     */

    interface OnOneDialogListener {
        fun onPositiveButtonClick(tag: Int, dialog: Dialog)
    }

    interface OnTwoDialogListener {
        fun onPositiveButtonClick(tag: Int, dialog: Dialog)

        fun onNegativeButtonClick(tag: Int, dialog: Dialog)
    }

    /********************************************************************************
     * Inner Class ***********************************
     */

    class Builder(private val f: FragmentManager) {

        private var onOneDialogListener: OnOneDialogListener? = null
        private var onTwoDialogListener: OnTwoDialogListener? = null

        private var tag = 0

        private var single = true
        private var cancelable = true

        private var state = State.NORMAL

        private var title = "แจ้งเตือน"
        private var message = ""
        private var strOk = "ใช่"
        private var strCancel = "ไม่"

        fun setTag(tag: Int): Builder {
            this.tag = tag
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setState(state: State): Builder {
            this.state = state
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setMessage(resId: Int): Builder {
            this.message = message
            return this
        }

        fun setStrOk(strOk: String): Builder {
            this.strOk = strOk
            return this
        }

        fun setStrCancel(strCancel: String): Builder {
            this.strCancel = strCancel
            return this
        }

        fun setOnDialogListener(onOneDialogListener: OnOneDialogListener): Builder {
            single = true
            this.onOneDialogListener = onOneDialogListener
            return this
        }

        fun setOnDialogListener(onTwoDialogListener: OnTwoDialogListener): Builder {
            single = false
            this.onTwoDialogListener = onTwoDialogListener
            return this
        }

        fun build() {
            try {
                val fragment = DialogConfirmFragment.newInstance(tag, cancelable, state, single, title, message, strOk, strCancel)
                fragment.setOnOneDialogListener(onOneDialogListener)
                fragment.setOnTwoDialogListener(onTwoDialogListener)
                fragment.isCancelable = cancelable
                fragment.show(f, TAG)
            } catch (e: IllegalStateException) {
                Log.w(TAG, "IGNORE EXCEPTION")
            }

        }
    }

    companion object {

        private val TAG = "DialogConfirmFragment"

        private val KEY_TAG = "KEY_TAG"
        private val KEY_CANCELABLE = "KEY_CANCELABLE"
        private val KEY_STATE = "KEY_STATE"
        private val KEY_TITLE = "KEY_TITLE"
        private val KEY_MESSAGE = "KEY_MESSAGE"
        private val KEY_STR_OK = "KEY_STR_OK"
        private val KEY_STR_CANCEL = "KEY_STR_CANCEL"
        private val KEY_SINGLE = "KEY_SINGLE"

        fun newInstance(tag: Int, cancelable: Boolean, state: State, single: Boolean, title: String, message: String, strOk: String, strCancel: String): DialogConfirmFragment {
            val fragment = DialogConfirmFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_TAG, tag)
            bundle.putBoolean(KEY_CANCELABLE, cancelable)
            bundle.putInt(KEY_STATE, state.ordinal)
            bundle.putBoolean(KEY_SINGLE, single)
            bundle.putString(KEY_TITLE, title)
            bundle.putString(KEY_MESSAGE, message)
            bundle.putString(KEY_STR_OK, strOk)
            bundle.putString(KEY_STR_CANCEL, strCancel)
            fragment.arguments = bundle
            return fragment
        }
    }
}
