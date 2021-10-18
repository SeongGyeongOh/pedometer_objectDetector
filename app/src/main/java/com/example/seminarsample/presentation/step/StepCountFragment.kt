package com.example.seminarsample.presentation.step

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.seminarsample.BaseFragment
import com.example.seminarsample.databinding.FragmentStepCountBinding
import com.example.seminarsample.utils.Logger
import com.example.seminarsample.utils.Pref
import com.example.seminarsample.utils.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepCountFragment @Inject constructor() : BaseFragment<FragmentStepCountBinding>() {

    private val viewModel: StepCountViewModel by viewModels()
    private var lastProgress = 0f
    private var fragment : Fragment? = null
    private var last : Float = 0f

    @Inject
    lateinit var pref: Pref

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepCountBinding {
        return FragmentStepCountBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisibility()

        binding.graphBtn.setOnClickListener {
            navigateToGraphFragment()
        }

        binding.startWalkBtn.setOnClickListener {
            checkPermission(Build.VERSION.SDK_INT)
        }

        binding.endWalkBtn.setOnClickListener {
            stopService()
        }

        requestIntent()
        handleState()
    }

    private fun navigateToGraphFragment() {
        val action = StepCountFragmentDirections.actionStepCountFragmentToStepCountGraphFragment()
        findNavController().navigate(action)
    }

    private fun requestIntent() {
        viewModel.setIntent(
            StepCountIntent.GetTodayData(date = System.currentTimeMillis().getCurrentDate())
        )
    }

    private fun handleState() {
        viewModel.stepCountState.asLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is StepCountState.TodayCount -> {
                    state.walkData.asLiveData().observe(viewLifecycleOwner) {
                        binding.walkFixText.text = "오늘 걸은 걸음 :  ${it.count}"
                    }
                }
                is StepCountState.Fail -> {
                    Logger.d("실패 ${state.error.message}")
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(sdk: Int) {
        val permission = if (sdk >= 29) {
            Manifest.permission.ACTIVITY_RECOGNITION
        } else {
            "com.google.android.gms.permission.ACTIVITY_RECOGNITION"
        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) -> {
                startService()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
    }

    private val permissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startService()
        } else {
            Toast.makeText(requireContext(), "만보기 사용을 위해 권한을 허용해야 합니다", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }

    private fun startService() {
        pref.setBoolValue("isServiceRunning", true)
        val intent = Intent(requireContext(), StepCountService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(intent)
        } else {
            activity?.startService(intent)
        }

        setVisibility()
    }

    private fun stopService() {
        pref.setBoolValue("isServiceRunning", false)
        setVisibility()

        val intent = Intent(requireContext(), StepCountService::class.java)
        activity?.stopService(intent)
    }

    private fun setVisibility() {
        Logger.d("비저빌리티 확인 ${pref.getBoolVal("isServiceRunning")}")
        if (pref.getBoolVal("isServiceRunning")) {
            binding.startWalkBtn.visibility = View.GONE
            binding.walkFixText.visibility = View.VISIBLE
        } else {
            binding.startWalkBtn.visibility = View.VISIBLE
            binding.walkFixText.visibility = View.GONE
        }
    }
}