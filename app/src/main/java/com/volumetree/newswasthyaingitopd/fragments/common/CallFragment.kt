package com.volumetree.newswasthyaingitopd.fragments.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.peoplelink.instapeer.InstaSDK
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentCallBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.interfaces.VCConnectListener
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import org.webrtc.RendererCommon

class CallFragment(private val doctorName: String = "") : DialogFragment(), View.OnClickListener {


    private var _binding: FragmentCallBinding? = null
    val binding get() = _binding
    private var p2PClientControls: InstaSDK? = null
    private val TAG = "CallFragment"
    private var voiceOn = false
    private var videoOn: Boolean = false
    private var mVCListener: VCConnectListener? = null

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.imgBack?.setOnClickListener(this)
        binding?.imgRX?.setOnClickListener(this)
        activity?.showBottomNavigationView(false)
        binding!!.tvActionbarTitle.text = doctorName
        setupRenderesView()
        if (PrefUtils.getLoginUserType(requireActivity()) == UserTypes.DOCTOR.type) {
            binding!!.imgRX.visibility = View.VISIBLE
        }
    }

    private fun setupRenderesView() {
        binding?.receiverRenderer?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        binding?.receiverRenderer?.setEnableHardwareScaler(true)
        binding?.receiverRenderer?.setZOrderMediaOverlay(true)
        binding?.imgEndCall?.setOnClickListener(this)
        binding?.imgChat?.setOnClickListener(this)
        binding?.imgMoreOption?.setOnClickListener(this)
        binding?.selfRenderer?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        binding?.selfRenderer?.setEnableHardwareScaler(true)
        binding?.selfRenderer?.setZOrderMediaOverlay(true)
        binding?.let {
            mVCListener!!.onReady(
                it.selfRenderer,
                it.receiverRenderer,
                it.topLayout,
                it.bottomOptions
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mVCListener = context as VCConnectListener
    }

    override fun onDetach() {
        super.onDetach()
        binding?.receiverRenderer?.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var moreOptionPopup: PopupWindow? = null

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                requireActivity().onBackPressed()
            }
            R.id.imgRX -> {
                mVCListener?.onShowRXForDoctor()
            }
            R.id.imgEndCall -> {
                mVCListener?.onEndCall()
            }
            R.id.imgChat -> {
                mVCListener?.onChats()
            }
            R.id.imgMoreOption -> {
                dismissPopup()
                moreOptionPopup = showMoreOption()
                moreOptionPopup?.isOutsideTouchable = true
                moreOptionPopup?.isFocusable = true
                moreOptionPopup?.showAtLocation(binding?.imgMoreOption, Gravity.BOTTOM, 0, 250)
            }

        }
    }


    private fun showMoreOption(): PopupWindow {
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.call_more_option, null)
        view.findViewById<LinearLayout>(R.id.viewCaseRecord).setOnClickListener {
            mVCListener?.onViewCaseHistory()
            dismissPopup()
        }
        view.findViewById<LinearLayout>(R.id.viewPrescription).setOnClickListener {
            mVCListener?.onPrescription()
            dismissPopup()
        }

        return PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun dismissPopup() {
        moreOptionPopup?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            moreOptionPopup = null
        }
    }

    private fun audioOnOff(enable: Boolean) {
        try {
            if (enable) {
                if (p2PClientControls != null) {
                    InstaSDK.audioUnMute()
                    voiceOn = true
                    binding?.imgAudio?.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_audio)
                    )
                }
            } else {
                if (p2PClientControls != null) {
                    InstaSDK.audioMute()
                    voiceOn = false
                    binding?.imgAudio?.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.ic_mute
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "CallFragment audioOnOff fail : " + e.message.toString()
            )
        }
    }

    private fun videoOnOff(enable: Boolean) {
        try {
            if (enable) {
                if (p2PClientControls != null) {
                    InstaSDK.videoUnMute()
                    videoOn = true
                    binding?.imgVideo?.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_video)
                    )
                }
            } else {
                if (p2PClientControls != null) {
                    InstaSDK.videoMute()
                    videoOn = false
                    binding?.imgVideo?.setImageDrawable(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_video_off)
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "CallFragment videoOnOff fail : " + e.message.toString()
            )
        }
    }

//    private fun cameraChange() {
//        try {
//            if (p2PClientControls != null) {
//                InstaSDK.switchCamera()
//            }
//        } catch (e: Exception) {
//            Log.e(
//                TAG,
//                "CallFragment cameraChange fail : " + e.message.toString()
//            )
//        }
//    }

    fun onPublished(succeed: Boolean, p2PClient: InstaSDK?) {
        try {
            if (succeed && p2PClient != null && binding != null) {
                this.p2PClientControls = p2PClient

                binding!!.imgVideo.visibility = View.VISIBLE
                binding!!.imgAudio.visibility = View.VISIBLE

                if (this.p2PClientControls != null) {
                    binding!!.imgAudio.setOnClickListener {
                        audioOnOff(!voiceOn)
                    }

                    binding!!.imgVideo.setOnClickListener {
                        videoOnOff(!videoOn)
                    }

                }
            } else {
                binding?.imgVideo?.visibility = View.GONE
                binding?.imgAudio?.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e(TAG, "CallFragment onPublished fail : " + e.message.toString())
        }
    }

}