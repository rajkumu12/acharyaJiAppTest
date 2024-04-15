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
        binding.imageRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.getPhotos()
                }
            }
        })

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
                    imagesList= (imagesList+it.data!!.results).toMutableList()
                    binding.imageRecyclerview.layoutManager =
                        GridLayoutManager(this@MainActivity, 2) // 2 columns in the grid
                    val adapter = ImagesAdapter(this@MainActivity)
                    adapter.setsImagesList(imagesList)
                    binding.imageRecyclerview.adapter = adapter
                }
            }
        })
    }
}