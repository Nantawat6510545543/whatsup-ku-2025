package org.classapp.whatsup

import android.app.DownloadManager.Query
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import org.classapp.whatsup.customui.theme.AppTheme
import org.classapp.whatsup.ui.theme.WhatsUpTheme
import java.text.SimpleDateFormat

data class Event(
    val event_name: String? = "",
    val event_venue: String? = "",
    val start_date: Timestamp? = null,
    val end_date: Timestamp? = null,
)

@Composable
fun HighlightScreen() {
    val screenContext = LocalContext.current
    val eventList = remember { mutableStateListOf<Event?>() }
    val onFireBaseQueryFailed =
        { e: Exception -> Toast.makeText(screenContext, e.message, Toast.LENGTH_LONG).show() }

    val onFireBaseQuerySuccess = { results: QuerySnapshot ->
        if (!results.isEmpty) {
            for (document in results) {
                val event: Event? = document.toObject(Event::class.java)
                eventList.add(event)

                val dataFormat = SimpleDateFormat("dd-MM-yyyy")
                val startDate = dataFormat.format(event?.start_date?.toDate())
                val endDate = dataFormat.format(event?.end_date?.toDate())
                Toast.makeText(
                    screenContext,
                    "Event: ${event?.event_name}",
                    Toast.LENGTH_LONG
                ).show()
//                Toast.makeText(
//                    screenContext,
//                    "${event?.event_name} at ${event?.event_venue} ($endDate - $startDate)",
//                    Toast.LENGTH_LONG
//                ).show()
            }
        }
    }
    getEventsFromFirebase(onFireBaseQuerySuccess, onFireBaseQueryFailed)
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Highlights")
                EventList(events = eventList)
            }
        }
    }
}

@Composable
fun EventItem(event: Event) {
    ElevatedCard(elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth(1f).padding((8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.md_theme_surfaceContainerHigh))) {
        Column(modifier = Modifier.fillMaxWidth(1f).padding(8.dp)) {
            Text(
                text = event.event_name!!,
                style = TextStyle(
                    color = colorResource(id = R.color.md_theme_primary),
                    fontSize = 20.sp)
            )
            Text(
                text = event.event_venue!!,
                style = TextStyle(
                    color = colorResource(id = R.color.md_theme_primary),
                    fontSize = 18.sp))
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
        Row{
            Text(text = "From: ",
                style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                    fontSize = 18.sp))
            Text(text = dateFormatter.format(event.start_date?.toDate()),
                style = TextStyle(fontSize = 18.sp))
        }
        Row{
            Text(text = "To: ",
                style = TextStyle(color = colorResource(id = R.color.md_theme_secondary),
                    fontSize = 18.sp))
            Text(text = dateFormatter.format(event.end_date?.toDate()),
                style = TextStyle(fontSize = 18.sp))
        }
        }
   }
}

@Composable
fun EventList(events: List<Event?>){
    LazyColumn(contentPadding = PaddingValues(all = 4.dp)) {
        items(items =  events.filterNotNull()) {
            EventItem(event = it)
        }
    }
}

private fun getEventsFromFirebase(
    onSuccess: (QuerySnapshot) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = Firebase.firestore
    db.collection("event").get()
        .addOnSuccessListener { results -> onSuccess(results) }
        .addOnFailureListener { result -> onFailure(result) }
}