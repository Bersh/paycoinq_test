package example.com.archtest.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.com.archtest.R
import example.com.archtest.data.RepoInfo

class ReposAdapterNew : PagedListAdapter<RepoInfo, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<RepoInfo>() {
    override fun areItemsTheSame(oldItem: RepoInfo, newItem: RepoInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepoInfo, newItem: RepoInfo): Boolean {
        return oldItem.equals(newItem)
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        val txtName = view.findViewById<TextView>(R.id.txt_name)

        return ItemViewHolder(view, txtName)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).txtName.text = getItem(position)?.name
    }

    private class ItemViewHolder internal constructor(itemView: View, internal var txtName: TextView) : RecyclerView.ViewHolder(itemView)

    private class FooterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}