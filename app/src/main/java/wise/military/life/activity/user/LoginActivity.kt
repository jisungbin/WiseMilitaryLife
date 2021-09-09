package wise.military.life.activity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wise.military.life.R
import wise.military.life.activity.MainActivity
import wise.military.life.repo.doWhen
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.config.IntentConfig
import wise.military.life.util.extension.exceptionToast
import wise.military.life.util.extension.getErrorMessage
import wise.military.life.util.extension.toast

class LoginActivity : ComponentActivity() {
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
        val userVm: UserViewModel = viewModel()
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        var idField by remember { mutableStateOf(TextFieldValue()) }
        var passwordField by remember { mutableStateOf(TextFieldValue()) }
        var passwordVisibility by remember { mutableStateOf(false) }
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
                    text = stringResource(R.string.app_name),
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = idField,
                    onValueChange = { idField = it },
                    colors = outlineTextFieldBorderTheme,
                    placeholder = { Text(text = stringResource(R.string.activity_login_placeholder_login)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions {
                        focusRequester.requestFocus()
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = passwordField,
                    onValueChange = { passwordField = it },
                    colors = outlineTextFieldBorderTheme,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisibility) R.drawable.ic_round_visibility_off_24
                                    else R.drawable.ic_round_visibility_24
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    placeholder = { Text(text = stringResource(R.string.activity_login_placeholder_password)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        coroutineScope.launch {
                            val id = idField.text
                            val password = passwordField.text

                            if (id.isNotBlank() && password.isNotBlank()) {
                                userVm.get(id).collect { userResult ->
                                    userResult.doWhen(
                                        onSuccess = { users ->
                                            if (users.isNotEmpty()) {
                                                val user = users.first()
                                                if (user.password == password) {
                                                    finish()
                                                    startActivity(
                                                        Intent(
                                                            this@LoginActivity,
                                                            MainActivity::class.java
                                                        ).apply {
                                                            putExtra(
                                                                IntentConfig.UserId,
                                                                user.id
                                                            )
                                                        }
                                                    )
                                                } else {
                                                    toast(getString(R.string.activity_login_toast_confirm_password))
                                                }
                                            } else {
                                                toast(getString(R.string.activity_login_toast_non_exist_id))
                                            }
                                        },
                                        onFail = { exception ->
                                            exceptionToast("로그인 중", exception)
                                        }
                                    )
                                }
                            } else {
                                toast(getString(R.string.activity_login_toast_confirm_all_filed))
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.activity_login_button_login))
                }
                Text(
                    text = stringResource(R.string.activity_login_button_register),
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}
