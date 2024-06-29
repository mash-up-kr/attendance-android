package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.common.extensions.format
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileCardDetailBinding
import com.mashup.feature.mypage.profile.card.ProfileCardDetailContent
import com.mashup.feature.mypage.profile.model.ProfileCardData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class MyProfileCardDetailActivity : BaseActivity<ActivityMyProfileCardDetailBinding>() {
    override val layoutId = R.layout.activity_my_profile_card_detail

    private var team by mutableStateOf("")
    private var staff by mutableStateOf("")
    private lateinit var profileCardData: ProfileCardData

    private val profileCardEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            team = result.data?.getStringExtra(EXTRA_CARD_TEAM).orEmpty()
            staff = result.data?.getStringExtra(EXTRA_CARD_STAFF).orEmpty()
            setResult(RESULT_OK) // 마이페이지 데이터 업데이트를 위해 RESULT_OK 설정해줌
        }
    }

    override fun initViews() {
        super.initViews()

        val extra = intent.extras ?: return

        team = extra.getString(EXTRA_CARD_TEAM).orEmpty()
        staff = extra.getString(EXTRA_CARD_STAFF).orEmpty()

        profileCardData = ProfileCardData(
            id = extra.getInt(EXTRA_CARD_ID),
            name = extra.getString(EXTRA_CARD_NAME) ?: return,
            isRunning = extra.getBoolean(EXTRA_CARD_IS_RUNNING),
            generationNumber = extra.getInt(EXTRA_CARD_GENERATION),
            platform = Platform.getPlatform(extra.getString(EXTRA_CARD_PLATFORM) ?: return),
            projectTeamName = team,
            role = staff
        )

        viewBinding.composeView.setContent {
            MashUpTheme {
                Surface(
                    color = Gray50
                ) {
                    ProfileCardDetailContent(
                        cardData = profileCardData,
                        team = team,
                        staff = staff,
                        modifier = Modifier.fillMaxSize(),
                        onBackPressed = { finish() },
                        onDownLoadClicked = ::downloadProfileCardImage,
                        onEditClicked = ::startMyProfileCardActivity
                    )
                }
            }
        }
    }

    private fun startMyProfileCardActivity() {
        val intent = MyProfileCardEditActivity.newIntent(
            this,
            profileCardData.copy(
                projectTeamName = team,
                role = staff
            )
        )
        profileCardEditLauncher.launch(intent)
    }

    private fun downloadProfileCardImage(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        val base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        base64ToFile(base64)

        ToastUtil.showToast(this, "저장 완료!")
    }

    private fun base64ToFile(base64: String?): File {
        val imgBytesData = Base64.decode(
            base64,
            Base64.DEFAULT
        )

        val file = createTempFileInCache()
        FileOutputStream(file).buffered().use { it.write(imgBytesData) }

        return file
    }

    private fun createTempFileInCache(): File {
        val timeStamp: String = Date().format("yyyyMMdd_HHmmss")
        val destDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        destDir.mkdirs()
        return File.createTempFile(timeStamp, ".jpg", destDir)
    }

    companion object {
        private const val EXTRA_CARD_ID = "EXTRA_CARD_ID"
        private const val EXTRA_CARD_NAME = "EXTRA_CARD_NAME"
        private const val EXTRA_CARD_GENERATION = "EXTRA_CARD_GENERATION"
        private const val EXTRA_CARD_PLATFORM = "EXTRA_CARD_PLATFORM"
        private const val EXTRA_CARD_IS_RUNNING = "EXTRA_CARD_IS_RUNNING"
        const val EXTRA_CARD_TEAM = "EXTRA_CARD_TEAM"
        const val EXTRA_CARD_STAFF = "EXTRA_CARD_STAFF"

        fun newIntent(context: Context, card: ProfileCardData): Intent {
            return Intent(context, MyProfileCardDetailActivity::class.java).apply {
                putExtra(EXTRA_CARD_ID, card.id)
                putExtra(EXTRA_CARD_NAME, card.name)
                putExtra(EXTRA_CARD_GENERATION, card.generationNumber)
                putExtra(EXTRA_CARD_PLATFORM, card.platform.detailName)
                putExtra(EXTRA_CARD_IS_RUNNING, card.isRunning)
                putExtra(EXTRA_CARD_TEAM, card.projectTeamName)
                putExtra(EXTRA_CARD_STAFF, card.role)
            }
        }
    }
}
