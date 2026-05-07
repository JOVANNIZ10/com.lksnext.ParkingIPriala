package com.lksnext.ParkingIPriala.ui.auth

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lksnext.ParkingIPriala.data.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var db: FirebaseFirestore

    @Mock
    private lateinit var authResult: AuthResult

    @Mock
    private lateinit var collectionRef: CollectionReference

    @Mock
    private lateinit var documentRef: DocumentReference

    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        repository = AuthRepository(auth, db)
    }

    @Test
    fun `registerUser returns success when auth and firestore succeed`() = runTest {
        whenever(auth.createUserWithEmailAndPassword("test@test.com", "password")).thenReturn(Tasks.forResult(authResult))
        whenever(db.collection("users")).thenReturn(collectionRef)
        whenever(collectionRef.document("test@test.com")).thenReturn(documentRef)
        whenever(documentRef.set(any())).thenReturn(Tasks.forResult(null))

        val user = User(nombre = "Test", usuario = "testuser", email = "test@test.com")
        val result = repository.registerUser("test@test.com", "password", user)

        assertTrue(result.isSuccess)
        verify(auth).createUserWithEmailAndPassword("test@test.com", "password")
        verify(documentRef).set(any())
    }

    @Test
    fun `registerUser returns failure when auth fails`() = runTest {
        whenever(auth.createUserWithEmailAndPassword("test@test.com", "password")).thenReturn(Tasks.forException(Exception("Auth error")))

        val user = User(nombre = "Test", usuario = "testuser", email = "test@test.com")
        val result = repository.registerUser("test@test.com", "password", user)

        assertTrue(result.isFailure)
        assertEquals("Auth error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `loginUser returns success when auth succeeds`() = runTest {
        whenever(auth.signInWithEmailAndPassword("test@test.com", "password")).thenReturn(Tasks.forResult(authResult))

        val result = repository.loginUser("test@test.com", "password")

        assertTrue(result.isSuccess)
        verify(auth).signInWithEmailAndPassword("test@test.com", "password")
    }

    @Test
    fun `loginUser returns failure when auth fails`() = runTest {
        whenever(auth.signInWithEmailAndPassword("test@test.com", "password")).thenReturn(Tasks.forException(Exception("Login error")))

        val result = repository.loginUser("test@test.com", "password")

        assertTrue(result.isFailure)
        assertEquals("Login error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `resetPassword returns success when auth succeeds`() = runTest {
        whenever(auth.sendPasswordResetEmail("test@test.com")).thenReturn(Tasks.forResult(null))

        val result = repository.resetPassword("test@test.com")

        assertTrue(result.isSuccess)
        verify(auth).sendPasswordResetEmail("test@test.com")
    }

    @Test
    fun `resetPassword returns failure when auth fails`() = runTest {
        whenever(auth.sendPasswordResetEmail("test@test.com")).thenReturn(Tasks.forException(Exception("Reset error")))

        val result = repository.resetPassword("test@test.com")

        assertTrue(result.isFailure)
        assertEquals("Reset error", result.exceptionOrNull()?.message)
    }
}
