package wise.military.life.activity.check

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wise.military.life.R
import wise.military.life.model.Temperature
import wise.military.life.repo.doWhen
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.extension.exceptionToast
import wise.military.life.util.extension.getUserId
import wise.military.life.util.extension.isAdminId
import wise.military.life.util.extension.toast

class CheckTempActivity : ComponentActivity() {

    private val isAdmin by lazy { intent.getUserId().isAdminId() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                if (isAdmin) {
                    AdminContent()
                } else {
                    UserContent()
                }
            }
        }
    }

    @Composable
    private fun AdminContent() {
        val checkVm: CheckViewModel = viewModel()
        val tempList = remember { mutableStateListOf<Temperature>() }

        LaunchedEffect(Unit) {
            checkVm.getTemperature().collect { tempResult ->
                tempResult.doWhen(
                    onSuccess = { _tempList ->
                        tempList.addAll(_tempList)
                    },
                    onFail = { exception ->
                        exceptionToast("체온 목록을 불러오는 중", exception)
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            if (tempList.isNotEmpty()) {
                items(items = tempList.distinct()) { temp ->
                    TemperatureItem(temperature = temp)
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.notice_empty)
                        )
                        LottieAnimation(
                            modifier = Modifier.size(250.dp),
                            composition = composition,
                            iterations = LottieConstants.IterateForever
                        )
                        Text(
                            text = stringResource(R.string.activity_check_temp_empty),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TemperatureItem(temperature: Temperature) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "${temperature.temp}°C",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = temperature.userId,
                    color = Color.Gray,
                )
                Text(
                    text = temperature.checkAt,
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }
        }
    }

    @Composable
    private fun UserContent() {
        val checkVm: CheckViewModel = viewModel()
        val focusManager = LocalFocusManager.current
        val coroutineScope = rememberCoroutineScope()
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
                verticalArrangement = Arrangement.spacedBy(30.dp)
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
                                tempField = temp
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
                        coroutineScope.launch {
                            try {
                                val temp = tempField.text.toFloat()
                                if (temp >= 45 || temp <= 30) throw Exception()
                                checkVm.updateTemperature(
                                    Temperature(
                                        userId = intent.getUserId(),
                                        temp = temp
                                    )
                                ).collect { tempResult ->
                                    tempResult.doWhen(
                                        onSuccess = {
                                            toast(getString(R.string.activity_check_temp_toast_uploaded))
                                        },
                                        onFail = { exception ->
                                            exceptionToast("등록중", exception)
                                        }
                                    )
                                }
                            } catch (ignored: Exception) {
                                toast(getString(R.string.activity_check_temp_toast_confirm))
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.activity_check_temp_button_done))
                }
            }
        }
    }
}
