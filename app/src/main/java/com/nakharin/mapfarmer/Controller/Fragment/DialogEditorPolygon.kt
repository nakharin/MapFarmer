package com.nakharin.mapfarmer.Controller.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.emcsthai.emcslibrary.ViewGroup.EditTextView
import com.nakharin.mapfarmer.R

/**
 * Created by nakarin on 8/22/2017 AD.
 */

class DialogEditorPolygon : DialogFragment() {

    companion object {

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CANCELABLE = "KEY_CANCELABLE"

        fun newInstance(cancelable: Boolean, title: String): DialogEditorPolygon {
            val fragment = DialogEditorPolygon()
            val bundle = Bundle()
            bundle.putBoolean(KEY_CANCELABLE, cancelable)
            bundle.putString(KEY_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var edtPolygonName: EditTextView

    private lateinit var btnPositive: Button
    private lateinit var btnNegative: Button

    private var title = ""
    private var cancel = true

    private var onDialogListener: OnDialogListener? = null

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
        return inflater.inflate(R.layout.dialog_editor_polygon, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Method from this class
        bindView(view)
        // Method from this class
        setupView()
    }

    private fun bindView(v: View) {

        edtPolygonName = v.findViewById(R.id.edtPolygonName)
        edtPolygonName.setText(title)

        btnPositive = v.findViewById(R.id.btnPositive)
        btnNegative = v.findViewById(R.id.btnNegative)
    }

    private fun setupView() {

        btnPositive.setOnClickListener(onClickListener)
        btnNegative.setOnClickListener(onClickListener)
    }

    fun setOnDialogListener(onDialogListener: OnDialogListener?) {
        this.onDialogListener = onDialogListener
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_CANCELABLE, cancel)
        outState.putString(KEY_TITLE, title)
    }

    private fun restoreInstanceState(bundle: Bundle) {
        cancel = bundle.getBoolean(KEY_CANCELABLE)
        title = bundle.getString(KEY_TITLE)
    }

    private fun restoreArguments(bundle: Bundle) {
        cancel = bundle.getBoolean(KEY_CANCELABLE)
        title = bundle.getString(KEY_TITLE)
    }

    /********************************************************************************
     ********************************* Listener *************************************
     ********************************************************************************/

    private val onClickListener = View.OnClickListener { v ->
        if (v === btnPositive) {
            onDialogListener.let {
                it!!.onPositiveClick(edtPolygonName.text.toString(), dialog)
            }
        }

        if (v === btnNegative) {
            onDialogListener.let {
                it!!.onNegativeClick(dialog)
            }
        }
    }

    /********************************************************************************
     ********************************* Interface ************************************
     ********************************************************************************/

    interface OnDialogListener {
        fun onPositiveClick(s : String, d: Dialog)
        fun onNegativeClick(d: Dialog)
    }

    /********************************************************************************
     ******************************** Inner Class ***********************************
     ********************************************************************************/

    class Builder(private val f: FragmentManager) {

        private companion object {
            const val TAG = "DialogEditorPolygon"
        }

        private var title = ""
        private var cancelable = true
        private var onDialogListener: OnDialogListener? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }


        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnDialogListener(onDialogListener: OnDialogListener): Builder {
            this.onDialogListener = onDialogListener
            return this
        }

        fun build() {
            try {
                val fragment = DialogEditorPolygon.newInstance(cancelable, title)
                fragment.setOnDialogListener(onDialogListener)
                fragment.isCancelable = cancelable
                fragment.show(f, TAG)
            } catch (e: IllegalStateException) {
            }
        }
    }
}
