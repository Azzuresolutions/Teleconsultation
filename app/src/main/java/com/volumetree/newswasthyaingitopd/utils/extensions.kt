package com.volumetree.newswasthyaingitopd.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import android.text.Html
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils
import com.volumetree.newswasthyaingitopd.BuildConfig
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.cho.ChoDashBoardActivity
import com.volumetree.newswasthyaingitopd.activities.doctor.DoctorDashBoardActivity
import com.volumetree.newswasthyaingitopd.activities.patient.DashBoardActivity
import com.volumetree.newswasthyaingitopd.adapters.*
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RelationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import okhttp3.internal.and
import java.io.*
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

//fun Activity.hideKeyboard() {
//    hideKeyboard(currentFocus ?: View(this))
//}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.showKeyBoard(context: Context) {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

}

fun Context.showToast(strMessage: String, isShort: Boolean = true) {
    if (strMessage.isNotEmpty()) {
        if (isShort) {
            Toast.makeText(this, strMessage, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, strMessage, Toast.LENGTH_LONG).show()
        }
    }
}

var localDateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
var dobLocalDateFormat: SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

fun Date.formatDate(): String {
    var formatDate = "-"

    try {
        formatDate = localDateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatDate
}

fun String.fromStrToDate(): String {
    val formatDate: Date

    try {
        formatDate = serverDateFormat.parse(this)!!
        return formatDate.formatDate()

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun String.fromDOBToDate(): Date {
    var formatDate: Date = Calendar.getInstance().time

    try {
        formatDate = serverDateFormat.parse(this)!!
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatDate
}

fun String.fromDOBToStr(): String {
    val formatDate: Date

    try {
        formatDate = dobLocalDateFormat.parse(this)!!
        return formatDate.formatDate()

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun String.fromStrToDate2(): Date? {

    try {
        return serverDateFormat.parse(this)!!

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun Button.selectGender(context: Context, btnUnselect: Button, btnUnselect2: Button) {
    this.isSelected = true
    this.setTextColor(ContextCompat.getColor(context, R.color.white))
    btnUnselect.isSelected = false
    btnUnselect.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
    btnUnselect2.isSelected = false
    btnUnselect2.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
}

fun Button.unSelectButton(context: Context, btnUnselect: Button, btnUnselect2: Button) {
    this.isSelected = false
    this.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
    btnUnselect.isSelected = false
    btnUnselect.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
    btnUnselect2.isSelected = false
    btnUnselect2.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
}

fun Button.manageAllergyButton(
    context: Context,
    btnUnselect: Button,
    btnUnselect2: Button,
    btnUnselect3: Button
) {
    this.isSelected = true
    this.setTextColor(ContextCompat.getColor(context, R.color.white))
    btnUnselect.isSelected = false
    btnUnselect.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
    btnUnselect2.isSelected = false
    btnUnselect2.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
    btnUnselect3.isSelected = false
    btnUnselect3.setTextColor(ContextCompat.getColor(context, R.color.unselect_gender))
}

fun TextView.setGradientTextColor(vararg colorRes: Int) {
    val floatArray = ArrayList<Float>(colorRes.size)
    for (i in colorRes.indices) {
        floatArray.add(i, i.toFloat() / (colorRes.size - 1))
    }
    val textShader: Shader = LinearGradient(
        0f,
        0f,
        0f,
        this.height.toFloat(),
        colorRes.map { ContextCompat.getColor(context, it) }.toIntArray(),
        floatArray.toFloatArray(),
        Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}

fun RecyclerView.setVerticalLayoutManager(context: Context) {
    this.layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}

fun RecyclerView.setHorizontalLayoutManager(context: Context) {
    this.layoutManager =
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}

fun Dialog.fullWidthDialog() {
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(this.window?.attributes)
    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    this.window?.attributes = layoutParams
}

fun EditText.getTextFromEt(): String {
    return this.text.toString().trim()
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun EditText.emailValidation(): Boolean {
    return if (this.getTextFromEt().isEmpty()) {
        true
    } else {
        android.util.Patterns.EMAIL_ADDRESS.matcher(this.getTextFromEt()).matches()
    }
}

fun EditText.phoneNotMandatoryValidation(): Boolean {
    return if (this.getTextFromEt().isEmpty()) {
        true
    } else {
        this.getTextFromEt().length >= 10
    }
}

fun EditText.pinCodeValidation(): Boolean {
    return this.getTextFromEt().isEmpty() || this.getTextFromEt().length < 6
}


var serverDateFormat: SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

var serverDateFormatChat: SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

fun Date.localToServerDate(): String {
    var formatDate = "-"

    try {
        formatDate = serverDateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatDate
}

fun Date.localChatToServerDate(): String {
    var formatDate = "-"

    try {
        formatDate = serverDateFormatChat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formatDate
}

fun String.serverDateToLocalChatDate(): String {

    try {
        val formatDate = serverDateFormat.parse(this)!!
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(formatDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun Button.enableDisable(isEnable: Boolean) {
    this.isEnabled = isEnable
    this.isSelected = isEnable
}


fun Bitmap.getBase64(): String? {
    try {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return encodeToString(b, Base64.DEFAULT)
    } catch (e: Exception) {
    }
    return null
}

fun FragmentActivity.showBottomNavigationView(isVisible: Boolean) {
    when (this) {
        is DashBoardActivity -> {
            this.dashboardBinding?.bottomNavigation?.visibility =
                if (isVisible) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        is DoctorDashBoardActivity -> {
            this.binding?.bottomNavigation?.visibility =
                if (isVisible) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
        is ChoDashBoardActivity -> {
            this.binding?.bottomNavigation?.visibility =
                if (isVisible) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }
}

var docPrescriptionDialog: Dialog? = null
fun Context.dialogPrescriptionViewSync(data: DraftConsultationResponse, dismiss: () -> Unit) {
    if (docPrescriptionDialog != null && docPrescriptionDialog!!.isShowing) {
        docPrescriptionDialog!!.dismiss()
    }
    docPrescriptionDialog = Dialog(this)
    docPrescriptionDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
    docPrescriptionDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    docPrescriptionDialog!!.setContentView(R.layout.custom_dialog_doc_prescription)
    docPrescriptionDialog!!.setCancelable(false)
    docPrescriptionDialog!!.setCanceledOnTouchOutside(false)
    docPrescriptionDialog!!.setOnCancelListener {
        dismiss.invoke()
    }
    docPrescriptionDialog!!.setOnDismissListener {
        dismiss.invoke()
    }
    docPrescriptionDialog!!.fullWidthDialog()

    try {
        val tvTitle: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_title)
        val tvConsultationId: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_consultaionIdValue)
        val tvConsultationDate: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_consultaiondateValue)
        val tvPatientId: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_patientIdValue)
        val tvPatientInfo: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_patient_infoValue)
        val tvMobileNumber: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_mobilenumberValue)
        val tvSpecialitySync: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_specialitysyncValue)
        val tvAddress: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_addressValue)
        val tvHistDiab: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_hist_diab)
        val tvHistSmok: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_hist_smok)
        val tvHistAlco: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_hist_alco)
        val tvHistHyperten: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_hist_hyperten)
        val tvGeNote: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_ge_note)
        val tvAllergies: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_allergies)
        val tvAllergyNote: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_allergy_note)
        val tvDiagnosis: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_diagnosis)
        val tvDiagnosisNotes: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tv_diagnosis_notes)
        val tvRxNote: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_rx_note)
        val tvAdvice: TextView = docPrescriptionDialog!!.findViewById(R.id.tv_advice)
        val tlMedics: TableLayout = docPrescriptionDialog!!.findViewById(R.id.tl_medics)
        val ivClose: ImageView = docPrescriptionDialog!!.findViewById(R.id.iv_close)
        val llAllergy: LinearLayout =
            docPrescriptionDialog!!.findViewById(R.id.ll_allergy)
        val llPd: LinearLayout = docPrescriptionDialog!!.findViewById(R.id.ll_pd)
        val llRx: LinearLayout = docPrescriptionDialog!!.findViewById(R.id.ll_rx)
        val llGe: LinearLayout = docPrescriptionDialog!!.findViewById(R.id.ll_ge)
        val llAdvice: LinearLayout =
            docPrescriptionDialog!!.findViewById(R.id.ll_advice)

        //Signature
        val rvSignature: RelativeLayout =
            docPrescriptionDialog!!.findViewById(R.id.rv_signature)
        rvSignature.visibility = View.VISIBLE
        val ivSignature: ImageView = docPrescriptionDialog!!.findViewById(R.id.iv_signature)
        val tvSignRegNumber: TextView =
            docPrescriptionDialog!!.findViewById(R.id.tvsignregnumber)
        val tvSignDrName: TextView = docPrescriptionDialog!!.findViewById(R.id.tvsigndrname)
        val tvSignAddress: TextView = docPrescriptionDialog!!.findViewById(R.id.tvsignaddress)
        val tvSignState: TextView = docPrescriptionDialog!!.findViewById(R.id.tvsignstate)
        val tvSign5: TextView = docPrescriptionDialog!!.findViewById(R.id.tvsign5)
        tvSign5.visibility = View.GONE
        val btnPrintDialogue: Button =
            docPrescriptionDialog!!.findViewById(R.id.btn_print_dialogue)
        btnPrintDialogue.visibility = View.GONE
        ivClose.setOnClickListener { view: View? ->
            if (view != null) {
                docPrescriptionDialog!!.dismiss()
            }
        }
        val toInsAddress = ""
        tvTitle.text = Html.fromHtml(toInsAddress)
        tvConsultationId.text = data.model
            .consultationModel.consultationId.toString()
        tvPatientId.text = data.model
            .patientConsultationModel.crNumber
        var patientInfo: String =
            (data.model.patientConsultationModel.patientFirstName
                .toString() + " "
                    + data.model.patientConsultationModel.patientLastName + ", " + data.model.patientConsultationModel
                .patientAge.getAge())
        if (data.model.patientConsultationModel
                .patientGenderId.toInt() == 1
        ) patientInfo = patientInfo.uppercase() + "/MALE" else if (data.model
                .patientConsultationModel.patientGenderId.toInt() == 2
        ) patientInfo = patientInfo.uppercase() + "/FEMALE" else if (data.model
                .patientConsultationModel.patientGenderId.toInt() == 3
        ) patientInfo = patientInfo.uppercase() + "/TRANSGENDER"
        tvPatientInfo.text = patientInfo

        //tv_consultaiondate.setText("Date: " + CommonUtils.getDateTimeFromUTC(data.model.getConsultationModel().getCreatedDate()));
        tvConsultationDate.text =
            data.model.patientConsultationModel.closeTime.getDateTimeFromUTC()
        tvMobileNumber.text = data.model
            .patientConsultationModel.patientMobile

        // 07-07-2021
        // Show Institution Speciality
        tvSpecialitySync.text = data.model
            .toInstitutionPrescription?.specialityName ?: "" //getToMembersPrescription

        //tv_address.setText("Address: " + data.model.patientConsultationModel.getPatientAddress().toUpperCase());
        var patientAddress = ""
        if (data.model.patientConsultationModel.patientStateName != null
            && data.model.patientConsultationModel.patientStateName!!.isNotEmpty()
        ) {
            val pAdr1 = if (data.model.patientConsultationModel
                    .patientAddressLine1 != null &&
                data.model.patientConsultationModel.patientAddressLine1!!.isNotEmpty()
            ) data.model.patientConsultationModel.patientAddressLine1
                .toString() + ", " else ""

            //String pC = (data.model.patientConsultationModel.getPatientCityName()!=null &&
            //        !data.model.patientConsultationModel.getPatientCityName().isEmpty()) ?
            //        data.model.patientConsultationModel.getPatientCityName() +"," : "";
            val pD = if (data.model.patientConsultationModel
                    .patientDistrictName != null &&
                data.model.patientConsultationModel.patientDistrictName!!.isNotEmpty()
            ) data.model.patientConsultationModel.patientDistrictName
                .toString() + ", " else ""
            val pS: String = data.model.patientConsultationModel.patientStateName
                .toString() + ", "
            val pPin = if ((data.model.patientConsultationModel
                    .patientPinCode != null &&
                        data.model.patientConsultationModel.patientPinCode!!.isNotEmpty() &&
                        data.model.patientConsultationModel.patientPinCode!!
                            .toInt() > 0)
            ) data.model.patientConsultationModel.patientPinCode else ""

            //pC+
            if (data.model.patientConsultationModel.patientDistrictName!!.uppercase() == data.model.patientConsultationModel.patientStateName
                    .uppercase()
            ) {
                patientAddress = pAdr1 + pS + pPin
                if (patientAddress.endsWith(", ")) {
                    patientAddress = patientAddress.substring(0, patientAddress.length - 2)
                }
            } else {
                patientAddress = pAdr1 + pD + pS + pPin
                if (patientAddress.endsWith(", ")) {
                    patientAddress = patientAddress.substring(0, patientAddress.length - 2)
                }
            }
        }
        tvAddress.text = patientAddress.uppercase()
        if (data.model.consultationModel.isDiabetic) tvHistDiab.text =
            getString(R.string.history_diabetes) + " " + (if (data.model
                    .consultationModel.isDiabetic
            ) "Yes" else "No") else tvHistDiab.visibility =
            View.GONE
        if (data.model.consultationModel.isSmoker) tvHistSmok.text =
            getString(R.string.history_smoking) + " " + (if (data.model
                    .consultationModel.isSmoker
            ) "Yes" else "No") else tvHistSmok.visibility =
            View.GONE
        if (data.model.consultationModel.isAlcoholic) tvHistAlco.text =
            getString(R.string.history_intake) + " " + (if (data.model
                    .consultationModel.isAlcoholic
            ) "Yes" else "No") else tvHistAlco.visibility =
            View.GONE
        if (data.model.consultationModel.isHypertension) tvHistHyperten.text =
            getString(R.string.history_hypertension) + " " + (if (data.model
                    .consultationModel.isHypertension
            ) "Yes" else "No") else tvHistHyperten.visibility =
            View.GONE
        if (data.model.consultationModel
                .physicalExamination != null && data.model.consultationModel
                .physicalExamination != ""
        ) {
            var peNotes: String =
                data.model.consultationModel.physicalExamination
            if (peNotes.contains("\n")) {
                peNotes = peNotes.replace("\n".toRegex(), " ")
            }
            tvGeNote.text = peNotes
        } else tvGeNote.visibility = View.GONE
        if ((tvHistDiab.visibility == View.VISIBLE
                    ) || (tvHistAlco.visibility == View.VISIBLE
                    ) || (tvHistSmok.visibility == View.VISIBLE
                    ) || (tvHistHyperten.visibility == View.VISIBLE
                    ) || (tvGeNote.visibility == View.VISIBLE)
        ) {
            llGe.visibility = View.VISIBLE
        } else {
            llGe.visibility = View.GONE
        }

        if (data.model.lstConsultationMedicineModel != null && data.model
                .lstConsultationMedicineModel.size > 0
        ) {
            tlMedics.visibility = View.VISIBLE
            var position = 0
            data.model.lstConsultationMedicineModel.forEach { medicineData ->
                try {
                    val row = TableRow(this)
                    val lp = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    row.layoutParams = lp
                    row.weightSum = 1f
                    row.setPadding(0, 2, 0, 2)
//                    val slNO = TextView(this)
//                    val slNOlp =
//                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f)
//                    slNO.layoutParams = slNOlp
                    //                        slNO.setPadding(0,2,5,2);
                    val medNAme = TextView(this)
                    val medNAmeLp =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f)
                    medNAme.layoutParams = medNAmeLp
                    //                        medNAme.setPadding(0,2,5,2);
                    val freq = TextView(this)
                    val freqLp =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f)
                    freq.gravity = Gravity.CENTER
                    freq.layoutParams = freqLp
                    //                        freq.setPadding(0,2,5,2);
                    val dose = TextView(this)
                    val doseLp =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f)
                    dose.gravity = Gravity.CENTER
                    dose.layoutParams = doseLp
                    //                        dose.setPadding(0,2,5,2);
                    val type = TextView(this)
                    val typeLp =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f)
                    type.layoutParams = typeLp
                    //                        type.setPadding(0,2,5,2);
                    val duration = TextView(this)
                    val durationLp =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f)
                    duration.layoutParams = durationLp
                    //                        duration.setPadding(0,2,5,2);

//                    slNO.text = "" + (position + 1)
                    medNAme.text = medicineData.rxName
                    freq.text = medicineData.frequency
                    dose.text = medicineData.quant
                    type.text = medicineData.dosageType
                    duration.text =
                        medicineData.durationtype.toString() + " " + medicineData.durationvalue
//                    row.addView(slNO)
                    row.addView(medNAme)
                    row.addView(freq)
                    row.addView(dose)
                    row.addView(type)
                    row.addView(duration)
                    tlMedics.addView(row, position + 1)
                    position += 1
                } catch (e: Exception) {
                    Log.e("Prescription-1", e.message.toString())
                    e.printStackTrace()
                }
            }
        } else {
            tlMedics.visibility = View.GONE
        }
        if (data.model.consultationModel
                .additionalMedicine != null && data.model.consultationModel
                .additionalMedicine != ""
        ) {
            var rxNotes: String = data.model.consultationModel.additionalMedicine
            if (rxNotes.contains("\n")) {
                rxNotes = rxNotes.replace("\n".toRegex(), " ")
            }
            tvRxNote.text = rxNotes
        } else tvRxNote.visibility = View.GONE
        if (tlMedics.visibility == View.VISIBLE || tvRxNote.visibility == View.VISIBLE) llRx.visibility =
            View.VISIBLE else llRx.visibility = View.GONE
        if (data.model.lstConsultationAllergyModel != null && data.model
                .lstConsultationAllergyModel.size > 0
        ) {
            val allergies = StringBuilder()
            for (i in 0 until data.model.lstConsultationAllergyModel.size) {
                allergies.append(
                    data.model.lstConsultationAllergyModel[i].name
                )
                    .append("(")
                    .append(
                        data.model.lstConsultationAllergyModel[i]
                            .allergyDuration
                    )
                    .append(",")
                    .append(
                        data.model.lstConsultationAllergyModel[i]
                            .allergySeverityName
                    )
                    .append("), ")
            }
            if (allergies.toString().endsWith(", ")) {
                //remove last comma
                tvAllergies.text =
                    allergies.toString().substring(0, allergies.toString().length - 2)
            } else {
                tvAllergies.text = allergies
            }
            tvAllergies.visibility = View.VISIBLE
        } else tvAllergies.visibility = View.GONE
        if (data.model.consultationModel
                .additionalAllergy != null && data.model.consultationModel
                .additionalAllergy != ""
        ) {
            var alNotes: String = data.model.consultationModel.additionalAllergy
            if (alNotes.contains("\n")) {
                alNotes = alNotes.replace("\n".toRegex(), " ")
            }
            tvAllergyNote.text = alNotes
        } else tvAllergyNote.visibility = View.GONE
        if (tvAllergies.visibility == View.VISIBLE || tvAllergyNote.visibility == View.VISIBLE) llAllergy.visibility =
            View.VISIBLE else llAllergy.visibility = View.GONE
        if (data.model.lstConsultationDiagnosisModel != null && data.model
                .lstConsultationDiagnosisModel.size > 0
        ) {
            val diagnosis = StringBuilder()
            for (i in 0 until data.model.lstConsultationDiagnosisModel.size) {
                diagnosis.append(
                    data.model.lstConsultationDiagnosisModel[i].name
                ).append("\n")
            }
            tvDiagnosis.text = diagnosis
            tvDiagnosis.visibility = View.VISIBLE
        } else tvDiagnosis.visibility = View.GONE
        if (data.model.consultationModel
                .additionalProblem != null && data.model.consultationModel
                .additionalProblem != ""
        ) {
            var pdNotes: String = data.model.consultationModel.additionalProblem
            if (pdNotes.contains("\n")) {
                pdNotes = pdNotes.replace("\n".toRegex(), " ")
            }
            tvDiagnosisNotes.text = pdNotes
        } else tvDiagnosisNotes.visibility = View.GONE
        if (tvDiagnosis.visibility == View.VISIBLE || tvDiagnosisNotes.visibility == View.VISIBLE) llPd.visibility =
            View.VISIBLE else llPd.visibility = View.GONE
        if (data.model.lstConsultationMessageModel != null && data.model
                .lstConsultationMessageModel.size > 0
        ) {

            tvAdvice.text = data.model.lstConsultationMessageModel.last().message
            llAdvice.visibility = View.VISIBLE
        } else llAdvice.visibility = View.GONE
        if (data.model.toMembersPrescription != null) {
            if (data.model.toMembersPrescription.signaturePath != null) {
                //String base64Image = base64String.split(",")[1];
                val decodedString: ByteArray = Base64.decode(
                    data.model.toMembersPrescription.signaturePath,
                    Base64.DEFAULT
                )
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                ivSignature.setImageBitmap(decodedByte)
                ivSignature.visibility = View.VISIBLE
            } else {
                ivSignature.visibility = View.GONE
            }

            if (data.model.toMembersPrescription.registrationNumber != null &&
                data.model.toMembersPrescription.registrationNumber!!.isNotEmpty()
            ) {
                tvSignRegNumber.text = data.model.toMembersPrescription.registrationNumber
                tvSignRegNumber.visibility = View.VISIBLE
            } else {
                tvSignRegNumber.visibility = View.GONE
            }
            if ((data.model.toMembersPrescription.firstName != null
                        && data.model.toMembersPrescription.firstName!!.isNotEmpty())
            ) {
                val prefix =
                    if ((data.model.toMembersPrescription.prefix != null ||
                                data.model.toMembersPrescription.prefix!!.isNotEmpty())
                    ) data.model.toMembersPrescription.prefix
                        .toString() + " " else ""
                val firstName =
                    if ((data.model.toMembersPrescription.firstName != null ||
                                data.model.toMembersPrescription.firstName!!.isNotEmpty())
                    ) data.model.toMembersPrescription.firstName
                        .toString() + " " else ""
                val middleName =
                    if ((data.model.toMembersPrescription.middleName != null ||
                                data.model.toMembersPrescription.middleName!!.isNotEmpty())
                    ) data.model.toMembersPrescription.middleName
                        .toString() + " " else ""
                val lastName =
                    if ((data.model.toMembersPrescription.lastName != null ||
                                data.model.toMembersPrescription.lastName!!.isNotEmpty())
                    ) data.model.toMembersPrescription.lastName
                        .toString() + " " else ""
                tvSignDrName.text =
                    prefix + firstName.uppercase(Locale.getDefault()) + middleName.uppercase(
                        Locale.getDefault()
                    ) + lastName.uppercase(
                        Locale.getDefault()
                    )
                tvSignDrName.visibility = View.VISIBLE
            } else {
                tvSignDrName.visibility = View.GONE
            }
            if ((data.model.toMembersPrescription.addressLine1 != null
                        && data.model.toMembersPrescription.addressLine1!!.isNotEmpty())
            ) {
                val address =
                    if ((data.model.toMembersPrescription.addressLine1 != null ||
                                data.model.toMembersPrescription.addressLine1!!.isNotEmpty())
                    ) data.model.toMembersPrescription.addressLine1
                        .toString() + " " else ""
                tvSignAddress.text = address
                tvSignAddress.visibility = View.VISIBLE
            } else {
                tvSignAddress.visibility = View.GONE
            }
            if ((data.model.toMembersPrescription.stateName != null
                        && data.model.toMembersPrescription.stateName!!.isNotEmpty())
            ) {
                //String C = (data.model.toMembersPrescription.getCityName()!=null||
                //        !data.model.toMembersPrescription.getCityName().isEmpty()) ? data.model.toMembersPrescription.getCityName() +"," : "";
                val districtName =
                    if ((data.model.toMembersPrescription.districtName != null ||
                                data.model.toMembersPrescription.districtName!!.isNotEmpty())
                    ) data.model.toMembersPrescription.districtName
                        .toString() + "," else ""
                val stateName: String? = data.model.toMembersPrescription.stateName

                //C+
                if (data.model.toMembersPrescription.districtName!!
                        .uppercase(Locale.getDefault()) == data.model.toMembersPrescription.stateName!!
                        .uppercase(Locale.getDefault())
                ) {
                    tvSignState.text = stateName
                } else {
                    tvSignState.text = districtName + stateName
                }
                tvSignState.visibility = View.VISIBLE
            } else {
                tvSignState.visibility = View.GONE
            }
        } else {
            rvSignature.visibility = View.GONE
        }
        docPrescriptionDialog!!.show()
    } catch (e: Exception) {
        Log.e("Prescription-2", e.message.toString())
        e.printStackTrace()
    }
}

fun getRelations(isChoRelation: Boolean): ArrayList<RelationData> {
    val relations = ArrayList<RelationData>()

    if (isChoRelation) {
        relations.add(RelationData("Father", "Father"))
        relations.add(RelationData("Mother", "Mother"))
        relations.add(RelationData("Spouse", "Spouse"))
        relations.add(RelationData("Guardian", "Guardian"))
    } else {
        relations.add(RelationData("Son", "1"))
        relations.add(RelationData("Daughter", "2"))
        relations.add(RelationData("Spouse", "9"))
        relations.add(RelationData("Brother", "3"))
        relations.add(RelationData("Sister", "4"))
        relations.add(RelationData("Father", "5"))
        relations.add(RelationData("Mother", "6"))
        relations.add(RelationData("Grandfather", "7"))
        relations.add(RelationData("Grandmother", "8"))
//        relations.add(RelationData("Institution", "20"))
    }
    return relations
}


fun String.getDateTimeFromUTC(): String {
    var dateTime = this
    dateTime = try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val value = formatter.parse(dateTime)
        val dateFormatter =
            SimpleDateFormat("dd MMM yyy, hh:mm aa", Locale.ENGLISH) //this format changeable
        if (value != null) {
            dateFormatter.format(value)
        } else {
            ""
        }
    } catch (e: java.lang.Exception) {
        "00/00/0000, 00:00"
    }
    return dateTime
}


fun MasterViewModel.showDistrictListDialog(
    activity: FragmentActivity,
    click: (DistrictModelData, Dialog) -> Unit
) {
    this.getDistrictMaster().observe(activity) { districtData ->
        val districtDialog = Dialog(activity)
        districtDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        districtDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        districtDialog.setContentView(R.layout.layout_list_dialog)
        val rvDialogList =
            districtDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
        districtDialog.setCancelable(true)
        districtDialog.setCanceledOnTouchOutside(true)
        districtDialog.show()
        districtDialog.fullWidthDialog()
        rvDialogList.setVerticalLayoutManager(activity)
        rvDialogList.adapter = DistrictAdapter(
            districtData.lstModel,
            click, districtDialog
        )
    }
}


fun MasterViewModel.showCityListDialog(
    districtId: Int,
    activity: FragmentActivity,
    click: (CityModelData, Dialog) -> Unit
) {
    this.getCityMaster(districtId)
        .observe(activity) { cityData ->
            val cityDialog = Dialog(activity)
            cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            cityDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            cityDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                cityDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            cityDialog.setCancelable(true)
            cityDialog.setCanceledOnTouchOutside(true)
            cityDialog.show()
            cityDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = CityAdapter(
                cityData.lstModel,
                click, cityDialog
            )
        }
}

fun MasterViewModel.showBlockListDialog(
    districtId: Int,
    activity: FragmentActivity,
    click: (BlockModelData, Dialog) -> Unit
) {
    this.getBlockMaster(districtId)
        .observe(activity) { blockData ->
            val cityDialog = Dialog(activity)
            cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            cityDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            cityDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                cityDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            cityDialog.setCancelable(true)
            cityDialog.setCanceledOnTouchOutside(true)
            if (blockData.lstModel.isNotEmpty()) {
                cityDialog.show()
            }
            cityDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = BlockAdapter(
                blockData.lstModel,
                click, cityDialog
            )
        }
}

fun MasterViewModel.showMaritalStatusDialog(
    activity: FragmentActivity,
    click: (MaritalData, Dialog) -> Unit
) {
    this.getMaritalStatus()
        .observe(activity) { maritalData ->
            val maritalDialog = Dialog(activity)
            maritalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            maritalDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            maritalDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                maritalDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            maritalDialog.setCancelable(true)
            maritalDialog.setCanceledOnTouchOutside(true)
            maritalDialog.show()
            maritalDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = MaritalStatusAdapter(
                maritalData.lstModel,
                click, maritalDialog
            )
        }
}


fun Context.showIncomingCallDialog(
    senderReceiverModel: SignalR.SenderReceiverModel,
    consultationReceived: SignalR.ConsultationReceived,
    onAccept: (SignalR.SenderReceiverModel, SignalR.ConsultationReceived) -> Unit,
    onDeny: (SignalR.SenderReceiverModel, SignalR.ConsultationReceived) -> Unit
) {
    val incomingCallDialog = Dialog(this)
    incomingCallDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    incomingCallDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    incomingCallDialog.setContentView(R.layout.incoming_call_dialog)
    incomingCallDialog.setCancelable(false)
    incomingCallDialog.setCanceledOnTouchOutside(false)
    incomingCallDialog.show()
    incomingCallDialog.window?.setGravity(Gravity.TOP)
    incomingCallDialog.fullWidthDialog()
    incomingCallDialog.findViewById<TextView>(R.id.tvDrName).text = consultationReceived.displayName
    incomingCallDialog.findViewById<ImageView>(R.id.imgAcceptCall).setOnClickListener {
        onAccept.invoke(senderReceiverModel, consultationReceived)
        incomingCallDialog.dismiss()
    }
    incomingCallDialog.findViewById<ImageView>(R.id.imgDeclineCall).setOnClickListener {
        onDeny.invoke(senderReceiverModel, consultationReceived)
        incomingCallDialog.dismiss()
    }
}

fun MasterViewModel.showBloodGroupDialog(
    activity: FragmentActivity,
    click: (BloodGroupData, Dialog) -> Unit
) {
    this.getBloodGroup()
        .observe(activity) { bloodGroupResponse ->
            val bloodGroupDialog = Dialog(activity)
            bloodGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            bloodGroupDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            bloodGroupDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                bloodGroupDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            bloodGroupDialog.setCancelable(true)
            bloodGroupDialog.setCanceledOnTouchOutside(true)
            bloodGroupDialog.show()
            bloodGroupDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = BloodGroupAdapter(
                bloodGroupResponse.lstModel,
                click, bloodGroupDialog
            )
        }
}


fun ChoViewModel.showFamilyMemberDialog(
    activity: FragmentActivity,
    patientData: ChoPatientData,
    click: (ChoPatientData, Dialog, Int) -> Unit
) {
    this.getFamilyMembers(patientData.patientInfoId.toString())
        .observe(activity) { familyMemberResponse ->
            val familyDialog = Dialog(activity)
            familyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            familyDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            familyDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                familyDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            familyDialog.setCancelable(true)
            familyDialog.setCanceledOnTouchOutside(true)
            familyDialog.show()
            familyDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            val patients = ArrayList<ChoPatientData>()
            patientData.relationName = "Myself"
            patients.add(patientData)
            familyMemberResponse.lstModel?.let { patients.addAll(it) }
            val itemDecoration =
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)

            val drawable = ContextCompat.getDrawable(activity, R.drawable.receive_message_bg)
            if (drawable != null) {
                itemDecoration.setDrawable(
                    drawable
                )
            }
            rvDialogList.addItemDecoration(
                itemDecoration
            )
            rvDialogList.adapter = FamilyListAdapter(
                patients, click, familyDialog
            )
        }
}

fun MasterViewModel.showDurationDialog(
    activity: FragmentActivity,
    click: (DurationData, Dialog) -> Unit
) {
    this.getDuration()
        .observe(activity) { durationResponse ->
            val durationDialog = Dialog(activity)
            durationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            durationDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            durationDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                durationDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            durationDialog.setCancelable(true)
            durationDialog.setCanceledOnTouchOutside(true)
            durationDialog.show()
            durationDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = DurationAdapter(
                durationResponse.lstModel,
                click, durationDialog
            )
        }
}


fun MasterViewModel.showSeverityDialog(
    activity: FragmentActivity,
    click: (SeverityData, Dialog) -> Unit
) {
    this.getSeverity()
        .observe(activity) { severityResponse ->
            val severityDialog = Dialog(activity)
            severityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            severityDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            severityDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                severityDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            severityDialog.setCancelable(true)
            severityDialog.setCanceledOnTouchOutside(true)
            severityDialog.show()
            severityDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = SeverityAdapter(
                severityResponse.lstModel,
                click, severityDialog
            )
        }
}

fun MasterViewModel.showVitals(
    activity: FragmentActivity,
    click: (VitalData, Dialog) -> Unit
) {
    this.getVitals()
        .observe(activity) { vitalResponse ->
            val vitalDialog = Dialog(activity)
            vitalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            vitalDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            vitalDialog.setContentView(R.layout.layout_list_dialog)
            val rvDialogList =
                vitalDialog.findViewById<RecyclerView>(com.volumetree.newswasthyaingitopd.R.id.rvDialogList)
            vitalDialog.setCancelable(true)
            vitalDialog.setCanceledOnTouchOutside(true)
            vitalDialog.show()
            vitalDialog.fullWidthDialog()
            rvDialogList.setVerticalLayoutManager(activity)
            rvDialogList.adapter = VitalsAdapter(
                vitalResponse.lstModel,
                click, vitalDialog
            )
        }
}


fun Context.showRelationDialog(
    isChoRelation: Boolean = false,
    relationClick: (RelationData, Dialog) -> Unit
) {
    val relationDialog = Dialog(this)
    relationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    relationDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    relationDialog.setContentView(R.layout.layout_list_dialog)
    val rvDialogList =
        relationDialog.findViewById<RecyclerView>(R.id.rvDialogList)

    relationDialog.setCancelable(true)
    relationDialog.setCanceledOnTouchOutside(true)
    relationDialog.show()
    relationDialog.fullWidthDialog()
    rvDialogList.setVerticalLayoutManager(this)
    rvDialogList.adapter =
        RelationAdapter(getRelations(isChoRelation), relationClick, relationDialog)
}

fun Context.showDatePicker(
    myCalendar: Calendar = Calendar.getInstance(),
    dateClick: (Calendar) -> Unit
) {
    val datePicker = DatePickerDialog(
        this,
        { view, year, month, day ->
            view.id
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            dateClick.invoke(myCalendar)
        },
        myCalendar.get(Calendar.YEAR),
        myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.datePicker.maxDate = System.currentTimeMillis()
    datePicker.show()
}

fun EditText.getSHA512Text(): String {
    val md: MessageDigest = MessageDigest.getInstance("SHA-512")
    val digest: ByteArray = md.digest(this.getTextFromEt().toByteArray())
    val sb = StringBuilder()
    for (i in digest.indices) {
        sb.append(((digest[i] and 0xff) + 0x100).toString(16).substring(1))
    }
    return sb.toString()
}

fun EditText.showHidePassword(context: Context, imageView: ImageView) {
    if (this.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
        this.transformationMethod = PasswordTransformationMethod.getInstance()
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pass_invisible))
    } else if (this.transformationMethod == PasswordTransformationMethod.getInstance()) {
        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pass_visible))
    }
    this.setSelection(this.getTextFromEt().length)
}

fun Int.getGenderNameFromId(): String {
    return when (this) {
        1 -> {
            "Male"
        }
        2 -> {
            "Female"
        }
        3 -> {
            "TG"
        }
        else -> {
            ""
        }
    }
}

fun String.getAge(): String {
    val formattedDOB = this.fromDOBToDate()
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    (dob as GregorianCalendar).time = formattedDOB
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    val ageInt = age
    return "$ageInt Years"
}

fun Int.getAge(): String {
    val months = this % 12
    val years = this / 12
    return "$years Years, $months Months"
}

fun String.getIntAge(): Int {
    val formattedDOB = this.fromDOBToDate()
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    (dob as GregorianCalendar).time = formattedDOB
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    return age
}


fun FragmentManager.dismissFragmentDialog(stringTag: String) {
    val fragment = this.findFragmentByTag(stringTag)
    if (fragment != null) {
        (fragment as DialogFragment).dialog?.dismiss()
    }
}

fun Fragment.takePhotoLauncher(status: (Boolean) -> Unit): ActivityResultLauncher<Uri> {
    return this.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        status.invoke(isSuccess)
    }
}

fun Fragment.getImageLauncher(
    context: Context,
    picDocumentListener: PicDocumentSelector,
    type: Int
): ActivityResultLauncher<String> {
    return this.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.updateCaptureImageUI(context, picDocumentListener, type)
    }
}

fun Fragment.getImageLauncherMultiple(
    context: Context,
    picDocumentListener: PicDocumentSelector,
    type: Int
): ActivityResultLauncher<String> {
    return this.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        var maxCount = 0
        uris?.forEach { uri ->
            if (maxCount == Constants.MAX_DOCUMENT) {
                return@forEach
            } else {
                maxCount++
                uri.updateCaptureImageUI(context, picDocumentListener, type)
            }
        }

    }
}


fun Fragment.getDocumentMultipleLauncher(
    context: Context,
    picDocumentListener: PicDocumentSelector,
    type: Int
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var selectedCount = 0
            if (result.data != null && result.data!!.clipData != null) {
                for (i in 0 until result?.data?.clipData?.itemCount!!) {
                    result.data?.clipData?.getItemAt(i)
                    if (selectedCount == Constants.MAX_DOCUMENT) {
                        break
                    } else {
                        selectedCount++
                        result.data?.clipData?.getItemAt(i)?.uri
                            ?.updateCaptureImageUI(context, picDocumentListener, type)
                    }
                }
            } else {
                result.data!!.data?.updateCaptureImageUI(context, picDocumentListener, type)
            }
        }
    }
}

//fun Fragment.getImageLauncherMultipleFile(
//    context: Context,
//    picDocumentListener: PicDocumentSelector,
//    type: Int
//): ActivityResultLauncher<Intent> {
//    return this.registerForActivityResult(Intent) { uris: List<Uri>? ->
//        var max_count = 0
//        uris?.forEach { uri ->
//            if (max_count == Constants.MAX_DOCUMENT) {
//                return@forEach
//            } else {
//                max_count++
//                uri.updateCaptureImageUI(context, picDocumentListener, type)
//            }
//        }
//
//    }
//}

fun Uri.updateCaptureImageUI(
    context: Context,
    picDocumentListener: PicDocumentSelector,
    type: Int
) {


    val extension: String
    val profileImgBase64: String
    var selectedDocData: SelectedDocData? = null
    var selectedImage: Bitmap? = null

    when (this.scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            val returnCursor: Cursor? =
                context.contentResolver.query(this, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            val name = returnCursor?.getString(nameIndex!!)
            extension = name?.substring(name.lastIndexOf(".") + 1).toString()
            profileImgBase64 = this.convertFileUriToString(context).toString()
            if (extension == "jpg" || extension == "jpeg" || extension == "png") {
                val imageStream: InputStream? = context.contentResolver?.openInputStream(this)
                selectedImage = BitmapFactory.decodeStream(imageStream)
            }
            selectedDocData =
                SelectedDocData(
                    bitmap = selectedImage,
                    imgName = name.toString(),
                    base64 = profileImgBase64,
                    "",
                    fileFlag = extension
                )
        }
        ContentResolver.SCHEME_FILE -> {
            val path = FileUtils.getPath(context, this)
            extension = path.substring(path.lastIndexOf(".") + 1)
            profileImgBase64 = path.convertFilePathToByte().toString()
            val fileName = path?.substring(path.lastIndexOf("/") + 1).toString()
            if (extension == "jpg" || extension == "jpeg" || extension == "png") {
                val imageStream: InputStream? = context.contentResolver?.openInputStream(this)
                selectedImage = BitmapFactory.decodeStream(imageStream)
            }
            selectedDocData =
                SelectedDocData(
                    bitmap = selectedImage,
                    imgName = fileName,
                    base64 = profileImgBase64,
                    "",
                    fileFlag = extension
                )
        }
        else -> {

            val path = try {
                RealPathUtil.getRealPath(context, this)
            } catch (e: java.lang.Exception) {
                this.path
            }
            extension = FilenameUtils.getExtension(path)
            val fileName = path?.substring(path.lastIndexOf("/") + 1).toString()

            if (extension == "jpg" || extension == "jpeg" || extension == "png") {
                val imageStream: InputStream? = context.contentResolver?.openInputStream(this)
                selectedImage = BitmapFactory.decodeStream(imageStream)
                selectedDocData = selectedImage.getBase64()?.let {
                    SelectedDocData(
                        bitmap = selectedImage,
                        imgName = fileName,
                        base64 = it,
                        "",
                        fileFlag = extension
                    )
                }
            }
        }
    }
    if (selectedDocData != null) {
        picDocumentListener.onProfilePicSelected(selectedDocData, type)
    }
}

private fun String.convertFilePathToByte(): String? {
    val file = File(this)
    val size = file.length().toInt()
    val bytes = ByteArray(size)
    var encodeString: String? = null
    try {
        val buf = BufferedInputStream(FileInputStream(file))
        buf.read(bytes, 0, bytes.size)
        buf.close()
        encodeString = encodeToString(bytes, Base64.DEFAULT)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return encodeString
}

private fun Uri.convertFileUriToString(mActivity: Context): String? {
    Log.d("data", "onActivityResult: uri$this")
    var ansValue: String? = ""
    try {
        val inputStream = mActivity.contentResolver.openInputStream(this)
        val bytes: ByteArray? = inputStream?.getBytes()
        if (bytes != null) {
            if (bytes.isNotEmpty()) {
                Log.d("data", "onActivityResult: bytes size=" + bytes.size)

                ansValue = encodeToString(bytes, Base64.DEFAULT)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("error", "onActivityResult: $e")
    }
    return ansValue
}

@Throws(IOException::class)
fun InputStream.getBytes(): ByteArray {
    val byteBuffer = ByteArrayOutputStream()
    try {
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (this.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
    } catch (e: java.lang.Exception) {
    }
    return byteBuffer.toByteArray()
}


fun ImageView.showImageAsBitmap(bitmap: Bitmap) {
    Glide.with(this.context)
        .asBitmap()
        .load(bitmap).into(this)
}

fun ImageView.loadImageURL(
    context: Context,
    imagePath: String,
    user: Boolean = false,
    doctor: Boolean = false,
    isNoCache: Boolean = false
) {
    var isUser = user
    var isDoctor = doctor
    if (!isUser && !isDoctor) {
        when (PrefUtils.getLoginUserType(context)) {
            1 -> {
                isDoctor = true
            }
            2 -> {
                isUser = true
            }
            3 -> {
                isUser = true
            }
        }
    }

    if (isNoCache) {
        Glide.with(context)
            .load(imagePath).placeholder(R.drawable.ic_pic_placeholder)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    } else {
        if (isDoctor) {
            Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.dr)
                .error(R.drawable.dr)
                .into(this)
        } else if (isUser) {
            Glide.with(context)
                .load(imagePath).placeholder(R.drawable.ic_pic_placeholder)
                .error(R.drawable.ic_pic_placeholder)
                .into(this)
        }
    }
}

fun Context.getFromType(): Int {
    return PrefUtils.getLoginUserType(this)
}

fun Context.getSenderId(): String {
    return when (PrefUtils.getLoginUserType(this)) {
        UserTypes.PATIENT.type -> {
            PrefUtils.getPatientUserData(this)?.patientInfoId.toString()
        }
        UserTypes.CHO.type -> {
            PrefUtils.getChoData(this)?.memberId.toString()
        }
        UserTypes.DOCTOR.type -> {
            PrefUtils.getDoctorData(this)?.memberId.toString()
        }
        else -> {
            "0"
        }
    }
}

fun TextView.manageOnlineOfflineStatusWithColor(
    context: Context,
    doctorSpecializationData: DoctorSpecializationData
) {
    this.text = doctorSpecializationData.doctor_Status
    when (doctorSpecializationData.doctor_Status) {
        "Offline" -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.offline_color))
        }
        "Busy" -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
    }
}

fun TextView.manageOnlineOfflineStatusWithColor(
    context: Context,
    doctorStatus: String
) {
    this.text = doctorStatus
    when (doctorStatus) {
        "Offline" -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.offline_color))
        }
        "Busy" -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else -> {
            this.setTextColor(ContextCompat.getColor(context, R.color.green))
        }
    }
}

fun String.capitalizeLetter(): String {
    val split = this.split(" ")
    val stringBuilder = StringBuilder()
    split.forEach { eachWord ->
        val str = eachWord.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(Locale.getDefault())
            } else {
                it.toString()
            }
        }
        stringBuilder.append("$str ")
    }
    return stringBuilder.toString()
}

fun Context.confirmationDialog(
    title: String,
    description: String,
    confirmation: (delete: Boolean, position: Int, context: Context) -> Unit,
    adapterPosition: Int
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(description)
        .setPositiveButton(
            "Yes"
        ) { dialog, whichButton ->
            confirmation.invoke(true, adapterPosition, this)
            dialog.dismiss()
        }
        .setNegativeButton("No") { dialog, whichButton ->
            confirmation.invoke(false, adapterPosition, this)
            dialog.dismiss()
        }.show()
}

fun String.getFileName(): String {
    return this.substring(this.lastIndexOf("/") + 1)
}

fun String.splitName(): String {
    if (this.length > 15) {
        return this.replace(" ", "\n")
    }
    return this
}

fun Context.getTmpFileUri(): Uri {
    val tmpFile =
        File.createTempFile(Calendar.getInstance().timeInMillis.toString(), ".png", cacheDir)
            .apply {
                createNewFile()
                deleteOnExit()
            }

    return FileProvider.getUriForFile(
        applicationContext,
        "${BuildConfig.APPLICATION_ID}.provider",
        tmpFile
    )
}

fun Context.checkCameraReadExternalPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun ImageView.setIcon(context: Context, fileType: String) {
    if (fileType.lowercase(Locale.getDefault())
            .contains("jpg") || fileType.lowercase(Locale.getDefault()).contains("jpeg")
    ) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    } else if (fileType.lowercase(Locale.getDefault()).contains("jifif")) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    } else if (fileType.lowercase(Locale.getDefault()).contains("bmp")) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    } else if (fileType.lowercase(Locale.getDefault()).contains("pdf")) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    } else if (fileType.lowercase(Locale.getDefault()).contains("png")) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    } else if (fileType.lowercase(Locale.getDefault()).contains("gif")) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf))
    }
}

fun Context.isNetworkConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    val isConnected = networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    if (!isConnected) {
        this.showToast(getString(R.string.no_internet_connection))
    }
    return isConnected
}

fun Context.showYearsDialog(
    ageSelect: (String, Dialog) -> Unit
) {
    val ageDialog = Dialog(this)
    ageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    ageDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    ageDialog.setContentView(R.layout.layout_list_dialog)
    val rvDialogList =
        ageDialog.findViewById<RecyclerView>(R.id.rvDialogList)

    ageDialog.setCancelable(true)
    ageDialog.setCanceledOnTouchOutside(true)
    ageDialog.show()
    ageDialog.fullWidthDialog()
    rvDialogList.setVerticalLayoutManager(this)
    val ageList = ArrayList<String>()
    for (i in 0..110) {
        ageList.add(i.toString())
    }
    rvDialogList.adapter = AgeMonthAdapter(ageList, ageDialog, ageSelect)
}

fun Context.showMonthsDialog(
    ageSelect: (String, Dialog) -> Unit
) {
    val ageDialog = Dialog(this)
    ageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    ageDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    ageDialog.setContentView(R.layout.layout_list_dialog)
    val rvDialogList =
        ageDialog.findViewById<RecyclerView>(R.id.rvDialogList)

    ageDialog.setCancelable(true)
    ageDialog.setCanceledOnTouchOutside(true)
    ageDialog.show()
    ageDialog.fullWidthDialog()
    rvDialogList.setVerticalLayoutManager(this)
    val ageList = ArrayList<String>()
    for (i in 0..12) {
        ageList.add(i.toString())
    }
    rvDialogList.adapter = AgeMonthAdapter(ageList, ageDialog, ageSelect)
}


fun Context.showStringListDialog(
    stringList: ArrayList<String>, performClick: (String, Dialog) -> Unit
) {
    val stringListDialog = Dialog(this)
    stringListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    stringListDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    stringListDialog.setContentView(R.layout.layout_list_dialog)
    val rvDialogList =
        stringListDialog.findViewById<RecyclerView>(R.id.rvDialogList)

    stringListDialog.setCancelable(true)
    stringListDialog.setCanceledOnTouchOutside(true)
    stringListDialog.show()
    stringListDialog.fullWidthDialog()
    rvDialogList.setVerticalLayoutManager(this)

    rvDialogList.adapter =
        CommonStringListDialogAdapter(stringList, stringListDialog, performClick)
}
