package com.example.seminarsample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>(

) : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    private var useBackPressed: Boolean = false
    protected open fun useBackPress(): Boolean =  useBackPressed

    private lateinit var backPressCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        if (useBackPress()) {
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(this, backPressCallback)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun onBackPressed() {

    }

    open fun setBackPressed(_useBackPressed: Boolean) {
        useBackPressed = _useBackPressed
    }
}