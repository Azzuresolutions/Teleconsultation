package com.volumetree.newswasthyaingitopd.fragments.cho

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoHomeBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DashboardConsultationsReportData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DashboardConsultationsReportsResponse
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChoHomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChoHomeBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private var tfRegular: Typeface? = null

    companion object {
        var isAddMoreNewPatient = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        binding.btnAddNewPatient.setOnClickListener(this)
        tfRegular = Typeface.createFromAsset(requireActivity().assets, "lexend_regular.ttf")
        getMemberProfile()

        val rotateAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotation)
        rotateAnim.fillAfter = true
        binding.tvConsultation.animation = rotateAnim


        if (isAddMoreNewPatient) {
            binding.btnAddNewPatient.performClick()
        }
        binding.rgFilter.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rgWeek -> {
                    masterViewModel.getConsultationsDayDashboard(1, 1)
                        .observeOnce(viewLifecycleOwner) {
                            manageDayWiseReports(it)
                        }
                }
                R.id.rgMonthly -> {
                    masterViewModel.getConsultationsDayDashboard(2, 1)
                        .observeOnce(viewLifecycleOwner) {
                            manageDayWiseReports(it)
                        }
                }
                R.id.rgYearly -> {
                    masterViewModel.getConsultationsDayDashboard(3, 1)
                        .observeOnce(viewLifecycleOwner) {
                            manageDayWiseReports(it)
                        }
                }
            }
        }
        binding.rgWeek.isChecked = true


    }

    private fun manageDayWiseReports(dashboardConsultationsReportsResponse: DashboardConsultationsReportsResponse) {
        if (dashboardConsultationsReportsResponse.success && dashboardConsultationsReportsResponse.lstModel.size > 0) {
            binding.chartConsultation.invalidate()
            binding.chartConsultation.refreshDrawableState()
            setupLineChart(dashboardConsultationsReportsResponse.lstModel)
        }
    }

    private fun getMemberProfile() {
        profileViewModel.getMemberProfile().observeOnce(viewLifecycleOwner) { choProfileResponse ->
            if (choProfileResponse.success) {
                setupDashBoardData()
                binding.layToolbar.tvLoginUserName.text =
                    choProfileResponse.model?.firstName?.capitalizeLetter() ?: ""
                binding.layToolbar.tvLoginUserName.isSelected = true
                PrefUtils.setChoData(requireActivity(), choProfileResponse.model)
                PrefUtils.setLoginToken(requireActivity(), choProfileResponse.token)
                choProfileResponse.model?.let {
                    binding.layToolbar.imgProfile.loadImageURL(
                        requireActivity(),
                        it.imagePath,
                        isNoCache = true
                    )
                }
            }
        }
    }

    private fun setupDashBoardData() {
        masterViewModel.getConsultationsDashboard(1).observeOnce(viewLifecycleOwner) {
            if (it.success && it.lstModel.size > 0) {
                val dashboardConsultationsData = it.lstModel[0]
                binding.tvTotalConsultation.text = dashboardConsultationsData.totalCons
                binding.tvTodaysConsultation.text = dashboardConsultationsData.prescription
                binding.tvTotalTeleConsultation.text = dashboardConsultationsData.avgCons
                binding.tvTodaysTeleConsultation.text = dashboardConsultationsData.totalPatient
            }
            setupCharts()
        }
    }

    private fun setupCharts() {
        masterViewModel.getConsultationsVC(1, 1).observeOnce(viewLifecycleOwner) {
            if (it.success && it.lstModel.size > 0) {
                binding.chartNoMinutesCall.invalidate()
                binding.chartNoMinutesCall.refreshDrawableState()
                setupMinuteOfVideoCallChart(it.lstModel)
            }
        }
        masterViewModel.getConsultationsGenderDashboard(1).observeOnce(viewLifecycleOwner) {
            if (it.success && it.lstModel != null && it.lstModel.size > 0) {
                binding.chatHorizontalDistribution.invalidate()
                binding.chatHorizontalDistribution.refreshDrawableState()
                setupHorizontalCharts(it.lstModel)
            }
        }
    }


    private fun setupHorizontalCharts(lstModel: ArrayList<DashboardConsultationsReportData>) {

        binding.chatHorizontalDistribution.setDrawBarShadow(false)

        binding.chatHorizontalDistribution.setDrawValueAboveBar(true)

        binding.chatHorizontalDistribution.description.isEnabled = false


        binding.chatHorizontalDistribution.setPinchZoom(false)

        binding.chatHorizontalDistribution.setDrawGridBackground(false)

        binding.chatHorizontalDistribution.setFitBars(true)
        binding.chatHorizontalDistribution.animateY(1000)

        val xl: XAxis = binding.chatHorizontalDistribution.xAxis
        xl.position = XAxisPosition.BOTTOM
        xl.setDrawAxisLine(false)
        xl.setDrawGridLines(false)

        val yl: YAxis = binding.chatHorizontalDistribution.axisLeft
        yl.setDrawAxisLine(false)
        yl.setDrawGridLines(false)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)

//        yl.setInverted(true);

        //        yl.setInverted(true);
        val yr: YAxis = binding.chatHorizontalDistribution.axisRight
        yr.setDrawAxisLine(false)
        yr.setDrawGridLines(false)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)

//        yr.setInverted(true);

        val l: Legend = binding.chatHorizontalDistribution.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.formSize = 8f
        l.xEntrySpace = 4f
        setData(lstModel)
    }

    private fun setData(count: ArrayList<DashboardConsultationsReportData>) {
        val spaceForBar = 15f
        val values = ArrayList<BarEntry>()
        for (i in 0 until count.size) {
            val reportData = count[i]
            val value = reportData.value.toFloat()
            val barEntry = BarEntry(
                i * spaceForBar, value,
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_close)
            )
            values.add(barEntry)

            val set1 = BarDataSet(values, reportData.label)


            set1.colors = arrayListOf(
                ColorTemplate.rgb("#044BB4"),
                ColorTemplate.rgb("#F878A9"),
                ColorTemplate.rgb("#690098"),
            )
            set1.setDrawIcons(false)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTypeface(tfRegular)
            data.setValueTextSize(10f)
            val barWidth = 10f
            data.barWidth = barWidth
            binding.chatHorizontalDistribution.axisLeft.setDrawGridLines(false)
            binding.chatHorizontalDistribution.xAxis.setDrawGridLines(false)
            binding.chatHorizontalDistribution.axisRight.setDrawGridLines(false)
            binding.chatHorizontalDistribution.axisRight.isEnabled = false
            binding.chatHorizontalDistribution.xAxis.isEnabled = false
            binding.chatHorizontalDistribution.axisLeft.isEnabled = false
            binding.chatHorizontalDistribution.legend.isEnabled = false
            binding.chatHorizontalDistribution.axisLeft.axisMinimum = 0F
            binding.chatHorizontalDistribution.data = data
        }
    }


    private fun setupMinuteOfVideoCallChart(lstModel: ArrayList<DashboardConsultationsReportData>) {

        binding.chartNoMinutesCall.setDrawBarShadow(false)
        binding.chartNoMinutesCall.setDrawValueAboveBar(true)

        binding.chartNoMinutesCall.description.isEnabled = false
        binding.chartConsultation.legend.isEnabled = false

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        binding.chartNoMinutesCall.setPinchZoom(true)

        binding.chartNoMinutesCall.setDrawGridBackground(true)


        val xAxis: XAxis = binding.chartNoMinutesCall.xAxis
        val position = XAxisPosition.BOTTOM
        xAxis.position = position
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 315f
        xAxis.valueFormatter = ClaimsXAxisValueFormatter(lstModel)

        setVideoCallMinuteChartData(lstModel)
    }

    private fun setVideoCallMinuteChartData(
        count: ArrayList<DashboardConsultationsReportData>,
    ) {
        val values = java.util.ArrayList<BarEntry>()
        for (i in 0 until count.size) {
            val reportData = count[i]
            val value = reportData.value.toFloat()
            values.add(BarEntry(i.toFloat(), value))
        }
        val set1: IBarDataSet
        if (binding.chartNoMinutesCall.data != null &&
            binding.chartNoMinutesCall.data.dataSetCount > 0
        ) {
            binding.chartNoMinutesCall.data.notifyDataChanged()
            binding.chartNoMinutesCall.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            val startColor1 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_light)
            val startColor2 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_light)
            val startColor3 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_light)
            val startColor4 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_green_light)
            val startColor5 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_red_light)
            val endColor1 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark)
            val endColor2 = ContextCompat.getColor(requireActivity(), android.R.color.holo_purple)
            val endColor3 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark)
            val endColor4 = ContextCompat.getColor(requireActivity(), android.R.color.holo_red_dark)
            val endColor5 =
                ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark)
            val gradientFills: MutableList<Fill> = java.util.ArrayList<Fill>()
            gradientFills.add(Fill(startColor1, endColor1))
            gradientFills.add(Fill(startColor2, endColor2))
            gradientFills.add(Fill(startColor3, endColor3))
            gradientFills.add(Fill(startColor4, endColor4))
            gradientFills.add(Fill(startColor5, endColor5))
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfRegular)
            data.barWidth = 0.5f
            binding.chartNoMinutesCall.axisLeft.setDrawGridLines(true)
            binding.chartNoMinutesCall.xAxis.setDrawGridLines(false)
            binding.chartNoMinutesCall.axisRight.setDrawGridLines(false)
            binding.chartNoMinutesCall.axisRight.isEnabled = false
            binding.chartNoMinutesCall.xAxis.labelRotationAngle = 315f
            binding.chartNoMinutesCall.legend.isEnabled = false
            binding.chartNoMinutesCall.data = data
            binding.chartNoMinutesCall.axisLeft.axisMinimum = 0F
        }
    }

    private fun setupLineChart(lstModel: ArrayList<DashboardConsultationsReportData>) {

//        binding.chartConsultation.setBackgroundColor(Color.TRANSPARENT)

        // disable description text

        // disable description text
        binding.chartConsultation.description.isEnabled = false
        binding.chartConsultation.legend.isEnabled = false
        // enable touch gestures

        // enable touch gestures
        binding.chartConsultation.setTouchEnabled(true)

        binding.chartConsultation.setDrawGridBackground(false)

        // create marker to display box when values are selected

        // create marker to display box when values are selected
        val mv = MyMarkerView(requireActivity(), R.layout.custom_marker_view)

        // Set the marker to the chart

        // Set the marker to the chart
        mv.chartView = binding.chartConsultation
        binding.chartConsultation.marker = mv

        // enable scaling and dragging

        // enable scaling and dragging

        val xAxis: XAxis = binding.chartConsultation.xAxis
        val position = XAxisPosition.BOTTOM
        xAxis.position = position
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 315f
        xAxis.valueFormatter = ClaimsXAxisValueFormatter(lstModel)
        xAxis.setCenterAxisLabels(true)

        binding.chartConsultation.isDragEnabled = true
        binding.chartConsultation.setScaleEnabled(true)
        binding.chartConsultation.setPinchZoom(true)
        binding.chartConsultation.isAutoScaleMinMaxEnabled = true
        setDataConsultation(lstModel)
    }

    private fun setDataConsultation(
        count: ArrayList<DashboardConsultationsReportData>
    ) {
        val values = java.util.ArrayList<Entry>()
        for (i in 0 until count.size) {
            val reportData = count[i]
            val value = reportData.value.toFloat()
            values.add(
                Entry(
                    i.toFloat(),
                    value,
                    ContextCompat.getDrawable(requireActivity(), R.drawable.ic_close)
                )
            )
        }
        val set1: LineDataSet
        if (binding.chartConsultation.data != null &&
            binding.chartConsultation.data.dataSetCount > 0
        ) {
            set1 = binding.chartConsultation.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            binding.chartConsultation.data.notifyDataChanged()
            binding.chartConsultation.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "")
            set1.setDrawIcons(false)

            // black lines and points
            set1.color = ContextCompat.getColor(requireActivity(), R.color.blue_color)

            set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.blue_color))

            // line thickness and point size
            set1.lineWidth = 2f
            set1.circleRadius = 5f

            // draw points as solid circles
            set1.setDrawCircleHole(true)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 0.0F

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 0f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> binding.chartConsultation.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.fade_blue)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets = java.util.ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            binding.chartConsultation.axisLeft.setDrawGridLines(true)
            binding.chartConsultation.xAxis.setDrawGridLines(false)
            binding.chartConsultation.axisRight.setDrawGridLines(false)
            binding.chartConsultation.axisRight.isEnabled = false
            binding.chartConsultation.axisLeft.isEnabled = true
            binding.chartConsultation.axisLeft.axisMinimum = 0F
            binding.chartConsultation.legend.isEnabled = false
            binding.chartConsultation.data = data

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnAddNewPatient -> {
                findNavController().navigate(ChoHomeFragmentDirections.actionCreatePatientCho())
            }
        }
    }

}