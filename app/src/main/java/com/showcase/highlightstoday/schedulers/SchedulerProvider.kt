package com.showcase.highlightstoday.schedulers

import io.reactivex.Scheduler

/**
 * Scheduler for threading requirements.
 */
interface SchedulerProvider {
    val io: Scheduler
    val ui: Scheduler
    val computation: Scheduler
    val newThread: Scheduler
}