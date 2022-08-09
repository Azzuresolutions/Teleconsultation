package com.volumetree.newswasthyaingitopd.utils


class Constants {
    companion object {
        const val LOGIN_MOBILE = "mobileNo"
        const val LOGIN_OTP = "otp"

        const val PATIENT_TYPE = 2
        const val DOCTOR_TYPE = 1
        const val CHO_TYPE = 3

        const val ALLERGY = "Allergy"
        const val FAMILY_MEMBER = "FamilyMember"
        const val FILE_PICKER = "FilePicker"
        const val PROBLEM = "Problem"
        const val MEDICINE = "Medicine"
        const val DIAGNOSIS = "Diagnosis"
        const val SELECT_DOCTOR = "SelectDoctor"
        const val SELECT_SPECIALIZATION = "SelectSpecialization"
        const val SELECTED_ALLERGIES = "selectedAllergies"
        const val PREVIEW_CASE = "previewCase"
        const val SELECTED_VITALS = "selectedVital"

        const val MESSAGE_DATA = "MESSAGE_DATA"
        const val TO_DOCTOR_ID = "doctorId"
        const val IS_CONSULTATION_HISTORY = "consultationHistory"
        const val TO_TYPE = "toType"
        const val TO_DOCTOR_NAME = "doctorName"
        const val CONSULTATION_ID = "consultationId"
        const val DOCTOR_PROFILE_PIC = "profilePic"


        const val MAX_DOCUMENT = 10

        val imageExtensions = arrayOf("jpg", "jpeg", "png")


        private const val MIME_TYPE_PDF = "application/pdf"
        private const val MIME_TYPE_DOC = "application/msword"
        private const val MIME_TYPE_DOCX =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        private const val MIME_TYPE_EXCEL = "application/vnd.ms-excel"
        private const val MIME_TYPE_ODS = "application/vnd.oasis.opendocument.spreadsheet"
        private const val MIME_TYPE_XLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        private const val MIME_TYPE_TEXT_RTF = "text/rtf"
        private const val MIME_TYPE_TEXT_RICH_TEXT = "text/richtext"
        private const val MIME_TYPE_APPLICATION_RTF = "application/rtf"
        private const val MIME_TYPE_APPLICATION_X_RTF = "application/x-rtf"
        private const val MIME_TYPE_CSV = "text/comma-separated-values"
        private const val MIME_TYPE_TEXT = "text/plain"

        public const val TIMER_INTERVAL: Long = 45
        public const val QUEUE_STATE_INTERVAL: Long = 10

        fun getMimeTypeOfSupportedFiles(): Array<String> {
            return arrayOf(
                MIME_TYPE_PDF, MIME_TYPE_EXCEL,
                MIME_TYPE_XLSX, MIME_TYPE_DOCX,
                MIME_TYPE_DOC, MIME_TYPE_TEXT,
                MIME_TYPE_ODS, MIME_TYPE_CSV,
                MIME_TYPE_TEXT_RTF, MIME_TYPE_APPLICATION_RTF,
                MIME_TYPE_TEXT_RICH_TEXT, MIME_TYPE_APPLICATION_X_RTF
            )
        }


        var medicineFrequency = arrayListOf("BD", "HS", "OS", "QID", "SOS", "TDS", "TSF")

        var doseQuantity = arrayListOf("1/2", "1", "1 1/2", "2", "3")

        var doseType = arrayListOf(
            "tablet(s)/capsule(s)",
            "drop(s)/tsp(s)",
            "puff(s)/application(s)",
            "Injection"
        )

        var doseDurationType =
            arrayListOf(
                "Day/Days",
                "Week/Weeks",
                "Month/Months",
                "Year/Years"
            )

        var doseNoOfDay =
            arrayListOf(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
            )
    }
}