package com.gmail.tatsukimatsumo.imagelab.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.tatsukimatsumo.imagelab.databinding.PhotoListItemBinding
import com.gmail.tatsukimatsumo.imagelab.viewmodel.PhotoListViewModel


class PhotoAdapter(private var photoList: List<PhotoListViewModel.Photo>) :
RecyclerView.Adapter<PhotoAdapter.ItemHolder>() {

    class ItemHolder(private val binding: PhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(photo: PhotoListViewModel.Photo) {
            Glide
                .with(binding.root)
                .load(photo.url)
                .placeholder(ColorDrawable(Color.LTGRAY))
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PhotoListItemBinding.inflate(layoutInflater, parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = holder.setItem(photoList[position])
    override fun getItemCount() = photoList.size

    private fun setList(photos: List<PhotoListViewModel.Photo>) {
        photoList = photos
        notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun RecyclerView.bindItems(items: List<PhotoListViewModel.Photo>?) {
            if (items == null) {
                return
            }

            val adapter = adapter as PhotoAdapter
            adapter.setList(items)
        }
    }
}