package com.example.seminarsample

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.seminarsample.databinding.ActivityMainBinding
import com.example.seminarsample.presentation.scanner.ObjectDetectorActivity
import com.example.seminarsample.presentation.step.StepCountActivity
import com.example.seminarsample.utils.PermissionObserver
import com.example.seminarsample.utils.startAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionObserver: PermissionObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveToStepCount()
        moveToObjectDetector()

        setPermissionObserver()
    }

    override fun onResume() {
        super.onResume()
        binding.root.isVisible = true
    }

    private fun setPermissionObserver() {
        permissionObserver = PermissionObserver(activityResultRegistry)
        lifecycle.addObserver(permissionObserver)
    }

    private fun moveToStepCount() {
        val animation = setButtonAnimation()

        binding.btnWalk.setOnClickListener {
            binding.btnWalk.isVisible = false
            binding.btnCapture.isVisible = false
            binding.circle.isVisible = true
            binding.circle.startAnimation(animation) {
                binding.root.isVisible = false
                binding.circle.isVisible = false
                binding.btnWalk.isVisible = true
                binding.btnCapture.isVisible = true
                val intent = Intent(this@MainActivity, StepCountActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
            }
        }
    }

    private fun moveToObjectDetector() {
        val animation = setButtonAnimation()

        binding.btnCapture.setOnClickListener {
            permissionObserver.requestCameraPermission { isGranted ->
                if(isGranted) {
                    binding.btnWalk.isVisible = false
                    binding.btnCapture.isVisible = false
                    binding.circle.isVisible = true
                    binding.circle.startAnimation(animation) {
                        binding.root.isVisible = false
                        binding.circle.isVisible = false
                        binding.btnWalk.isVisible = true
                        binding.btnCapture.isVisible = true
                        val intent = Intent(this@MainActivity, ObjectDetectorActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setButtonAnimation(): Animation {
        return AnimationUtils.loadAnimation(this, R.anim.circle_explosion_anim).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
        }
    }
}