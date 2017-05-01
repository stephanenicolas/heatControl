package com.github.stephanenicolas.heatcontrol.features.setting.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R
import com.github.stephanenicolas.heatcontrol.features.control.state.Host
import com.github.stephanenicolas.heatcontrol.features.control.state.MOCK

class HostView : FrameLayout {

    @BindView(R.id.radio_selected)
    lateinit var selectedView: RadioButton
    @BindView(R.id.edit_text_name)
    lateinit var nameView: EditText
    @BindView(R.id.edit_text_address)
    lateinit var addressView: EditText
    @BindView(R.id.delete_button)
    lateinit var deleteButton: ImageButton

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.host_view, this)
        ButterKnife.bind(this)
    }

    fun setHost(host: Host) {
        nameView.setText(host.name)
        addressView.setText(host.address)
        selectedView.isChecked = host.isSelected
        val isMock = host.name.equals(MOCK)
        nameView.isEnabled = !isMock
        addressView.isEnabled = !isMock
        deleteButton.visibility = if (isMock) View.INVISIBLE else View.VISIBLE
    }

    fun getName(): String {
        return nameView.text.toString()
    }

    fun getAddress(): String {
        return addressView.text.toString()
    }

    fun setDeleteCallback(clickListener: OnClickListener) {
        deleteButton.setOnClickListener(clickListener)
    }

    fun setSelectCallback(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener) {
        selectedView.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    fun setRenameCallBack(onFocusChangeListener: OnFocusChangeListener) {
        nameView.setOnFocusChangeListener(onFocusChangeListener)
    }

    fun setChangeAddressCallBack(onFocusChangeListener: OnFocusChangeListener) {
        addressView.setOnFocusChangeListener(onFocusChangeListener)
    }

}