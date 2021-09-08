package wise.military.life.activity.check

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wise.military.life.R
import wise.military.life.model.Vaccine
import wise.military.life.model.toVaccineString
import wise.military.life.repo.doWhen
import wise.military.life.theme.MaterialTheme
import wise.military.life.util.extension.getErrorMessage
import wise.military.life.util.extension.getUserId
import wise.military.life.util.extension.toast

class CheckVaccineActivity : ComponentActivity() {
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
        val coroutineScope = rememberCoroutineScope()
        val vaccineTextShape = RoundedCornerShape(15.dp)
        var vaccineCount by remember { mutableStateOf<Int?>(null) }
        var selectedVaccineType by remember { mutableStateOf<Int?>(null) }

        Column(
            modifier = Modifier.fillMaxSize(),
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
                    text = stringResource(R.string.activity_main_button_check_vaccine),
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
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(PaddingValues(horizontal = 30.dp)),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        repeat(4) { type ->
                            @Composable
                            fun VaccineTextBackgroundColor() =
                                animateColorAsState(if (selectedVaccineType == type) Color.LightGray else Color.White)

                            Text(
                                text = type.toVaccineString(),
                                color = Color.Black,
                                modifier = Modifier
                                    .clip(vaccineTextShape)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = vaccineTextShape
                                    )
                                    .background(
                                        color = VaccineTextBackgroundColor().value,
                                        shape = vaccineTextShape
                                    )
                                    .clickable { selectedVaccineType = type }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        repeat(if (selectedVaccineType == null) 0 else if (selectedVaccineType == 3) 1 else 2) { count ->
                            @Composable
                            fun VaccineTextBackgroundColor() =
                                animateColorAsState(if (vaccineCount == count) Color.LightGray else Color.White)

                            Text(
                                text = "${count + 1}차 접종",
                                color = Color.Black,
                                modifier = Modifier
                                    .clip(vaccineTextShape)
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = vaccineTextShape
                                    )
                                    .background(
                                        color = VaccineTextBackgroundColor().value,
                                        shape = vaccineTextShape
                                    )
                                    .clickable { vaccineCount = count }
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier.height(50.dp),
                    onClick = {
                        coroutineScope.launch {
                            if (selectedVaccineType != null && vaccineCount != null) {
                                checkVm.vaccine(
                                    Vaccine(
                                        userId = intent.getUserId(),
                                        type = selectedVaccineType!!,
                                        count = vaccineCount!!
                                    )
                                ).collect { tempResult ->
                                    tempResult.doWhen(
                                        onSuccess = {
                                            toast(getString(R.string.activity_check_vaccine_toast_uploaded))
                                        },
                                        onFail = { exception ->
                                            toast(
                                                getString(
                                                    R.string.activity_check_temp_toast_error,
                                                    exception.getErrorMessage()
                                                )
                                            )
                                        }
                                    )
                                }
                            } else {
                                toast(getString(R.string.activity_check_vaccine_toast_confirm_all_field))
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
