package com.example.todo_mvvm_room.fragments.update

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.todo_mvvm_room.R
import com.example.todo_mvvm_room.databinding.FragmentUpdateBinding
import com.example.todo_mvvm_room.model.Address
import com.example.todo_mvvm_room.model.User
import com.example.todo_mvvm_room.viewmodel.UserViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragment : Fragment() {
    private var binding: FragmentUpdateBinding? = null
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding?.etUpdateFirstName?.setText(args.currentUser.firstName)
        binding?.etUpdateLastName?.setText(args.currentUser.lastName)
        binding?.etUpdateAge?.setText(args.currentUser.age.toString())
        binding?.ivUpdateAvatar?.setImageBitmap(args.currentUser.avatar)

        binding?.btnUpdate?.setOnClickListener {
            updateItem()
        }

        setupMenu()

        return binding?.root
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteUser()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete `${args.currentUser.firstName}`?")
        builder.setMessage("Are you sure you want to delete `${args.currentUser.firstName}`?")
        builder.create().show()
    }

    private fun validate(
        fname: String,
        lname: String,
        age: String,
        streetName: String,
        streetNumber: String
    ): Boolean {
        return !(fname.isEmpty() && lname.isEmpty() && age.isEmpty() && streetName.isEmpty() && streetNumber.isEmpty())
    }

    private fun updateItem() {
        try {
            val fname = binding?.etUpdateFirstName?.text.toString()
            val lname = binding?.etUpdateLastName?.text.toString()
            val age = binding?.etUpdateAge?.text.toString()
            val streetName = binding?.etUpdateStreetName?.text.toString()
            val streetNumber = binding?.etUpdateStreetNumber?.text.toString()
            lifecycleScope.launch {
                if (validate(fname, lname, age, streetName, streetNumber)) {
                    mUserViewModel.updateUser(
                        User(
                            args.currentUser.id,
                            fname,
                            lname,
                            age.toInt(),
                            Address(streetName, streetNumber.toInt()),
                            args.currentUser.avatar
                        )
                    )
                    findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                    Toast.makeText(requireContext(), "Updated", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            Log.e("taitest", e.toString())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url).build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}