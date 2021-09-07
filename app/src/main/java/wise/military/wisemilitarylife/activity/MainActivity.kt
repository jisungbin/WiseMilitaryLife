package wise.military.wisemilitarylife.activity

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wise.military.wisemilitarylife.R
import wise.military.wisemilitarylife.theme.MaterialTheme
import wise.military.wisemilitarylife.theme.SystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemUiController(window).setSystemBarsColor(Color.White)
        setContent {
            MaterialTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        var idField by remember { mutableStateOf(TextFieldValue()) }
        var passwordField by remember { mutableStateOf(TextFieldValue()) }
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
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 20.sp
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
                    placeholder = { Text(text = "아이디") },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordField,
                    onValueChange = { passwordField = it },
                    colors = outlineTextFieldBorderTheme,
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text(text = "비밀번호") },
                    singleLine = true
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "로그인")
                }
                Text(text = "회원가입", color = Color.Gray)
            }
        }
    }
}
