package com.djx.kmpappsubstrate.router

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.djx.kmpappsubstrate.views.index.IndexScreen
import com.djx.kmpappsubstrate.views.local.LocalScreen
import com.djx.kmpappsubstrate.views.remote.RemoteScreen


@Composable
fun Router() {
    val navController: NavHostController = rememberNavController()
    fun back() {
        navController.popBackStack()
    }
    NavHost(navController = navController, startDestination = Routes.Index.route) {
        composable(Routes.Index.route) {
            IndexScreen(go = {
                navController.navigate(it)
            })
        }
        composable(Routes.Local.route) {
            LocalScreen(back = ::back)
        }
        composable(Routes.Remote.route) {
            RemoteScreen(back = ::back)
        }
    }
}