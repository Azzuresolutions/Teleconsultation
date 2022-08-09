package com.volumetree.newswasthyaingitopd.interfaces

import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import org.webrtc.SurfaceViewRenderer

interface VCConnectListener {
    fun onReady(
        localRenderer: SurfaceViewRenderer,
        remoteRenderer: SurfaceViewRenderer,
        topLayout: ConstraintLayout,
        bottomOptions: LinearLayout
    )

    fun onEndCall()
    fun onChats()
    fun onShowRXForDoctor()
    fun onPrescription()
    fun onViewCaseHistory()
}