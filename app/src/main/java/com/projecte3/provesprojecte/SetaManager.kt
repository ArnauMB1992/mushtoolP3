package com.projecte3.provesprojecte

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue

object SetaManager {

    var setas = mutableListOf<Seta>()
    private val database: FirebaseDatabase = Firebase.database
    private val myRef = database.getReference("post")

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