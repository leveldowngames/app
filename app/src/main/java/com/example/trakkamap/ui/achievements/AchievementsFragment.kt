package com.example.trakkamap.ui.achievements

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.trakkamap.R
import com.example.trakkamap.databinding.FragmentAchievementsBinding
import java.io.File
import java.util.Locale

class AchievementsFragment : Fragment() {

    private var _binding: FragmentAchievementsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var hasObtained : ArrayList<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // set the percentage explored
        val percentage = calculateExploredPercentage()
        val textView = root.findViewById<TextView>(R.id.percentage)
        textView.text = String.format(Locale.US, "%.2f%%", percentage)

        hasObtained = hasObtainedAchievements(percentage)

        if (hasObtained[1]){
            root.findViewById<ImageButton>(R.id.achievement1).setBackgroundResource(R.drawable.round_corner_achievement1_unlocked)
        }
        if (hasObtained[2]){
            root.findViewById<ImageButton>(R.id.achievement2).setBackgroundResource(R.drawable.round_corner_achievement2_unlocked)
        }
        if (hasObtained[3]){
            root.findViewById<ImageButton>(R.id.achievement3).setBackgroundResource(R.drawable.round_corner_achievement3_unlocked)
        }
        if (hasObtained[4]){
            root.findViewById<ImageButton>(R.id.achievement4).setBackgroundResource(R.drawable.round_corner_achievement4_unlocked)
        }
        if (hasObtained[5]){
            root.findViewById<ImageButton>(R.id.achievement5).setBackgroundResource(R.drawable.round_corner_achievement5_unlocked)
        }
        if (hasObtained[6]){
            root.findViewById<ImageButton>(R.id.achievement6).setBackgroundResource(R.drawable.round_corner_achievement6_unlocked)
        }
        if (hasObtained[7]){
            root.findViewById<ImageButton>(R.id.achievement7).setBackgroundResource(R.drawable.round_corner_achievement7_unlocked)
        }
        if (hasObtained[8]){
            root.findViewById<ImageButton>(R.id.achievement8).setBackgroundResource(R.drawable.round_corner_achievement8_unlocked)
        }
        if (hasObtained[9]){
            root.findViewById<ImageButton>(R.id.achievement9).setBackgroundResource(R.drawable.round_corner_achievement9_unlocked)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button1 = requireView().findViewById<ImageButton>(R.id.achievement1)
        button1.setOnClickListener {
            AchievementDialog(requireView(), 1, hasObtained).show(childFragmentManager, "Achievement1Dialog")
        }

        val button2 = requireView().findViewById<ImageButton>(R.id.achievement2)
        button2.setOnClickListener {
            AchievementDialog(requireView(), 2, hasObtained).show(childFragmentManager, "Achievement2Dialog")
        }

        val button3 = requireView().findViewById<ImageButton>(R.id.achievement3)
        button3.setOnClickListener {
            AchievementDialog(requireView(), 3, hasObtained).show(childFragmentManager, "Achievement3Dialog")
        }

        val button4 = requireView().findViewById<ImageButton>(R.id.achievement4)
        button4.setOnClickListener {
            AchievementDialog(requireView(), 4, hasObtained).show(childFragmentManager, "Achievement4Dialog")
        }

        val button5 = requireView().findViewById<ImageButton>(R.id.achievement5)
        button5.setOnClickListener {
            AchievementDialog(requireView(), 5, hasObtained).show(childFragmentManager, "Achievement5Dialog")
        }

        val button6 = requireView().findViewById<ImageButton>(R.id.achievement6)
        button6.setOnClickListener {
            AchievementDialog(requireView(), 6, hasObtained).show(childFragmentManager, "Achievement6Dialog")
        }

        val button7 = requireView().findViewById<ImageButton>(R.id.achievement7)
        button7.setOnClickListener {
            AchievementDialog(requireView(), 7, hasObtained).show(childFragmentManager, "Achievement7Dialog")
        }

        val button8 = requireView().findViewById<ImageButton>(R.id.achievement8)
        button8.setOnClickListener {
            AchievementDialog(requireView(), 8, hasObtained).show(childFragmentManager, "Achievement8Dialog")
        }

        val button9 = requireView().findViewById<ImageButton>(R.id.achievement9)
        button9.setOnClickListener {
            AchievementDialog(requireView(), 9, hasObtained).show(childFragmentManager, "Achievement9Dialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateExploredPercentage() : Double
    {
        val file = File(requireContext().filesDir, "recordsC.txt")
        if(!file.exists())
        {
            return 0.0
        }

        return 100*file.readText().split("\n").count()/500000.0
    }


    private fun hasObtainedAchievements(percentage: Double) : ArrayList<Boolean>
    {
        val achievementStatus = ArrayList<Boolean>()
        // first element is ignored so we initialize it with anything
        achievementStatus.add(false)

        if (percentage > 0.0)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)

        if (percentage > 0.01)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)

        if (percentage > 0.1)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)

        if (percentage > 1.0)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)

        if (percentage > 10.0)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)

        if (percentage > 99.9)
            achievementStatus.add(true)
        else
            achievementStatus.add(false)


        achievementStatus.add(checkNorthSouth())
        achievementStatus.add(checkEastWest())
        achievementStatus.add(checkAllQuadrants())

        return achievementStatus
    }

    private fun checkNorthSouth() : Boolean
    {
        val file = File(requireContext().filesDir, "recordsC.txt")
        if(!file.exists())
        {
            return false
        }

        var north = false
        var south = false

        for(line in file.readText().split("\n"))
        {
            try {
                val tuple = line.replace(")", "").replace("(", "").replace(" ", "").split(",")
                if(tuple[1].toInt() >= 250) // if its north
                    north = true
                else // if its south
                    south = true

                if (north && south)
                    return true
            }

            catch (e : Exception) {
                continue
            }
        }

        return false
    }

    private fun checkEastWest() : Boolean
    {
        val file = File(requireContext().filesDir, "recordsC.txt")
        if(!file.exists())
        {
            return false
        }

        var east = false
        var west = false

        for(line in file.readText().split("\n"))
        {
            try {
                val tuple = line.replace(")", "").replace("(", "").replace(" ", "").split(",")
                if (tuple[0].toInt() >= 500) // if its east
                    east = true
                else // if its west
                    west = true

                if (east && west)
                    return true
            }

            catch (e : Exception) {
                continue
            }
        }

        return false
    }

    private fun checkAllQuadrants() : Boolean
    {
        val file = File(requireContext().filesDir, "recordsC.txt")
        if(!file.exists())
        {
            return false
        }

        var northeast = false
        var northwest = false
        var southeast = false
        var southwest = false

        for(line in file.readText().split("\n"))
        {
            try {
                val tuple = line.replace(")", "").replace("(", "").replace(" ", "").split(",")
                if(tuple[1].toInt() >= 250)  // if its north
                {
                    if(tuple[0].toInt() >= 500) // if its east
                        northeast = true

                    else
                        northwest = true
                }
                else // if its south
                {
                    if(tuple[0].toInt() >= 500) // if its east
                        southeast = true

                    else
                        southwest = true
                }


                if (northeast && northwest && southeast && southwest)
                    return true
            }

            catch (e : Exception) {
                continue
            }

        }

        return false
    }
}