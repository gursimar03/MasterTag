/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.scar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scar.ui.theme.Gun

@Database(entities = [Gun::class], version = 1, exportSchema = false)
abstract class GunDatabase : RoomDatabase() {

    abstract fun gunDao(): GunDao

    companion object {
        @Volatile
        private var Instance: GunDatabase? = null

        fun getDatabase(context: Context): GunDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GunDatabase::class.java, "gun_database")
                    .build().also { Instance = it }
            }
        }
    }
}

