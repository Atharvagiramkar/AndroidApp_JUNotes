package com.gurudev.junotes.Activities.Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.gurudev.junotes.Adapter.NotesSubjectAdapter
import com.gurudev.junotes.Constants.Constant
import com.gurudev.junotes.Constants.CustomProgressDialog
import com.gurudev.junotes.Constants.SPref
import com.gurudev.junotes.ViewModel.NotesViewModel
import com.gurudev.junotes.databinding.FragmentNotesBinding

class SubjectFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private lateinit var binding : FragmentNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        val progress = CustomProgressDialog(requireContext())
        progress.show()

        binding.actionBar.toolbar.title = "Subjects"
        binding.actionBar.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val token = SPref.get(requireContext(), SPref.token)
        val year = SPref.get(requireContext(), SPref.yearId).toInt()

        viewModel.getSubjects(token,year)

        viewModel.observeSubject().observe(viewLifecycleOwner){data->
            binding.recyclerView.adapter = NotesSubjectAdapter(requireContext(), data!!.CONTENT)
            progress.dismiss()
        }

        viewModel.observeError().observe(viewLifecycleOwner)
        {
            progress.dismiss()
            Constant.error(requireContext(),it)
        }

        return binding.root
    }

}