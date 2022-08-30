package com.example.googlemaps.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlemaps.databinding.WeatherTimeItemBinding
import com.example.googlemaps.model.response.WeatherTimeData

class WeatherTimeUpdateAdapter :
    RecyclerView.Adapter<WeatherTimeUpdateAdapter.WeatherTimeViewHolder>() {
    private val list = arrayListOf<WeatherTimeData>()
    fun addData(list: List<WeatherTimeData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class WeatherTimeViewHolder(val binding: WeatherTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherTimeViewHolder {
        return WeatherTimeViewHolder(
            WeatherTimeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherTimeViewHolder, position: Int) {
        holder.binding.apply {
            val data = list[position]
            time.text = data.time
            temp.text = data.temperature
            Glide.with(holder.itemView).load(data.icon+".png").into(statusIcon)
        }
    }

    override fun getItemCount() = list.size
}