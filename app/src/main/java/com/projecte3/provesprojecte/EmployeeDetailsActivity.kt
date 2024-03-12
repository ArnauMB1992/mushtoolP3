package com.projecte3.provesprojecte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.bumptech.glide.Glide


class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpNombre_comun: TextView
    private lateinit var tvEmpNombre_cientifico: TextView
    private lateinit var tvEmpComestible: TextView
    private lateinit var tvEmpSombrero: TextView
    private lateinit var tvEmpPie: TextView
    private lateinit var tvEmpFoto: ImageView
    private lateinit var tvEmpHabitat: TextView
    private lateinit var tvEmpObservaciones: TextView
    private lateinit var tvEmpComun: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("EmpNombre_comun").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }

    }

    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpNombre_comun = findViewById(R.id.tvEmpNombre_comun)
        tvEmpNombre_cientifico = findViewById(R.id.tvEmpNombre_cientifico)
        tvEmpComestible = findViewById(R.id.tvEmpComestible)
        tvEmpSombrero = findViewById(R.id.tvEmpSombrero)
        tvEmpPie = findViewById(R.id.tvEmpPie)
        tvEmpFoto = findViewById(R.id.tvEmpFoto)
        tvEmpHabitat = findViewById(R.id.tvEmpHabitat)
        tvEmpObservaciones = findViewById(R.id.tvEmpObservaciones)
        tvEmpComun = findViewById(R.id.tvEmpComun)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("empId")
        tvEmpNombre_comun.text = intent.getStringExtra("empNombre_comun")
        tvEmpNombre_cientifico.text = intent.getStringExtra("empNombre_cientifico")
        tvEmpComestible.text = intent.getStringExtra("empComestible")
        tvEmpSombrero.text = intent.getStringExtra("empSombrero")
        tvEmpPie.text = intent.getStringExtra("empPie")
        //tvEmpFoto.text = intent.getStringExtra("empFoto")
        tvEmpHabitat.text = intent.getStringExtra("empHabitat")
        tvEmpObservaciones.text = intent.getStringExtra("empObservaciones")

        val comunValue = intent.getStringExtra("empComun").toString().toIntOrNull()
        when (comunValue) {
            1 -> tvEmpComun.text = "Muy Común"
            2 -> tvEmpComun.text = "Común"
            3 -> tvEmpComun.text = "Poco Común"
            else -> tvEmpComun.text = "" // puedes manejar otros casos aquí si es necesario
        }

        Glide.with(this)
            .load(intent.getStringExtra("empFoto"))
            .into(tvEmpFoto)
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Setas").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Seta Borrada", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

   private fun openUpdateDialog(
        empId: String,
        empNombre_comun: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmpNombre_comun = mDialogView.findViewById<EditText>(R.id.etEmpNombre_comun)
        val etEmpNombre_cientifico = mDialogView.findViewById<EditText>(R.id.etEmpNombre_cientifico)
        val etEmpComestible = mDialogView.findViewById<EditText>(R.id.etEmpComestible)
        val etEmpSombrero = mDialogView.findViewById<EditText>(R.id.etEmpSombrero)
        val etEmpPie = mDialogView.findViewById<EditText>(R.id.etEmpPie)
        val etEmpFoto = mDialogView.findViewById<EditText>(R.id.etEmpFoto)
        val etEmpHabitat = mDialogView.findViewById<EditText>(R.id.etEmpHabitat)
        val etEmpObservaciones = mDialogView.findViewById<EditText>(R.id.etEmpObservaciones)
        val etEmpComun = mDialogView.findViewById<EditText>(R.id.etEmpComun)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

       etEmpNombre_comun.setText(intent.getStringExtra("empNombre_comun").toString())
       etEmpNombre_cientifico.setText(intent.getStringExtra("empNombre_cientifico").toString())
       etEmpComestible.setText(intent.getStringExtra("empComestible").toString())
       etEmpSombrero.setText(intent.getStringExtra("empSombrero").toString())
       etEmpPie.setText(intent.getStringExtra("empPie").toString())
       etEmpFoto.setText(intent.getStringExtra("empFoto").toString())
       etEmpHabitat.setText(intent.getStringExtra("empHabitat").toString())
       etEmpObservaciones.setText(intent.getStringExtra("empObservaciones").toString())
       etEmpComun.setText(intent.getStringExtra("empComun").toString())

        mDialog.setTitle("Updating $empNombre_comun Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpNombre_comun.text.toString(),
                etEmpNombre_cientifico.text.toString(),
                etEmpComestible.text.toString(),
                etEmpSombrero.text.toString(),
                etEmpPie.text.toString(),
                etEmpFoto.text.toString(),
                etEmpHabitat.text.toString(),
                etEmpObservaciones.text.toString(),
                etEmpComun.text.toString()
            )

            Toast.makeText(applicationContext, "Seta Actualizada", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvEmpNombre_comun.text = etEmpNombre_comun.text.toString()
            tvEmpNombre_cientifico.text = etEmpNombre_cientifico.text.toString()
            tvEmpComestible.text = etEmpComestible.text.toString()
            tvEmpSombrero.text = etEmpSombrero.text.toString()
            tvEmpPie.text = etEmpPie.text.toString()
            //tvEmpFoto.text = etEmpFoto.text.toString()
            tvEmpHabitat.text = etEmpHabitat.text.toString()
            tvEmpObservaciones.text = etEmpObservaciones.text.toString()
            tvEmpComun.text = etEmpComun.text.toString()
            Glide.with(this)
                .load(etEmpFoto.text.toString())
                .into(tvEmpFoto)


            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        nombre_comun: String,
        nombre_cientifico: String,
        comestible: String,
        sombrero: String,
        pie: String,
        foto: String,
        habitat: String,
        observaciones: String,
        comun: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Setas").child(id)
        val setaInfo = EmployeeModel(id, nombre_comun, nombre_cientifico, comestible, sombrero, pie, foto, habitat, observaciones, comun)
        dbRef.setValue(setaInfo)
    }

}