package com.android.ntduc.baseproject.ui.component.navigation.fragment.home

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.data.Resource
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.databinding.FragmentChildHomeBinding
import com.android.ntduc.baseproject.ui.base.BaseFragment
import com.android.ntduc.baseproject.ui.component.navigation.NavigationViewModel
import com.android.ntduc.baseproject.ui.component.navigation.fragment.home.adapter.FileGirdAdapter
import com.android.ntduc.baseproject.ui.component.navigation.fragment.home.adapter.FileLinearAdapter
import com.android.ntduc.baseproject.utils.navigateToDesWithMotionItem
import com.android.ntduc.baseproject.utils.observe
import com.android.ntduc.baseproject.utils.view.gone
import com.android.ntduc.baseproject.utils.view.visible
import com.ntduc.recyclerviewadvanced.animator.DraggableItemAnimator
import com.ntduc.recyclerviewadvanced.draggable.RecyclerViewDragDropManager
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChildHomeFragment : BaseFragment<FragmentChildHomeBinding>(R.layout.fragment_child_home) {

    companion object {
        const val LINEAR = 1
        const val GRID = 2

        fun newInstance(type: Int): ChildHomeFragment {
            val args = Bundle()
            args.putInt("TYPE", type)

            val fragment = ChildHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: NavigationViewModel by activityViewModels()
    private lateinit var fileLinearAdapter: FileLinearAdapter
    private lateinit var fileGirdAdapter: FileGirdAdapter
    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewDragDropManager: RecyclerViewDragDropManager
    private var type: Int = LINEAR

    override fun initView() {
        super.initView()

        type = requireArguments().getInt("TYPE", type)

        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewDragDropManager.setInitiateOnLongPress(true)
        recyclerViewDragDropManager.setInitiateOnMove(false)

        fileLinearAdapter = FileLinearAdapter(requireContext())
        fileGirdAdapter = FileGirdAdapter(requireContext())

        wrappedAdapter = when (type) {
            LINEAR -> recyclerViewDragDropManager.createWrappedAdapter(fileLinearAdapter)
            GRID -> recyclerViewDragDropManager.createWrappedAdapter(fileGirdAdapter)
            else -> recyclerViewDragDropManager.createWrappedAdapter(fileLinearAdapter)
        }

        binding.rcv.apply {
            adapter = wrappedAdapter
            layoutManager = when (type) {
                LINEAR -> LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                GRID -> {
                    val gridLayoutManagerMr = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    gridLayoutManagerMr.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position % 5 == 0) {
                                2
                            } else 1
                        }
                    }
                    gridLayoutManagerMr
                }

                else -> LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            itemAnimator = DraggableItemAnimator()
        }
        recyclerViewDragDropManager.attachRecyclerView(binding.rcv)
    }

    override fun addEvent() {
        super.addEvent()

        fileLinearAdapter.setOnMoveItemListener { fromPosition, toPosition ->
            if (fromPosition == toPosition) {
                return@setOnMoveItemListener
            }
            Hawk.put("STATE_LAYOUT_LINEAR_MANAGER", binding.rcv.layoutManager?.onSaveInstanceState())
            val list = arrayListOf<BaseFile>()
            list.addAll(fileLinearAdapter.currentList)
            val item = list[fromPosition]
            list.remove(item)
            list.add(toPosition, item)
            fileLinearAdapter.submitList(null)
            fileLinearAdapter.submitList(list) {
                binding.rcv.post {
                    val oldState = Hawk.get<Parcelable?>("STATE_LAYOUT_LINEAR_MANAGER")
                    if (oldState != null) {
                        binding.rcv.layoutManager?.onRestoreInstanceState(oldState)
                        Hawk.delete("STATE_LAYOUT_LINEAR_MANAGER")
                    }
                }
            }
        }

        fileGirdAdapter.setOnMoveItemListener { fromPosition, toPosition ->
            if (fromPosition == toPosition) {
                return@setOnMoveItemListener
            }
            Hawk.put("STATE_LAYOUT_GIRD_MANAGER", binding.rcv.layoutManager?.onSaveInstanceState())
            val list = arrayListOf<BaseFile>()
            list.addAll(fileGirdAdapter.currentList)
            val item = list[fromPosition]
            list.remove(item)
            list.add(toPosition, item)
            fileGirdAdapter.submitList(null)
            fileGirdAdapter.submitList(list) {
                binding.rcv.post {
                    val oldState = Hawk.get<Parcelable?>("STATE_LAYOUT_GIRD_MANAGER")
                    if (oldState != null) {
                        binding.rcv.layoutManager?.onRestoreInstanceState(oldState)
                        Hawk.delete("STATE_LAYOUT_GIRD_MANAGER")
                    }
                }
            }
        }

        fileLinearAdapter.setOnClickListener { view, item ->
            navigateToDesWithMotionItem(R.id.detailFragment, view, getString(R.string.transition_name_detail), R.id.nav_host_fragment)
        }

        fileGirdAdapter.setOnClickListener { view, item ->
            navigateToDesWithMotionItem(R.id.detailFragment, view, getString(R.string.transition_name_detail), R.id.nav_host_fragment)
        }
    }

    override fun addObservers() {
        super.addObservers()

        observe(viewModel.filesLiveData, ::handleListFile)
    }

    private fun handleListFile(status: Resource<List<BaseFile>>) {
        when (status) {
            is Resource.Loading -> {
                showLoadingView()
            }

            is Resource.Success -> status.data?.let {
                bindListFile(list = it)
            }

            is Resource.DataError -> {
                status.errorCode?.let { Log.d("ntduc_debug", "handleListFile: Error " + it) }
            }
        }
    }

    private fun bindListFile(list: List<BaseFile>) {
        binding.loading.gone()
        if (list.isEmpty()) {
            binding.empty.visible()
            binding.rcv.gone()
        } else {
            binding.empty.gone()
            binding.rcv.visible()
        }
        fileLinearAdapter.submitList(list)
        fileGirdAdapter.submitList(list)
    }

    private fun showLoadingView() {
        binding.loading.visible()
    }

    override fun onPause() {
        recyclerViewDragDropManager.cancelDrag()
        super.onPause()
    }

    override fun onDestroyView() {
        recyclerViewDragDropManager.release()
        super.onDestroyView()
    }
}