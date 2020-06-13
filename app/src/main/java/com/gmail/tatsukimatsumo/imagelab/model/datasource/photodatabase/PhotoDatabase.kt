package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Photo(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "uri_string") val uriString: String
)

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo")
    fun getAll(): List<Photo>

    @Query("DELETE FROM photo")
    fun deleteAll()
}

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile private var INSTANCE: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            return INSTANCE
                ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "photo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}