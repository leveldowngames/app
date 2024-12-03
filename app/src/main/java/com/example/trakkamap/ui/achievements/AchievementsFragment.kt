package com.example.trakkamap.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trakkamap.databinding.FragmentAchievementsBinding

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
        val achievementsViewModel =
            ViewModelProvider(this).get(AchievementsViewModel::class.java)

        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAchievements
        achievementsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}