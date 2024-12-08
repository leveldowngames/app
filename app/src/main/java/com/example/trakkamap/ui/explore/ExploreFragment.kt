package com.example.trakkamap.ui.explore

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.trakkamap.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.animation.AnimatableView.Listener
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class ExploreFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var buttonCenterMap : ImageButton

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
        colorMatrix.preConcat(inverseMatrix)
        val filter = ColorMatrixColorFilter(colorMatrix)
        mapView.overlayManager.tilesOverlay.setColorFilter(filter)

        // prevents from showing repeated maps
        mapView.minZoomLevel = 4.0
        mapView.isVerticalMapRepetitionEnabled = false
        mapView.setScrollableAreaLimitLatitude(85.0, -85.0, 1)

        // Set the default zoom and center
        val mapController = mapView.controller
        mapController.setZoom(16.0)

        val locationOverlay =
            object : MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView) {
                override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
                    super.onLocationChanged(location, source)
                }
            }

        locationOverlay.enableFollowLocation()
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_arrow_circle_down_24)?.toBitmap()

        locationOverlay.setDirectionIcon(icon)
        locationOverlay.setPersonIcon(icon)

        locationOverlay.enableMyLocation()
        locationOverlay.isDrawAccuracyEnabled = false
        locationOverlay.setPersonAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        locationOverlay.setDirectionAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        mapView.overlays.add(locationOverlay)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    mapController.setCenter(GeoPoint(latitude, longitude))
                }


            }.addOnFailureListener {
                mapController.setCenter(GeoPoint(0.0, 0.0))
                mapController.setZoom(4.0)
            }

        // adds event listener for scroll and zoom with callback
        // to the functions defined in class ChangeMaker
        val listener = ChangeMaker(mapView, requireContext())
        mapView.addMapListener(listener)

        ChangeMaker.drawGrid(mapView, requireContext())


        return rootView
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonCenterMap = requireView().findViewById(R.id.ic_center_map)
        buttonCenterMap.setOnClickListener {
            var myPosition : Location? = null
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    myPosition = location
                    if(myPosition != null)
                    {
                        val point = GeoPoint(myPosition!!.latitude, myPosition!!.longitude)
                        mapView.controller.animateTo(point, 15.0, 1000)

                    }
                }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach() // Cleanup to avoid memory leaks
    }
}