package co.nimblehq.event

sealed class NavigationEvent {

    sealed class Onboarding : NavigationEvent() {
        object Main : Onboarding()
    }

    sealed class SignIn : NavigationEvent() {
        object Main : SignIn()
    }

    sealed class Surveys : NavigationEvent() {
        object Onboarding : Surveys()
    }
}
