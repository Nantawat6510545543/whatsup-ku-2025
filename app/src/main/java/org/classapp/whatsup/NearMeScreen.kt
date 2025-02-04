package org.classapp.whatsup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.classapp.whatsup.ui.theme.WhatsUpTheme
import androidx.constraintlayout.compose.*

@Composable
fun NearMeScreen() {
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
                LocationCoordinateDisplay(lat = "0.0", lon = "0.0")
            } // End Column
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun NearMeScreenPreview() {
    NearMeScreen()
}

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
