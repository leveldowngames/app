package com.example.trakkamap.ui.explore

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.trakkamap.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class ExploreFragment : Fragment() {

    private lateinit var mapView: MapView

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
        mapView.minZoomLevel = 4.0
        mapView.isVerticalMapRepetitionEnabled = false
        mapView.setScrollableAreaLimitLatitude(85.0, -85.0, 1)


        // Set the default zoom and center
        val mapController = mapView.controller
        mapController.setZoom(15.0)
        mapController.setCenter(org.osmdroid.util.GeoPoint(-29.71322, -53.71666)) // Example: Eiffel Tower coordinates

        // Add a marker
        val marker = Marker(mapView)
        marker.position = org.osmdroid.util.GeoPoint(-29.71322, -53.71666)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Centro de Tecnologia"
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.twotone_arrow_circle_down_24)
        mapView.overlays.add(marker)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach() // Cleanup to avoid memory leaks
    }
}
