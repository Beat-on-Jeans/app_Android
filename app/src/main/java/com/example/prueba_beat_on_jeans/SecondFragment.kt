package com.example.prueba_beat_on_jeans

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class SecondFragment : Fragment() {

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

    private fun getLocation() {
        // Verificar si los permisos son correctos
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la última ubicación conocida
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Obtener la ubicación actual
                val lat = location.latitude
                val lon = location.longitude

                // Mostrar la ubicación en el mapa
                val geoPoint = GeoPoint(lat, lon)
                mapView.controller.setCenter(geoPoint)
                mapView.controller.setZoom(18)

                // Detener la actualización de la ubicación para no consumir recursos innecesarios
                locationManager.removeUpdates(this)
            }

            // Puedes eliminar estas funciones si no las vas a usar
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        })
    }

    // Manejar la respuesta de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si se concede el permiso, obtener la ubicación
                getLocation()
            } else {
                // Si no se concede el permiso, mostrar mensaje
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}