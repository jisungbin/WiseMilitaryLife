package wise.military.life.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import wise.military.life.model.Temperature
import wise.military.life.model.Vaccine
import wise.military.life.repo.FirebaseResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val now get() = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(Date())

    @OptIn(ExperimentalCoroutinesApi::class)
    fun temperature(_temperature: Temperature) = callbackFlow {
        val temperature = _temperature.copy(checkAt = now)
        firestore.collection("temperature").add(temperature)
            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun vaccine(vaccine: Vaccine) = callbackFlow {
        firestore.collection("vaccine").add(vaccine)
            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }
}
