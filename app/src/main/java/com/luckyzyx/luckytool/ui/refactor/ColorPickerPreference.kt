@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.luckyzyx.luckytool.ui.refactor

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.SPUtils
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener

@Obfuscate
class ColorPickerPreference : Preference {

    private lateinit var colorBox: View
    private lateinit var preferenceDialog: AlertDialog
    private lateinit var preferenceColorPickerView: ColorPickerView

    var preferenceColorListener: ColorPickerViewListener? = null
    var defaultColor: Int = Color.BLACK
    var cornerRadius: Int = 0
    var paletteDrawable: Drawable? = null
    var selectorDrawable: Drawable? = null
    var dialogTitle: String? = null
    var positive: String? = "Ok"
    var neutral: String? = "Cancel"
    var attachAlphaSlideBar = true
    var attachBrightnessSlideBar = true
    var prefsName: String? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, prefsName: String) : super(context) {
        this.prefsName = prefsName
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference, defStyle, 0)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun setTypeArray(typedArray: TypedArray) {
        defaultColor =
            typedArray.getColor(R.styleable.ColorPickerPreference_defaultColor, defaultColor)
        cornerRadius = typedArray.getDimensionPixelSize(
            R.styleable.ColorPickerPreference_preference_colorBox_radius,
            cornerRadius
        )
        paletteDrawable =
            typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_palette)
        selectorDrawable =
            typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_selector)
        dialogTitle =
            typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_title)
        positive =
            typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_positive)
        neutral =
            typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_neutral)
        attachAlphaSlideBar =
            typedArray.getBoolean(
                R.styleable.ColorPickerPreference_preference_attachAlphaSlideBar,
                attachAlphaSlideBar
            )
        attachBrightnessSlideBar =
            typedArray.getBoolean(
                R.styleable.ColorPickerPreference_preference_attachBrightnessSlideBar,
                attachBrightnessSlideBar
            )
    }

    init {
        widgetLayoutResource = R.layout.layout_preference_colorpicker
    }

    private fun notifyColorChanged(envelope: ColorEnvelope) {
        preferenceColorListener?.let {
            if (it is ColorListener) {
                it.onColorSelected(envelope.color, true)
            } else if (it is ColorEnvelopeListener) {
                it.onColorSelected(envelope, true)
            }
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        preferenceDialog = ColorPickerDialog.Builder(context).apply {
            setTitle(dialogTitle)
            setPositiveButton(
                positive,
                ColorEnvelopeListener { envelope, _ ->
                    if (colorBox.background is GradientDrawable) {
                        (colorBox.background as GradientDrawable).setColor(envelope.color)
                        notifyColorChanged(envelope)
                        SPUtils.putInt(context, prefsName, key, envelope.color)
                    }
                }
            )
            setNeutralButton(neutral) { dialogInterface, _ -> dialogInterface.dismiss() }
            attachAlphaSlideBar(attachAlphaSlideBar)
            attachBrightnessSlideBar(attachBrightnessSlideBar)
            this@ColorPickerPreference.preferenceColorPickerView = this.colorPickerView.apply {
                paletteDrawable?.let { setPaletteDrawable(it) }
                selectorDrawable?.let { setSelectorDrawable(it) }
                preferenceName = key
                setInitialColor(defaultColor)
            }
        }.create()

        colorBox = holder.findViewById(R.id.preference_colorBox)
        colorBox.background = GradientDrawable().apply {
            cornerRadius = this@ColorPickerPreference.cornerRadius.toFloat()
            setColor(
                if (key == null) {
                    this@ColorPickerPreference.defaultColor
                } else {
                    SPUtils.getInt(context, prefsName, key, this@ColorPickerPreference.defaultColor)
                }
            )
        }
    }

    override fun onClick() {
        super.onClick()
        preferenceDialog.show()
    }

    /** gets an [AlertDialog] that created by preferences. */
    fun getPreferenceDialog(): AlertDialog = preferenceDialog

    /** gets a [ColorPickerView] that created by preferences. */
    fun getColorPickerView(): ColorPickerView = preferenceColorPickerView
}
