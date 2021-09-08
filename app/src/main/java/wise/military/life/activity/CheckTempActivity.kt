package wise.military.life.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import wise.military.life.R
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.extension.toast
import wise.military.life.viewmodel.CheckViewModel

class CheckTempActivity : ComponentActivity() {
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
        val checkVm: CheckViewModel = viewModel()
        val focusManager = LocalFocusManager.current
        var tempField by remember { mutableStateOf(TextFieldValue()) }
        val outlineTextFieldBorderTheme = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Gray,
            backgroundColor = Color.White,
            cursorColor = Color.Black,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            disabledBorderColor = Color.LightGray,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = stringResource(R.string.activity_main_button_check_temp),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 25.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(100.dp)
                            .focusRequester(FocusRequester()),
                        value = tempField,
                        onValueChange = { temp ->
                            if (temp.text.length < 5) {
                                try {
                                    val floatTemp = temp.text.toFloat()
                                    tempField = temp
                                } catch (ignored: Exception) {
                                }
                            }
                        },
                        colors = outlineTextFieldBorderTheme,
                        placeholder = { Text(text = stringResource(R.string.activity_check_temp_placeholder_365)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        textStyle = TextStyle(fontSize = 30.sp),
                        keyboardActions = KeyboardActions {
                            focusManager.clearFocus()
                        }
                    )
                    Text(
                        text = stringResource(R.string.activity_check_temp_icon),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
                Button(
                    modifier = Modifier.height(50.dp),
                    onClick = {
                        val temp = tempField.text
                        if (temp.isNotBlank()) {
                            checkVm.temp()
                        } else {
                            toast(getString(R.string.activity_check_temp_toast_confirm_temp))
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.activity_check_temp_button_done))
                }
            }
        }
    }
}
