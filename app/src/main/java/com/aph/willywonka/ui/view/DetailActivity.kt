package com.aph.willywonka.ui.view

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.DEFAULT_SLINGSHOT_DISTANCE
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.LARGE
import com.aph.willywonka.R
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.databinding.ActivityDetailBinding
import com.aph.willywonka.ui.viewmodel.WorkerViewModel
import com.aph.willywonka.utils.isFullDownload
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val workerViewModel : WorkerViewModel by viewModels()
    private lateinit var worker: WorkerBO

    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scrollView = binding.workerDetailScrollView

        val worker = intent.extras?.getParcelable<WorkerBO>("worker")
        if (worker == null) {
            finish()
        } else {
            this.worker= worker
            setUpObservers()
            setUpListeners()
            setUpContentData()

            if(!this.worker.isFullDownload()){
                this.worker.id?.let{ workerViewModel.requestWorker(it) }
            }
        }
    }

    private fun setUpObservers() {
        binding.workerDetailLayoutSwipeRefresh.setOnRefreshListener {//SWIPE TO REFRESH ACTION
            this.worker.id?.let{ workerViewModel.requestWorker(it) }
        }

        workerViewModel.isLoading.observe(this) {  isLoading ->
            binding.workerDetailLayoutSwipeRefresh.isRefreshing = isLoading
        }

        workerViewModel.worker.observe(this) { updatedWorker ->
            updatedWorker?.let {
                this.worker = updatedWorker
                setUpContentData()
            }
        }
    }

    private fun setUpListeners() {
        binding.workerDetailButtonClose.setOnClickListener { finish() }

        binding.workerDetailAboutMeButtonExpand.setOnClickListener {
            if (binding.workerDetailMotionLayoutAboutMe.progress == 0.0F) {
                binding.workerDetailMotionLayoutAboutMe.transitionToEnd()
            } else {
                binding.workerDetailMotionLayoutAboutMe.transitionToStart()
            }
        }

        binding.workerDetailDescriptionButtonExpand.setOnClickListener {
            if (binding.workerDetailMotionLayoutDescription.progress == 0.0F) {
                binding.workerDetailMotionLayoutDescription.transitionToEnd()
            } else {
                binding.workerDetailMotionLayoutDescription.transitionToStart()
            }
        }
        binding.workerDetailMotionLayoutQuota.progress=1f
        binding.workerDetailQuotaButtonExpand.setOnClickListener {
            if (binding.workerDetailMotionLayoutQuota.progress == 0.0F) {
                binding.workerDetailMotionLayoutQuota.transitionToEnd()
            } else {
                binding.workerDetailMotionLayoutQuota.transitionToStart()
            }
        }

        binding.workerDetailMotionLayoutPreferences.progress=1f
        binding.workerDetailPreferencesButtonExpand.setOnClickListener {
            if (binding.workerDetailMotionLayoutPreferences.progress == 0.0F) {
                binding.workerDetailMotionLayoutPreferences.transitionToEnd()
            } else {
                binding.workerDetailMotionLayoutPreferences.transitionToStart()
            }
        }
    }

    private fun setUpContentData() {
        Picasso.get().load(worker.image).into(binding.workerDetailImage)
        binding.workerDetailLabelName.text = getString(R.string.placeholder_name, worker.first_name,worker.last_name)

        binding.workerDetailImageGenderIcon.setImageResource(
            when(worker.gender) {
                "M"->R.mipmap.ic_male
                "F"-> R.mipmap.ic_female
                else->R.mipmap.ic_female
            }
        )

        binding.workerDetailLabelEmail.text = getString(R.string.placeholder_email,worker.email)
        binding.workerDetailLabelProfession.text =  worker.profession
        binding.workerDetailLabelCountry.text = getString(R.string.placeholder_country,worker.country)
        binding.workerDetailLabelAge.text = getString(R.string.placeholder_age,worker.age)
        binding.workerDetailLabelHeigh.text =  getString(R.string.placeholder_height,worker.height)

        binding.workerDetailLabelFood.text = getString(R.string.placeholder_food,worker.favorite?.food)
        binding.workerDetailLabelColor.text = getString(R.string.placeholder_color,worker.favorite?.color)

        binding.workerDetailLabelSong.text = worker.favorite?.song

        worker.description?.let{ binding.workerDetailLabelDescription.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)}
        worker.quota?.let{ binding.workerDetailLabelQuota.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)}
        worker.favorite?.random_string?.let{ binding.workerDetailLabelRandom.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)}

    }
}