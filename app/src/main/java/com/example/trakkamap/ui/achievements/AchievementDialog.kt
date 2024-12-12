package com.example.trakkamap.ui.achievements

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.trakkamap.R
import java.io.File

class AchievementDialog(private val previousView : View, private val id: Int, private val hasObtained : ArrayList<Boolean>) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.achievement_dialog, null) // Inflate the dialog layout

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialog = builder.create()
        val closeButton = dialogView.findViewById<Button>(R.id.close_achievement_button)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        setResources(dialogView, id)

        return dialog
    }

    private fun setResources(view : View, id: Int)
    {
        if (id == 1)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "A Small Step for a Man"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring your first map tile"

            if (hasObtained[1])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_outlined_flag_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement1_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement1_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_outlined_flag_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 2)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "By foot we go"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring 0.01% of the world"

            if (hasObtained[2])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_hiking_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement2_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement2_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_hiking_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 3)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "Let's ride!"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring 0.1% of the world"

            if (hasObtained[3])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_pedal_bike_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement3_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement3_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_pedal_bike_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 4)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "I call shotgun!"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring 1% of the world"

            if (hasObtained[4])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_directions_car_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement4_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement4_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_directions_car_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 5)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "The Orient Express"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring 10% of the world"

            if (hasObtained[5])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_train_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement5_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement5_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_train_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 6)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "Great Leap for Mankind"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring 100% of the world"

            if (hasObtained[6])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_connecting_airports_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement6_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement6_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_connecting_airports_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 7)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "Real or imaginary lines?"
            view.findViewById<TextView>(R.id.achievement_description).text = "Crossing the Equator - exploring the Northern and Southern hemispheres"

            if (hasObtained[7])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_explore_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement7_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement7_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_explore_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 8)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "Spice Trade"
            view.findViewById<TextView>(R.id.achievement_description).text = "Crossing the Prime Meridian - exploring the Western and Eastern hemispheres"

            if (hasObtained[8])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_directions_boat_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement8_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement8_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_directions_boat_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }

        else if (id == 9)
        {
            view.findViewById<TextView>(R.id.achievement_name).text = "Mr. Worldwide"
            view.findViewById<TextView>(R.id.achievement_description).text = "Exploring all 4 corners of the World"

            if (hasObtained[9])
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_language_unlocked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement unlocked"
                view.findViewById<TextView>(R.id.achievement_name).setTextColor(resources.getColor(R.color.achievement9_color, requireActivity().theme))
                view.findViewById<Button>(R.id.close_achievement_button).setBackgroundColor(resources.getColor(R.color.achievement9_color, requireActivity().theme))
            }

            else
            {
                view.findViewById<ImageView>(R.id.achievement_icon).setImageResource(R.drawable.baseline_language_locked_24)
                view.findViewById<TextView>(R.id.achievement_status).text = "Achievement locked"
            }
        }
    }

    private fun hasObtainedAchievement(id: Int) : Boolean
    {
        return true
    }

    private fun setGreeting(name : String) {
        val nameFile = File(requireContext().filesDir,"name.txt")
        nameFile.writeText(name)
    }
}