package com.mashup.ui.main.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.constant.EXTRA_POPUP_KEY
import com.mashup.constant.log.LOG_COMMON_POPUP_CANCEL
import com.mashup.constant.log.LOG_COMMON_POPUP_CONFIRM
import com.mashup.core.common.utils.getDrawableResIdByName
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.bottomsheet.BottomSheetHandler
import com.mashup.ui.main.MainViewModel
import com.mashup.ui.main.model.MainPopupEntity
import com.mashup.ui.main.model.MainPopupType
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainBottomPopup : BottomSheetDialogFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    companion object {
        fun newInstance(popupType: MainPopupType) = MainBottomPopup().apply {
            arguments = bundleOf(
                EXTRA_POPUP_KEY to popupType.name
            )
        }
    }

    private val viewModel: MainBottomPopupViewModel by viewModels()

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.patchPopupViewed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    MainBottomPopupScreen(
                        viewModel = viewModel,
                        onClickLeftButton = {
                            AnalyticsManager.addEvent(
                                LOG_COMMON_POPUP_CANCEL,
                                bundleOf("key" to viewModel.popupKey)
                            )
                            dismiss()
                        },
                        onClickRightButton = {
                            mainViewModel.onClickPopup(viewModel.popupKey.orEmpty())
                            AnalyticsManager.addEvent(
                                LOG_COMMON_POPUP_CONFIRM,
                                bundleOf("key" to viewModel.popupKey)
                            )
                            dismiss()
                        }
                    )
                }
            }
        }
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
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (this@MainBottomPopup.view?.height == 0) return
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                behavior?.peekHeight = this@MainBottomPopup.view?.height ?: 0
            }
        })
    }
}

@Composable
fun MainBottomPopupScreen(
    viewModel: MainBottomPopupViewModel,
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    val uiState: MainBottomPopupUiState by viewModel.uiState

    (uiState as? MainBottomPopupUiState.Success)?.let {
        MainBottomPopupContent(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            mainPopupEntity = it.mainPopupEntity,
            onClickLeftButton = onClickLeftButton,
            onClickRightButton = onClickRightButton
        )
    }
}

@Composable
fun MainBottomPopupContent(
    mainPopupEntity: MainPopupEntity,
    modifier: Modifier = Modifier,
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(
                color = White,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        BottomSheetHandler()

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 20.dp),
            text = mainPopupEntity.title,
            style = SubTitle1,
            color = Gray950,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            text = mainPopupEntity.description,
            style = Body4,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        val context = LocalContext.current
        val imageResId = remember(mainPopupEntity.imageResName) {
            context.getDrawableResIdByName(mainPopupEntity.imageResName)
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            painter = painterResource(id = imageResId),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (mainPopupEntity.leftButtonText.isNotEmpty()) {
                MashUpButton(
                    modifier = Modifier.wrapContentWidth(),
                    text = mainPopupEntity.leftButtonText,
                    buttonStyle = ButtonStyle.INVERSE,
                    onClick = onClickLeftButton
                )
            }

            if (mainPopupEntity.rightButtonText.isNotEmpty()) {
                MashUpButton(
                    modifier = Modifier.weight(1f),
                    text = mainPopupEntity.rightButtonText,
                    buttonStyle = ButtonStyle.PRIMARY,
                    onClick = onClickRightButton
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
