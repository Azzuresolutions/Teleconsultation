package com.volumetree.newswasthyaingitopd.utils

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse


class CommonDialog {
    companion object {

        fun showTokenDialog(
            context: Context,
            draftConsultationResponse: ConsultationOnlineDoctorResponse,
            confirmationClick: (ConsultationOnlineDoctorResponse) -> Unit
        ) {
            val tokenDialog = Dialog(context)
            tokenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            tokenDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            tokenDialog.setContentView(R.layout.layout_token_dialog)
            tokenDialog.setCancelable(true)
            tokenDialog.setCanceledOnTouchOutside(true)
            tokenDialog.show()
            tokenDialog.fullWidthDialog()
            val btnContinue = tokenDialog.findViewById<Button>(R.id.btnContinue)
            btnContinue.isEnabled = true
            val tvToken = tokenDialog.findViewById<TextView>(R.id.tvToken)
            tvToken.text = Html.fromHtml(
                "${context.getString(R.string.dear_user_token_sent_to_your_mobile_1201739227950662_is_your_patient_id_ch1_is_your_token_number)} <b>${
                    draftConsultationResponse.token
                }</b>?"
            )
            btnContinue.setOnClickListener {
                confirmationClick.invoke(draftConsultationResponse)
                tokenDialog.dismiss()
            }
            tokenDialog.findViewById<TextView>(R.id.tvDoItLater)
                .setOnClickListener { tokenDialog.dismiss() }
        }

        fun showPatientAddedSuccessDialog(
            context: Context,
            confirmationClick: (ChoPatientResponse) -> Unit,
            addMoreClick: () -> Unit,
            choPatientData: ChoPatientResponse
        ) {
            val patientSuccess = Dialog(context)
            patientSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE)
            patientSuccess.window?.setBackgroundDrawableResource(R.color.transparent)
            patientSuccess.setContentView(R.layout.layout_patient_success)
            patientSuccess.setCancelable(false)
            patientSuccess.setCanceledOnTouchOutside(false)
            patientSuccess.show()
            patientSuccess.fullWidthDialog()
            val btnContinue = patientSuccess.findViewById<Button>(R.id.btnContinue)
            btnContinue.isEnabled = true
            btnContinue.setOnClickListener {
                confirmationClick.invoke(choPatientData)
                patientSuccess.dismiss()
            }
            patientSuccess.findViewById<TextView>(R.id.tvRegisterMore)
                .setOnClickListener {
                    addMoreClick.invoke()
                    patientSuccess.dismiss()
                }
        }


        fun showDraftRecordSuccess(
            context: Context,
            ok: () -> Unit,
        ) {
            val draftDialog = Dialog(context)
            draftDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            draftDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            draftDialog.setContentView(R.layout.layout_save_draft)
            draftDialog.setCancelable(false)
            draftDialog.setCanceledOnTouchOutside(false)
            draftDialog.fullWidthDialog()
            draftDialog.findViewById<TextView>(R.id.tvOk)
                .setOnClickListener {
                    ok.invoke()
                    draftDialog.dismiss()
                }
            draftDialog.show()

        }

        class CustomBottomSheetDialogFragment(private val confirmationClick: () -> Unit) :
            BottomSheetDialogFragment() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
            }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                val view: View =
                    inflater.inflate(
                        R.layout.orange_bottomsheet_confirmation_dialog,
                        container,
                        false
                    )
                view.findViewById<TextView>(R.id.tvCancel)
                    .setOnClickListener { dismiss() }
                view.findViewById<TextView>(R.id.btnConfirm)
                    .setOnClickListener {
                        confirmationClick.invoke()
                        dismiss()
                    }
                return view
            }
        }


        class ShowExitConfirmationDialog(private val confirmationClick: () -> Unit) :
            BottomSheetDialogFragment() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
            }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                val view: View =
                    inflater.inflate(
                        R.layout.green_bottomsheet_confirmation_dialog,
                        container,
                        false
                    )
                view.findViewById<TextView>(R.id.tvCancel)
                    .setOnClickListener { dismiss() }
                view.findViewById<TextView>(R.id.btnConfirm)
                    .setOnClickListener {
                        confirmationClick.invoke()
                        dismiss()
                    }
                return view
            }
        }

        class ShowSelectAnotherTryAgainWaitingListDialog(
            private val anotherDoctor: () -> Unit,
            private val tryAgain: () -> Unit
        ) :
            BottomSheetDialogFragment() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
            }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                val view: View =
                    inflater.inflate(
                        R.layout.waiting_try_again_another_dialog,
                        container,
                        false
                    )
                view.findViewById<TextView>(R.id.tvTryAgain)
                    .setOnClickListener {
                        tryAgain.invoke()
                        dismiss()
                    }
                view.findViewById<TextView>(R.id.btnCallAnotherDoctor)
                    .setOnClickListener {
                        anotherDoctor.invoke()
                        dismiss()
                    }
                return view
            }
        }

        class ShowImagePicker(
            private val isAllOption: Boolean = false,
            val context: FragmentActivity,
            val fragment: Fragment,
            private val picDocumentListener: PicDocumentSelector,
            private val type: Int = 1,
            private val isMultiple: Boolean = false
        ) :
            BottomSheetDialogFragment() {

            private val fileUri = context.getTmpFileUri()

            private lateinit var resultLauncher: ActivityResultLauncher<String>
            private lateinit var documentMultipleLauncher: ActivityResultLauncher<Intent>
            private lateinit var resultLauncherMultiple: ActivityResultLauncher<String>
            private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                if (!requireActivity().checkCameraReadExternalPermission()) {
                    fragment.requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE
                        ),
                        100
                    )
                    dismiss()
                }

                resultLauncher = this.getImageLauncher(requireActivity(), picDocumentListener, type)
                documentMultipleLauncher =
                    this.getDocumentMultipleLauncher(requireActivity(), picDocumentListener, type)
                resultLauncherMultiple =
                    this.getImageLauncherMultiple(requireActivity(), picDocumentListener, type)
                takePhotoLauncher = this.takePhotoLauncher(::onImageCapture)

                view.findViewById<TextView>(R.id.tvGallery)
                    .setOnClickListener {

                        if (isMultiple) {
                            resultLauncherMultiple.launch("image/*")
                        } else {
                            resultLauncher.launch("image/*")
                        }
                    }

                view.findViewById<TextView>(R.id.tvFile)
                    .setOnClickListener {
                        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                        chooseFile.type = "*/*"
                        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        chooseFile.putExtra(
                            Intent.EXTRA_MIME_TYPES,
                            Constants.getMimeTypeOfSupportedFiles()
                        )

                        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                        documentMultipleLauncher.launch(chooseFile)
                    }

                view.findViewById<TextView>(R.id.tvCamera)
                    .setOnClickListener {
                        takePhotoLauncher.launch(fileUri)
                    }

                view.findViewById<TextView>(R.id.tvCancel)
                    .setOnClickListener {
                        dismiss()
                    }

                if (!isAllOption) {
                    view.findViewById<TextView>(R.id.tvFile).visibility = View.GONE
                } else {
                    view.findViewById<TextView>(R.id.tvFile).visibility = View.VISIBLE
                }
            }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                return inflater.inflate(
                    R.layout.image_picker_dialog,
                    container,
                    false
                )
            }

            private fun onImageCapture(isSuccess: Boolean) {
                if (isSuccess) {
                    fileUri.updateCaptureImageUI(context, picDocumentListener, type)
                }
            }
        }


    }
}