package com.github.stephanenicolas.heatcontrol.features.setting.ui

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.EditText
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R
import com.github.stephanenicolas.heatcontrol.features.control.state.Host
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingState
import com.github.stephanenicolas.heatcontrol.features.control.state.SettingStore
import com.github.stephanenicolas.heatcontrol.features.control.usecases.SettingController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsView : FrameLayout {

    @Inject
    lateinit var settingStore: SettingStore
    @Inject
    lateinit var settingController: SettingController

    @BindView(R.id.edit_text_api_key_value)
    lateinit var apiKeyView: EditText
    @BindView(R.id.recycler_hosts)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.reset_button)
    lateinit var resetButton: Button
    @BindView(R.id.save_button)
    lateinit var saveButton: Button

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.settings_view, this)
        ButterKnife.bind(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        settingStore.getStateObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateState)
        apiKeyView.setOnFocusChangeListener { v, hasFocus -> if(!hasFocus) settingController.setApiKey(apiKeyView.text.toString()) }
        resetButton.setOnClickListener { settingController.reset() }
        saveButton.setOnClickListener { settingController.save(settingStore.getState()) }
    }

    private fun updateState(state: SettingState) {
        apiKeyView.setText(state.key)
        recyclerView.adapter = HostListAdapter(state.hostList, settingController)
    }
}


class HostListAdapter(var hosts: List<Host>, var settingController: SettingController) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HOST: Int = 0
    val TYPE_ADD: Int = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position < hosts.size) {
            val hostViewViewHolder: HostViewViewHolder = holder as HostViewViewHolder
            val host: Host = hosts[position]
            hostViewViewHolder.hostView.setHost(host)
            hostViewViewHolder.hostView.setSelectCallback(OnCheckedChangeListener { buttonView, isChecked -> settingController.setlectHost(position) })
            hostViewViewHolder.hostView.setRenameCallBack(View.OnFocusChangeListener {
                view, hasFocus ->
                val name = hostViewViewHolder.hostView.getName()
                if (!hasFocus) settingController.renameHost(position, name)
            })
            hostViewViewHolder.hostView.setChangeAddressCallBack(View.OnFocusChangeListener {
                view, hasFocus ->
                val address = hostViewViewHolder.hostView.getAddress()
                if (!hasFocus) settingController.changeAddressHost(position, address)
            })
            hostViewViewHolder.hostView.setDeleteCallback(View.OnClickListener { settingController.removeHost(position) })
        } else {
            val addNewHostViewHolder: AddNewHostViewHolder = holder as AddNewHostViewHolder
            addNewHostViewHolder.button.setOnClickListener { settingController.addHost() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val context: Context = parent!!.context
        when (viewType) {
            TYPE_HOST -> {
                return HostViewViewHolder(context)
            }
            else -> {
                return AddNewHostViewHolder(context)
            }
        }
    }

    override fun getItemCount(): Int {
        return hosts.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == hosts.size) {
            return TYPE_ADD
        } else {
            return TYPE_HOST
        }
    }
}


