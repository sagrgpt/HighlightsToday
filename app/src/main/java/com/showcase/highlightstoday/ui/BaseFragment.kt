package com.showcase.highlightstoday.ui

import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.showcase.highlightstoday.di.CompositionRoot
import com.showcase.highlightstoday.di.Injector
import com.showcase.highlightstoday.di.PresentationRoot

/**
 * A low-level abstraction for dependency injection
 * logic for all fragments.
 */
open class BaseFragment: Fragment() {
    private val presentationRoot by lazy {
        PresentationRoot(getCompositionRoot())
    }

    @UiThread
    protected fun getInjector(): Injector {
        return Injector(presentationRoot)
    }

    private fun getCompositionRoot(): CompositionRoot {
        return (activity as HostActivity).getCompositionRoot()
    }

}