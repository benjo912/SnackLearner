package com.example.snacklearner

import android.app.Application
import com.example.snacklearner.data.AppDatabase

class SnackLearnerApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
