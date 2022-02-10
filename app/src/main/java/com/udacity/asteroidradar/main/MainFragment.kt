package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        val activity = requireActivity()
        val factory = MainViewModelFactory(activity.application)
        ViewModelProvider(this,factory)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        viewModel.pictureOfTheDay.observe(viewLifecycleOwner, Observer {
            Picasso.with(this.context).load(it?.url).into(binding.activityMainImageOfTheDay);
        })
        val adapter = MainAdapter(MainAdapter.OnClickListener{it->viewModel.setNavigateIndicator(it)})
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.navigateIndicator.observe(viewLifecycleOwner, Observer {
            if( null!= it){
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.unsetNavigateIndicator()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
