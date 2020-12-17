package co.nimblehq.ui.screen.main.surveys

import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.data.error.Ignored
import co.nimblehq.data.lib.common.DATE_FORMAT_SHORT_DISPLAY
import co.nimblehq.data.lib.extension.subscribeOnClick
import co.nimblehq.data.model.User
import co.nimblehq.event.NavigationEvent
import co.nimblehq.extension.loadImage
import co.nimblehq.extension.loadImageWithFadeAnimation
import co.nimblehq.extension.switchTextWithFadeAnimation
import co.nimblehq.extension.toDisplayFormat
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.common.listener.OnSwipeTouchListener
import co.nimblehq.ui.screen.common.UserViewModel
import co.nimblehq.ui.screen.main.LoaderAnimatable
import co.nimblehq.ui.screen.main.MainNavigator
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_fragment_surveys.*
import kotlinx.android.synthetic.main.fragment_surveys.*
import kotlinx.android.synthetic.main.navigation_drawer_header_surveys.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var navigator: MainNavigator

    private val surveysViewModel by viewModels<SurveysViewModel>()

    private val userViewModel by activityViewModels<UserViewModel>()

    private val loaderAnimator: LoaderAnimatable? by lazy {
        requireActivity() as? LoaderAnimatable
    }

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() {
        surveysViewModel.getLocalCachedSurveys()
    }

    override fun setupView() {
        tvSurveysDate.text = Date().toDisplayFormat(DATE_FORMAT_SHORT_DISPLAY).toUpperCase(Locale.ROOT)

        srlSurveys.setOnRefreshListener { surveysViewModel.refreshSurveys() }
    }

    override fun bindViewEvents() {
        btSurveysItemNext.subscribeOnClick {
            surveysViewModel.output.selectedSurveyUiModel?.let {
                navigator.navigateToSurveyDetails(it)
            }
        }.bindForDisposable()

        clSurveys.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                surveysViewModel.input.nextIndex()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                surveysViewModel.input.previousIndex()
            }
        })

        ivSurveysUserAvatar.subscribeOnClick {
            toggleDrawer(!dlSurveys.isDrawerOpen(GravityCompat.END))
        }.bindForDisposable()

        nvDrawerContainer.setNavigationItemSelectedListener(this)
    }

    override fun bindViewModel() {
        surveysViewModel.output.error
            .subscribe(::bindError)
            .bindForDisposable()

        surveysViewModel.output.navigator
            .subscribe(::bindNavigator)
            .bindForDisposable()

        surveysViewModel.output.showLoading
            .subscribe(::bindLoading)
            .bindForDisposable()

        surveysViewModel.output.showRefreshing
            .subscribe(::bindRefreshing)
            .bindForDisposable()

        surveysViewModel.output.selectedSurveyIndex
            .subscribe(::bindSelectedSurveyIndex)
            .bindForDisposable()

        surveysViewModel.output.surveyItemUiModels
            .subscribe(::bindSurveyItemUiModels)
            .bindForDisposable()

        userViewModel.output.user
            .subscribe(::bindCurrentUserInfo)
            .bindForDisposable()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
                surveysViewModel.logout()
            }
        }
        toggleDrawer(false)
        return false
    }

    private fun bindCurrentUserInfo(user: User) {
        val defaultAvatar = ContextCompat.getDrawable(requireContext(), R.drawable.ic_general_default_user_avatar)
        this.view?.post {
            tvMenuDrawerUserName.text = user.email
            ivMenuDrawerUserAvatar.loadImage(user.avatarUrl, defaultAvatar)
        }
        ivSurveysUserAvatar.loadImage(user.avatarUrl, defaultAvatar)
    }

    private fun bindError(throwable: Throwable) {
        if (throwable !is Ignored) displayError(throwable)
    }

    private fun bindNavigator(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.Surveys.Onboarding -> navigator.navigateToOnboardingActivity()
            else -> Timber.d("Not handled")
        }
    }

    private fun bindLoading(isLoading: Boolean) {
        if (!isLoading) loaderAnimator?.toggleShimmerLoader(isLoading)
        toggleLoading(isLoading)
    }

    private fun bindRefreshing(isRefreshing: Boolean) {
        srlSurveys.isRefreshing = isRefreshing
    }

    private fun bindSelectedSurveyIndex(index: Int) {
        surveysViewModel.output.selectedSurveyUiModel?.let {
            ciSurveysIndicator.animatePageSelected(index)
            tvSurveysItemHeader.switchTextWithFadeAnimation(it.header)
            tvSurveysItemDescription.switchTextWithFadeAnimation(it.description)
            ivSurveysItemBackground.loadImageWithFadeAnimation(it.imageUrl)
        }
    }

    private fun bindSurveyItemUiModels(uiModels: List<SurveyItemUiModel>) {
        if (uiModels.isEmpty()) return
        ciSurveysIndicator.createIndicators(uiModels.size, surveysViewModel.output.selectedSurveyIndexValue)
    }

    private fun toggleDrawer(shouldShow: Boolean) {
        if (shouldShow) dlSurveys.openDrawer(GravityCompat.END) else dlSurveys.closeDrawer(GravityCompat.END)
    }
}
