package com.example.lab_2_flasz_asystent_nauczyciela

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.lab_2_flasz_asystent_nauczyciela.databinding.FragmentAddStudentBinding
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
 * Use the [AddStudent.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddStudent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    private var studentsList: MutableList<Students> = mutableListOf()

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
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())

        dataInitialize()

        binding.buttonAdd.setOnClickListener {
            if(addStudentToList()) {
                Navigation.findNavController(binding.root).navigate(R.id.action_addStudent_to_studentsList)
            }
        }

        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_addStudent_to_studentsList)
        }

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addStudentToList(): Boolean {
        val name = binding.name.text.toString().trim(' ')
        val surname = binding.surname.text.toString().trim(' ')
        val nameSurname = "$name $surname"
        val idNumber = binding.idNumber.text.toString().trim(' ').replace(" ","")
        val albumRegex = Regex("^[0-9]{6}\$")

        if (name.isEmpty() || surname.isEmpty() || idNumber.isEmpty()) {
            Toast.makeText(activity, "Uzupełnij dane studenta!", Toast.LENGTH_SHORT).show()
        } else if (!idNumber.matches(albumRegex)) {
            Toast.makeText(activity, "Wpisz prawidłowy identyfikator!", Toast.LENGTH_SHORT).show()
        } else if (studentsList.find{ it.albumNumber == idNumber.toInt()} != null) {
            Toast.makeText(activity, "Ten identyfikator jest już zajęty!", Toast.LENGTH_SHORT).show()
        } else {
            val student = Students(nameSurname, idNumber.toInt())
            GlobalScope.launch(Dispatchers.IO) {
                appDatabase.studentsDao().insert(student)
                studentsList.add(student)
            }
            binding.name.text.clear()
            binding.surname.text.clear()
            binding.idNumber.text.clear()

            Toast.makeText(activity, "Dodano studenta!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun dataInitialize() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = appDatabase.studentsDao().getAll()
        }
    }
}