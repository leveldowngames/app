package com.example.trakkamap.ui.explore

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.util.Log
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import java.io.File
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.floor

// ChangeMaker extends MapListener
// MapListener interface https://github.com/osmdroid/osmdroid/blob/master/osmdroid-android/src/main/java/org/osmdroid/events/MapListener.java

class ChangeMaker(private val mapView: MapView, private val context: Context) : MapListener {
    companion object // makes the functions inside static
    {
        private fun getExploredIDs(context: Context) : List<String> {
            val file = File(context.filesDir, "records.txt")
            if(!file.exists())
            {
                file.appendText("")
            }

            val recordFileString = file.readText()
            val exploredGeotags = recordFileString.split("\n")

            return exploredGeotags
        }

        private fun calculateIDWithLatLong(latitude : Double, longitude : Double) : Int {
            return (floor((longitude+180)/0.009) + 40000 * floor((latitude+90)/0.009)).toInt()
        }

        private fun getScreenIDs(mapView : MapView, context : Context) : List<Int> {
            val bounds = mapView.projection.boundingBox
            val maxlat = bounds.latNorth
            val minlat = bounds.latSouth

            val maxlong = bounds.lonEast
            val minlong = bounds.lonWest

            // matemática chata que mapeia isso nos blocos

            val n_x = ceil((maxlong - minlong)/0.009).toInt()
            val n_y = ceil((maxlat - minlat)/0.009).toInt()

            val id_init_square = calculateIDWithLatLong(minlat, minlong)
            val ids = ArrayList<Int>()

            var y_index = 0
            while(y_index < n_y)
            {
                var x_index = 0
                while (x_index < n_x)
                {
                    ids.add(id_init_square + n_x + 40000*n_y)
                    x_index++
                }
                y_index++
            }

            return ids
        }

        private fun idFromCartesianCoords(lat: Int, long: Int) : Int{
            return long + 40000 * lat
        }

        private fun cartesianCoordsFromID(ID: Int) : Pair<Int, Int> {
            return Pair<Int, Int>(floor(ID/40000.0).toInt(), ID%40000)
        }

        private fun geographicCoordsFromID(ID: Int) : Pair<Double, Double> {
            // returns latitude, longitude
            return Pair<Double, Double>(floor(ID/40000.0)*0.009-90, (ID%40000)*0.009-180)
        }

        private fun generatePolygonFromID(mapView: MapView, id: Int) : Polygon{
            // pega uma id e, dependendo do zoom, faz merge com vizinhas
            // subsequentemente, cria e retorna o poligono

            // EXEMPLO: o código real vai ser mais complicado que isso
            val geoPoints = ArrayList<GeoPoint>()
            val coords = geographicCoordsFromID(id)
            //add your points here
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

        fun killPrevious(mapView: MapView)
        {
            for(overlay in mapView.overlays)
            {
                if (overlay::class is Polygon)
                {
                    mapView.overlays.remove(overlay)
                }
            }
        }

        fun drawGrid(mapView: MapView, context: Context){
            // killPrevious(mapView)
            val exploredGeotags = getExploredIDs(context)
            val screenGeotags = getScreenIDs(mapView, context)

            for (screenTag in screenGeotags)
            {
                if (exploredGeotags.indexOf(screenTag.toString()) == -1)
                {
                    val polygon = (generatePolygonFromID(mapView, screenTag))
                    Log.i("polygon", polygon.toString())
                    // mapView.overlays.add(polygon)
                }
            }
        }
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        //Log.i("changemaker info", "onscroll called")
        //drawGrid(mapView, context)
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //Log.i("changemaker info", "onzoom called")
        //drawGrid(mapView, context)
        return true
    }
}