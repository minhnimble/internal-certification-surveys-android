package co.nimblehq.event

sealed class NavigationEvent {

	sealed class SignIn : NavigationEvent() {
		object Main : SignIn()
	}
}
