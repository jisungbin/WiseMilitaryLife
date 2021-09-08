package wise.military.life.activity.user

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import wise.military.life.model.User
import wise.military.life.repo.FirebaseResult
import wise.military.life.repo.doWhen

class UserViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    @OptIn(ExperimentalCoroutinesApi::class)
    fun get(id: String) = callbackFlow {
        firestore.collection("users").whereEqualTo("id", id).get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
                trySend(FirebaseResult.Success(users))
            }
            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun register(user: User) = callbackFlow {
        get(user.id).collect { userResult ->
            userResult.doWhen(
                onSuccess = { users ->
                    if (users.isEmpty()) {
                        firestore.collection("users").add(user)
                            .addOnSuccessListener { trySend(FirebaseResult.Success(Unit)) }
                            .addOnFailureListener { trySend(FirebaseResult.Fail(it)) }
                    } else {
                        trySend(FirebaseResult.Fail(Exception("이미 가입된 아이디가 있어요")))
                    }
                }
            )
        }

        awaitClose { close() }
    }
}
