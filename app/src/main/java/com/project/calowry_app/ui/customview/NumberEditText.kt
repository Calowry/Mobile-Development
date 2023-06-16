package com.project.calowry_app.ui.customview

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.project.calowry_app.R

class NumberEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // Set input type to numeric
        inputType = InputType.TYPE_CLASS_NUMBER

        // Set digits key listener to restrict input to numeric values
        keyListener = DigitsKeyListener.getInstance("0123456789")

        // Add input filter to restrict input to numeric values
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]+"))) {
                source
            } else {
                ""
            }
        }
        filters = arrayOf(inputFilter)

        // Add the onTextChanged listener
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val number = s.toString()
                if (number.isBlank()) {
                    error = context.getString(R.string.not_filled)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed
            }
        })
    }
}