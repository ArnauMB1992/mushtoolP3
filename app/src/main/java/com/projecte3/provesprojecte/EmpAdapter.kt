package com.projecte3.provesprojecte

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpAdapter(private val empList: ArrayList<EmployeeModel>) :
    RecyclerView.Adapter<EmpAdapter.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.tvEmpNombre_comun.text = currentEmp.empNombre_comun
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View, private val clickListener: onItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        val tvEmpNombre_comun : TextView = itemView.findViewById(R.id.tvEmpNombre_comun)

        init {
            itemView.setOnClickListener {
                clickListener?.onItemClick(adapterPosition)
            }
        }

    }

}