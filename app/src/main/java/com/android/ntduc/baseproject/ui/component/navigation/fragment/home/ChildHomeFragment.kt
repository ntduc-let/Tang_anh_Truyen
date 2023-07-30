package com.android.ntduc.baseproject.ui.component.navigation.fragment.home

import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.data.Resource
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.databinding.FragmentChildHomeBinding
import com.android.ntduc.baseproject.ui.base.BaseFragment
import com.android.ntduc.baseproject.ui.component.navigation.NavigationViewModel
import com.android.ntduc.baseproject.ui.component.navigation.fragment.home.adapter.FileAdapter
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

    private val viewModel: NavigationViewModel by activityViewModels()
    private lateinit var fileAdapter: FileAdapter
    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewDragDropManager: RecyclerViewDragDropManager

    override fun initView() {
        super.initView()

        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewDragDropManager.setInitiateOnLongPress(true)
        recyclerViewDragDropManager.setInitiateOnMove(false)

        fileAdapter = FileAdapter(requireContext())
        wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(fileAdapter)

        binding.rcv.apply {
            adapter = wrappedAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            itemAnimator = DraggableItemAnimator()
        }
        recyclerViewDragDropManager.attachRecyclerView(binding.rcv)
    }

    override fun addEvent() {
        super.addEvent()

        fileAdapter.setOnMoveItemListener { fromPosition, toPosition ->
            if (fromPosition == toPosition) {
                return@setOnMoveItemListener
            }
            Hawk.put("STATE_LAYOUT_MANAGER", binding.rcv.layoutManager?.onSaveInstanceState())
            val list = arrayListOf<BaseFile>()
            list.addAll(fileAdapter.currentList)
            val item = list[fromPosition]
            list.remove(item)
            list.add(toPosition, item)
            fileAdapter.submitList(null)
            fileAdapter.submitList(list) {
                binding.rcv.post {
                    val oldState = Hawk.get<Parcelable?>("STATE_LAYOUT_MANAGER")
                    if (oldState != null) {
                        binding.rcv.layoutManager?.onRestoreInstanceState(oldState)
                        Hawk.delete("STATE_LAYOUT_MANAGER")
                    }
                }
            }
        }

        fileAdapter.setOnClickListener { view, item ->
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
        fileAdapter.submitList(list.subList(0, 10))
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