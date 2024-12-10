package com.example.trakkamap.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access the TextView from the layout
        val textView = root.findViewById<TextView>(R.id.percentage)

        // Change the text content of the TextView
        textView.text = String.format(Locale.US, "%.2f%%", calculateExploredPercentage())

        return root
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
}