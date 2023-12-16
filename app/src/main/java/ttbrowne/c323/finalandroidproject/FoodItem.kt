package ttbrowne.c323.finalandroidproject

//food item to track user order
data class FoodItem (
    val name: String,
    val price: Double,
    var quantity: Int = 0
)