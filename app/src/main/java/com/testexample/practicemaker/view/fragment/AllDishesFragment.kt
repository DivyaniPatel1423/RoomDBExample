package com.testexample.practicemaker.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.testexample.practicemaker.R
import com.testexample.practicemaker.application.FavDishapplication
import com.testexample.practicemaker.databinding.FragmentAllDishesBinding
import com.testexample.practicemaker.view.activity.AddUpdateDishActivity
import com.testexample.practicemaker.view.adapter.FavDishAdapter
import com.testexample.practicemaker.viewmodel.FavDishViewModel

class AllDishesFragment : Fragment()  {

    private lateinit var mBinding :FragmentAllDishesBinding
    private val mFavDishViewModel : FavDishViewModel by viewModels(){
        FavDishViewModel.FavDishViewModelFactory((requireActivity().application as FavDishapplication).respository)
    }
    private lateinit var favDishAdapter:FavDishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAllDishesBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.rcvAllDished.layoutManager = GridLayoutManager(requireActivity(),2)
        favDishAdapter = FavDishAdapter(requireActivity(),mFavDishViewModel)
        mBinding.rcvAllDished.adapter = favDishAdapter
        mFavDishViewModel.allFavDishes.observe(viewLifecycleOwner, Observer {
                  it.let {
                      Log.e("alldishes", it.toString())
                      for(item in it){
                          if(it.isNotEmpty()){
                              mBinding.rcvAllDished.visibility = View.VISIBLE
                              mBinding.textHome.visibility = View.GONE
                              favDishAdapter.dishesList(it)
                          }else{
                              mBinding.textHome.visibility = View.VISIBLE
                              mBinding.rcvAllDished.visibility = View.GONE
                          }
                      }

                  }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(),AddUpdateDishActivity::class.java))
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

}