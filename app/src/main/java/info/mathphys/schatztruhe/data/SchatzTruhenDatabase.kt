package info.mathphys.schatztruhe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [bietet_an::class,Product::class,Theke::class,verkauft::class], version = 1)
@TypeConverters(DateTypeConverter::class)
 abstract class SchatzTruhenDatabase : RoomDatabase() {
    abstract fun thekeDao(): ThekeDao
    abstract fun productDao():ProductDao
    companion object {
        @Volatile
        private var INSTANCE: SchatzTruhenDatabase? = null

        fun getDatabase(context: Context): SchatzTruhenDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchatzTruhenDatabase::class.java,
                    "schatztruhen_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}