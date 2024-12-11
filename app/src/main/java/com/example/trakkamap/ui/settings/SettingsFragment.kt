package com.example.trakkamap.ui.settings

import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.trakkamap.R
import com.example.trakkamap.databinding.SettingsSectionBinding
import com.example.trakkamap.ui.help.HelpFragment

class SettingsFragment : Fragment() {

    private var _binding: SettingsSectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SettingsSectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = requireView().findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .remove(this)
                .commit()
        }

        val deleteButton = requireView().findViewById<Button>(R.id.delete_button)
        deleteButton.setOnClickListener {
            ConfirmationDialog().show(childFragmentManager, "DeleteDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}