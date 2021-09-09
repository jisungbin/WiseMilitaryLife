package wise.military.life.activity.check

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
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
    fun updateTemperature(_temperature: Temperature) = callbackFlow {
        val temperature = _temperature.copy(checkAt = now)
        firestore.collection("temperature").document(temperature.userId).set(temperature)
            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun updateVaccine(_vaccine: Vaccine) = callbackFlow {
        val vaccine = _vaccine.copy(checkAt = now)
        firestore.collection("vaccine").document(vaccine.userId).set(vaccine)
            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getVaccine() = callbackFlow {
        firestore.collection("vaccine").orderBy("checkAt", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                val vaccines =
                    querySnapshot.documents.mapNotNull { it.toObject(Vaccine::class.java) }
                trySend(FirebaseResult.Success(vaccines))
            }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTemperature() = callbackFlow {
        firestore.collection("temperature").orderBy("checkAt", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                val temperature =
                    querySnapshot.documents.mapNotNull { it.toObject(Temperature::class.java) }
                trySend(FirebaseResult.Success(temperature))
            }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }
}
