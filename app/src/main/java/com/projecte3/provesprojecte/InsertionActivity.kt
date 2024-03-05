package com.projecte3.provesprojecte


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etEmpNombre_comun: EditText
    private lateinit var etEmpNombre_cientifico: EditText
    private lateinit var etEmpComestible: EditText
    private lateinit var etEmpSombrero: EditText
    private lateinit var etEmpPie: EditText
    private lateinit var etEmpFoto: EditText
    private lateinit var etEmpHabitat: EditText
    private lateinit var etEmpObservaciones: EditText
    private lateinit var etEmpComun: EditText
    private lateinit var btnSaveData: Button


    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpNombre_comun = findViewById(R.id.etEmpNombre_comun)
        etEmpNombre_cientifico = findViewById(R.id.etEmpNombre_cientifico)
        etEmpComestible = findViewById(R.id.etEmpComestible)
        etEmpSombrero = findViewById(R.id.etEmpSombrero)
        etEmpPie = findViewById(R.id.etEmpPie)
        etEmpFoto = findViewById(R.id.etEmpFoto)
        etEmpHabitat = findViewById(R.id.etEmpHabitat)
        etEmpObservaciones = findViewById(R.id.etEmpObservaciones)
        etEmpComun = findViewById(R.id.etEmpComun)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Setas")

        btnSaveData.setOnClickListener {
            saveSetasData()
        }
    }

    private fun saveSetasData() {

        //getting values
        val empNombre_comun = etEmpNombre_comun.text.toString()
        val empNombre_cientifico = etEmpNombre_cientifico.text.toString()
        val empComestible = etEmpComestible.text.toString()
        val empSombrero = etEmpSombrero.text.toString()
        val empPie = etEmpPie.text.toString()
        val empFoto = etEmpFoto.text.toString()
        val empHabitat = etEmpHabitat.text.toString()
        val empoObservaciones = etEmpObservaciones.text.toString()
        val empComun = etEmpComun.text.toString()

        if (empNombre_comun.isEmpty()) {
            etEmpNombre_comun.error = "Please enter nombre_comun"
        }
        if (empNombre_cientifico.isEmpty()) {
            etEmpNombre_cientifico.error = "Please enter nombre_cientifico"
        }
        if (empComestible.isEmpty()) {
            etEmpComestible.error = "Please enter comestible"
        }
        if (empSombrero.isEmpty()) {
            etEmpSombrero.error = "Please enter sombrero"
        }
        if (empPie.isEmpty()) {
            etEmpPie.error = "Please enter pie"
        }
        if (empFoto.isEmpty()) {
            etEmpFoto.error = "Please enter foto"
        }
        if (empHabitat.isEmpty()) {
            etEmpHabitat.error = "Please enter habitat"
        }
        if (empoObservaciones.isEmpty()) {
            etEmpObservaciones.error = "Please enter observaciones"
        }
        if (empComun.isEmpty()) {
            etEmpComun.error = "Please enter comun"
        }

        val empId = dbRef.push().key!!

        val Seta = EmployeeModel(empId, empNombre_comun, empNombre_cientifico, empComestible, empSombrero, empPie, empFoto, empHabitat, empoObservaciones, empComun)

        dbRef.child(empId).setValue(Seta)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etEmpNombre_comun.text.clear()
                etEmpNombre_cientifico.text.clear()
                etEmpComestible.text.clear()
                etEmpSombrero.text.clear()
                etEmpPie.text.clear()
                etEmpFoto.text.clear()
                etEmpHabitat.text.clear()
                etEmpObservaciones.text.clear()
                etEmpComun.text.clear()

                // Redirigir a WikiSetas después de guardar los datos
                val intent = Intent(this, WikiSetas::class.java)
                startActivity(intent)

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}