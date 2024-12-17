package com.rigodev.tareasapp.Fragmentos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.rigodev.tareasapp.R

class F_AcercaDe : Fragment() {
    private var irYoutube: ImageView? = null
    private var irInstagram: ImageView? = null
    private var irFacebook: ImageView? = null
    private var irTwitter: ImageView? = null
    private var irWhatsapp: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f__acerca_de, container, false)

        irYoutube = view.findViewById(R.id.Ir_youtube)
        irInstagram = view.findViewById(R.id.Ir_instagram)
        irFacebook = view.findViewById(R.id.Ir_facebook)
        irTwitter = view.findViewById(R.id.Ir_twiter)
        irWhatsapp = view.findViewById(R.id.Ir_whatsaap)

        irYoutube?.setOnClickListener{
            val ir_p_youtube = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            startActivity(ir_p_youtube)
        }

        irInstagram?.setOnClickListener{
            val ir_p_instagram = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
            startActivity(ir_p_instagram)
        }

        irFacebook?.setOnClickListener{
            val ir_p_facebook = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
            startActivity(ir_p_facebook)
        }

        irTwitter?.setOnClickListener{
            val ir_p_twitter = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"))
            startActivity(ir_p_twitter)
        }

        irWhatsapp?.setOnClickListener{
            val ir_p_whatsapp = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com"))
            startActivity(ir_p_whatsapp)
        }
        return view
    }
}