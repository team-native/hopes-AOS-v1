package com.example.hopes.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

/** 기기가 인터넷에 연결돼 있는지 알려준다. 채팅 오프라인 안내(피그마 08-5)에 쓴다. */
interface NetworkMonitor {
    /** 연결 상태가 바뀔 때마다 true(온라인)/false(오프라인)를 내보낸다. 구독하면 현재 상태부터 바로 내보낸다. */
    val isOnline: Flow<Boolean>
}

@Singleton
class ConnectivityNetworkMonitor
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : NetworkMonitor {
        override val isOnline: Flow<Boolean> =
            callbackFlow {
                val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
                if (connectivityManager == null) {
                    trySend(true)
                    close()
                    return@callbackFlow
                }

                val onlineNetworks = mutableSetOf<Network>()
                val callback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            onlineNetworks += network
                            trySend(true)
                        }

                        override fun onLost(network: Network) {
                            onlineNetworks -= network
                            trySend(onlineNetworks.isNotEmpty())
                        }
                    }
                val request =
                    NetworkRequest
                        .Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build()

                connectivityManager.registerNetworkCallback(request, callback)
                trySend(connectivityManager.isCurrentlyOnline())

                awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
            }.distinctUntilChanged().conflate()

        private fun ConnectivityManager.isCurrentlyOnline(): Boolean =
            getNetworkCapabilities(activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
