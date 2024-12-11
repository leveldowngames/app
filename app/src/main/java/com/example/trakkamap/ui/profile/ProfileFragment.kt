package com.example.trakkamap.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trakkamap.R
import com.example.trakkamap.databinding.FragmentProfileBinding
import com.example.trakkamap.ui.help.HelpFragment
import com.example.trakkamap.ui.settings.SettingsFragment
import com.example.trakkamap.ui.privacy.PrivacyFragment
import com.example.trakkamap.ui.settings.ConfirmationDialog
import org.w3c.dom.Text
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val nameText = root.findViewById<TextView>(R.id.name_text)
        nameText.text = createGreeting()

        return root
    }

    private fun createGreeting(): String {
        val nameFile = File(requireContext().filesDir,"name.txt")
        return if (!nameFile.exists())
            "Hi, User"
        else
            "Hi, " + nameFile.readText()
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
                .addToBackStack(null)
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
                .addToBackStack(null)
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
                .addToBackStack(null)
                .commit()
        }

        val nameButton = requireView().findViewById<ImageButton>(R.id.edit_name_button)
        nameButton.setOnClickListener {
            NameDialog(requireView()).show(childFragmentManager, "NameDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}