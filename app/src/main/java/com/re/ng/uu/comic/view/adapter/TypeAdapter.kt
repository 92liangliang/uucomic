package com.re.ng.uu.comic.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.http.bean.TypeBean
import kotlinx.android.synthetic.main.cell_type.view.*

class TypeAdapter(
    val context: Context,
    var dataList: List<TypeBean>
) :
    RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    lateinit var onClickItem: OnClickListener
    lateinit var selected: TypeBean
    private var viewList = ArrayList<View>()

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setListener(mListener: OnClickListener) {
        this.onClickItem = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_type, parent, false)
        viewList.add(view.findViewById<View>(R.id.linear_layout))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(dataList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(data: TypeBean) {
            itemView.tv_name_cell_type.text = "${data.tag_name}"
            if (data.click) {
                selected = data
                itemView.tv_name_cell_type.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.colorWhite
                    )
                )
                itemView.linear_layout.setBackgroundResource(R.drawable.shape_red)
            } else {
                itemView.linear_layout.setBackgroundResource(0)
                itemView.tv_name_cell_type.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.colorBlack
                    )
                )
            }
            itemView.linear_layout.setOnClickListener {
                reload(data)
                if (onClickItem != null) {
                    onClickItem.onClickItem(data)
                }
            }
        }
    }

    fun reload(data: TypeBean) {
        for (n in dataList) {
            n.click = n.id.equals(data.id)
        }
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onClickItem(type: TypeBean?)
    }
}
