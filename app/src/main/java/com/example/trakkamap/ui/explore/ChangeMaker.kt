package com.example.trakkamap.ui.explore

import android.content.Context
import android.graphics.Color
import android.util.Log
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

// ChangeMaker extends MapListener
// MapListener interface https://github.com/osmdroid/osmdroid/blob/master/osmdroid-android/src/main/java/org/osmdroid/events/MapListener.java

class ChangeMaker(private var mapView : MapView, private var context: Context) : MapListener {

    private var lastTime = System.currentTimeMillis()

    private fun getExploredIDs(context: Context, zoom: Double) : List<Pair<Int, Int>> {
        val file = File(context.filesDir, "records.txt")
        if(!file.exists())
        {
            return emptyList()
        }

        val recordFileString = file.readText()
        val readGeotags = recordFileString.split("\n")
        val exploredGeotags = arrayListOf<Pair<Int, Int>>()

        for (tag in readGeotags)
        {
            try {
                val tuple = tag.replace(")", "").replace("(", "").replace(" ", "").split(",")
                exploredGeotags.add(Pair<Int, Int>(tuple[0].toInt(), tuple[1].toInt()))
            }
            catch (e: NumberFormatException)
            {
                continue
            }
        }

        return exploredGeotags
    }

    private fun getScreenIDs(context: Context, zoom: Double) : List<Pair<Int, Int>>{
        val highLonID = ceil((mapView.projection.boundingBox.lonEast+180)/0.009).toInt()
        val lowLonID = floor((mapView.projection.boundingBox.lonWest+180)/0.009).toInt()
        val highLatID = ceil((mapView.projection.boundingBox.latNorth+90)/0.009).toInt()
        val lowLatID = floor((mapView.projection.boundingBox.latSouth+90)/0.009).toInt()

        val listedIDs = arrayListOf<Pair<Int, Int>>()
        // (longitudeID, latitudeID) is the order
        for (i in lowLonID until highLonID)
        {
            for (j in lowLatID until highLatID)
            {
                listedIDs.add(Pair<Int, Int>(i, j))
            }
        }

        return listedIDs
    }

    private fun getGeographicalCoordsFromID(id: Pair<Int, Int>) : Pair<Double, Double>
    {
        // receives (lonID, latID), returns (lon, lat)
        return Pair<Double, Double>(id.second*0.009-90,id.first*0.009-180)
    }


    private fun generatePolygonFromID(id: Pair<Int, Int>) : Polygon{
        val geoPoints = ArrayList<GeoPoint>()
        val coords = getGeographicalCoordsFromID(id)


        // add your points here
        val polygon = Polygon();
        geoPoints.add(GeoPoint(coords.first, coords.second))
        geoPoints.add(GeoPoint(coords.first, coords.second+0.009))
        geoPoints.add(GeoPoint(coords.first+0.009, coords.second+0.009))
        geoPoints.add(GeoPoint(coords.first+0.009, coords.second))
        geoPoints.add(geoPoints[0]);    //forces the loop to close(connect last point to first point)

        polygon.fillPaint.color = Color.parseColor("#1Ebb42fc") //set fill color
        polygon.setPoints(geoPoints);
        polygon.title = "A sample polygon"
        polygon.outlinePaint.color = Color.parseColor("#0Fbb42fc")
        polygon.outlinePaint.strokeWidth = 10.0f

        return polygon
    }

    private fun killPrevious()
    {
        for(overlay in mapView.overlays)
        {
            if (overlay is Polygon)
            {
                mapView.overlays.remove(overlay)
            }
        }
    }

    fun drawGrid(context: Context){
        Log.i("drawGrid", mapView.boundingBox.toString())

        killPrevious()
        val zoom = mapView.zoomLevelDouble
        val exploredGeotags = getExploredIDs(context, zoom)
        val screenGeotags = getScreenIDs(context, zoom)


        for (screenTag in screenGeotags)
        {
            if (exploredGeotags.indexOf(screenTag) == -1)
            {
                val polygon = (generatePolygonFromID(screenTag))
                mapView.overlays.add(polygon)
            }
        }

    }


    override fun onScroll(event: ScrollEvent?): Boolean {
        val curTime = System.currentTimeMillis()
        if((curTime - lastTime) > 1000)
        {
            lastTime = curTime
            drawGrid(context)
        }
        return false
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }
}