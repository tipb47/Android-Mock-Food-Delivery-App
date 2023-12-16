package ttbrowne.c323.finalandroidproject

import com.google.firebase.database.Exclude

data class Order (
    @get:Exclude
    var orderId: String = "",
    var restaurant: String = "",
    var items: Map<String, Int> = hashMapOf(),
    var price: String = "",
    var date: String = "",
    var time: String = "",
    var address: String = "",
    var specialInstructions: String = ""
)