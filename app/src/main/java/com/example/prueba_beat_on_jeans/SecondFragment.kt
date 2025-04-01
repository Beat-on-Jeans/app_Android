package com.example.prueba_beat_on_jeans

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
// Para las librerías de Retrofit y las llamadas de red
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Para la clase de Adapter (suponiendo que usas un RecyclerView Adapter)
import com.example.prueba_beat_on_jeans.User

// Para manejar los SharedPreferences y la sesión del usuario (si es necesario)
import com.example.prueba_beat_on_jeans.MainActivity

// Para los métodos de Retrofit y la creación del cliente Retrofit
import com.example.prueba_beat_on_jeans.RetrofitClient
import org.json.JSONArray
import java.io.IOException
import java.net.URLEncoder


class SecondFragment : Fragment() {

    private lateinit var imageViewMostrar: ImageView
    private lateinit var location: ImageView
    private lateinit var text_name: TextView
    private lateinit var text_location: TextView
    private lateinit var text_description: TextView
    private lateinit var buttonContact: Button
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var locationOverlay: MyLocationNewOverlay

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        imageViewMostrar = view.findViewById(R.id.imageViewMostrar)
        location = view.findViewById(R.id.location)
        text_name = view.findViewById(R.id.text_name)
        text_location = view.findViewById(R.id.text_location)
        text_description = view.findViewById(R.id.text_description)
        buttonContact = view.findViewById(R.id.button_contact)

        when(MainActivity.UserSession.rolId){
            1 -> {
                getLocals()

            }
            2 -> {
                getMusicians()

            }
        }


        // Configuración inicial de osmdroid
        Configuration.getInstance().load(requireContext(), requireActivity().getPreferences(android.content.Context.MODE_PRIVATE))

        // Referencia al MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)

        // Configurar el LocationManager
        locationManager = requireActivity().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager

        // Configurar el overlay para mostrar la ubicación
        locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation() // Habilitar la ubicación
        mapView.overlays.add(locationOverlay)

        val barcelonaGeoPoint = GeoPoint(41.3784, 2.1925) // Coordenadas de Barcelona
        mapView.controller.setCenter(barcelonaGeoPoint) // Centrar el mapa en Barcelona
        mapView.controller.setZoom(12) // Ajusta el nivel de zoom (puedes cambiarlo)


        // Botón para obtener la ubicación actual
        val btnShowLocation = view.findViewById<Button>(R.id.btnShowLocation)
        btnShowLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Si se tienen permisos, se obtiene la ubicación
                getLocation()
            } else {
                // Si no, se solicitan permisos
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        return view
    }

    private fun getMusicians() {
        RetrofitClient.instance.getMusicos().enqueue(object : Callback<List<UserRecieved>> {
            override fun onResponse(call: Call<List<UserRecieved>>, response: Response<List<UserRecieved>>) {
                if (response.isSuccessful) {
                    val musicianss = response.body()
                    if (musicianss != null) {
                        Log.e("Musicos", musicianss.toString())
                    }
                } else {
                    Log.e("Error", "Error de conexión")
                }
            }

            override fun onFailure(call: Call<List<UserRecieved>>, t: Throwable) {
                Log.e("Error", "Error al cargar los locales")
            }
        })
    }

    private fun getLocals() {
        RetrofitClient.instance.getLocales().enqueue(object : Callback<List<UserRecieved>> {
            override fun onResponse(call: Call<List<UserRecieved>>, response: Response<List<UserRecieved>>) {
                if (response.isSuccessful) {
                    val locals = response.body()
                    if (locals != null) {
                        Log.e("Locales", locals.toString())
                        showLocalsOnMap(locals)
                    }
                } else {
                    Log.e("Error", "Error de conexión")
                }
            }

            override fun onFailure(call: Call<List<UserRecieved>>, t: Throwable) {
                Log.e("Error", "Error al cargar los locales")
            }
        })
    }

    fun getCoordinatesFromAddressNominatim(address: String, callback: (latitude: Double?, longitude: Double?) -> Unit) {
        val client = OkHttpClient()

        try {
            // Format address better by adding country and proper encoding
            val formattedAddress = "$address, Spain".trim().replace(" ", "+")
            val encodedAddress = URLEncoder.encode(formattedAddress, "UTF-8")

            val url = "https://nominatim.openstreetmap.org/search?q=$encodedAddress&format=json&addressdetails=1"

            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "YourAppName/1.0") // Required by Nominatim
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("Geocoding", "Network error: ${e.message}")
                    activity?.runOnUiThread {
                        callback(null, null)
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    try {
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            if (body.isNullOrEmpty()) {
                                Log.e("Geocoding", "Empty response body")
                                activity?.runOnUiThread {
                                    callback(null, null)
                                }
                                return
                            }

                            val jsonArray = JSONArray(body)
                            if (jsonArray.length() > 0) {
                                val firstResult = jsonArray.getJSONObject(0)
                                val lat = firstResult.getDouble("lat")
                                val lon = firstResult.getDouble("lon")

                                Log.d("Geocoding", "Found coordinates: $lat,$lon for address: $address")

                                activity?.runOnUiThread {
                                    callback(lat, lon)
                                }
                            } else {
                                Log.e("Geocoding", "No results found for address: $address")
                                activity?.runOnUiThread {
                                    callback(null, null)
                                }
                            }
                        } else {
                            Log.e("Geocoding", "API error: ${response.code} - ${response.message}")
                            activity?.runOnUiThread {
                                callback(null, null)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Geocoding", "Parsing error: ${e.message}")
                        activity?.runOnUiThread {
                            callback(null, null)
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.e("Geocoding", "Initialization error: ${e.message}")
            activity?.runOnUiThread {
                callback(null, null)
            }
        }
    }

    private fun showLocalsOnMap(locals: List<UserRecieved>) {
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(10)

        for (local in locals) {
            val address = local.ubicacion ?: continue

            getCoordinatesFromAddressNominatim(address) { latitude, longitude ->
                activity?.runOnUiThread {
                    if (latitude != null && longitude != null) {
                        val point = GeoPoint(latitude, longitude)

                        val marker = Marker(mapView).apply {
                            position = point
                            title = local.nombre
                            snippet = "Dirección: ${local.ubicacion}"

                            setOnMarkerClickListener { _, _ ->
                                text_name.text = local.nombre
                                text_location.text = local.ubicacion
                                text_description.text = "No se ha proporcionado descripción"
                                imageViewMostrar.load(local.imagen){
                                    crossfade(true)
                                }
                                buttonContact.isVisible = true
                                buttonContact.setOnClickListener {
                                    Toast.makeText(context, "Contactando con ${local.nombre}", Toast.LENGTH_SHORT).show()
                                }

                                true
                            }
                        }

                        mapView.overlays.add(marker)
                        mapView.invalidate() // Refrescar el mapa
                    }
                }
            }
        }

        if (locals.isNotEmpty()) {
            val firstLocal = locals[0]
            val address = firstLocal.ubicacion
            if (address != null) {
                getCoordinatesFromAddressNominatim(address) { latitude, longitude ->
                    if (latitude != null && longitude != null) {
                        mapController.setCenter(GeoPoint(latitude, longitude))
                    }
                }
            }
        }
    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show()
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Obtener la ubicación actual
                val lat = location.latitude
                val lon = location.longitude

                // Mostrar la ubicación en el mapa
                val geoPoint = GeoPoint(lat, lon)
                mapView.controller.setCenter(geoPoint)
                mapView.controller.setZoom(18)

                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}