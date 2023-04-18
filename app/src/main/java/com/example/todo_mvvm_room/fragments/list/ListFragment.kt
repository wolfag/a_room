package com.example.todo_mvvm_room.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_mvvm_room.R
import com.example.todo_mvvm_room.databinding.FragmentListBinding
import com.example.todo_mvvm_room.viewmodel.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var binding: FragmentListBinding? = null
    private lateinit var mUserViewModel: UserViewModel
    private val _adapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setupList()

        binding?.floatingActionButton?.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setupMenu()

        return binding?.root
    }


    private fun setupMenu() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.mn_delete -> {
                        deleteAll()
                        true
                    }
                    R.id.mn_search -> {
//                        TODO: this is not work
                        val searchView = menuItem.actionView as? SearchView
                        searchView?.isSubmitButtonEnabled = true
                        searchView?.setOnQueryTextListener(this@ListFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mUserViewModel.deleteAll()
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }

    private fun setupList() {
        binding?.rvList?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = _adapter
        }

        mUserViewModel.fetchAll.observe(viewLifecycleOwner, Observer { users ->
            _adapter.setData(users)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDB(newText)
        }
        return true
    }

    private fun searchDB(query: String) {
        Log.e("taitest",query)
        val queryStr = "%$query%"
        mUserViewModel.searchAll(queryStr).observe(this) { users ->
            users.let {
                _adapter.setData(users)
            }
        }
    }
}