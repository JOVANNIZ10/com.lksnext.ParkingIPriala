package com.lksnext.ParkingIPriala.viewmodel

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
class LoginViewModelTest {

    @Mock
    private lateinit var repository: AuthRepository

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login returns Success when repository succeeds`() = runTest(testDispatcher) {
        runBlocking { whenever(repository.loginUser("test@test.com", "password")).thenReturn(Result.success(Unit)) }

        viewModel.login("test@test.com", "password")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is LoginState.Success)
    }

    @Test
    fun `login returns Error when repository fails`() = runTest(testDispatcher) {
        runBlocking { whenever(repository.loginUser("test@test.com", "password")).thenReturn(Result.failure(Exception("Error"))) }

        viewModel.login("test@test.com", "password")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is LoginState.Error)
        assertEquals("Error", (viewModel.state.value as LoginState.Error).message)
    }

    @Test
    fun `resetPassword returns PasswordResetSent when repository succeeds`() = runTest(testDispatcher) {
        runBlocking { whenever(repository.resetPassword("test@test.com")).thenReturn(Result.success(Unit)) }

        viewModel.resetPassword("test@test.com")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is LoginState.PasswordResetSent)
        assertEquals("test@test.com", (viewModel.state.value as LoginState.PasswordResetSent).email)
    }

    @Test
    fun `resetPassword returns Error when repository fails`() = runTest(testDispatcher) {
        runBlocking { whenever(repository.resetPassword("test@test.com")).thenReturn(Result.failure(Exception("Error"))) }

        viewModel.resetPassword("test@test.com")
        advanceUntilIdle()

        assertTrue(viewModel.state.value is LoginState.Error)
    }
}
