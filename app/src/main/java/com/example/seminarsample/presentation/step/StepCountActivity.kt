package com.example.seminarsample.presentation.step

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seminarsample.databinding.ActivityStepCountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepCountActivity : AppCompatActivity() {
    lateinit var binding : ActivityStepCountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStepCountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}