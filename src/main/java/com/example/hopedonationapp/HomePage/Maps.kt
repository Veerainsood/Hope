import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.hopedonationapp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.mapView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/map.html")

        // Example: Load a default location
//        loadMapWithLocation(51.505, -0.09)

        // Example: Search for nearby places
//        searchNearbyPlaces("Anath Ashram")
    }

    private fun loadMapWithLocation(latitude: Double, longitude: Double) {
        val script = """
            var map = L.map('map').setView([$latitude, $longitude], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            var marker = L.marker([$latitude, $longitude]).addTo(map);
            marker.bindPopup('Here').openPopup();
        """.trimIndent()
        webView.evaluateJavascript("javascript: $script", null)
    }

//    private fun searchNearbyPlaces(query: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://nominatim.openstreetmap.org")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(NominatimService::class.java)
//        val call = service.searchPlaces(query)
//
//        call.enqueue(object : Callback<List<Place>> {
//            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
//                if (response.isSuccessful) {
//                    val places = response.body()
//                    places?.forEach {
//                        val latitude = it.lat
//                        val longitude = it.lon
//                        val name = it.display_name
//                        // Update map with new markers
//                        addMarkerToMap(latitude, longitude, name)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
//                // Handle failure
//            }
//        })
//    }

    private fun addMarkerToMap(latitude: String, longitude: String, name: String) {
        val script = """
            var marker = L.marker([$latitude, $longitude]).addTo(map);
            marker.bindPopup('$name').openPopup();
        """.trimIndent()
        webView.evaluateJavascript("javascript: $script", null)
    }
}
