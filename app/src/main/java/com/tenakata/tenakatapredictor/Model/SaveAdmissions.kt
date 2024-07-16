package com.tenakata.tenakatapredictor.Model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tenakata.tenakatapredictor.UI.Admission.Admission

@Entity(tableName = "SaveAdmission")
class SaveAdmission(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "age") val age: String,
    @ColumnInfo(name = "gender") val gender:String,
    @ColumnInfo(name = "marital_status") val marital_status:String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "iq_result") val iq: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "image_url") val image_url:String,
    @ColumnInfo(name = "score") val score:Double,
    @ColumnInfo(name = "time_stamp") val timeStamp:String
)
{
    constructor() : this(id = "", name = "", age = "", gender ="", marital_status = "", height ="", iq="", location="", image_url="", score=0.0, timeStamp = "") // this constructor is an explicit
    // "empty" constructor, as seen by Java.
}

@Dao
interface SaveAdmissionDao {

    @Query("SELECT * FROM SaveAdmission ORDER BY score DESC")
    fun getAll(): LiveData<List<SaveAdmission>>

    /**@Query("SELECT loan_description FROM loan_products")
    fun getLoanDescription(): LiveData<List<String>>*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg saveAdmission: SaveAdmission)

    @Query("DELETE FROM SaveAdmission")
    fun clearProperty()


    @Transaction
    fun syncAdmission(vararg saveAdmission: SaveAdmission) {
        //clearProperty()
        insert(*saveAdmission)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAll(objects: List<SaveAdmission>)
}