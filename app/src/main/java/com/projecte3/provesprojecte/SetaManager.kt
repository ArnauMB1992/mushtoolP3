package com.projecte3.provesprojecte

import android.content.Context
import android.media.MediaPlayer
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue

object SetaManager {

    private val database: FirebaseDatabase = Firebase.database
    private val myRef = database.getReference("post")

    var setas = mutableListOf<Seta>()

    fun addSeta(seta: Seta, context: Context) {
        val setaId = myRef.push().key
        myRef.child(setaId.toString()).setValue(seta).addOnCompleteListener {
            if (it.isSuccessful) {
                seta.id = setaId
                setas.add(seta)

                // Reproduce el sonido de guardado
                val mediaPlayer = MediaPlayer.create(context, R.raw.save)
                mediaPlayer.start()
            }
        }
    }

    fun removeSeta(seta: Seta) {
        if (seta.id != null) {
            myRef.child(seta.id!!).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    setas.remove(seta)
                }
            }
        }
    }

    fun loadSetas() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setas.clear()
                for (setaSnapshot in dataSnapshot.children) {
                    val seta = setaSnapshot.getValue<Seta>()
                    if (seta != null) {
                        seta.id = setaSnapshot.key
                        setas.add(seta)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}