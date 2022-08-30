package com.example.googlemaps.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlemaps.R
import com.example.googlemaps.databinding.WeatherGraphItemBinding
import com.example.googlemaps.model.response.WeatherResponseItem
import com.example.googlemaps.utils.CustomPainter
import com.example.googlemaps.utils.GraphPainter
import com.example.googlemaps.utils.RectFFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherGraphAdapter(private val context: Context) :
    RecyclerView.Adapter<WeatherGraphAdapter.WeatherGraphViewHolder>() {
    private val list = arrayListOf<WeatherResponseItem>()
    var prevMinEnd = 0f
    var prevEnd = 0f
    fun addData(list: List<WeatherResponseItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class WeatherGraphViewHolder(val binding: WeatherGraphItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherGraphViewHolder {
        return WeatherGraphViewHolder(
            WeatherGraphItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherGraphViewHolder, position: Int) {
        val binding = holder.binding
        val width = context.resources.getDimension(R.dimen.widht).toInt()
        val height = context.resources.getDimension(R.dimen.card_height).toInt()
        val bounds = RectFFactory.fromLTWH(0f, 0f, width.toFloat(), height.toFloat())
        val daySimple = SimpleDateFormat("EEE")
        val dayFormat = daySimple.format(Date(list[position].dt!! * 1000))


        val dateSimple=SimpleDateFormat("dd/MM")
        val dateFormat=dateSimple.format(Date(list[position].dt!!*1000))
        binding.apply {
            Glide.with(holder.itemView)
                .load("http://openweathermap.org/img/wn/${list[position].weather?.get(0)?.icon}.png")
                .into(weatherIcon)
            day.text=dayFormat
            date.text=dateFormat
            if (position == 0) {

                val previousMaxTemp = list[position].main!!.temp_max!!
                val nextMaxTemp = list[position + 1].main!!.temp_max!!
                var endY = 0f
                var startY = previousMaxTemp
                endY = if (previousMaxTemp < nextMaxTemp) {
                    previousMaxTemp - (nextMaxTemp - previousMaxTemp)
                } else {
                    previousMaxTemp + (previousMaxTemp - nextMaxTemp)
                }
                prevEnd = endY
                val painter = CustomPainter(
                    context = context,
                    width = width,
                    height = height,
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = startY,
                        endPointX = bounds.right,
                        endPointY = endY,
                        temp = list[position].main!!.temp_max!!
                    )

                )
                maxTempGraph.addView(painter)

                val previousMinTemp = list[position].main!!.temp_min!!
                val nextMinTemp = list[position + 1].main!!.temp_min!!
                var minEndY = 0f
                var minStartY = previousMinTemp
                minEndY = if (previousMinTemp < nextMinTemp) {
                    previousMinTemp - (nextMinTemp - previousMinTemp)
                } else {
                    previousMinTemp + (previousMinTemp - nextMinTemp)
                }
                prevMinEnd = minEndY

                val minPainter = CustomPainter(
                    context = context,
                    width = width,
                    height = height,
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = minStartY,
                        endPointX = bounds.right,
                        endPointY = minEndY,
                        temp = list[position].main!!.temp_min!!
                    )

                )
                minTempGraph.addView(minPainter)


            } else if (position != list.size - 1) {
                val previousMaxTemp = list[position].main!!.temp_max!!
                val nextMaxTemp = list[position + 1].main!!.temp_max!!
                var endY = 0f
                var startY = prevEnd
                endY = if (previousMaxTemp < nextMaxTemp) {
                    previousMaxTemp - (nextMaxTemp - previousMaxTemp)
                } else {
                    previousMaxTemp + (previousMaxTemp - nextMaxTemp)
                }
                prevEnd = endY
                val painter = CustomPainter(
                    context = context,
                    width = ViewGroup.LayoutParams.WRAP_CONTENT,
                    height = context.resources.getDimension(R.dimen.card_height).toInt(),
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = startY,
                        endPointX = bounds.right,
                        endPointY = endY,
                        temp = list[position].main!!.temp_max!!
                    )

                )
                maxTempGraph.addView(painter)


                val previousMinTemp = list[position].main!!.temp_min!!
                val nextMinTemp = list[position + 1].main!!.temp_min!!
                var minEndY = 0f
                var minStartY = prevMinEnd
                minEndY = if (previousMinTemp < nextMinTemp) {
                    previousMinTemp - (nextMinTemp - previousMinTemp)
                } else {
                    previousMinTemp + (previousMinTemp - nextMinTemp)
                }
                prevMinEnd = minEndY

                val minPainter = CustomPainter(
                    context = context,
                    width = width,
                    height = height,
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = minStartY,
                        endPointX = bounds.right,
                        endPointY = minEndY,
                        temp = list[position].main!!.temp_min!!
                    )

                )
                minTempGraph.addView(minPainter)
            } else {
                val previousMaxTemp = list[position].main!!.temp_max!!
                val nextMaxTemp = list[position].main!!.temp_max!!
                var endY = 0f
                var startY = prevEnd

                endY = if (previousMaxTemp < nextMaxTemp) {
                    previousMaxTemp - (nextMaxTemp - previousMaxTemp)
                } else {
                    previousMaxTemp + (previousMaxTemp - nextMaxTemp)
                }

                prevEnd = endY
                val painter = CustomPainter(
                    context = context,
                    width = ViewGroup.LayoutParams.WRAP_CONTENT,
                    height = context.resources.getDimension(R.dimen.card_height).toInt(),
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = startY,
                        endPointX = bounds.right,
                        endPointY = endY,
                        temp = list[position].main!!.temp_max!!
                    )

                )
                maxTempGraph.addView(painter)


                val previousMinTemp = list[position].main!!.temp_min!!
                val nextMinTemp = list[position].main!!.temp_min!!
                var minEndY = 0f
                var minStartY = prevMinEnd
                minEndY = if (previousMinTemp < nextMinTemp) {
                    previousMinTemp - (nextMinTemp - previousMinTemp)
                } else {
                    previousMinTemp + (previousMinTemp - nextMinTemp)
                }
                prevMinEnd = minEndY
                val minPainter = CustomPainter(
                    context = context,
                    width = width,
                    height = height,
                    painter = GraphPainter(
                        colorInt = Color.WHITE,
                        startPointX = bounds.left,
                        startPointY = minStartY,
                        endPointX = bounds.right,
                        endPointY = minEndY,
                        temp = list[position].main!!.temp_min!!
                    )

                )
                minTempGraph.addView(minPainter)

            }

        }
    }

    override fun getItemCount() = this.list.size
}