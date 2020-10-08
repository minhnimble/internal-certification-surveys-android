package co.nimblehq.navigator

sealed class NavigationEvent {

	sealed class SignIn : NavigationEvent() {
		object Main : SignIn()
	}
}
