package br.iesb.mobile.netflics.ui.fragment.main.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.iesb.mobile.netflics.databinding.FragmentSelectProfileBinding
import br.iesb.mobile.netflics.domain.AppResult
import br.iesb.mobile.netflics.ui.component.AnimatedProfile
import br.iesb.mobile.netflics.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectProfileFragment : Fragment() {

    private lateinit var binding: FragmentSelectProfileBinding
    private val viewmodel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSelectProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewmodel = viewmodel

        binding.animatedProfile2.setOnEditClickListener { edit(it) }

        loadProfiles()

        return binding.root
    }

    private fun loadProfiles() {
        viewmodel.result.observe(viewLifecycleOwner) {
            if (it is AppResult.Error) {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }
        viewmodel.loadProfiles()
    }

    private fun showProfileDialog(index: Int, control: AnimatedProfile) {
        val alert = AlertDialog.Builder(context)
        val edittext = EditText(context)

        alert.setMessage("Informe o nome do novo Perfil:")
        alert.setTitle("Perfil")
        alert.setView(edittext)

        alert.setPositiveButton("Continuar") { dialog, _ ->
            val name = edittext.text.toString()
            viewmodel.currentProfile?.value?.id = "Profile$index"
            viewmodel.currentProfile?.value?.name = name
            viewmodel.createOrUpdateProfile()
            dialog.dismiss()
        }

        alert.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    @Suppress("UNUSED_PARAMETER")
    fun createOrSelectProfile(v: View) {
        val tag = (v.tag as String).toInt()
        viewmodel.selectProfile(tag)
        viewmodel.currentProfile?.value?.let {
            if (it.id == "new") {
                showProfileDialog(tag, v as AnimatedProfile)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun animation(v: View) {
        (v as AnimatedProfile).profileAnimatedCounter = !v.profileAnimatedCounter
    }

    private fun edit(name: String) {
        println("EDIT: $name")
    }

}