package com.example.tododo.addToDo


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tododo.databinding.AddToDoFragmentBinding
import com.example.tododo.mainList.ToDoListFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddToDoFragment : Fragment() {
    private var _binding: AddToDoFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var toDoRecordTitleEdit: EditText
    private lateinit var sPref: SharedPreferences

    companion object {
        const val SAVED_TEXT = "saved_text"
        const val SAVED_TODO_RECORDS = "saved_to_do_records"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    fun getSavedRecordsList():MutableList<ToDoListFragment.ToDoRecord>{
        sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sPref.getString(AddToDoFragment.SAVED_TODO_RECORDS, "")

        val type = object : TypeToken<List<ToDoListFragment.ToDoRecord>>() {}.type
        val records: MutableList<ToDoListFragment.ToDoRecord> = gson.fromJson(json, type)
        return records
    }

    private fun saveClickListener(it: View){
        var records = getSavedRecordsList()

        val newRecordTitle = binding.toDoRecordTitle
        val newRecordDescription = binding.toDoRecordDescription

        val newRecord = ToDoListFragment.ToDoRecord(
            newRecordTitle.text.toString(),
            newRecordDescription.text.toString()
        )

        records.add(newRecord)

        val ed: Editor = sPref.edit()

        val gson = Gson()
        val json = gson.toJson(records)

        ed.putString(SAVED_TODO_RECORDS, json)
        ed.apply()

//        // Note to self, when needing activity, use requireActivity to proveid it without null
//        sPref = requireActivity().getPreferences(MODE_PRIVATE)
//        val ed: Editor = sPref.edit()
//
//        val gson = Gson()
//        val json = gson.toJson(userToDoRecords)
//        ed.putString(SAVED_TODO_RECORDS, json)
//        ed.apply()
        Toast.makeText(context, "Text saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadClickListener(it: View){
        sPref = requireActivity().getPreferences(MODE_PRIVATE)
        val gson = Gson()
        val json = sPref.getString(SAVED_TODO_RECORDS, "")

        val type = object : TypeToken<List<ToDoListFragment.ToDoRecord>>() {}.type
        val records: List<ToDoListFragment.ToDoRecord> = gson.fromJson(json, type)

        toDoRecordTitleEdit.setText(modText(records))
        Toast.makeText(context, "Text loaded", Toast.LENGTH_SHORT).show()
    }

    private fun modText(list: List<ToDoListFragment.ToDoRecord>): String{
        var str = ""
        list.forEach{
            str += " " + it.title
        }

        return str
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddToDoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toDoRecordTitleEdit = binding.toDoRecordTitle

        val btnSave = binding.btnSave
        btnSave.setOnClickListener{
            saveClickListener(it)
        }

        val btnLoad =binding.btnLoad
        btnLoad.setOnClickListener {
            loadClickListener(it)
        }

        super.onViewCreated(view, savedInstanceState)
    }

//    data class ToDoRecord(val title: String, val description: String, val isDone: Boolean = false)
//
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
}