package wise.military.life.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wise.military.life.R
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.config.IntentConfig
import wise.military.life.util.extension.toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toast(getString(R.string.activity_login_toast_welcome))

        setContent {
            MaterialTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
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
                    text = stringResource(R.string.activity_login_toast_welcome),
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
                Button(
                    modifier = Modifier.size(
                        width = 200.dp,
                        height = 100.dp
                    ),
                    onClick = { startActivity(CheckTempActivity::class.java) }
                ) {
                    Text(
                        text = stringResource(R.string.activity_main_button_check_temp),
                        fontSize = 20.sp
                    )
                }
                Button(
                    modifier = Modifier.size(
                        width = 200.dp,
                        height = 100.dp
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(R.string.activity_main_button_check_vaccine),
                        fontSize = 20.sp
                    )
                }
                Button(
                    modifier = Modifier.size(
                        width = 200.dp,
                        height = 100.dp
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = stringResource(R.string.activity_main_button_notice),
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = stringResource(R.string.activity_main_button_logout),
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        finish()
                        startActivity(LoginActivity::class.java)
                        toast(getString(R.string.activity_main_toast_logouted))
                    }
                )
            }
        }
    }

    private fun <T> startActivity(clazz: Class<T>) {
        startActivity(Intent(this@MainActivity, clazz).apply {
            putExtra(IntentConfig.UserId, intent.getStringExtra(IntentConfig.UserId)!!)
        })
    }
}
