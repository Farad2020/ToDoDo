package com.example.tododo.details

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tododo.MainActivity
import com.example.tododo.R
import com.example.tododo.addToDo.AddToDoFragment
import com.example.tododo.databinding.ToDoDetailsFragmentBinding
import com.example.tododo.databinding.ToDoListFragmentBinding
import com.example.tododo.mainList.ToDoListFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ToDoDetailsFragment : Fragment() {
    private var _binding: ToDoDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var toDoRecordTitle: String
    private lateinit var toDoRecordDescription: String
    private var toDoRecordIsChecked: Boolean = true

    companion object {
        fun newInstance() = ToDoDetailsFragment()
        const val TITLE = "record_title"
        const val DESCRIPTION = "record_description"
        const val IS_DONE = "record_is_checked"
    }

    private lateinit var viewModel: ToDoDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            toDoRecordTitle = it.getString(TITLE).toString()
            toDoRecordDescription = it.getString(DESCRIPTION).toString()
            toDoRecordIsChecked = it.getBoolean(IS_DONE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ToDoDetailsFragmentBinding.inflate(inflater, container, false)

        val switchView = binding.toDoDoneSwitch
        switchView.setOnCheckedChangeListener{ _ , isChecked ->

            updateToDoRecord(isChecked)
//            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
//            Toast.makeText(context, message,
//                Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ToDoDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textViewTitle = binding.recordTitleView
        textViewTitle.text = toDoRecordTitle

        val textViewDesc = binding.recordDescView
        textViewDesc.text = toDoRecordDescription

        val switchView = binding.toDoDoneSwitch
        switchView.isChecked = toDoRecordIsChecked

        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateToDoRecord(isChecked: Boolean){
        var records = getSavedRecordsList()

        val recordTitle = binding.recordTitleView.text.toString()
        val recordDescription = binding.recordDescView.text.toString()
        val recordIsChecked = binding.toDoDoneSwitch.isChecked

        var updatedRecord = records.find {
            it.title == recordTitle &&
            it.description == recordDescription
        }

        updatedRecord?.isDone = recordIsChecked

        val sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sPref.edit()

        val gson = Gson()
        val json = gson.toJson(records)

        ed.putString(AddToDoFragment.SAVED_TODO_RECORDS, json)
        ed.apply()
    }

    fun getSavedRecordsList():MutableList<ToDoListFragment.ToDoRecord>{
        val sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sPref.getString(AddToDoFragment.SAVED_TODO_RECORDS, "")

        val type = object : TypeToken<List<ToDoListFragment.ToDoRecord>>() {}.type
        val records: MutableList<ToDoListFragment.ToDoRecord> = gson.fromJson(json, type)
        return records
    }
}