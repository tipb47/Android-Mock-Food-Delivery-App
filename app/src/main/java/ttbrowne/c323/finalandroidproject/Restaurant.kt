package ttbrowne.c323.finalandroidproject

import com.google.firebase.firestore.GeoPoint

data class Restaurant(
    var name: String = "",
    var location: GeoPoint? = null,
    var images: List<String> = listOf(),
    var food: List<String> = listOf()
)
