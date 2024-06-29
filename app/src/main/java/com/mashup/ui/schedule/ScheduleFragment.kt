package com.mashup.ui.schedule

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.common.R as CR
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.FragmentScheduleBinding
import com.mashup.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        requireActivity().setStatusBarColorRes(CR.color.white)
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
