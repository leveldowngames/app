package com.example.trakkamap.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trakkamap.R
import com.example.trakkamap.databinding.FragmentProfileBinding
import com.example.trakkamap.ui.help.HelpFragment
import com.example.trakkamap.ui.settings.SettingsFragment
import com.example.trakkamap.ui.privacy.PrivacyFragment

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var helpButton: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helpButton = requireView().findViewById<ImageButton>(R.id.help_button)
        helpButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.innerFragmentContainer, HelpFragment())
                .commit()
        }

        val settingsButton = requireView().findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.innerFragmentContainer, SettingsFragment())
                .commit()
        }

        val privacyButton = requireView().findViewById<ImageButton>(R.id.privacy_button)
        privacyButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.innerFragmentContainer, PrivacyFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}