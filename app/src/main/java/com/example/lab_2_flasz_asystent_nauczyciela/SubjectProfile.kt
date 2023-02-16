package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentStudentProfileBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentSubjectProfileBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentSubjectsListBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSubjectProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    lateinit var name: String
    lateinit var date: String

    private var studentsList : MutableList<Students> = mutableListOf()
    private lateinit var studentsSubjectsList: MutableList<StudentsSubjects>

    private lateinit var adapter: StudentAdapter
    private lateinit var recyclerView: RecyclerView


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
        _binding = FragmentSubjectProfileBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        val bundle: Bundle? = arguments

        name = bundle?.getString("string").toString()
        date = bundle?.getString("string1")!!

        binding.nameOfSubject.text = name
        binding.dayAndHour.text = date

        dataInitialize()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.scrollStudents)

        adapter = StudentAdapter(studentsList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : StudentAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // Do nothing
            }
        })

        view.findViewById<Button>(R.id.buttonBack).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_subjectProfile_to_subjectsList)
        }

        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("string", name)
            bundle.putString("string1", date)

            val fragment = AddStudentToSubject()
            fragment.arguments = bundle

            Navigation.findNavController(view).navigate(R.id.action_subjectProfile_to_addStudentToSubject, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            val students = appDatabase.studentsDao().getAll()
            val studentsSubjects = appDatabase.studentsSubjectsDao().getAll()

            studentsSubjects.forEach {
                it -> if(it.subjectName == name) {
                    val id = it.albumNumber
                students.forEach {
                        if(it.albumNumber == id) {
                            studentsList.add(it)
                        }
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubjectProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubjectProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}