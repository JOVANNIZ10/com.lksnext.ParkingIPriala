package com.lksnext.ParkingIPriala.viewmodel

import com.lksnext.ParkingIPriala.data.User
import com.lksnext.ParkingIPriala.ui.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @Mock
    private lateinit var repository: AuthRepository

    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register returns Success when repository succeeds`() = runTest(testDispatcher) {
        val user = User(nombre = "Test", usuario = "testuser", email = "test@test.com")
        runBlocking { whenever(repository.registerUser("test@test.com", "password", user)).thenReturn(Result.success(Unit)) }

        viewModel.register("Test", "testuser", "test@test.com", "password")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is RegisterState.Success)
    }

    @Test
    fun `register returns Error when repository fails`() = runTest(testDispatcher) {
        val user = User(nombre = "Test", usuario = "testuser", email = "test@test.com")
        runBlocking { whenever(repository.registerUser("test@test.com", "password", user)).thenReturn(Result.failure(Exception("Error"))) }

        viewModel.register("Test", "testuser", "test@test.com", "password")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is RegisterState.Error)
        assertEquals("Error", (viewModel.state.value as RegisterState.Error).message)
    }
}
