package com.projecte3.provesprojecte

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var spinnerFilter: Spinner
    private lateinit var mAdapter: EmpAdapter // Declare mAdapter as a lateinit property
    private lateinit var btnBack: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        spinnerFilter = findViewById(R.id.spinnerFilter)
        val options = arrayOf("Todo", "Muy común", "Común", "Poco común") // Cambia las opciones
        spinnerFilter.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                filterByEmpComun(position.toString()) // Usa la posición como valor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }

        empList = arrayListOf<EmployeeModel>()
        mAdapter = EmpAdapter(empList) // Initialize mAdapter here

        getEmployeesData()

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, WikiSetas::class.java)
            startActivity(intent)
        }
    }

    private fun filterByEmpComun(value: String) {
        val filteredList = if (value == "0") { // Si el valor seleccionado es "0", muestra todos los elementos
            ArrayList(empList)
        } else {
            ArrayList(empList.filter { it.empComun == value })
        }
        mAdapter = EmpAdapter(filteredList) // Actualiza mAdapter con la lista filtrada
        mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                //put extras
                intent.putExtra("empId", filteredList[position].empId)
                intent.putExtra("empNombre_comun", filteredList[position].empNombre_comun)
                intent.putExtra("empNombre_cientifico", filteredList[position].empNombre_cientifico)
                intent.putExtra("empComestible", filteredList[position].empComestible)
                intent.putExtra("empSombrero", filteredList[position].empSombrero)
                intent.putExtra("empPie", filteredList[position].empPie)
                intent.putExtra("empFoto", filteredList[position].empFoto)
                intent.putExtra("empHabitat", filteredList[position].empHabitat)
                intent.putExtra("empObservaciones", filteredList[position].empObservaciones)
                intent.putExtra("empComun", filteredList[position].empComun)
                startActivity(intent)
            }

        })
        empRecyclerView.adapter = mAdapter
    }

    private fun getEmployeesData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Setas")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    mAdapter = EmpAdapter(empList) // Update mAdapter with the new list
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empNombre_comun", empList[position].empNombre_comun)
                            intent.putExtra("empNombre_cientifico", empList[position].empNombre_cientifico)
                            intent.putExtra("empComestible", empList[position].empComestible)
                            intent.putExtra("empSombrero", empList[position].empSombrero)
                            intent.putExtra("empPie", empList[position].empPie)
                            intent.putExtra("empFoto", empList[position].empFoto)
                            intent.putExtra("empHabitat", empList[position].empHabitat)
                            intent.putExtra("empObservaciones", empList[position].empObservaciones)
                            intent.putExtra("empComun", empList[position].empComun)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}