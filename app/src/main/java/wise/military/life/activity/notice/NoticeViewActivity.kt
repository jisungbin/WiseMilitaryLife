package wise.military.life.activity.notice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import wise.military.life.theme.MaterialTheme

class NoticeViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
            }
        }
    }
}
