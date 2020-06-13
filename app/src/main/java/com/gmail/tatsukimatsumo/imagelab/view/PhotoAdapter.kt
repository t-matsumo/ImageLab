package com.gmail.tatsukimatsumo.imagelab.view

import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.tatsukimatsumo.imagelab.databinding.PhotoListItemBinding
import com.gmail.tatsukimatsumo.imagelab.viewmodel.PhotoListViewModel


class PhotoAdapter(private val context: Context, private var photoList: List<PhotoListViewModel.Photo>) :
RecyclerView.Adapter<PhotoAdapter.ItemHolder>() {

    class ItemHolder(private val context: Context, private val binding: PhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(photo: PhotoListViewModel.Photo) {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, photo.url)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, photo.url)
            }
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PhotoListItemBinding.inflate(layoutInflater, parent, false)
        return ItemHolder(context, binding)
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