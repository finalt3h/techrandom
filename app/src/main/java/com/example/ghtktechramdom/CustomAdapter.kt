package com.example.ghtktechramdom

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(var mList: MutableList<ItemsViewModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.bindData(itemsViewModel,position)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(
        ItemView: View
    ) : RecyclerView.ViewHolder(ItemView) {
        val tv1: EditText = itemView.findViewById(R.id.tv1)
        val tv2: EditText = itemView.findViewById(R.id.tv2)
        val tvIndex: TextView = itemView.findViewById(R.id.tvIndex)
        val btnClear1: ImageView = itemView.findViewById(R.id.btnClean1)
        val btnClear2: ImageView = itemView.findViewById(R.id.btnClean2)
        val edtV1Listener = EditTextListener()
        val edtV2Listener = EditTextListener()

        init {
            tv1.addTextChangedListener(edtV1Listener)
            tv2.addTextChangedListener(edtV2Listener)
        }
        fun bindData(customer: ItemsViewModel, position: Int) {
            edtV1Listener.updateData { text ->
                customer.v1 = text
                if(tv1.isFocused && !text.isNullOrEmpty()) {
                    btnClear1.visibility = View.VISIBLE
                } else {
                    btnClear1.visibility = View.GONE
                }
            }
            edtV2Listener.updateData { text ->
                customer.v2 = text
                if(tv2.isFocused && !text.isNullOrEmpty()) {
                    btnClear2.visibility = View.VISIBLE
                } else {
                    btnClear2.visibility = View.GONE
                }
            }
            tvIndex.text = (position + 1).toString()+"."
            tv1.setText(customer.v1)
            tv2.setText(customer.v2)
            btnClear1.setOnClickListener {  tv1.setText("")}
            btnClear2.setOnClickListener {  tv2.setText("")}
            tv1.setOnFocusChangeListener { view, b ->
                if(b && !customer.v1.isNullOrEmpty()) {
                    btnClear1.visibility = View.VISIBLE
                } else {
                    btnClear1.visibility = View.GONE
                }
            }
            tv2.setOnFocusChangeListener { view, b ->
                if(b && !customer.v2.isNullOrEmpty()) {
                    btnClear2.visibility = View.VISIBLE
                } else {
                    btnClear2.visibility = View.GONE
                }
            }
        }
    }


}