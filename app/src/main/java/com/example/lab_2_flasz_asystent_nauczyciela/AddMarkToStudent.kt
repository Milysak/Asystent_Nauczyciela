package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddMarkToStudentBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddStudentToSubjectBinding
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
 * Use the [AddMarkToStudent.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMarkToStudent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddMarkToStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    lateinit var name: String
    lateinit var date: String

    private lateinit var subjectsArrayAdapter: ArrayAdapter<String>
    private lateinit var studentsArrayAdapter: ArrayAdapter<String>
    private lateinit var typeOfMarkArrayAdapter: ArrayAdapter<String>
    private lateinit var valueOfMarkArrayAdapter: ArrayAdapter<String>

    val subjects: MutableList<String> = arrayListOf()
    val students: MutableList<String> = arrayListOf()

    private val typeOfMark = arrayOf("kolokwium", "egzamin", "projekt")
    private val valueOfMark = arrayOf("2","3","3.5","4","4.5","5")

    private lateinit var subjectsList: MutableList<Subjects>
    private lateinit var studentsList: MutableList<Students>
    private lateinit var marksList: MutableList<Marks>

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
        _binding = FragmentAddMarkToStudentBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        val bundle: Bundle? = arguments

        name = bundle?.getString("string").toString()
        date = bundle?.getString("string1").toString()

        bundle?.putString("string", name)
        bundle?.putString("string1", date)

        val fragment = StudentProfile()
        fragment.arguments = bundle

        dataInitialize()

        binding.buttonAdd.setOnClickListener {
            if(addMarkToStudent()) {
                Navigation.findNavController(binding.root).navigate(R.id.action_addMarkToStudent_to_studentProfile, bundle)
            }
        }

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_addMarkToStudent_to_studentProfile, bundle)
        }

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addMarkToStudent(): Boolean {
        if(subjects.isNotEmpty() && students.isNotEmpty()) {
            val selectedStudentPosition: Int = binding.spinnerNameOfStudent.selectedItemPosition
            val selectedSubjectPosition: Int = binding.spinnerNameOfSubject.selectedItemPosition

            val selectedTypeOfMark: String = binding.spinnerTypeOfMark.selectedItem.toString()
            val selectedValueOfMark: String = binding.spinnerValueOfMark.selectedItem.toString()

            val newMarkToStudent = Marks(subjectsList[selectedSubjectPosition].subjectName, studentsList[selectedStudentPosition].albumNumber, selectedTypeOfMark, selectedValueOfMark)

            GlobalScope.launch(Dispatchers.IO) {
                appDatabase.marksDao().insert(newMarkToStudent)
                marksList.add(newMarkToStudent)
            }

            return true
        }

        Toast.makeText(binding.root.context, "Brak wszystkich danych!", Toast.LENGTH_SHORT).show()
        return false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            subjectsList = appDatabase.subjectsDao().getAll()

            studentsList = appDatabase.studentsDao().getAll()

            marksList = appDatabase.marksDao().getAll()

            subjectsList.forEach {
                subjects.add(it.subjectName + " " + it.day + " - " + it.hour)
            }

            studentsList.forEach {
                students.add(it.albumNumber.toString() + " - " + it.nameSurname)
            }

            subjectsArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, subjects)
            studentsArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, students)
            typeOfMarkArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, typeOfMark)
            valueOfMarkArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, valueOfMark)

            binding.spinnerNameOfStudent.adapter = studentsArrayAdapter
            binding.spinnerNameOfSubject.adapter = subjectsArrayAdapter
            binding.spinnerTypeOfMark.adapter = typeOfMarkArrayAdapter
            binding.spinnerValueOfMark.adapter = valueOfMarkArrayAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddMarkToStudent.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMarkToStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}