package com.showcase.highlightstoday.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultScheduler : SchedulerProvider {
    override val io: Scheduler
        get() = Schedulers.io()
    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val computation: Scheduler
        get() = Schedulers.computation()
    override val newThread: Scheduler
        get() = Schedulers.newThread()
}