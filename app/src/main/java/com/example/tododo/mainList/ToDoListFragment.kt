package com.example.tododo.mainList

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tododo.R
import com.example.tododo.addToDo.AddToDoFragment
import com.example.tododo.databinding.ToDoListFragmentBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ToDoListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var _binding: ToDoListFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ToDoListFragment()
    }

    private lateinit var viewModel: ToDoListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        recyclerView = container!!.findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.adapter = LetterAdapter()
        _binding = ToDoListFragmentBinding.inflate(inflater, container, false)

        val addToDoRecordView: View = binding.addToDoRecord
        addToDoRecordView.setOnClickListener {
            addToDoRecordOnClick()
        }


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.todoListRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =
            ToDoRecordsListAdapter(
                getToDoRecords()
            )

    }

    private fun addToDoRecordOnClick(){
        val action = ToDoListFragmentDirections.actionToDoListFragmentToAddToDoFragment2()
        findNavController().navigate(action)
    }


    private fun getToDoRecords(): List<ToDoRecord>{
        val sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sPref.getString(AddToDoFragment.SAVED_TODO_RECORDS, "")

        val type = object : TypeToken<List<ToDoRecord>>() {}.type
        val records: List<ToDoRecord> = gson.fromJson(json, type)
        Toast.makeText(context, "Records loaded", Toast.LENGTH_SHORT).show()

        return records
    }

//    private val userToDoRecords = mutableListOf(
//        ToDoRecord(
//            "Eat",
//            "Keeping healthy diet is good for health"
//        ),
//        ToDoRecord(
//            "Sleep",
//            "Good Sleep - is Good Looks"
//        ),
//        ToDoRecord(
//            "Work",
//            "Keep yourself busy tho"
//        ),
//        ToDoRecord(
//            "Think",
//            "Without thought there would be nothing"
//        ),ToDoRecord(
//            "Eat",
//            "Keeping healthy diet is good for health"
//        ),
//        ToDoRecord(
//            "Sleep",
//            "Good Sleep - is Good Looks"
//        ),
//        ToDoRecord(
//            "Work",
//            "Keep yourself busy tho"
//        ),
//        ToDoRecord(
//            "Think",
//            "Without thought there would be nothing"
//        )
//    )

    class ToDoRecordViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_view, parent, false)) {
        private var recordTitleView: TextView? = null
        private var recordDescriptionView: TextView? = null
        private var recordIsDoneView: TextView? = null


        init {
            recordTitleView = itemView.findViewById(R.id.list_title)
            recordDescriptionView = itemView.findViewById(R.id.list_description)
            recordIsDoneView = itemView.findViewById(R.id.record_is_done)
        }

        fun bind(toDoRecord: ToDoRecord) {
            recordTitleView?.text = toDoRecord.title
            recordDescriptionView?.text = toDoRecord.description
            recordIsDoneView?.text =  if (toDoRecord.isDone) "It is done" else "Not Done"
        }

    }


    data class ToDoRecord(val title: String, val description: String, var isDone: Boolean = false)



    fun getSavedRecordsList():MutableList<ToDoRecord>{
        val sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sPref.getString(AddToDoFragment.SAVED_TODO_RECORDS, "")

        val type = object : TypeToken<List<ToDoRecord>>() {}.type
        val records: MutableList<ToDoRecord> = gson.fromJson(json, type)
        return records
    }
}