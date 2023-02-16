package com.example.lab_2_flasz_asystent_nauczyciela

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddStudentBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddSubjectBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddSubject.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSubject : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddSubjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    private lateinit var subjectsList: MutableList<Subjects>

    private val days = arrayOf("poniedziałek", "wtorek", "środa", "czwartek", "piątek")
    private val hours = arrayOf("8:30", "10:15", "12:00", "13:45", "15:30", "17:15", "19:00")

    private lateinit var daysArrayAdapter: ArrayAdapter<String>
    private lateinit var hoursArrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddSubjectBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        daysArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, days)
        hoursArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, hours)

        dataInitialize()

        binding.buttonAdd.setOnClickListener {
            if(addSubjectToList()) {
                Navigation.findNavController(binding.root).navigate(R.id.action_addSubject_to_subjectsList)
            }
        }

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_addSubject_to_subjectsList)
        }

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addSubjectToList(): Boolean {
        val name = binding.name.text.toString().trim(' ')
        val day = binding.spinnerDay.selectedItem.toString()
        val hour = binding.spinnerHour.selectedItem.toString()
        val subject = Subjects(name, day, hour)

        if (name.isEmpty()) {
            Toast.makeText(activity, "Uzupełnij dane przedmiotu!", Toast.LENGTH_SHORT).show()
        } else if (subjectsList.find{it.subjectName == subject.subjectName} != null) {
            Toast.makeText(activity, "Te zajęcia już istnieją!", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                appDatabase.subjectsDao().insert(subject)
                subjectsList.add(subject)
            }
            binding.name.text.clear()

            Toast.makeText(activity, "Dodano przedmiot!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            subjectsList = appDatabase.subjectsDao().getAll()
        }

        binding.spinnerDay.adapter = daysArrayAdapter

        binding.spinnerHour.adapter = hoursArrayAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSubject.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSubject().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}