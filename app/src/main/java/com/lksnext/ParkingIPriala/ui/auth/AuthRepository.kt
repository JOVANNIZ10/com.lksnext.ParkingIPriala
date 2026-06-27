package com.lksnext.ParkingIPriala.ui.auth

import com.lksnext.ParkingIPriala.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val firebaseAuth: FirebaseAuth? = null,
    private val firestore: FirebaseFirestore? = null
) {
    private val auth by lazy { firebaseAuth ?: FirebaseAuth.getInstance() }
    private val db by lazy { firestore ?: FirebaseFirestore.getInstance() }

    suspend fun registerUser(
        email: String,
        password: String,
        user: User
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userData = user.copy(id = email)
            db.collection("users").document(email).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyResetCode(code: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val email = auth.verifyPasswordResetCode(code).await()
            Result.success(email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmPasswordReset(code: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.confirmPasswordReset(code, newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
