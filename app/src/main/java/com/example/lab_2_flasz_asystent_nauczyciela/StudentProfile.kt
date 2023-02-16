package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentStudentProfileBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentStudentsListBinding
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
 * Use the [StudentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    lateinit var nameSurname: String
    var albumNumber by Delegates.notNull<Int>()

    private var marksList : MutableList<Marks> = mutableListOf()

    private lateinit var adapter: MarksAdapter
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
        _binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        val bundle: Bundle? = arguments

        nameSurname = bundle?.getString("string").toString()
        albumNumber = bundle?.getInt("int")!!

        binding.nameSurname.text = nameSurname
        binding.albumNumber.text = albumNumber.toString()

        dataInitialize()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.scrollSubjectsAndMarks)

        adapter = MarksAdapter(marksList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : MarksAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // Do nothing
            }
        })

        view.findViewById<Button>(R.id.buttonBack).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_studentProfile_to_studentsList)
        }

        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("string", nameSurname)
            bundle.putInt("int", albumNumber)

            val fragment = AddMarkToStudent()
            fragment.arguments = bundle

            Navigation.findNavController(view).navigate(R.id.action_studentProfile_to_addMarkToStudent, bundle)
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
            val marks = appDatabase.marksDao().getAll()

            studentsSubjects.forEach {
                if(it.albumNumber == albumNumber) {
                    val subject = it.subjectName
                    val album = it.albumNumber
                    var marksString = ""

                    marks.forEach {
                        if(it.albumNumber == album && it.subjectName == subject) {
                            marksString += " ${it.markValue},"
                        }
                    }

                    if(marksString.isEmpty()) marksString = "Brak ocen!"

                    marksList.add(Marks(it.subjectName, it.albumNumber, "kolokwium", marksString))
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
         * @return A new instance of fragment StudentProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            StudentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}