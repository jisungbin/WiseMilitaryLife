package wise.military.wisemilitarylife.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import wise.military.wisemilitarylife.R
import wise.military.wisemilitarylife.util.extension.toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            toast(getString(R.string.activity_main_toast_welcome))
        }
    }
}
