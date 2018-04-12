package com.nakharin.mapfarmer.Controller.Activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import co.metalab.asyncawait.async
import com.emcsthai.emcslibrary.Model.Utils.StringUtils
import com.emcsthai.emcslibrary.ViewGroup.EditTextView
import com.identive.libs.SCard
import com.nakharin.mapfarmer.Model.Personal
import com.nakharin.mapfarmer.R
import com.nakharin.mapfarmer.Utils.SCardUtility
import com.zqg.kotlin.LoadingDialog
import java.util.*

class IdentiveGetZActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "IdentiveGetZActivity"
        private const val EXCLUSIVE_MODE = 1
        private const val PROTOCOL_TX = 3

        private val SELECT = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00, 0x08, 0xA0.toByte(), 0x00, 0x00, 0x00, 0x54, 0x48, 0x00, 0x01)
        private val CID = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x00, 0x04, 0x02, 0x00, 0x0D)
        private val NAME_TH = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x00, 0x11, 0x02, 0x00, 0x64)
        private val NAME_EN = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x00, 0x75, 0x02, 0x00, 0x64)
        private val DATE_OF_BIRTH = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x00, 0xD9.toByte(), 0x02, 0x00, 0x08)
        private val GENDER = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x00, 0xE1.toByte(), 0x02, 0x00, 0x01)
        private val ADDRESS = byteArrayOf(0x80.toByte(), 0xB0.toByte(), 0x15, 0x79, 0x02, 0x00, 0x64)
    }

    private lateinit var txtSCardName: TextView

    private lateinit var btnConnect: AppCompatButton
    private lateinit var btnRead: AppCompatButton

    private lateinit var edtResult: EditTextView

    private lateinit var personal: Personal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identive_get_z)

        val trans = SCard()
        var lRetval = trans.USBRequestPermission(this)
        lRetval = trans.SCardEstablishContext(this)
        Log.d(TAG, "SCardEstablishContext - $lRetval")

        initWidget()

        btnConnect.setOnClickListener(onClickListener)
        btnRead.setOnClickListener(onClickListener)
    }

    private fun initWidget() {

        txtSCardName = findViewById(R.id.txtSCardName)

        btnConnect = findViewById(R.id.btnConnect)
        btnRead = findViewById(R.id.btnRead)

        edtResult = findViewById(R.id.edtResult)
    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {

        if (it == btnConnect) {
            val deviceList = ArrayList<String>()
            val trans = SCard()
            val lRetval = trans.SCardListReaders(baseContext, deviceList)
            Log.d(TAG, "Result - $lRetval")
            var items = deviceList.toTypedArray()
            val builder = AlertDialog.Builder(this)
                    .setTitle("Select a Reader")
                    .setSingleChoiceItems(items, -1, { dialog, position ->

                        val selectedRdr: String = items[position]
                        Log.d(TAG, "Selected Reader - $selectedRdr")

                        txtSCardName.text = selectedRdr
                        val status = trans.SCardConnect(selectedRdr, EXCLUSIVE_MODE, PROTOCOL_TX)
                        if (status != 0L) {
                            Toast.makeText(this, "Card Connection Error", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Card Connected Successfully", Toast.LENGTH_SHORT).show()
                        }

                        dialog.dismiss()
                    })

            val alert = builder.create()
            alert.show()
        }

        if (it == btnRead) {

            val dialog = LoadingDialog(this, "Reading...")
            dialog.cancelable(false)
            dialog.show()

            async {

                personal = Personal()

                await { SCardUtility().transmitExtended(SELECT) }
                personal.citizenId = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(CID)) }

                val nameTH = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(NAME_TH)) }
                personal.setNameTH(nameTH)

                val nameEN = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(NAME_EN)) }
                personal.setNameEN(nameEN)

                val gender = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(GENDER)) }
                personal.setGender(gender)

                val dateOfBirth = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(DATE_OF_BIRTH)) }
                personal.setDateOfBirth(dateOfBirth)

                val address = await { StringUtils.getInstance().getUTF8FromAsciiBytes(SCardUtility().transmitExtended(ADDRESS)) }
                personal.setAddress(address)

            }.finally {

                Handler().postDelayed({
                    dialog.close()

                    var result = "CitizenId : ${personal.citizenId}\n\n"

                    result += "TitleTH : ${personal.nameTH.title}\n"
                    result += "NameTH : ${personal.nameTH.name}\n"
                    result += "LastTH : ${personal.nameTH.lastName}\n\n"

                    result += "TitleEN : ${personal.nameEN.title}\n"
                    result += "NameEN : ${personal.nameEN.name}\n"
                    result += "LastEN : ${personal.nameEN.lastName}\n\n"

                    result += "gender : ${personal.gender.nameTH}\n\n"

                    result += "strDate :  ${personal.dateOfBirth.dateBE}\n\n"

                    result += "houseNo : ${personal.address.houseNo}\n"
                    result += "villageNo : ${personal.address.villageNo}\n"
                    result += "lane : ${personal.address.lane}\n"
                    result += "road : ${personal.address.road}\n"
                    result += "district : ${personal.address.district}\n"
                    result += "subDistrict : ${personal.address.subDistrict}\n"
                    result += "province : ${personal.address.province}\n"
                    result += "allAddress : ${personal.address.allAddress}\n\n"

                    edtResult.setText(result)
                }, 2000)
            }
        }
    }
}