package wise.military.wisemilitarylife.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wise.military.wisemilitarylife.R
import wise.military.wisemilitarylife.model.User
import wise.military.wisemilitarylife.model.toLevelString
import wise.military.wisemilitarylife.repo.doWhen
import wise.military.wisemilitarylife.theme.MaterialTheme
import wise.military.wisemilitarylife.util.config.IntentConfig
import wise.military.wisemilitarylife.util.extension.toast
import wise.military.wisemilitarylife.viewmodel.ServerViewModel

class RegisterActivity : ComponentActivity() {
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
        val vm: ServerViewModel = viewModel()
        val focusManager = LocalFocusManager.current
        val passwordFocusRequester = remember { FocusRequester() }
        val ageFocusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        var idField by remember { mutableStateOf(TextFieldValue()) }
        var passwordField by remember { mutableStateOf(TextFieldValue()) }
        var ageField by remember { mutableStateOf(TextFieldValue()) }
        var selectedLevel by remember { mutableStateOf<Int?>(null) }
        val levelTextShape = RoundedCornerShape(15.dp)
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
                    text = stringResource(R.string.activity_login_button_register),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 35.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = idField,
                    onValueChange = { idField = it },
                    colors = outlineTextFieldBorderTheme,
                    placeholder = { Text(text = stringResource(R.string.activity_login_placeholder_login)) },
                    singleLine = true,
                    keyboardActions = KeyboardActions {
                        passwordFocusRequester.requestFocus()
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester),
                    value = passwordField,
                    onValueChange = { passwordField = it },
                    colors = outlineTextFieldBorderTheme,
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text(text = stringResource(R.string.activity_login_placeholder_password)) },
                    singleLine = true,
                    keyboardActions = KeyboardActions {
                        ageFocusRequester.requestFocus()
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(ageFocusRequester),
                    value = ageField,
                    onValueChange = { age ->
                        if (age.text.length < 3) {
                            ageField = age
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = outlineTextFieldBorderTheme,
                    placeholder = { Text(text = stringResource(R.string.activity_register_placeholder_age)) },
                    singleLine = true,
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    }
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        repeat(4) { level ->
                            @Composable
                            fun LevelTextBackgroundColor() =
                                animateColorAsState(if (selectedLevel == level) Color.LightGray else Color.White)

                            Text(
                                text = level.toLevelString(),
                                color = Color.Black,
                                modifier = Modifier
                                    .clip(levelTextShape)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = levelTextShape
                                    )
                                    .background(
                                        color = LevelTextBackgroundColor().value,
                                        shape = levelTextShape
                                    )
                                    .clickable { selectedLevel = level }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        coroutineScope.launch {
                            val id = idField.text
                            val password = passwordField.text
                            val age = ageField.text

                            if (
                                id.isNotBlank() && password.isNotBlank() &&
                                age.isNotBlank() && selectedLevel != null
                            ) {
                                vm.register(
                                    User(
                                        id = id,
                                        password = password,
                                        age = age.toInt(),
                                        level = selectedLevel!!
                                    )
                                ).collect { registerResult ->
                                    registerResult.doWhen(
                                        onSuccess = {
                                            finishAffinity()
                                            startActivity(
                                                Intent(
                                                    this@RegisterActivity,
                                                    MainActivity::class.java
                                                ).apply {
                                                    putExtra(IntentConfig.UserId, id)
                                                }
                                            )
                                        },
                                        onFail = { exception ->
                                            toast(
                                                message = getString(
                                                    R.string.activity_register_toast_error,
                                                    exception.message ?: "오류를 불러올 수 없어요"
                                                ),
                                                length = Toast.LENGTH_LONG
                                            )
                                        }
                                    )
                                }
                            } else {
                                toast(getString(R.string.activity_login_toast_confirm_all_filed))
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.activity_register_button_label))
                }
            }
        }
    }
}
