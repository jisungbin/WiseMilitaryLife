package wise.military.life.activity.notice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.config.IntentConfig

class NoticeViewActivity : ComponentActivity() {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = intent.getStringExtra(IntentConfig.NoticeTitle)!!,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = intent.getStringExtra(IntentConfig.NoticeTime)!!,
                color = Color.LightGray,
                fontSize = 13.sp
            )
            Divider(modifier = Modifier.fillMaxWidth())
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = intent.getStringExtra(IntentConfig.NoticeContent)!!,
                color = Color.Black,
                fontSize = 15.sp
            )
        }
    }
}
