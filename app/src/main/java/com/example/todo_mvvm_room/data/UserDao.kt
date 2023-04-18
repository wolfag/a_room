package com.example.todo_mvvm_room.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo_mvvm_room.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("delete from user")
    suspend fun deleteAll()

    @Query("select * from user order by id asc")
    fun fetchAll(): LiveData<List<User>>

    @Query("select * from user where firstName like :query or lastName like :query")
    fun search(query: String): LiveData<List<User>>
}