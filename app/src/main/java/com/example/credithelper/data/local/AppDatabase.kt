package com.example.credithelper.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.credithelper.data.local.dao.DebtDao
import com.example.credithelper.data.local.dao.IncomeDao
import com.example.credithelper.data.local.entity.DebtEntity
import com.example.credithelper.data.local.entity.IncomeEntity

@Database(
    entities = [IncomeEntity::class, DebtEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
    abstract fun debtDao(): DebtDao

    companion object {
        private const val DATABASE_NAME = "credit_helper_db"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE debts ADD COLUMN totalAmount REAL NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE debts ADD COLUMN remainingAmount REAL NOT NULL DEFAULT 0")
            }
        }

        fun create(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_1_2).build()
    }
}
