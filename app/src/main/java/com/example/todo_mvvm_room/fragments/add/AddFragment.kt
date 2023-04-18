package com.example.todo_mvvm_room.fragments.add

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.todo_mvvm_room.R
import com.example.todo_mvvm_room.model.User
import com.example.todo_mvvm_room.viewmodel.UserViewModel
import com.example.todo_mvvm_room.databinding.FragmentAddBinding
import com.example.todo_mvvm_room.model.Address
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    private var binding: FragmentAddBinding? = null
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        testData()

        binding?.btnAdd?.setOnClickListener {
            insertDataToDB()
        }

        return binding?.root
    }

    private fun testData() {
        binding?.etAge?.setText("2")
        binding?.etFirstName?.setText("fname")
        binding?.etLastName?.setText("lname")
        binding?.etStreetName?.setText("street")
        binding?.etStreetNumber?.setText("30")
    }

    private fun insertDataToDB() {
        val fname = binding?.etFirstName?.text.toString()
        val lname = binding?.etLastName?.text.toString()
        val age = binding?.etAge?.text.toString()
        val streetName = binding?.etStreetName?.text.toString()
        val streetNumber = binding?.etStreetNumber?.text.toString()

        if (validate(fname, lname, age, streetName, streetNumber)) {
            lifecycleScope.launch {
                val address = Address(streetName, streetNumber.toInt())
                val user = User(
                    0,
                    fname,
                    lname,
                    age.toInt(),
                    address,
                    getBitmap("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
                )
                mUserViewModel.addUser(user)

                Toast.makeText(requireContext(), "Successfully", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url).build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}