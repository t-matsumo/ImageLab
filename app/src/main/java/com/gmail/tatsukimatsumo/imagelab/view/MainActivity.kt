package com.gmail.tatsukimatsumo.imagelab.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.tatsukimatsumo.imagelab.R
import com.gmail.tatsukimatsumo.imagelab.databinding.ActivityMainBinding
import com.gmail.tatsukimatsumo.imagelab.viewmodel.PhotoListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: PhotoListViewModel by viewModels()

    private val requestPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val viewManager = GridLayoutManager(this, 4)
        val viewAdapter = PhotoAdapter(emptyList())
        this.photoListView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val requiredPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this,  requiredPermission) == PackageManager.PERMISSION_GRANTED) {
            viewModel.onCreate()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermission)) {
                val toast: Toast = Toast.makeText(this, getString(R.string.photo_permission_description), Toast.LENGTH_SHORT)
                toast.show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(requiredPermission), requestPermissionCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestPermissionCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                viewModel.onCreate()
            } else {
                val toast: Toast = Toast.makeText(this, getString(R.string.photo_permission_denied), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
