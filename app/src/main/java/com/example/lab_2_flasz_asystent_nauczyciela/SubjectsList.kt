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
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentStudentsListBinding
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentSubjectsListBinding
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
 * Use the [SubjectsList.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectsList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSubjectsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    private var subjectsList : MutableList<Subjects> = mutableListOf()

    private lateinit var adapter: SubjectsAdapter
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_subjectsList_to_mainMenu)
        }

        val addButton = view.findViewById<Button>(R.id.buttonAdd)
        addButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_subjectsList_to_addSubject)
        }

        recyclerView = view.findViewById(R.id.scrollView1)

        adapter = SubjectsAdapter(subjectsList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : SubjectsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("string", subjectsList[position].subjectName)
                bundle.putString("string1", "${subjectsList[position].day} - ${subjectsList[position].hour}")

                val fragment = StudentProfile()
                fragment.arguments = bundle

                Navigation.findNavController(view).navigate(R.id.action_subjectsList_to_subjectProfile, bundle)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = appDatabase.subjectsDao().getAll()

            list.forEach {
                subjectsList.add(it)
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
         * @return A new instance of fragment SubjectsList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubjectsList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}