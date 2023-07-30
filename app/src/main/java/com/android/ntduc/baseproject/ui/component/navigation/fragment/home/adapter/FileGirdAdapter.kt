package com.android.ntduc.baseproject.ui.component.navigation.fragment.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.constant.FileTypeExtension
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.databinding.ItemDocumentGirdBinding
import com.android.ntduc.baseproject.utils.formatBytes
import com.android.ntduc.baseproject.utils.loadImg
import com.ntduc.recyclerviewadvanced.draggable.DraggableItemAdapter
import com.ntduc.recyclerviewadvanced.draggable.ItemDraggableRange
import com.ntduc.recyclerviewadvanced.utils.AbstractDraggableItemViewHolder
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileGirdAdapter(
    val context: Context
) : BindingListAdapter<BaseFile, FileGirdAdapter.ItemViewHolder>(diffUtil), DraggableItemAdapter<FileGirdAdapter.ItemViewHolder> {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = currentList[position]?.id ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        parent.binding<ItemDocumentGirdBinding>(R.layout.item_document_gird).let(::ItemViewHolder)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ItemViewHolder constructor(
        val binding: ItemDocumentGirdBinding
    ) : AbstractDraggableItemViewHolder(binding.root) {

        fun bind(baseFile: BaseFile) {
            binding.apply {
                this@apply.baseFile = baseFile
            }

            when (FileTypeExtension.getTypeFile(baseFile.data!!)) {
                FileTypeExtension.AUDIO -> {
                    val coroutineScope =
                        itemView.findViewTreeLifecycleOwner()?.lifecycleScope ?: CoroutineScope(
                            Dispatchers.IO
                        )
                    coroutineScope.launch {
                        val image = try {
                            val mData = MediaMetadataRetriever()
                            mData.setDataSource(baseFile.data)
                            val art = mData.embeddedPicture
                            BitmapFactory.decodeByteArray(art, 0, art!!.size)
                        } catch (e: Exception) {
                            null
                        }

                        withContext(Dispatchers.Main) {
                            context.loadImg(
                                imgUrl = image,
                                view = binding.img,
                                error = FileTypeExtension.getIconFile(baseFile.data!!),
                                placeHolder = R.color.black
                            )
                        }
                    }
                }

                else -> {
                    context.loadImg(
                        imgUrl = baseFile.data!!,
                        view = binding.img,
                        error = FileTypeExtension.getIconFile(baseFile.data!!),
                        placeHolder = R.color.black
                    )
                }
            }
            binding.txtTitle.text = baseFile.displayName
            binding.txtDescription.text = baseFile.size?.formatBytes()

            binding.root.setOnClickListener {
                onClickListener?.let {
                    it(binding.cardView, baseFile)
                }
            }

            binding.executePendingBindings()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<BaseFile>() {
            override fun areItemsTheSame(oldItem: BaseFile, newItem: BaseFile): Boolean =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: BaseFile, newItem: BaseFile): Boolean =
                oldItem == newItem
        }
    }

    private var onClickListener: ((View, BaseFile) -> Unit)? = null

    fun setOnClickListener(listener: (View, BaseFile) -> Unit) {
        onClickListener = listener
    }

    override fun onCheckCanStartDrag(holder: ItemViewHolder, position: Int, x: Int, y: Int): Boolean {
        val containerView: View = holder.binding.cardView
        val offsetX = containerView.left + (containerView.translationX + 0.5f).toInt()
        val offsetY = containerView.top + (containerView.translationY + 0.5f).toInt()
        val tx = (containerView.translationX + 0.5f).toInt()
        val ty = (containerView.translationY + 0.5f).toInt()
        val left = containerView.left + tx
        val right = containerView.right + tx
        val top = containerView.top + ty
        val bottom = containerView.bottom + ty
        return (x - offsetX in left..right) && (y - offsetY in top..bottom)
    }

    override fun onGetItemDraggableRange(holder: ItemViewHolder, position: Int): ItemDraggableRange? = null

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        onMoveItemListener?.let {
            it(fromPosition, toPosition)
        }
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean = true
    override fun onItemDragStarted(position: Int) {}
    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {}

    private var onMoveItemListener: ((Int, Int) -> Unit)? = null

    fun setOnMoveItemListener(listener: (Int, Int) -> Unit) {
        onMoveItemListener = listener
    }
}