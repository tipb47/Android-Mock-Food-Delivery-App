package ttbrowne.c323.finalandroidproject

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.random.Random

class MapScreen : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    val viewModel: AppViewModel by activityViewModels()
    var userLocation: LatLng? = LatLng(39.169998, -86.535156) //default

    private var locationManager : LocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map_screen, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        // get restaurant location from view model
        val restaurantLocation =
            viewModel._currentRestaurant.value?.location?.let {
                viewModel._currentRestaurant.value!!.location?.let { it1 ->
                    LatLng(it.latitude,
                        it1.longitude)
                }
            }

        try {
            // request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch(ex: SecurityException) {
            Log.d("error", "Security Exception, no location available")
        }

        //set markers, camera zone
        userLocation?.let { MarkerOptions().position(it).title("Your Location") }
            ?.let { googleMap?.addMarker(it) }
        restaurantLocation?.let { MarkerOptions().position(it).title("Restaurant Location") }
            ?.let { googleMap?.addMarker(it) }
        userLocation?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
            ?.let { googleMap?.moveCamera(it) }

        //draw line between locations
        googleMap?.addPolyline(
            PolylineOptions()
                .add(userLocation, restaurantLocation)
                .width(5f) // Width of the polyline
                .color(android.graphics.Color.RED) // Color of the polyline
        )

        //setup notification to send
        val deliveryTime = calculateDeliveryTime(userLocation, restaurantLocation)
        val deliveryIntent = Intent(context, DeliveryService::class.java)
        deliveryIntent.putExtra("deliveryTime", deliveryTime)
        context?.startService(deliveryIntent)
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            userLocation = LatLng (location.latitude,location.longitude)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    //create delivery time for notification
    private fun calculateDeliveryTime(userLocation: LatLng?, restaurantLocation: LatLng?): Long {
        val randomFactor = Random.nextInt(5, 101) // Random number between 5 and 100
        val distance = FloatArray(1)
        if (userLocation != null && restaurantLocation != null) {
            Location.distanceBetween(
                userLocation.latitude,
                userLocation.longitude,
                restaurantLocation.latitude,
                restaurantLocation.longitude,
                distance
            )
            val distanceInKm = distance[0] / 1000
            return ((distanceInKm / 100) * randomFactor).toLong()
        }
        return 0L
    }

}