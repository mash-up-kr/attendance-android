package com.mashup.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.FragmentScheduleBinding
import com.mashup.ui.main.MainViewModel
import com.mashup.ui.webview.tester.WebViewTesterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val viewModel: ScheduleViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override val layoutId: Int = R.layout.fragment_schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getScheduleList()
    }

    override fun initViews() {
        super.initViews()
        val context = this.requireActivity()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.shakeEvent.collectLatest {
                startActivity(
                    WebViewTesterActivity.newIntent(context)
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.start()
            }
        }
        viewBinding.cvSchedule.setContent {
            MashUpTheme {
                ScheduleRoute(
                    modifier = Modifier.fillMaxSize(),
                    mainViewModel = mainViewModel,
                    viewModel = viewModel
                )
            }
        }
    }

    companion object {

        fun newInstance() = ScheduleFragment()
    }
}
