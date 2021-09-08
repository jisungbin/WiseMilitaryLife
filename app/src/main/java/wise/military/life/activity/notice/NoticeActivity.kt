package wise.military.life.activity.notice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.flow.collect
import wise.military.life.R
import wise.military.life.model.Notice
import wise.military.life.repo.doWhen
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.config.IntentConfig
import wise.military.life.util.extension.getErrorMessage
import wise.military.life.util.extension.getUserId
import wise.military.life.util.extension.isAdminId
import wise.military.life.util.extension.toast
import wise.military.life.viewmodel.NoticeViewModel

class NoticeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        if (intent.getUserId().isAdminId()) {
            AdminContent()
        } else {
            UserContent()
        }
    }

    @Composable
    private fun UserContent() {
        val noticeVm: NoticeViewModel = viewModel()
        val notices = remember { mutableStateListOf<Notice>() }

        LaunchedEffect(Unit) {
            noticeVm.get().collect { noticeResult ->
                noticeResult.doWhen(
                    onSuccess = { noticeList ->
                        notices.addAll(noticeList)
                    },
                    onFail = { exception ->
                        toast(
                            getString(
                                R.string.activity_notice_toast_error,
                                exception.getErrorMessage()
                            )
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            if (notices.isNotEmpty()) {
                items(notices) { notice ->
                    NoticeItem(notice)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.notice_empty))
                        LottieAnimation(
                            modifier = Modifier.size(250.dp),
                            composition = composition,
                            iterations = LottieConstants.IterateForever
                        )
                        Text(
                            text = stringResource(R.string.activity_notice_empty),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AdminContent() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    startActivity(Intent(this, NoticeAddActivity::class.java))
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_add_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            content = { UserContent() }
        )
    }

    @Composable
    private fun NoticeItem(notice: Notice) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    startActivity(
                        Intent(this, NoticeViewActivity::class.java).apply {
                            putExtra(IntentConfig.NoticeTitle, notice.title)
                            putExtra(IntentConfig.NoticeContent, notice.content)
                            putExtra(IntentConfig.NoticeTime, notice.createAt)
                        }
                    )
                },
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = notice.title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notice.content,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = notice.createAt,
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }
    }
}
