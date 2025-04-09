package com.example.prueba_beat_on_jeans.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.api.Match
import com.example.prueba_beat_on_jeans.api.Matches
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.UserRecievedWithDescription
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.ResponseBody
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
// Para las librerías de Retrofit y las llamadas de red
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Para la clase de Adapter (suponiendo que usas un RecyclerView Adapter)

// Para manejar los SharedPreferences y la sesión del usuario (si es necesario)

// Para los métodos de Retrofit y la creación del cliente Retrofit
import org.json.JSONArray
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import java.io.IOException
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SecondFragment : Fragment() {

    private lateinit var imageViewMostrar: ImageView
    private lateinit var location: ImageView
    private lateinit var text_name: TextView
    private lateinit var text_location: TextView
    private lateinit var text_description: TextView
    private lateinit var buttonContact: Button
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var frameDescription: LinearLayout
    private lateinit var layout_superior: LinearLayout
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var stars: TextView
    private lateinit var button_hide_description: Button
    private lateinit var zone_text: TextView
    private lateinit var musician_text: TextView
    private lateinit var zone_spinner: Spinner
    private lateinit var musician_spinner: Spinner
    private lateinit var btnShowLocation: Button
    private lateinit var btnLocationLayout: Button
    private var musiciansList: List<UserRecievedWithDescription> = listOf()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.visibility = View.VISIBLE
        imageViewMostrar = view.findViewById(R.id.imageViewMostrar)
        location = view.findViewById(R.id.location)
        text_name = view.findViewById(R.id.text_name)
        text_location = view.findViewById(R.id.text_location)
        text_description = view.findViewById(R.id.text_description)
        buttonContact = view.findViewById(R.id.button_contact)
        frameDescription = view.findViewById(R.id.local_description)
        layout_superior = view.findViewById(R.id.layout_superior)
        button_hide_description = view.findViewById(R.id.button_hide_description)
        stars = view.findViewById(R.id.stars)
        zone_text = view.findViewById(R.id.zone_text)
        musician_text = view.findViewById(R.id.musician_text)
        zone_spinner = view.findViewById(R.id.zone_spinner)
        musician_spinner = view.findViewById(R.id.musician_spinner)
        btnShowLocation = view.findViewById(R.id.btnShowLocation)
        btnLocationLayout = view.findViewById(R.id.btnLocationLayout)

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        mapView.let {
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.controller.setZoom(15)
            val barcelonaGeoPoint = GeoPoint(41.3784, 2.1925)
            mapView.controller.setCenter(barcelonaGeoPoint)
        }

        locationManager = requireActivity().getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager

        when(MainActivity.UserSession.rolId){
            1 -> {
                if (this::mapView.isInitialized) {
                    getLocalsWithFiltering()
                }
                zone_text.visibility = View.INVISIBLE
                zone_spinner.visibility = View.INVISIBLE
                musician_text.visibility = View.INVISIBLE
                musician_spinner.visibility = View.INVISIBLE
            }
            2 -> {
                getMusicians()
                mapView.visibility = View.INVISIBLE
                btnLocationLayout.visibility = View.INVISIBLE
                btnShowLocation.visibility = View.INVISIBLE
            }
        }

        button_hide_description.setOnClickListener{
            layout_superior.visibility = View.GONE
            frameDescription.visibility = View.GONE
        }

        // Configuración inicial de osmdroid
        Configuration.getInstance().load(requireContext(), requireActivity().getPreferences(android.content.Context.MODE_PRIVATE))

        // Configurar el overlay para mostrar la ubicación
        locationOverlay = MyLocationNewOverlay(mapView)
        locationOverlay.enableMyLocation() // Habilitar la ubicación
        mapView.overlays.add(locationOverlay)

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

    private fun getLocalsWithFiltering() {
        lifecycleScope.launch {
            try {
                // 1. Get user's existing matches with timeout
                val existingMatches = try {
                    withTimeout(3_000) {
                        getUserMatchesSuspend(MainActivity.UserSession.id!!)
                    }
                } catch (e: TimeoutCancellationException) {
                    Log.e("Error", "Timeout getting matches")
                    emptyList()
                } catch (e: Exception) {
                    Log.e("Error", "Error getting matches: ${e.message}")
                    emptyList()
                }

                // 2. Filter IDs to exclude
                val excludedUserIds = existingMatches
                    .filter { match ->
                        match.estado == 1 || match.estado == 3 || match.creador_id == MainActivity.UserSession.id
                    }
                    .flatMap { match ->
                        listOf(match.creador_id, match.finalizador_id)
                            .filterNotNull()
                            .filter { it != MainActivity.UserSession.id }
                    }
                    .toSet()

                // 3. Get all locals with timeout
                val allLocals = try {
                    withTimeout(3_000) {
                        getLocalesSuspend()
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Error getting locals: ${e.message}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error loading locals", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // 4. Filter locals
                val filteredLocals = allLocals.filter { local ->
                    !excludedUserIds.contains(local.id)
                }

                // 5. Update UI
                withContext(Dispatchers.Main) {
                    if (filteredLocals.isEmpty()) {
                        Toast.makeText(context, "No locals available", Toast.LENGTH_SHORT).show()
                    }
                    showLocalsOnMap(filteredLocals)
                }

            } catch (e: Exception) {
                Log.e("Error", "Unexpected error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Unexpected error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Add this new suspend function to get locals
    private suspend fun getLocalesSuspend(): List<UserRecievedWithDescription> {
        return try {
            suspendCoroutine { continuation ->
                RetrofitClient.instance.getLocales().enqueue(object : Callback<List<UserRecievedWithDescription>> {
                    override fun onResponse(
                        call: Call<List<UserRecievedWithDescription>>,
                        response: Response<List<UserRecievedWithDescription>>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(response.body() ?: listOf())
                        } else {
                            continuation.resumeWithException(Exception("HTTP ${response.code()}"))
                        }
                    }

                    override fun onFailure(call: Call<List<UserRecievedWithDescription>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
        } catch (e: Exception) {
            Log.e("API Error", "Error in getLocales: ${e.message}")
            throw e
        }
    }


    private fun getMusicians() {
        lifecycleScope.launch {
            try {
                // 1. Obtener matches del usuario con timeout personalizado
                val existingMatches = try {
                    withTimeout(3_000) { // 15 segundos de timeout
                        getUserMatchesSuspend(MainActivity.UserSession.id!!)
                    }
                } catch (e: TimeoutCancellationException) {
                    Log.e("Error", "Timeout al obtener matches")
                    emptyList() // Continuamos con lista vacía
                } catch (e: Exception) {
                    Log.e("Error", "Error al obtener matches: ${e.message}")
                    emptyList() // Continuamos con lista vacía
                }

                Log.e("pene", existingMatches.toString())

                // 2. Filtrar IDs a excluir
                val excludedUserIds = existingMatches
                    .filter { match ->
                        // Filtra matches con estado 1 ó 3 O donde el usuario sea el creador
                        match.estado == 1 || match.estado == 3 || match.creador_id == MainActivity.UserSession.id
                    }
                    .flatMap { match ->
                        // Toma ambos IDs (creador y finalizador) excluyendo al usuario actual
                        listOf(match.creador_id, match.finalizador_id)
                            .filterNotNull()
                            .filter { it != MainActivity.UserSession.id }
                    }
                    .toSet()

                // 3. Obtener todos los músicos con timeout personalizado
                val allMusicians = try {
                    withTimeout(3_000) { // 15 segundos de timeout
                        getMusicosSuspend()
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Error al obtener músicos: ${e.message}")
                    // Mostrar mensaje al usuario
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al cargar músicos", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // 4. Filtrar músicos
                val filteredMusicians = allMusicians.filter { musician ->
                    !excludedUserIds.contains(musician.id)
                }

                // 5. Actualizar UI
                withContext(Dispatchers.Main) {
                    if (filteredMusicians.isEmpty()) {
                        Toast.makeText(context, "No hay músicos disponibles", Toast.LENGTH_SHORT).show()
                    }
                    updateSpinners(filteredMusicians)
                }

            } catch (e: Exception) {
                Log.e("Error", "Excepción inesperada: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error inesperado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Versión mejorada de getMusicosSuspend con manejo de errores
    private suspend fun getMusicosSuspend(): List<UserRecievedWithDescription> {
        return try {
            suspendCoroutine { continuation ->
                RetrofitClient.instance.getMusicos().enqueue(object : Callback<List<UserRecievedWithDescription>> {
                    override fun onResponse(
                        call: Call<List<UserRecievedWithDescription>>,
                        response: Response<List<UserRecievedWithDescription>>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(response.body() ?: listOf())
                        } else {
                            continuation.resumeWithException(Exception("Código HTTP ${response.code()}"))
                        }
                    }

                    override fun onFailure(call: Call<List<UserRecievedWithDescription>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
        } catch (e: Exception) {
            Log.e("API Error", "Error en getMusicos: ${e.message}")
            throw e
        }
    }

    // Versión mejorada de getUserMatchesSuspend con manejo de errores
    private suspend fun getUserMatchesSuspend(userId: Int): List<Match> {
        return try {
            suspendCoroutine { continuation ->
                RetrofitClient.instance.getUserMatches(userId).enqueue(object : Callback<List<Match>> {
                    override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                        if (response.isSuccessful) {
                            continuation.resume(response.body() ?: listOf())
                        } else {
                            continuation.resumeWithException(Exception("Código HTTP ${response.code()}"))
                        }
                    }

                    override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
        } catch (e: Exception) {
            Log.e("API Error", "Error en getUserMatches: ${e.message}")
            throw e
        }
    }

    // Las funciones updateSpinners y updateMusicianSpinner permanecen igual que en la solución anterior
    private fun updateSpinners(musicians: List<UserRecievedWithDescription>) {
        // Obtener ubicaciones únicas
        val locations = musicians.mapNotNull { it.ubicacion }.distinct()

        // Configurar spinner de zonas
        val locationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locations
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        zone_spinner.adapter = locationAdapter

        // Configurar listener para el spinner de zonas
        zone_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedLocation = parent?.getItemAtPosition(pos) as? String ?: return
                updateMusicianSpinner(musicians, selectedLocation)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateMusicianSpinner(musicians: List<UserRecievedWithDescription>, location: String) {
        val musiciansInLocation = musicians.filter { it.ubicacion == location }
        val names = musiciansInLocation.map { it.nombre }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            names
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        musician_spinner.adapter = adapter

        // Configurar listener para el spinner de músicos
        musician_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedName = parent?.getItemAtPosition(pos) as? String ?: return
                musiciansInLocation.find { it.nombre == selectedName }?.let { musician ->
                    showMusicianDetails(musician)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showMusicianDetails(selectedMusician: UserRecievedWithDescription) {
        layout_superior.visibility = View.VISIBLE
        frameDescription.visibility = View.VISIBLE
        text_name.text = selectedMusician.nombre
        text_location.text = selectedMusician.ubicacion
        text_description.text = selectedMusician.descripcion
        if(selectedMusician.valoracion.toString() != "null"){
            stars.text = selectedMusician.valoracion.toString()
        } else {
            stars.text = "0"
        }
        imageViewMostrar.load(selectedMusician.imagen){
            crossfade(true)
            transformations(RoundedCornersTransformation(16f))
        }
        buttonContact.isVisible = true
        buttonContact.setOnClickListener {
            createMatch(selectedMusician)
            activity?.findViewById<BottomNavigationView>(R.id.navMenu)?.selectedItemId = R.id.itemFragment3
            Toast.makeText(
                context,
                "Esperando la respuesta de " + selectedMusician.nombre,
                Toast.LENGTH_SHORT
            ).show()
        }
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

    private fun showLocalsOnMap(locals: List<UserRecievedWithDescription>?) {
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(15)

        for (local in locals!!) {
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
                                layout_superior.visibility = View.VISIBLE
                                frameDescription.visibility = View.VISIBLE
                                text_name.text = local.nombre
                                text_location.text = local.ubicacion
                                text_description.text = local.descripcion
                                if(local.valoracion.toString() != "null"){
                                    stars.text = local.valoracion.toString()
                                } else {
                                    stars.text = "0"
                                }
                                imageViewMostrar.load(local.imagen){
                                    crossfade(true)
                                    transformations(RoundedCornersTransformation(16f))
                                }
                                buttonContact.isVisible = true
                                buttonContact.setOnClickListener {
                                    createMatch(local)
                                    activity?.findViewById<BottomNavigationView>(R.id.navMenu)?.selectedItemId = R.id.itemFragment3
                                    Toast.makeText(
                                        context,
                                        "Esperando la respuesta de " + local.nombre,
                                        Toast.LENGTH_SHORT
                                    ).show()
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

    private fun createMatch(user: UserRecievedWithDescription) {
        lifecycleScope.launch {
            try {
                val call = RetrofitClient.instance.createNewMatch(
                    MainActivity.UserSession.id!!,
                    user.id
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("API_RESPONSE", "Match creado correctamente")
                        } else {
                            Log.e("API_ERROR", "Error en la respuesta: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("API_ERROR", "Fallo en la petición: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error en la creación del match: ${e.message}")
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
                mapView.controller.setZoom(15)

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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

}