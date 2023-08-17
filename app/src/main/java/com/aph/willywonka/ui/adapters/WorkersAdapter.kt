package com.aph.willywonka.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.aph.willywonka.R
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.databinding.ItemWorkerBinding
import com.squareup.picasso.Picasso

class WorkersAdapter(private val workers: List<WorkerBO>, private val onClickListener:(WorkerBO) -> Unit) : RecyclerView.Adapter<WorkersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_worker, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(workers[position],onClickListener)
    }

    override fun getItemCount(): Int = workers.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemWorkerBinding.bind(itemView)
        fun bindItems(worker: WorkerBO,onClickListener:(WorkerBO) -> Unit) {
            binding.workerItemLabelName.text = itemView.context.getString(R.string.placeholder_name, worker.first_name,worker.last_name)
            binding.workerItemLabelProfession.text = worker.profession
            binding.workerItemLabelEmail.text = worker.email

            Picasso.get().load(worker.image).into(binding.workerItemImage)
            itemView.setOnClickListener{onClickListener(worker)}

            binding.workerItemImageGenderIcon.setImageResource(
                when(worker.gender) {
                    "M"->R.mipmap.ic_male
                    "F"-> R.mipmap.ic_female
                    else->R.mipmap.ic_female
                }
            )

            binding.itemWorkerCardview.startAnimation(AnimationUtils.loadAnimation(itemView.context,R.anim.slide_in))
        }
    }


}