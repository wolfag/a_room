package com.example.todo_mvvm_room.fragments.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_mvvm_room.model.User
import com.example.todo_mvvm_room.databinding.ItemRowBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list = emptyList<User>()

    inner class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val id = binding.tvId
        val fname = binding.tvFName
        val lname = binding.tvLName
        val age = binding.tvAge
        val address = binding.tvAddress
        val avatar = binding.ivAvatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.id.text = item.id.toString()
        holder.fname.text = item.firstName
        holder.lname.text = item.lastName
        holder.age.text = item.age.toString()
        holder.address.text = "${item.address.streetName} - ${item.address.streetNumber}"
        holder.avatar.setImageBitmap(item.avatar)


        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
            it.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(data: List<User>) {
        list = data
        notifyDataSetChanged()
    }
}