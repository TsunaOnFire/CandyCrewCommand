package com.aph.willywonka.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.aph.willywonka.data.model.GENDER_FEMALE
import com.aph.willywonka.data.model.GENDER_MALE
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.databinding.ActivityMainBinding
import com.aph.willywonka.ui.adapters.WorkersAdapter
import com.aph.willywonka.ui.viewmodel.WorkerListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val workerListViewModel : WorkerListViewModel by viewModels()

    private lateinit var workersAdapter: WorkersAdapter
    private val workers= mutableListOf<WorkerBO>()

    private lateinit var professionAdapter: ArrayAdapter<String>
    private var professions= mutableListOf<String>()

    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var  animationIn : Animation
    private lateinit var  animationOut : Animation

    private var prevWorkerListSize= 0;
    private var mustForceReloadRecycler= false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        animationIn = AnimationUtils.loadAnimation(this, com.google.android.material.R.anim.abc_slide_in_top)
        animationOut = AnimationUtils.loadAnimation(this, com.google.android.material.R.anim.abc_slide_out_top)

        setContentView(binding.root)
        setUpView()
        setUpObservers()
        setUpListeners()

        workerListViewModel.requestPage()
    }

    private fun setUpView() {
        setUpAutoCompleteTextView()
        setUpHamburgerAnimationView()
        initRecyclerView()
    }

    private fun setUpListeners() {
        binding.workerListSwipeRefreshLayout.setOnRefreshListener {//SWIPE TO REFRESH ACTION
            workerListViewModel.requestPage()
        }

        binding.workerListRecycler.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            if(atBottom()){
                workerListViewModel.requestPage()
            }
        }

        binding.workerListFilterApplyFilers.setOnClickListener {
            mustForceReloadRecycler = true
            workerListViewModel.filterWorkers()
            if (binding.workerListMotionHamburger.progress == 1.0F)  {
                binding.workerListMotionHamburger.transitionToStart()
                binding.workerListLayoutFiltersDialog.startAnimation(animationOut)
                binding.workerListLayoutFiltersDialog.visibility = View.GONE
            }
        }

        binding.workerListFilterGenderGroup.setOnCheckedChangeListener { radioGroup, id ->
            workerListViewModel.setGenderFilter(
                when (id) {
                    binding.workerListFilterGenderFemale.id -> GENDER_FEMALE
                    binding.workerListFilterGenderMale.id -> GENDER_MALE
                    else -> null
                }
            )
        }

        binding.workerListFilterProfessionAutocomplete.doOnTextChanged { text, start, before, count ->
            binding.workerListFilterProfessionAutocomplete.showDropDown()
            workerListViewModel.setProfessionFilter(text.toString())
        }
    }

    private fun setUpObservers() {
        workerListViewModel.isLoading.observe(this) { isLoading ->
            binding.workerListSwipeRefreshLayout.isRefreshing = isLoading
        }

        workerListViewModel.workersList.observe(this) { updated_list ->
            prevWorkerListSize = workers.size
            workers.clear()
            workers.addAll(updated_list)

            if(workerListViewModel.getFilterApply() && mustForceReloadRecycler) {
                mustForceReloadRecycler = false
                workersAdapter.notifyDataSetChanged()
                binding.workerListEmptyStateLabel.isVisible = workers.size == 0
            } else{
                if(prevWorkerListSize<workers.size){
                    workersAdapter.notifyItemRangeChanged(prevWorkerListSize,workers.size-prevWorkerListSize)
                } else {
                    workersAdapter.notifyItemRangeChanged(workers.size,prevWorkerListSize-workers.size)
                }
            }
        }

        workerListViewModel.professionList.observe(this) { updated_list ->
            professions.clear()
            professions.addAll(updated_list)
            professionAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpAutoCompleteTextView() {
        professionAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            professions
        )

        binding.workerListFilterProfessionAutocomplete.setAdapter(professionAdapter)
    }

    private fun setUpHamburgerAnimationView() {

        val buttonHamburgerListener: Animation.AnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.workerListButtonHamburger.interactive(false)
            }
            override fun onAnimationEnd(animation: Animation) {
                binding.workerListButtonHamburger.interactive(true)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }

        animationIn.setAnimationListener(buttonHamburgerListener);
        animationOut.setAnimationListener(buttonHamburgerListener);

        binding.workerListButtonHamburger.setOnClickListener {
            if (binding.workerListMotionHamburger.progress == 0.0F) {
                binding.workerListMotionHamburger.transitionToEnd()
                binding.workerListLayoutFiltersDialog.startAnimation(animationIn)
                binding.workerListLayoutFiltersDialog.visibility = View.VISIBLE
            } else if (binding.workerListMotionHamburger.progress == 1.0F)  {
                binding.workerListMotionHamburger.transitionToStart()
                binding.workerListLayoutFiltersDialog.startAnimation(animationOut)
                binding.workerListLayoutFiltersDialog.visibility = View.GONE

            }
        }
    }

    private fun initRecyclerView() {
        workersAdapter= WorkersAdapter(workers) { onItemSelected(it) }
        layoutManager = LinearLayoutManager(this)
        binding.workerListRecycler.layoutManager = layoutManager
        binding.workerListRecycler.adapter=workersAdapter
    }

    private fun onItemSelected(worker: WorkerBO){
        val intent = Intent(this, DetailActivity::class.java)
        val b = Bundle()
        b.putParcelable("worker", worker)
        intent.putExtras(b)
        startActivity(intent)
    }

    private fun atBottom(): Boolean {
        return (layoutManager.findLastCompletelyVisibleItemPosition()
                == workersAdapter.itemCount - 1)
    }

    private fun View.interactive(enabled: Boolean){
        this.isClickable = enabled
        this.isFocusable = enabled
    }
}