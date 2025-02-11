package org.classapp.whatsup

import android.annotation.SuppressLint
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.classapp.whatsup.ui.theme.WhatsUpTheme
import androidx.constraintlayout.compose.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun NearMeScreen() {
    val screenContext = LocalContext.current
    val locationProvider = LocationServices.getFusedLocationProviderClient(screenContext)

    var latValues: Double? by remember { mutableStateOf(0.0) }
    var lonValues: Double? by remember { mutableStateOf(0.0) }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            latValues = p0.lastLocation?.latitude
            lonValues = p0.lastLocation?.longitude
        }
    }

    val permissionDialog = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                getCurrentUserLocation(locationProvider, locationCallback)
            }
        }
    )

    DisposableEffect(key1 = locationProvider) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            screenContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            getCurrentUserLocation(locationProvider, locationCallback)
        } else {
            permissionDialog.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        onDispose {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }

    WhatsUpTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            // Content on Surface
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Column Content
                Text(text = "Events Near Me")
                LocationCoordinateDisplay(lat = latValues.toString(), lon = lonValues.toString())
                if (latValues != null && lonValues != null)
                    mapDisplay(lat = latValues!!, lon = lonValues!!)
                else mapDisplay()
            } // End Column
        }
    }
}

@Composable
fun rememberMarkerState(markerPosition: LatLng): MarkerState {
    return remember { MarkerState(position = markerPosition) }.apply { position = markerPosition }

}

@Composable
fun mapDisplay(
    lat: Double = 13.74466,
    lon: Double = 100.53291,
    zoomLevel: Float = 13f,
    mapType: MapType = MapType.NORMAL
) {
    val location = LatLng(lat, lon)
    val cameraState = rememberCameraPositionState()
    LaunchedEffect(key1 = location) {
        cameraState.centerOnLocation(location)
//      position = CameraPosition.fromLatLngZoom(location, zoomLevel)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(mapType = mapType),
        cameraPositionState = cameraState
    ) {
        Marker(
            state = rememberMarkerState(location),
            title = "You are Here",
            snippet = "Your Location"
        )
    }

}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun NearMeScreenPreview() {
    NearMeScreen()
}

@SuppressLint("MissingPermission")
private fun getCurrentUserLocation(
    locationProvider: FusedLocationProviderClient,
    locationCb: LocationCallback
) {
    val locationReq = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build()
    locationProvider.requestLocationUpdates(locationReq, locationCb, null)
}

private suspend fun CameraPositionState.centerOnLocation(location: LatLng) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        13f
    ),
    durationMs = 15000
)

@Composable
fun LocationCoordinateDisplay(lat: String, lon: String) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Declare reference points
        val (goBtn, latField, lonField) = createRefs()

        Button(
            onClick = { /* TODO: Handle click */ },
            modifier = Modifier.constrainAs(goBtn) {
                top.linkTo(parent.top, margin = 8.dp)
                end.linkTo(parent.end, margin = 0.dp)
                width = Dimension.wrapContent
            }
        ) {
            Text(text = "GO")
        }

        OutlinedTextField(
            value = lat,
            label = { Text("Latitude") },
            onValueChange = {},
            modifier = Modifier.constrainAs(latField) {
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(goBtn.start, margin = 8.dp)
                width = Dimension.fillToConstraints
            }
        )

        OutlinedTextField(
            value = lon,
            label = { Text("Longitude") },
            onValueChange = {},
            modifier = Modifier.constrainAs(lonField) {
                top.linkTo(latField.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(goBtn.start, margin = 8.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}
