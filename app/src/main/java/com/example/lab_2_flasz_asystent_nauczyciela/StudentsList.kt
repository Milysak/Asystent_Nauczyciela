package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentSubjectsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentsList.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentsList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSubjectsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    private var studentsList : MutableList<Students> = mutableListOf()

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
        _binding = FragmentSubjectsListBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        dataInitialize()

        recyclerView = binding.root.findViewById(R.id.scrollView1)

        val adapter: StudentAdapter = StudentAdapter(studentsList)
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        val backButton = binding.buttonBack
        backButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_studentsList_to_mainMenu)
        }

        val addButton = binding.buttonAdd
        addButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_studentsList_to_addStudent)
        }

        return binding.root
    }

    fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            val uczniowieFromDB = appDatabase.studentsDao().getAll()
            if (param1 == null || param2 == null) {
                uczniowieFromDB.forEach {
                    studentsList.add(it)
                }
            } else {
                appDatabase.studentsSubjectsDao().getAll().filter{ it.subjectName == param1 }.forEachIndexed { i, value ->
                    uczniowieFromDB.find { it.albumNumber == value.albumNumber }
                        ?.let { studentsList.add(it) }
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
         * @return A new instance of fragment StudentsList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentsList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}