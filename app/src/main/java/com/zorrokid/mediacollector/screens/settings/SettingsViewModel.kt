package com.zorrokid.mediacollector.screens.settings

import com.zorrokid.mediacollector.MyBasicJetpackComposeScreen
import com.zorrokid.mediacollector.model.service.AccountService
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
) : MediaCollectorViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(it.isAnonymous)
    }
    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.LogIn.name)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(MyBasicJetpackComposeScreen.SignUp.name)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(MyBasicJetpackComposeScreen.Splash.name)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(MyBasicJetpackComposeScreen.Splash.name)
        }
    }
}