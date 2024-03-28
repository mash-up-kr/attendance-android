package com.mashup.ui.attendance.member

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mashup.core.common.R
import com.mashup.core.common.extensions.dp
import com.mashup.core.common.extensions.gone
import com.mashup.core.model.Platform
import com.mashup.databinding.DialogMemberInfoBinding
import com.mashup.feature.mypage.profile.card.ProfileCard
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.viewholder.MyPageProfileViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@AndroidEntryPoint
class MemberInfoDialog : BottomSheetDialogFragment() {
    private var _binding: DialogMemberInfoBinding? = null
    private val binding: DialogMemberInfoBinding
        get() = _binding!!

    private val memberInfoViewModel: MemberInfoViewModel by viewModels()
    private val pagerState = PagerState()

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMemberInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val memberName = arguments?.getString(EXTRA_MEMBER_NAME).orEmpty()
        val memberId = arguments?.getString(EXTRA_MEMBER_ID).orEmpty()
        memberInfoViewModel.getMemberInfo(memberName, memberId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }

        addGlobalLayoutListener(view)
        initView()
        setObserver()
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            if (this@MemberInfoDialog.view?.height == 0) return@OnGlobalLayoutListener
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            behavior?.peekHeight = this@MemberInfoDialog.view?.height ?: 0
        })
    }

    private fun initView() {
        binding.introduceLayout.apply {
            tvSelfIntroduceTitle.text = "소개"
            btnEditSelfIntroduce.isInvisible = true // constraint 때문에 invisible
            tvBirthday.gone()
            divider.gone()
        }
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                memberInfoViewModel.memberInfo.collectLatest {
                    if (it != null) {
                        setScoreLayout(it.score)
                        setGenerationComposeView(it.generationList)
                        setIntroduceLayout(it.profile)
                    }
                }
            }
        }
    }

    private fun setScoreLayout(score: Double) {
        binding.scoreLayout.model = AttendanceModel.Score(id = 0, score = score)
    }

    private fun setGenerationComposeView(cards: List<ProfileCardData>) {
        binding.composeView.setContent {
            Box {
                HorizontalPager(
                    count = cards.size,
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 15.dp)
                ) { card ->
                    ProfileCard(
                        cardData = cards[card],
                        modifier = Modifier.padding(horizontal = 5.dp),
                        onClick = { }
                    )
                }
            }
        }
    }

    private fun setIntroduceLayout(profile: ProfileData) {
        binding.introduceLayout.data = profile

        binding.introduceLayout.tvBirthday.isVisible = profile.birthDay.isNotEmpty()
        binding.introduceLayout.tvIntroduceMyself.isVisible = profile.introduceMySelf.isNotEmpty()
        binding.introduceLayout.tvSelfIntroduceEmpty.isVisible = profile.introduceMySelf.isEmpty()

        getChipList(profile).also { chipList ->
            binding.introduceLayout.chipGroup.removeAllViews()
            binding.introduceLayout.chipGroup.isVisible = chipList.isNotEmpty()

            chipList.forEach {
                binding.introduceLayout.chipGroup.addChip(it)
            }
        }
    }

    // 표시되는 순서대로 ProfileData List 생성 (빈 값일 경우 표시하지 않는다)
    private fun getChipList(profile: ProfileData) = listOf(
        MyPageProfileViewHolder.ProfileChip.Text(profile.work),
        MyPageProfileViewHolder.ProfileChip.Text(profile.company),
        MyPageProfileViewHolder.ProfileChip.Text(profile.location),
        MyPageProfileViewHolder.ProfileChip.Link(
            displayText = if (profile.github.isNotEmpty()) "Github" else "",
            link = profile.github
        ),
        MyPageProfileViewHolder.ProfileChip.Link(
            displayText = if (profile.behance.isNotEmpty()) "LinkedIn" else "",
            link = profile.linkedIn
        ),
        MyPageProfileViewHolder.ProfileChip.Link(
            displayText = if (profile.behance.isNotEmpty()) "Behance" else "",
            link = profile.behance
        ),
        MyPageProfileViewHolder.ProfileChip.Instagram(
            displayText = if (profile.instagram.isNotEmpty()) "@${profile.instagram}" else ""
        )
    ).filter { it.displayText.isNotEmpty() }

    private fun ChipGroup.addChip(chipData: MyPageProfileViewHolder.ProfileChip) {
        val context = requireContext()
        addView(
            Chip(context).apply {
                text = chipData.displayText

                chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.white)
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.gray200)
                chipStrokeWidth = 1.dp(context).toFloat()

                chipStartPadding = 4.dp(context).toFloat()
                chipEndPadding = 4.dp(context).toFloat()

                chipSpacingHorizontal = 6.dp(context)
                chipSpacingVertical = (-8).dp(context)

                if (chipData is MyPageProfileViewHolder.ProfileChip.Link) {
                    chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_link)
                    chipIconSize = 14.dp(context).toFloat()
                    iconEndPadding = -4.dp(context).toFloat()

                    chipStartPadding = 10.dp(context).toFloat()
                    chipEndPadding = 10.dp(context).toFloat()
                }

                setTextAppearanceResource(R.style.TextAppearance_Mashup_Caption1_13_M)
                setOnClickListener {
                    if (chipData.clickable) {
                        onStartExternalLink(chipData.link)
                    }
                }
            }
        )
    }

    private fun onStartExternalLink(link: String) {
        kotlin.runCatching {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        }.onFailure {
            // FIXME: 링크 열리지 않을 때 메시지
            Toast.makeText(requireContext(), "올바르지 않은 링크입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val EXTRA_MEMBER_NAME = "EXTRA_MEMBER_NAME"
        private const val EXTRA_MEMBER_ID = "EXTRA_MEMBER_ID"

        fun newInstance(name: String, memberId: String) = MemberInfoDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_MEMBER_NAME, name)
                putString(EXTRA_MEMBER_ID, memberId)
            }
        }
    }
}
