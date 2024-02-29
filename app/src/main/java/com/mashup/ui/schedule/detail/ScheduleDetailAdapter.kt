package com.mashup.ui.schedule.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ItemEventTimelineContentBinding
import com.mashup.databinding.ItemEventTimelineHeaderBinding
import com.mashup.ui.schedule.detail.composable.ScheduleDetailLocationContent
import com.mashup.ui.schedule.model.EventDetail
import com.mashup.ui.schedule.model.EventDetailType

class EventDetailAdapter(
    private val copyToClipboard: (String) -> Unit
) :
    ListAdapter<EventDetail, RecyclerView.ViewHolder>(EventComparator) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.num
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EventDetailType.HEADER.num -> {
                TitleViewHolder(parent)
            }
            EventDetailType.CONTENT.num -> {
                ContentViewHolder(parent)
            }
            else -> {
                LocationViewHolder(ComposeView(parent.context))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> {
                holder.bind(getItem(position))
            }
            is ContentViewHolder -> {
                holder.bind(getItem(position))
            }
            is LocationViewHolder -> {
                holder.bind(getItem(position), copyToClipboard)
            }
        }
    }

    class TitleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ItemEventTimelineHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
        private val binding: ItemEventTimelineHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            binding?.model = item
            if (item.header?.eventId == 1) {
                binding?.line?.visibility = View.GONE
            }
        }
    }

    class ContentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ItemEventTimelineContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
        private val binding: ItemEventTimelineContentBinding? =
            DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            binding?.model = item
        }
    }

    class LocationViewHolder(private val composeView: ComposeView) :
        RecyclerView.ViewHolder(composeView) {
        init {
            composeView.setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool // (Default)
            )
        }

        fun bind(item: EventDetail, copyToClipboard: (String) -> Unit) {
            item.location?.let { location ->
                composeView.setContent {
                    MashUpTheme {
                        ScheduleDetailLocationContent(
                            placeName = location.placeName.orEmpty(),
                            address = location.address.orEmpty(),
                            latitude = location.latitude,
                            longitude = location.longitude,
                            copyToClipboard = copyToClipboard
                        )
                    }
                }
            }
        }
    }

    interface OnItemEventListener {
        fun onExitEventClick()
    }
}

object EventComparator : DiffUtil.ItemCallback<EventDetail>() {
    override fun areItemsTheSame(
        oldItem: EventDetail,
        newItem: EventDetail
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EventDetail,
        newItem: EventDetail
    ): Boolean {
        return oldItem == newItem
    }
}
