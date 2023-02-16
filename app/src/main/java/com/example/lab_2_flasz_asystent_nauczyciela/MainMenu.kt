package com.example.lab_2_flasz_asystent_nauczyciela

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenu : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var appDatabase: AppDatabase

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
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)

        appDatabase = AppDatabase.getDatabase(requireContext())

        val btnStudenci = view.findViewById<CardView>(R.id.buttonStudenci)
        btnStudenci.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainMenu_to_studentsList)
        }

        val btnPrzedmioty = view.findViewById<CardView>(R.id.buttonPrzedmioty)
        btnPrzedmioty.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainMenu_to_subjectsList)
        }

        val btnReset = view.findViewById<Button>(R.id.resetButton)
        btnReset.setOnClickListener {
            deleteAllData()
        }

        return view;
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteAllData() {
        (activity?.let {
            val builder =
                AlertDialog.Builder(it).setMessage("Na pewno usunąć wszystkie dane?")
            builder.apply {
                setPositiveButton("TAK",
                    DialogInterface.OnClickListener { dialog, id ->
                        GlobalScope.launch {
                            appDatabase.subjectsDao().deleteAll()
                            appDatabase.studentsDao().deleteAll()
                            appDatabase.studentsSubjectsDao().deleteAll()
                            appDatabase.marksDao().deleteAll()
                        }
                        Toast.makeText(
                            activity,
                            "Dane zostały unięte!",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                setNegativeButton("NIE",
                    DialogInterface.OnClickListener { dialog, id ->
                        //                            dialog.cancel()
                    })
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainMenu.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainMenu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}