package wise.military.life.activity.notice

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import wise.military.life.model.Notice
import wise.military.life.repo.FirebaseResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoticeViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val now get() = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(Date())

    @OptIn(ExperimentalCoroutinesApi::class)
    fun upload(_notice: Notice) = callbackFlow {
        val notice = _notice.copy(createAt = now)
        firestore.collection("notice").add(notice)
            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun get() = callbackFlow {
        firestore.collection("notice").orderBy("createAt", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                val notices = querySnapshot.documents.mapNotNull { it.toObject(Notice::class.java) }
                trySend(FirebaseResult.Success(notices))
            }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun delete(id: Int) = callbackFlow {
        firestore.collection("notice").whereEqualTo("id", id).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.first().reference.delete()
                    .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
                    .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }
            }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }
}
