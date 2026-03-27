package com.example.focusmode.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.focusmode.databinding.ItemHistoryBinding
import com.example.focusmode.models.StudySession
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : ListAdapter<StudySession, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: StudySession) {
            binding.tvSessionType.text = "${session.type} Session"
            binding.tvSessionDuration.text = "${session.duration} min"
            
            val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            binding.tvSessionDate.text = sdf.format(Date(session.date))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<StudySession>() {
        override fun areItemsTheSame(oldItem: StudySession, newItem: StudySession): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: StudySession, newItem: StudySession): Boolean = oldItem == newItem
    }
}
