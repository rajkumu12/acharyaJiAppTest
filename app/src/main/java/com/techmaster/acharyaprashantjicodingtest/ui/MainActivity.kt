package com.techmaster.acharyaprashantjicodingtest.ui

import Results
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techmaster.acharyaprashantjicodingtest.databinding.ActivityMainBinding
import com.techmaster.acharyaprashantjicodingtest.utility.NetworkResult
import com.techmaster.acharyaprashantjicodingtest.viewmodels.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<PhotosViewModel>()
    var imagesList = mutableListOf<Results>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getPhotos()
        loadData()
    }

    private fun loadData() {

        viewModel.photosResponseLiveData.observe(this@MainActivity, Observer {

            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(this@MainActivity, "Error:  " + it.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    binding.imageRecyclerview.layoutManager =
                        GridLayoutManager(this@MainActivity, 2) // 2 columns in the grid
                    val adapter = ImagesAdapter(this@MainActivity)
                    adapter.setsImagesList(it.data!!.results)
                    binding.imageRecyclerview.adapter = adapter
                }
            }
        })
    }
}