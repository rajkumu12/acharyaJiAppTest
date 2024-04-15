package com.techmaster.acharyaprashantjicodingtest.ui

import Results
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techmaster.acharyaprashantjicodingtest.R
import com.techmaster.acharyaprashantjicodingtest.databinding.ImagesItemBinding
import com.techmaster.acharyaprashantjicodingtest.utility.ImageLoaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagesAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var imagesList = mutableListOf<Results>()

    @SuppressLint("NotifyDataSetChanged")
    fun setsImagesList(imagesList: List<Results>) {
        this.imagesList = imagesList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return  MainViewHolder(ImagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            holder as MainViewHolder
            holder.bind(imagesList[position])


    }

    override fun getItemCount(): Int {
            return imagesList.size

    }

}

class MainViewHolder(val binding: ImagesItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photos: Results) {

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        val coroutineScope2 = CoroutineScope(Dispatchers.Main)
        val imageLoader = ImageLoaders(context, photos.urls.thumb) { bitmap ->

            if (bitmap != null) {
                coroutineScope2.launch {
                    binding.images.setImageBitmap(bitmap)
                }
            } else {
                coroutineScope2.launch {
                    binding.images.setImageResource(R.drawable.error)
                }

            }
        }
        coroutineScope.launch {
            imageLoader.loadImage()
        }

    }
}