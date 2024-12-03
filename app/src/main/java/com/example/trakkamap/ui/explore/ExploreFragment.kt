package com.example.trakkamap.ui.explore

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.icu.number.Scale
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trakkamap.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.ScaleBarOverlay


class ExploreFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize osmdroid configuration
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

        // Inflate the layout
        val rootView = inflater.inflate(R.layout.fragment_explore, container, false)

        // Initialize the MapView
        mapView = rootView.findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true) // Enable zoom gestures\


        // creates and applies color filter
        val inverseMatrix = ColorMatrix(
            floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255f,
                0.0f, -1.0f, 0.0f, 0.0f, 255f,
                0.0f, 0.0f, -1.0f, 0.0f, 255f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            )
        )
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                0.30f, 0.525f, 0.15f, 0.00f, 0.0f,
                0.30f, 0.45f, 0.15f, 0.00f, 0.0f,
                0.30f, 0.525f, 0.15f, 0.00f, 0.0f,
                0.00f, 0.00f, 0.00f, 1.00f, 0.0f,
            )
        )
        colorMatrix.preConcat(inverseMatrix);
        val filter = ColorMatrixColorFilter(colorMatrix)
        mapView.overlayManager.tilesOverlay.setColorFilter(filter)

        // prevents from showing repeated maps
        mapView.minZoomLevel = 4.0
        mapView.isVerticalMapRepetitionEnabled = false
        mapView.setScrollableAreaLimitLatitude(85.0, -85.0, 1)

        // Set the default zoom and center
        val mapController = mapView.controller
        mapController.setZoom(15.0)

        val marker = Marker(mapView)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Your location"
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_arrow_circle_down_24)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    val latitude = location.latitude

                    val longitude = location.longitude

                    mapController.setCenter(org.osmdroid.util.GeoPoint(latitude, longitude))
                    marker.position = org.osmdroid.util.GeoPoint(latitude, longitude)
                }

            }

        // Add a marker
        mapView.overlays.add(marker)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach() // Cleanup to avoid memory leaks
    }
}
