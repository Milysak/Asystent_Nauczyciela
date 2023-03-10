package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddStudentToSubjectBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentStudentProfileBinding
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
 * Use the [AddStudentToSubject.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddStudentToSubject : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddStudentToSubjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    lateinit var name: String
    lateinit var date: String

    private lateinit var subjectsArrayAdapter: ArrayAdapter<String>
    private lateinit var studentsArrayAdapter: ArrayAdapter<String>

    val subjects: MutableList<String> = arrayListOf()
    val students: MutableList<String> = arrayListOf()

    private lateinit var subjectsList: MutableList<Subjects>
    private lateinit var studentsList: MutableList<Students>
    private lateinit var studentsSubjectsList: MutableList<StudentsSubjects>

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
        _binding = FragmentAddStudentToSubjectBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        val bundle: Bundle? = arguments

        name = bundle?.getString("string").toString()
        date = bundle?.getString("string1").toString()

        bundle?.putString("string", name)
        bundle?.putString("string1", date)

        val fragment = SubjectProfile()
        fragment.arguments = bundle

        dataInitialize()

        binding.buttonAdd.setOnClickListener {
            if(addStudentToSubject()) {
                Navigation.findNavController(binding.root).navigate(R.id.action_addStudentToSubject_to_subjectProfile, bundle)
            }
        }

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_addStudentToSubject_to_subjectProfile, bundle)
        }

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addStudentToSubject(): Boolean {
        if(students.isNotEmpty() && subjects.isNotEmpty()) {
            val selectedStudentPosition: Int = binding.spinnerStudent.selectedItemPosition
            val selectedSubjectPosition: Int = binding.spinnerPrzedmiot.selectedItemPosition

            val newStudentToSubject = StudentsSubjects(subjectsList[selectedSubjectPosition].subjectName, studentsList[selectedStudentPosition].albumNumber)

            val text = studentsList[selectedStudentPosition].albumNumber.toString() + "\n" + subjectsList[selectedSubjectPosition].subjectName
            //Toast.makeText(binding.root.context, text,Toast.LENGTH_SHORT).show()

            if(studentsSubjectsList.find { it.subjectName + it.albumNumber == newStudentToSubject.subjectName + newStudentToSubject.albumNumber } != null) {
                Toast.makeText(binding.root.context, "To przypisanie ju?? istnieje!", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    appDatabase.studentsSubjectsDao().insert(newStudentToSubject)
                    studentsSubjectsList.add(newStudentToSubject)
                }
                return true
            }
            return false
        }

        return false
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            subjectsList = appDatabase.subjectsDao().getAll()

            studentsList = appDatabase.studentsDao().getAll()

            studentsSubjectsList = appDatabase.studentsSubjectsDao().getAll()

            subjectsList.forEach {
                subjects.add(it.subjectName + " " + it.day + " - " + it.hour)
            }

            studentsList.forEach {
                students.add(it.albumNumber.toString() + " - " + it.nameSurname)
            }

            subjectsArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, subjects)
            studentsArrayAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_spinner_dropdown_item, students)

            binding.spinnerPrzedmiot.adapter = subjectsArrayAdapter

            binding.spinnerStudent.adapter = studentsArrayAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddStudentToSubject.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddStudentToSubject().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}