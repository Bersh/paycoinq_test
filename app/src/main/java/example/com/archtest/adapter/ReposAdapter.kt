package example.com.archtest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.com.archtest.R
import example.com.archtest.data.RepoInfo
import java.util.ArrayList

class ReposAdapter(d: MutableList<RepoInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //item types
    private val ITEM = 1
    private val FOOTER = 2

    private var data: MutableList<RepoInfo> = d
    private var isFooterAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM -> createItemViewHolder(parent)
            FOOTER -> createFooterViewHolder(parent)
            else -> createItemViewHolder(parent)
        }
    }

    private fun createItemViewHolder(parent: ViewGroup): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        val txtName = view.findViewById<TextView>(R.id.txt_name)

        return ItemViewHolder(view, txtName)
    }

    private fun createFooterViewHolder(parent: ViewGroup): FooterViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_progress, parent, false)

        return FooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> (holder as ItemViewHolder).txtName.text = data[position].name
            FOOTER -> {
            }
            else -> {
            }
        }
    }

    fun addProgressFooter() {
        if (isFooterAdded) {
            return
        }

        isFooterAdded = true
        add(RepoInfo())
    }

    fun removeProgressFooter() {
        if (!isFooterAdded) {
            return
        }
        isFooterAdded = false

        val position = data.size - 1
        val item = getItem(position)

        if (item != null) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): RepoInfo? {
        return data[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position) && isFooterAdded) FOOTER else ITEM
    }

    private fun isLastPosition(position: Int): Boolean {
        return position == data.size - 1
    }

    private fun add(item: RepoInfo) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun setData(items: List<RepoInfo>) {
        data = ArrayList(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private class ItemViewHolder internal constructor(itemView: View, internal var txtName: TextView) : RecyclerView.ViewHolder(itemView)

    private class FooterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}