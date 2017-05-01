package com.github.stephanenicolas.heatcontrol.features.control.ui

import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.github.stephanenicolas.heatcontrol.R
import com.github.stephanenicolas.heatcontrol.features.control.state.ControlState
import com.github.stephanenicolas.heatcontrol.features.control.state.ControlStore
import com.github.stephanenicolas.heatcontrol.features.control.usecases.ControlController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ControlView : android.widget.RelativeLayout {

    @Inject
    lateinit var controlController: ControlController
    @Inject
    lateinit var controlHeatStore: ControlStore

    @BindView(R.id.text_view_ambient_temp_value)
    lateinit var ambientTempView: android.widget.TextView
    @BindView(R.id.edit_text_target_temp_value)
    lateinit var targetTempView: android.widget.EditText
    @BindView(R.id.text_view_error)
    lateinit var errorView: android.widget.TextView
    @BindView(R.id.progress_bar)
    lateinit var progressView: android.widget.ProgressBar
    @BindView(R.id.button_refresh)
    lateinit var refreshButton: android.widget.Button
    @BindView(R.id.button_submit)
    lateinit var submitButton: android.widget.Button

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    constructor(context: android.content.Context?) : this(context, null)
    constructor(context: android.content.Context?, attrs: android.util.AttributeSet?) : this(context, attrs, 0)

    constructor(context: android.content.Context?, attrs: android.util.AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        android.view.LayoutInflater.from(context).inflate(R.layout.control_heat_view, this)
        ButterKnife.bind(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        showControlState(controlHeatStore.getState())
        val disposable = controlHeatStore.getStateObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showControlState)
        subscriptions.add(disposable)

        refreshButton.setOnClickListener { this.refreshTemperatures() }
        submitButton.setOnClickListener { this.updateTargetTemperature() }
    }

    override fun onDetachedFromWindow() {
        controlController.detach()
        subscriptions.clear()
        super.onDetachedFromWindow()
    }

    private fun showControlState(controlState: ControlState) {
        if (controlState.throwable == null) {
            ambientTempView.text = "${controlState.ambiantTemp}"
            targetTempView.setText("${controlState.targetTemp}")
            errorView.visibility = android.view.View.INVISIBLE
            progressView.visibility = android.view.View.INVISIBLE
        } else {
            showError(controlState.throwable)
        }
    }

    private fun showError(throwable: Throwable?) {
        Log.e("", "Request failed", throwable)
        errorView.visibility = android.view.View.VISIBLE
        progressView.visibility = android.view.View.INVISIBLE
    }

    private fun showProgress() {
        errorView.visibility = android.view.View.INVISIBLE
        progressView.visibility = android.view.View.VISIBLE
    }


    private fun refreshTemperatures() {
        showProgress()
        controlController.refreshTemperatures()
    }

    private fun updateTargetTemperature() {
        showProgress()
        controlController.setTargetTemperature(targetTempView.text.toString())
    }
}