package com.showcase.highlightstoday.repository

import com.showcase.highlightstoday.repository.dataSource.NewsRemote


/**
 * A single point of entrance to the
 * network module. It exposes all the remotes that
 * the repository layer expects the network layer to host.
 */
interface NetworkGateway {
    fun getNewsRemote(): NewsRemote
}