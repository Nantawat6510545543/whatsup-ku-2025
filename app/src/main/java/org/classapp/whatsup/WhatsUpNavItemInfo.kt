package org.classapp.whatsup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class WhatsUpNavItemInfo(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Star,
    val route: String = ""
) {
    // Function to get all navigation items
    fun getAllNavItems(): List<WhatsUpNavItemInfo> {
        return listOf(
            WhatsUpNavItemInfo(
                label = "Highlight",
                icon = Icons.Filled.Star,
                route = DestinationScreen.Highlight.route
            ),
            WhatsUpNavItemInfo(
                label = "Near Me",
                icon = Icons.Filled.LocationOn,
                route = DestinationScreen.NearMe.route
            ),
            WhatsUpNavItemInfo(
                label = "My Events",
                icon = Icons.Filled.Face,
                route = DestinationScreen.MyEvents.route
            )
        )
    }
}



