package wise.military.wisemilitarylife.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import wise.military.wisemilitarylife.model.User
import wise.military.wisemilitarylife.repo.ServerResult
import wise.military.wisemilitarylife.repo.doWhen

class ServerViewModel : ViewModel() {
    private val firestore = Firebase.firestore

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUser(id: String) = callbackFlow {
        firestore.collection("users").whereEqualTo("id", id).get()
            .addOnSuccessListener { querySnapshot ->
                val users = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
                trySend(ServerResult.Success(users))
            }
            .addOnFailureListener { trySend(ServerResult.Fail(it)) }

        awaitClose { close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun register(user: User) = callbackFlow {
        getUser(user.id).collect { userResult ->
            userResult.doWhen(
                onSuccess = { users ->
                    if (users.isEmpty()) {
                        firestore.collection("users").add(user)
                            .addOnSuccessListener { trySend(ServerResult.Success(Unit)) }
                            .addOnFailureListener { trySend(ServerResult.Fail(it)) }
                    } else {
                        trySend(ServerResult.Fail(Exception("이미 가입된 아이디가 있습니다.")))
                    }
                }
            )
        }

        awaitClose { close() }
    }
}
