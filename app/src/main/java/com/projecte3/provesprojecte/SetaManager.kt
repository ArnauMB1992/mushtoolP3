package com.projecte3.provesprojecte

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    fun updateSeta(seta: Seta): Task<Seta> {
        val database = Firebase.database
        val myRef = database.getReference("post")

        val currentSetaRef = myRef.child(seta.id!!)
        val taskCompletionSource = TaskCompletionSource<Seta>()
        currentSetaRef.get().addOnSuccessListener { dataSnapshot ->
            val currentSeta = dataSnapshot.getValue(Seta::class.java)

            if (seta.nombre == null) seta.nombre = currentSeta?.nombre
            if (seta.descripcion == null) seta.descripcion = currentSeta?.descripcion
            if (seta.latitud == null) seta.latitud = currentSeta?.latitud
            if (seta.longitud == null) seta.longitud = currentSeta?.longitud
            if (seta.locationSerialized == null) seta.locationSerialized = currentSeta?.locationSerialized
            if (seta.dateTime == null) seta.dateTime = currentSeta?.dateTime
            if (seta.encodedImage == null || seta.encodedImage == "") seta.encodedImage = currentSeta?.encodedImage

            currentSetaRef.setValue(seta).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    taskCompletionSource.setResult(seta)
                } else {
                    taskCompletionSource.setException(task.exception!!)
                }
            }
        }

        return taskCompletionSource.task
    }

    suspend fun loadSetas(): List<Seta> = suspendCancellableCoroutine { continuation ->
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
                if (continuation.isActive) {
                    continuation.resume(setas)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                if (continuation.isActive) {
                    continuation.resumeWithException(databaseError.toException())
                }
            }
        })
    }
}