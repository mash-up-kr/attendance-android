package com.mashup.ui.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.common.R as CR
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.databinding.FragmentMyPageBinding
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {
    override val layoutId: Int = R.layout.fragment_my_page

    private val viewModel: MyPageViewModel by viewModels()

    private val myProfileEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) viewModel.getMyPageData()
    }

    private val attendanceAdapter by lazy {
        AttendanceListAdapter().apply {
            setOnItemClickListener(object : AttendanceListAdapter.OnItemEventListener {
                override fun onTotalAttendanceClick() {
                    showAttendanceInfoDialog()
                }

                override fun onStartSettingActivity() {
                    startActivity(SettingActivity.newIntent(requireContext()))
                }

                override fun onStartEditProfileActivity() {
                    val intent = MyProfileEditActivity.newIntent(requireContext())
                    myProfileEditLauncher.launch(intent)
                }

                override fun onStartEditProfileCardActivity(card: ProfileCardData) {
                    val intent = MyProfileCardDetailActivity.newIntent(requireContext(), card)
                    myProfileEditLauncher.launch(intent)
                }

                override fun onStartExternalLink(link: String) {
                    kotlin.runCatching {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }.onFailure {
                        // FIXME: 링크 열리지 않을 때 메시지
                        Toast.makeText(requireContext(), "올바르지 않은 링크입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun initViews() {
        super.initViews()
        initStatusBar()
        initSwipeRefresh()
        initRecyclerView()
    }

    private fun initStatusBar() {
        requireActivity().setStatusBarColorRes(CR.color.gray50)
    }

    private fun initSwipeRefresh() {
        viewBinding.layoutSwipe.apply {
            setOnRefreshListener { viewModel.getMyPageData() }
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), com.mashup.core.common.R.color.brand500)
            )
        }
    }

    private fun initRecyclerView() {
        viewBinding.rvMypage.adapter = attendanceAdapter
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.myPageData.collectLatest {
                viewBinding.layoutSwipe.isRefreshing = false
                attendanceAdapter.submitList(it)
            }
        }

        flowViewLifecycleScope {
            viewModel.errorCode.collectLatest {
                viewBinding.layoutSwipe.isRefreshing = false
                handleCommonError(it)
            }
        }
    }

    private fun showAttendanceInfoDialog() {
        AttendanceExplainDialog().show(childFragmentManager, AttendanceExplainDialog::class.simpleName)
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }
}
