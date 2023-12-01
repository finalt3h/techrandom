package com.example.ghtktechramdom

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ghtktechramdom.databinding.RqItemAddReportBinding
import com.example.ghtktechramdom.databinding.RqItemTagReasonCancelOrderBinding

class ReasonCancelOrderAdapter constructor(listReason: MutableList<ReasonCancelOrderModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listReason = mutableListOf<ReasonCancelOrderModel>()
    private var mEventListenter: EventListenter? = null
    private var showLoading = false

    init {
        this.listReason = listReason
    }

    fun setListReason(listReason: ArrayList<ReasonCancelOrderModel>): ReasonCancelOrderAdapter {
        this.listReason = listReason
        return this
    }

    fun setEventListenter(eventListenter: EventListenter?): ReasonCancelOrderAdapter {
        mEventListenter = eventListenter
        return this
    }

    override fun getItemViewType(position: Int): Int {
        return listReason[position].actionType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == ReasonCancelOrderModel.TYPE_ADD_ITEM) {
            view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rq_item_add_report, parent, false)
            DataBindingUtil.bind<RqItemAddReportBinding>(view).let {
                AddReasonVH(view, it!!)
            }
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.rq_item_tag_reason_cancel_order, parent, false)
            DataBindingUtil.bind<RqItemTagReasonCancelOrderBinding>(view).let {
                ReasonVH(view, it!!)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reasonReport = listReason[position]
        if (holder is ReasonVH) {
            val reasonVH = holder
            reasonVH.bindView(reasonReport)
        } else if (holder is AddReasonVH) {
            holder.bindView(reasonReport)
        }
    }

    override fun getItemCount(): Int {
        return listReason.size
    }

    inner class ReasonVH(itemView: View, val binding: RqItemTagReasonCancelOrderBinding) :
        RecyclerView.ViewHolder(itemView) {

        fun bindView(item: ReasonCancelOrderModel) {
            if (item.isSelect) {
                binding.llUnselected.visibility = View.GONE
                binding.rlSelected.visibility = View.VISIBLE
            } else {
                binding.llUnselected.visibility = View.VISIBLE
                binding.rlSelected.visibility = View.GONE
            }
            binding.contentSelectTv.text = item.name
            binding.contentTv.text = item.name
            binding.llUnselected.setOnClickListener {
                item.changeStatus()
                if (mEventListenter != null) {
                    mEventListenter!!.onSelectItem(
                        layoutPosition,
                        item.isSelect
                    )
                }
                if (item.isSelect) {
                    binding.llUnselected.visibility = View.GONE
                    binding.rlSelected.visibility = View.VISIBLE
                } else {
                    binding.llUnselected.visibility = View.VISIBLE
                    binding.rlSelected.visibility = View.GONE
                }
            }
            binding.rlSelected.setOnClickListener {
                item.changeStatus()
                if (mEventListenter != null) {
                    mEventListenter!!.onSelectItem(
                        layoutPosition,
                        item.isSelect
                    )
                }
                if (item.isSelect) {
                    binding.llUnselected.visibility = View.GONE
                    binding.rlSelected.visibility = View.VISIBLE
                } else {
                    binding.llUnselected.visibility = View.VISIBLE
                    binding.rlSelected.visibility = View.GONE
                }
            }
        }
    }

    inner class AddReasonVH(itemView: View, val binding: RqItemAddReportBinding) :
        RecyclerView.ViewHolder(itemView) {

        var contentNewReason = ""
        var inputNewReasonListener: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                contentNewReason = editable.toString()
                val towDpPixel = DeviceUtils.dpToPx(binding.tvSave.context, 2)
                val eightDpPixel = DeviceUtils.dpToPx(binding.tvSave.context, 8)
                if (!TextUtils.isEmpty(contentNewReason)) {
                    binding.tvSave.setBackgroundResource(R.drawable.bg_green_bottom_left_radius_5dp)
                    binding.tvSave.setTextColor(
                        binding.tvSave.resources.getColor(R.color.colorWhite)
                    )
                    binding.tvSave.setPadding(eightDpPixel, towDpPixel, eightDpPixel, towDpPixel)
                } else {
                    binding.tvSave.setBackgroundResource(
                        R.drawable.round_border_gray_bg_width_0_5dp
                    )
                    binding.tvSave.setTextColor(
                        binding.tvSave.resources.getColor(R.color.colorGrayV2)
                    )
                    binding.tvSave.setPadding(eightDpPixel, towDpPixel, eightDpPixel, towDpPixel)
                }
            }
        }

        init {
            binding.etAddTag.addTextChangedListener(inputNewReasonListener)
        }

        fun bindView(item: ReasonCancelOrderModel) {
            binding.rlSave.setOnClickListener {
                if (mEventListenter != null) {
                    if (!TextUtils.isEmpty(
                            binding.etAddTag.text.toString()
                        )
                    ) {
                        mEventListenter!!.onCreateNewReason(
                            binding.etAddTag.text.toString()
                        )
                    }
                }
            }
            if (showLoading) {
                binding.tvSave.visibility = View.INVISIBLE
            } else {
                binding.tvSave.visibility = View.VISIBLE
                binding.etAddTag.setText("")
            }
        }
    }

    interface EventListenter {
        fun onCreateNewReason(content: String)
        fun onSelectItem(position: Int, selected: Boolean)
    }
}
