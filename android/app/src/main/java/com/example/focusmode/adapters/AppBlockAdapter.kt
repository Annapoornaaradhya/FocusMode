package com.example.focusmode.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.focusmode.databinding.ItemAppBlockBinding
import com.example.focusmode.models.AppBlockInfo

class AppBlockAdapter(private val onBlockChanged: (AppBlockInfo, Boolean) -> Unit) : 
    ListAdapter<AppBlockInfo, AppBlockAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAppBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAppBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppBlockInfo) {
            binding.tvAppName.text = app.name
            binding.switchBlock.isChecked = app.isBlocked
            
            binding.switchBlock.setOnCheckedChangeListener { _, isChecked ->
                onBlockChanged(app, isChecked)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AppBlockInfo>() {
        override fun areItemsTheSame(oldItem: AppBlockInfo, newItem: AppBlockInfo): Boolean = oldItem.packageName == newItem.packageName
        override fun areContentsTheSame(oldItem: AppBlockInfo, newItem: AppBlockInfo): Boolean = oldItem == newItem
    }
}
