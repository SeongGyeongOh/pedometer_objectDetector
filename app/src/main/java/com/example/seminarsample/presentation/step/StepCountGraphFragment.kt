package com.example.seminarsample.presentation.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.example.seminarsample.BaseFragment
import com.example.seminarsample.R
import com.example.seminarsample.databinding.FragmentStepCountGraphBinding
import com.example.seminarsample.domain.model.StepCountModel
import com.example.seminarsample.utils.Logger
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepCountGraphFragment : BaseFragment<FragmentStepCountGraphBinding>() {

    private val viewModel: StepCountViewModel by viewModels()
    var cntList = listOf<StepCountModel>()
    var xAxisTitle = mutableListOf<String>()
    var title = mutableListOf<String>()
    var valueList = mutableListOf<Double>()
    val entries = mutableListOf<BarEntry>()
    var barChart: BarChart? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepCountGraphBinding {
        return FragmentStepCountGraphBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("[그래프 프래그먼트] onViewCreated")

        barChart = binding.barChart
        requestIntent()
        handleState()
    }

    private fun requestIntent() {
        viewModel.setIntent(StepCountIntent.GetData)
    }

    private fun handleState() {
        Logger.d("[그래프 프래그먼트] handleState")
        viewModel.stepCountState.asLiveData().observe(viewLifecycleOwner) { state ->
            when(state) {
                is StepCountState.TotalCount -> {
                    Logger.d("[그래프 프래그먼트] totalCount")

                    cntList = if (state.walkData.size <= 7) {
                        state.walkData
                    } else {
                        state.walkData.subList(state.walkData.size - 7, state.walkData.size)
                    }
                    setChart()
                }
                is StepCountState.Fail -> {
                    Logger.d("[그래프 프레그먼트] 데이터 못가져옴 ${state.error.message}")
                }
            }
        }
    }

    private fun setChart() {
        Logger.d("[그래프 프래그먼트] setChart")
        valueList = mutableListOf()

        cntList.forEach { data ->
            valueList.add(data.count.toDouble())
            xAxisTitle.add(data.date)
        }

        valueList.forEachIndexed { index, data ->
            val barEntry = BarEntry(index.toFloat(), data.toFloat())
            entries.add(barEntry)
        }

        val barDataSet = BarDataSet(entries, "걸음 수")
        val data = BarData(barDataSet)

        initBarDataSet(barDataSet)
        setBarChart(data)
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        barDataSet.apply {
            color = requireContext().getColor(R.color.chart_color)
            formSize = 15f
            setDrawValues(false)
            valueTextSize = 16f
        }
    }

    private fun setBarChart(data: BarData) {
        //우측 하단 그래프에 대한 설명 숨김
        val description = Description()
        description.isEnabled = false
        barChart?.description = description

        //차트 확대 못하게 설정
        barChart?.apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            setTouchEnabled(false)

            // y축의 왼쪽면 설정
            axisLeft.granularity = 1.0f
            axisLeft.isGranularityEnabled = true

            // y축의 오른쪽면 설정(안보이게)
            axisRight.setDrawLabels(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)
        }

        //x축 세팅
        barChart?.xAxis?.apply {
            labelCount = valueList.size
            valueFormatter = IndexAxisValueFormatter(xAxisTitle)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        //좌측 하단 bar에 대한 설명
        barChart?.legend?.apply {
            form = Legend.LegendForm.LINE
            textSize = 14f
        }

        barChart?.data = data
        barChart?.invalidate()
    }

    override fun onPause() {
        super.onPause()
        Logger.d("[그래프 프래그먼트] onPause")
    }

}