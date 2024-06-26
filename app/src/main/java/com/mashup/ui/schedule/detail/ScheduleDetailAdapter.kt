package com.mashup.ui.schedule.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ItemEventTimelineHeaderBinding
import com.mashup.ui.schedule.detail.composable.ScheduleDetailContentItem
import com.mashup.ui.schedule.detail.composable.ScheduleDetailInfoItem
import com.mashup.ui.schedule.detail.composable.ScheduleDetailLocationItem
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
                HeaderViewHolder(parent)
            }
            EventDetailType.CONTENT.num -> {
                ContentViewHolder(ComposeView(parent.context))
            }
            EventDetailType.LOCATION.num -> {
                LocationViewHolder(ComposeView(parent.context))
            }
            EventDetailType.INFO.num -> {
                InfoViewHolder(ComposeView(parent.context))
            }
            else -> {
                InfoViewHolder(ComposeView(parent.context))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position))
            }
            is ContentViewHolder -> {
                holder.bind(getItem(position))
            }
            is LocationViewHolder -> {
                holder.bind(getItem(position), copyToClipboard)
            }
            is InfoViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class HeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ItemEventTimelineHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {
        private val binding: ItemEventTimelineHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            if (item !is EventDetail.Header) return

            binding?.model = item
            if (item.eventId == 1) {
                binding?.line?.visibility = View.GONE
            }
        }
    }

    class ContentViewHolder(private val composeView: ComposeView) :
        RecyclerView.ViewHolder(composeView) {

        fun bind(item: EventDetail) {
            if (item !is EventDetail.Content) return

            composeView.setContent {
                MashUpTheme {
                    ScheduleDetailContentItem(
                        contentId = item.contentId,
                        title = item.title,
                        content = item.content,
                        time = item.formattedTime,
                    )
                }
            }
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
            if (item !is EventDetail.Location) return

            composeView.setContent {
                MashUpTheme {
                    ScheduleDetailLocationItem(
                        detailAddress = item.detailAddress.orEmpty(),
                        roadAddress = item.roadAddress.orEmpty(),
                        latitude = item.latitude,
                        longitude = item.longitude,
                        copyToClipboard = copyToClipboard
                    )
                }
            }
        }
    }

    class InfoViewHolder(private val composeView: ComposeView) :
        RecyclerView.ViewHolder(composeView) {
        init {
            composeView.setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool // (Default)
            )
        }

        fun bind(item: EventDetail) {
            if (item !is EventDetail.Info) return

            composeView.setContent {
                MashUpTheme {
                    ScheduleDetailInfoItem(
                        title = item.title,
                        date = item.date,
                        time = item.formattedTime,
                        modifier = Modifier.padding(top = 24.dp)
                    )
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
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(
        oldItem: EventDetail,
        newItem: EventDetail
    ): Boolean {
        return oldItem == newItem
    }
}
