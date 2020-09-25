package com.coolxtc.common.coustomview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.coolxtc.common.R

/**
 * Created by xtc on 2018/4/25.
 */
class TypeSelectorLayout : LinearLayout {
    private var mTypeList: ArrayList<TypeData> = arrayListOf()
    private var mTypeViewList: ArrayList<View> = arrayListOf()
    private var typeClickListener: TypeClickListener? = null
    private val ORDER_TYPE_ASC = "asc"
    private val ORDER_TYPE_DESC = "desc"

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        gravity = Gravity.CENTER_VERTICAL
    }

    fun init(typeList: ArrayList<TypeData>, listener: TypeClickListener) {
        mTypeViewList.clear()
        mTypeList.clear()
        mTypeList = typeList
        removeAllViews()
        mTypeList.forEach {
            val itemView = createItem(it)
            mTypeViewList.add(itemView)
        }
        this.typeClickListener = listener
    }

    private fun createItem(typeData: TypeData): View {
        val itemView = inflate(context, R.layout.layout_type_selector_item, null)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        addView(itemView, layoutParams)
        itemView.isSelected = typeData.isSelected
        itemView.findViewById<TextView>(R.id.tv_type).text = typeData.typeName
        val ivOrder = itemView.findViewById<ImageView>(R.id.iv_type)
        ivOrder.visibility = if (typeData.isNeedOrder) View.VISIBLE else View.GONE
        ivOrder.isSelected = typeData.isOrderSelected
        itemView.setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                if (!typeData.isNeedOrder && itemView.isSelected) {
                    return
                }
                if (typeData.isSelected) {
                    typeData.isOrderSelected = !typeData.isOrderSelected
                }
                mTypeList.forEach {
                    it.isSelected = false
                }
                typeData.isSelected = true
                refreshState()
                typeClickListener?.onTypeClickListener()
            }
        })
        return itemView
    }

    private fun refreshState() {
        mTypeViewList.forEachIndexed { index, view ->
            view.isSelected = mTypeList[index].isSelected
            val ivOrder = view.findViewById<ImageView>(R.id.iv_type)
            ivOrder.visibility = if (mTypeList[index].isNeedOrder) View.VISIBLE else View.GONE
            ivOrder.isSelected = mTypeList[index].isOrderSelected
        }
    }

    fun getType(): String {
        for (data in mTypeList) {
            if (data.isSelected) {
                return data.type
            }
        }
        return ""
    }

    fun getOrder(): String {
        for (data in mTypeList) {
            if (data.isSelected) {
                return if (data.isOrderSelected) {
                    ORDER_TYPE_DESC
                } else {
                    ORDER_TYPE_ASC
                }
            }
        }
        return ORDER_TYPE_DESC
    }

    interface TypeClickListener {
        fun onTypeClickListener()
    }

    class TypeData(
            var typeName: String,
            var type: String,
            var isSelected: Boolean = false,
            var isNeedOrder: Boolean = false,
            var isOrderSelected: Boolean = false,
    )
}
