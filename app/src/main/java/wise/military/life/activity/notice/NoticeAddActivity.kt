package wise.military.life.activity.notice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wise.military.life.R
import wise.military.life.model.Notice
import wise.military.life.repo.doWhen
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.extension.getErrorMessage
import wise.military.life.util.extension.toast
import wise.military.life.viewmodel.NoticeViewModel
import kotlin.random.Random

class NoticeAddActivity : ComponentActivity() {
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
        val noticeVm: NoticeViewModel = viewModel()
        val coroutineScope = rememberCoroutineScope()
        val titleField = remember { mutableStateOf(TextFieldValue()) }
        val contentField = remember { mutableStateOf(TextFieldValue()) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val title = titleField.value.text
                    val content = contentField.value.text

                    if (title.isNotBlank() && content.isNotBlank()) {
                        coroutineScope.launch {
                            noticeVm.upload(
                                Notice(
                                    id = Random.nextInt(),
                                    title = title,
                                    content = content
                                )
                            ).collect { uploadResult ->
                                uploadResult.doWhen(
                                    onSuccess = {
                                        toast("성공")
                                        finish()
                                    },
                                    onFail = { exception ->
                                        toast(
                                            getString(
                                                R.string.activity_notice_add_toast_error,
                                                exception.getErrorMessage()
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        toast("모두 입력해 주세요")
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_done_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            content = {
                NoticeWriteContent(titleField = titleField, contentField = contentField)
            }
        )
    }

    @Composable
    private fun NoticeWriteContent(
        titleField: MutableState<TextFieldValue>,
        contentField: MutableState<TextFieldValue>,
    ) {
        val focusManager = LocalFocusManager.current
        val textFieldTheme = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,
            disabledIndicatorColor = Color.White
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = titleField.value,
                onValueChange = { titleField.value = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = textFieldTheme
            )
            Divider(modifier = Modifier.fillMaxWidth())
            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(FocusRequester()),
                value = contentField.value,
                onValueChange = { contentField.value = it },
                colors = textFieldTheme,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
            )
        }
    }
}
