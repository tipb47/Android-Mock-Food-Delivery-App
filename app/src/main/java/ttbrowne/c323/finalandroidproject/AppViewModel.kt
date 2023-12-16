package ttbrowne.c323.finalandroidproject

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AppViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()

    var user: User = User("","","",null)
    var orderId: String = ""
    var order = MutableLiveData<Order>()

    private val _avatarUri = MutableLiveData<Uri?>()
    val avatarUri: MutableLiveData<Uri?> = _avatarUri

    fun updateAvatarUri(uri: Uri) {
        _avatarUri.value = uri
    }

    private val _restaurantsMap: MutableLiveData<Map<String, Restaurant>> = MutableLiveData()
    val restaurantsMap: LiveData<Map<String, Restaurant>>
        get() = _restaurantsMap

    public val _currentOrder: MutableLiveData<List<FoodItem>> = MutableLiveData()
    val currentOrder: LiveData<List<FoodItem>>
        get() = _currentOrder

    private val _orders: MutableLiveData<MutableList<Order>> = MutableLiveData()
    val orders: LiveData<List<Order>>
        get() = _orders as LiveData<List<Order>>

    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    public val _currentRestaurant = MutableLiveData<Restaurant?>()
    val currentRestaurant: LiveData<Restaurant?>
        get() = _currentRestaurant

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToTracker = MutableLiveData<Boolean>(false)
    val navigateToTracker: LiveData<Boolean>
        get() = _navigateToTracker

    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    private val _navigateToSelfie = MutableLiveData<Boolean>(false)
    val navigateToSelfie: LiveData<Boolean>
        get() = _navigateToSelfie

    private val _navigateToRestaurant = MutableLiveData<Boolean>(false)
    val navigateToRestaurant: LiveData<Boolean>
        get() = _navigateToRestaurant

    private val _navigateToOrderDetailsScreen = MutableLiveData<Boolean>(false)
    val navigateToOrderDetailsScreen: LiveData<Boolean>
        get() = _navigateToOrderDetailsScreen

    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _navigateToCheckout = MutableLiveData<Boolean>(false)
    val navigateToCheckout: LiveData<Boolean>
        get() = _navigateToCheckout

    lateinit var ordersCollection: DatabaseReference

    init {
        updateCurrentUser() //check if user signed in. update user() if needed.
        loadRestaurants() //load restaurant data from firebase

        if (orderId.trim() == "") {
            order.value = Order()
        }
        _orders.value = mutableListOf<Order>()

        _avatarUri.value = Uri.parse("android.resource://ttbrowne.c323.finalandroidproject/${R.drawable.default_pfp}") //default pfp

    }

    //place in firebase
    fun placeOrder() {
        ordersCollection.push().setValue(order.value)
    }

    fun takeAvatarSelfie() {
        _navigateToSelfie.value = true
    }

    fun saveAvatar() {
        _navigateToSignUp.value = true
    }

    //init databse of auth user
    fun initializeTheDatabase() {

        val database = Firebase.database
        ordersCollection = database
            .getReference("orders")
            .child(auth.currentUser!!.uid)


        ordersCollection.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //keep track of changing orders database
                var ordersList: ArrayList<Order> = ArrayList()
                for (orderSnapshot in dataSnapshot.children) {
                    Log.d("ViewModel", "Order Data: $order")
                    var order = orderSnapshot.getValue<Order>()
                    order?.orderId = orderSnapshot.key!!
                    ordersList.add(order!!)
                }
                _orders.value = ordersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        })

    }

    fun getOrderById(orderId: String, callback: (Order?) -> Unit) {
        val orderReference = ordersCollection.child(orderId)

        orderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val order = dataSnapshot.getValue(Order::class.java)
                callback(order)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // error
                callback(null)
            }
        })
    }


    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }

    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }

    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }

    fun navigateToOrderDetailsScreen() {
        _navigateToOrderDetailsScreen.value = true
    }

    fun onNavigatedToOrderDetailsScreen() {
        _navigateToOrderDetailsScreen.value = false
    }

    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    fun onSelfieNavigated() {
        _navigateToSelfie.value = false
    }

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun onNavigatedToHome() {
        _navigateToHome.value = false
    }

    fun navigateToRestaurant() {
        _navigateToRestaurant.value = true
    }

    fun onNavigatedToRestaurant() {
        _navigateToRestaurant.value = false
    }

    fun navigateToTracker() {
        _navigateToTracker.value = true
    }

    fun onNavigatedToTracker() {
        _navigateToTracker.value = false
    }

    fun navigateToCheckout() {
        _navigateToCheckout.value = true
    }

    fun onNavigatedToCheckout() {
        _navigateToCheckout.value = false
    }

    //sign in w/ email, password, throw error if incorrect login
    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                val firebaseUser = auth.currentUser

                // Check if the user's display name and photo URI are available
                val name = firebaseUser?.displayName
                val avatarUri = firebaseUser?.photoUrl

                // Now you have the name and avatarUri
                if (name != null && avatarUri != null) {
                    _avatarUri.value = avatarUri
                    user = User(email, password, name, avatarUri)
                }

                initializeTheDatabase()
                _navigateToHome.value = true
            } else {
                _errorHappened.value = authResult.exception?.message
            }
        }
    }

    //signup with email, password, name, ensure password match.
    fun signUp(email: String, password: String, verifyPassword: String, name: String) {

        // one of the fields is empty
        if (email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty() || name.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }

        // passwords do not match
        if (password != verifyPassword) {
            _errorHappened.value = "Password does not match re-entered password"
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                val firebaseUser = auth.currentUser

                // update the user's profile with name and pic uri
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(_avatarUri.value)
                    .build()

                firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener { updateProfileTask ->
                    if (updateProfileTask.isSuccessful) {
                        //successful
                        user = User(email, password, name, _avatarUri.value)
                        _navigateToHome.value = true
                    } else {
                        _errorHappened.value = updateProfileTask.exception?.message
                    }
                }
                initializeTheDatabase()
            } else {
                _errorHappened.value = authResult.exception?.message
            }
        }
    }

    //load restaurants on start
    private fun loadRestaurants() {
        val firestore = FirebaseFirestore.getInstance()
        val restaurants = mutableMapOf<String, Restaurant>()

        firestore.collection("restuarants").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Assuming the document data directly maps to the Restaurant class structure
                    val restaurant = document.toObject(Restaurant::class.java)
                    // Use the name as the key for the map
                    restaurant.name?.let { name ->
                        restaurants[name] = restaurant
                    }
                }
                // Post the values to the LiveData
                _restaurantsMap.postValue(restaurants)

                Log.d("firestore", "Restaurants Map updated: $restaurants")
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Error getting documents: ", exception)
            }
    }

    //update user from splash page
    private fun updateCurrentUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email ?: ""
            val name = firebaseUser.displayName ?: ""
            val avatarUri = firebaseUser.photoUrl
            _avatarUri.value = avatarUri
            user = User(email, "", name, avatarUri)
            initializeTheDatabase()
        }
    }

    //sign out, reset values
    fun signOut() {
        auth.signOut()
        _avatarUri.value = Uri.parse("android.resource://ttbrowne.c323.finalandroidproject/${R.drawable.default_pfp}") //default pfp
        _navigateToSignUp.value = true
    }


    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}
