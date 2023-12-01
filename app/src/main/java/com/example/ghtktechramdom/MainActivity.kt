package com.example.ghtktechramdom

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import com.example.ghtktechramdom.databinding.MainactivityBinding
import com.example.ghtktechramdom.ui.theme.GhtkTechRamdomTheme
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import java.util.Locale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    var binding: MainactivityBinding? = null
    var adapter: CustomAdapter? = null
    var mList: MutableList<ItemsViewModel> = mutableListOf(ItemsViewModel("", ""))
    var mListDefault: MutableList<String> = mutableListOf(
        "VinhLK", "Sơn Sói", "Hưng Đen", "Hà Thắng", "Hùng Gay", "Cườngtv",
        "ThànhDH", "TrongPV", "LinhHV", "Nam", "Luyện", "Sơn Vũ", "Hiếu béo",
        "Tiến", "TrungLX", "Hiếu Sale", "Kiên Hồ", "TrườngPQ", "TrungLN", "Anh Đức", "Trung Sale"
    )
    var mListTag: MutableList<ReasonCancelOrderModel> = mutableListOf()
    private var reasonCancelOrderAdapter: ReasonCancelOrderAdapter? = null

    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
        binding = DataBindingUtil.setContentView(this, R.layout.mainactivity)
        adapter = CustomAdapter(mList)
        mListTag.clear()
        mListDefault.forEachIndexed { index, s ->
            mListTag.add(ReasonCancelOrderModel(index, s, ReasonCancelOrderModel.TYPE_NORMAL))
        }
        mListTag.add(ReasonCancelOrderModel(0, "", ReasonCancelOrderModel.TYPE_ADD_ITEM))
        binding?.let {
            it.listV.adapter = adapter
            it.btnStart.setOnClickListener {
                start()
            }
            it.btnAdd.setOnClickListener { view ->
                mList.add(ItemsViewModel("", ""))
                Log.d("tessss", "onCreate: " + mList)
                adapter?.notifyDataSetChanged()
                it.listV?.run {
                    it.listV.scrollToPosition(mList.size - 1)
                }
            }
            it.btnRefresh.setOnClickListener {
                count = 0
                mList.clear()
                mList.add(ItemsViewModel("", ""))
                adapter?.notifyDataSetChanged()
                binding?.tvResult?.text = ""
                binding?.titleRe?.text =
                    "Kết quả"
                mListTag.clear()
                mListDefault.forEachIndexed { index, s ->
                    mListTag.add(
                        ReasonCancelOrderModel(
                            index,
                            s,
                            ReasonCancelOrderModel.TYPE_NORMAL
                        )
                    )
                }
                mListTag.add(ReasonCancelOrderModel(0, "", ReasonCancelOrderModel.TYPE_ADD_ITEM))
                reasonCancelOrderAdapter?.notifyDataSetChanged()
            }

            it.rcvReason.setHasFixedSize(true)
            it.rcvReason.clipToPadding = false
            val layoutManager = FlexboxLayoutManager(applicationContext)
            layoutManager.flexWrap = FlexWrap.WRAP
            layoutManager.flexDirection = FlexDirection.ROW
            it.rcvReason.layoutManager = layoutManager
            reasonCancelOrderAdapter = ReasonCancelOrderAdapter(mListTag)
            reasonCancelOrderAdapter!!.setEventListenter(object :
                ReasonCancelOrderAdapter.EventListenter {

                override fun onCreateNewReason(content: String) {
                    var isExisted = false
                    for (ReasonCancelOrderModel in mListTag) {
                        if (ReasonCancelOrderModel.name.trim().lowercase(Locale.getDefault()) ==
                            content.trim().lowercase(Locale.getDefault())
                        ) {
                            isExisted = true
                        }
                    }
                    if (isExisted) {
                        showErrorPopup("Đã tồn tại tên này : $content")
                    } else {
                        addReasonAndSelect(
                            ReasonCancelOrderModel(
                                0,
                                content,
                                ReasonCancelOrderModel.TYPE_NORMAL
                            ),
                            mListTag
                        )
                    }
                }

                override fun onSelectItem(position: Int, selected: Boolean) {
                    val reasonSelected: ReasonCancelOrderModel = mListTag[position]
                    if (selected) {
                        updateView(reasonSelected.name)
                    }
                    reasonCancelOrderAdapter?.notifyItemChanged(position)
                }
            })
            it.rcvReason.invalidate()
            it.rcvReason.adapter = reasonCancelOrderAdapter
        }


    }

    private fun updateView(name: String) {
        var isAdded = false
        mList.forEach {
            if (it.v1.isNullOrEmpty()) {
                it.v1 = name
                isAdded = true
            } else if (it.v2.isNullOrEmpty()) {
                it.v2 = name
                isAdded = true
            }
        }
        if (!isAdded) {
            mList.add(ItemsViewModel(name, ""))

        }
        adapter?.notifyDataSetChanged()
        binding?.listV?.run {
            binding?.listV?.scrollToPosition(mList.size - 1)
        }
    }

    fun updateItem(position: Int) {
        if (reasonCancelOrderAdapter != null) {
            reasonCancelOrderAdapter!!.notifyDataSetChanged()
            //            reasonCancelOrderAdapter.notifyItemChanged(position);
        }
    }


    fun addReasonAndSelect(
        reasonCancelOrderModel: ReasonCancelOrderModel,
        listReason: MutableList<ReasonCancelOrderModel>
    ) {
        if (listReason.size >= 1) {
            val positionAdd: Int = listReason.size - 1
            for (obj in listReason) {
                if (obj.actionType != ReasonCancelOrderModel.TYPE_ADD_ITEM) {
                    obj.setActionType(ReasonCancelOrderModel.TYPE_NORMAL)
                }
            }
//            reasonCancelOrderModel.actionType = ReasonCancelOrderModel.TYPE_SELECTED
            listReason.add(positionAdd, reasonCancelOrderModel)
            reasonCancelOrderAdapter!!.notifyItemInserted(positionAdd)
            val itemCreate: ReasonCancelOrderModel = listReason.get(listReason.size - 1)
            if (itemCreate.actionType == ReasonCancelOrderModel.TYPE_ADD_ITEM) {
                itemCreate.name = ""
                reasonCancelOrderAdapter!!.notifyItemChanged(listReason.size - 1)
            }
        }
    }

    private fun start() {
        count++
        var list1: MutableList<String> = mutableListOf()
        var list2: MutableList<String> = mutableListOf()
        mList.forEach {
            if (!it.v1.isNullOrEmpty() || !it.v2.isNullOrEmpty()) {
                var ramdom = Random.nextInt(2)
                Log.d("tesssss", "start: " + ramdom)
                if (ramdom == 1) {
                    list1.add(it.v1)
                    list2.add(it.v2)
                } else {
                    list1.add(it.v2)
                    list2.add(it.v1)
                }
            }
        }
        binding?.tvResult?.text =
            "Đội 1: ${TextUtils.join(", ", list1)} \nĐội 2: ${TextUtils.join(", ", list2)}"
        binding?.titleRe?.text =
            "Kết quả quay thưởng lần thứ: $count"
    }

    fun showErrorPopup(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val ret = super.dispatchTouchEvent(event)
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                outRect.right = outRect.right + 100
                outRect.top = outRect.top - 100
                outRect.bottom = outRect.bottom + 100
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.postDelayed(Runnable {
                        v.clearFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }, 50)
                }
            }
        }
        return ret
    }
}
