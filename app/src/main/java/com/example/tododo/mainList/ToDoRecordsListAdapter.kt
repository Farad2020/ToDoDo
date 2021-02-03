package com.example.tododo.mainList

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class ToDoRecordsListAdapter(private val list: List<ToDoListFragment.ToDoRecord>)
    : RecyclerView.Adapter<ToDoListFragment.ToDoRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListFragment.ToDoRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ToDoListFragment.ToDoRecordViewHolder(
                inflater,
                parent
        )
    }

    override fun onBindViewHolder(holder: ToDoListFragment.ToDoRecordViewHolder, position: Int) {
        val toDoRecord: ToDoListFragment.ToDoRecord = list[position]
        holder.bind(toDoRecord)

        holder.itemView.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoDetailsFragment(
                toDoRecord.title,
                toDoRecord.description,
                toDoRecord.isDone
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = list.size


}