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
        Log.i("zoom", zoom.toString())
        var file : File
        if (zoom > 14)
        {
            file = File(context.filesDir, "recordsA.txt")
            if(!file.exists())
            {
                return emptyList()
            }
        }

        else if(zoom > 12)
        {
            file = File(context.filesDir, "recordsB.txt")
            if(!file.exists())
            {
                return emptyList()
            }
        }

        else if(zoom > 9)
        {
            file = File(context.filesDir, "recordsC.txt")
            if(!file.exists())
            {
                return emptyList()
            }
        }

        else if(zoom > 7)
        {
            file = File(context.filesDir, "recordsD.txt")
            if(!file.exists())
            {
                return emptyList()
            }
        }

        else
        {
            file = File(context.filesDir, "recordsE.txt")
            if(!file.exists())
            {
                return emptyList()
            }
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

    private fun divisionSize(zoom: Double) : Double {
        return if (zoom > 14)
            0.009
        else if(zoom > 12)
            0.036
        else if(zoom > 9)
            0.36
        else if(zoom > 7)
            3.6
        else
            12.0
    }

    private fun getScreenIDs(context: Context, zoom: Double) : List<Pair<Int, Int>>{
        val highLonID = ceil((mapView.projection.boundingBox.lonEast+180)/divisionSize(zoom)).toInt()+3
        val lowLonID = floor((mapView.projection.boundingBox.lonWest+180)/divisionSize(zoom)).toInt()-3
        val highLatID = ceil((mapView.projection.boundingBox.latNorth+90)/divisionSize(zoom)).toInt()+3
        val lowLatID = floor((mapView.projection.boundingBox.latSouth+90)/divisionSize(zoom)).toInt()-3

        // the +- 3 is to hide from the user that the overlay is only loaded when the area is exposed

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

    private fun getGeographicalCoordsFromID(id: Pair<Int, Int>, zoom: Double) : Pair<Double, Double>
    {
        // receives (lonID, latID), returns (lon, lat)
        return Pair<Double, Double>(id.second*divisionSize(zoom)-90,id.first*divisionSize(zoom)-180)
    }


    private fun generatePolygonFromID(id: Pair<Int, Int>, zoom: Double) : Polygon{
        val geoPoints = ArrayList<GeoPoint>()
        val coords = getGeographicalCoordsFromID(id, zoom)

        // add your points here
        val polygon = Polygon();
        geoPoints.add(GeoPoint(coords.first, coords.second))
        geoPoints.add(GeoPoint(coords.first, coords.second+divisionSize(zoom)))
        geoPoints.add(GeoPoint(coords.first+divisionSize(zoom), coords.second+divisionSize(zoom)))
        geoPoints.add(GeoPoint(coords.first+divisionSize(zoom), coords.second))
        geoPoints.add(geoPoints[0]);    //forces the loop to close(connect last point to first point)

        polygon.fillPaint.color = Color.parseColor("#1Ebb42fc") //set fill color
        polygon.setPoints(geoPoints);
        polygon.outlinePaint.color = Color.parseColor("#1Fbb42fc")
        polygon.outlinePaint.strokeWidth = 8.0f

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
        killPrevious()
        val zoom = mapView.zoomLevelDouble
        val exploredGeotags = getExploredIDs(context, zoom)
        val screenGeotags = getScreenIDs(context, zoom)


        for (screenTag in screenGeotags)
        {
            if (exploredGeotags.indexOf(screenTag) == -1)
            {
                val polygon = (generatePolygonFromID(screenTag, zoom))
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
        val curTime = System.currentTimeMillis()
        if((curTime - lastTime) > 1000)
        {
            lastTime = curTime
            drawGrid(context)
        }
        return false
    }
}