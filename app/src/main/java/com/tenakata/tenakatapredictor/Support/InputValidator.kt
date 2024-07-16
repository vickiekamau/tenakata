package com.tenakata.tenakatapredictor.Support


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.apache.commons.text.WordUtils



class InputValidator {
    private val REQUIRED = "Field is Required!"
    private val INVALID_DATE = "Date NOT Valid"



    fun validateRequired(text: TextInputEditText): Boolean {
        return if (fieldNotBlank(text)) {
            true
        } else {
            setErrorWatcher(text, text)
            showError(text, REQUIRED)
            false
        }
    }

    fun validateTextRequired(text: AutoCompleteTextView): Boolean {
        return if (textNotBlank(text)) {
            true
        } else {
            setTextErrorWatcher(text, text)
            showError(text, REQUIRED)
            false
        }
    }


    fun validateRequired(layout: TextInputLayout?, text: TextInputEditText): Boolean {
        return if (fieldNotBlank(text)) {
            true
        } else {
            if (layout != null) {
                showError(layout, REQUIRED)
                setErrorWatcher(text, layout)
            } else {
                showError(text, REQUIRED)
                setErrorWatcher(text, text)
            }
            false
        }
    }

    fun validateRequired(layout: TextInputLayout?, text : AutoCompleteTextView): Boolean {
        return if (textNotBlank(text)) {
            true
        } else {
            if (layout != null) {
                showError(layout, REQUIRED)
                setTextErrorWatcher(text, layout)
            } else {
                showError(text, REQUIRED)
                setTextErrorWatcher(text, text)
            }
            false
        }
    }

    fun validateRequired(editText: TextInputEditText, type: String?, errorView: TextView): Boolean {
        return if (fieldNotBlank(editText)) {
            true
        } else {
            showError(errorView, REQUIRED.replace("Field", WordUtils.capitalize(type)))
            setErrorWatcher(editText, errorView)
            false
        }
    }






    private fun fieldNotBlank(text: TextInputEditText): Boolean {
        return text.text != null && !text.text.toString().isEmpty()
    }
    private fun textNotBlank(text: AutoCompleteTextView): Boolean {
        return text.text != null && !text.text.toString().isEmpty()
    }


    private fun clearError(view: View?) {
        if (view != null) {
            if (view is TextInputLayout) {
                view.error = null
            } else if (view is TextInputEditText) {
                view.error = null
            } else if (view is TextView) {
                view.error = null
                view.setVisibility(View.GONE)
            }
        }
    }

    private fun showError(view: View, error: String) {
        view.requestFocus()
        if (view is TextInputLayout) {
            view.error = error
        } else if (view is TextInputEditText) {
            view.error = error
        } else if (view is TextView) {
            view.setVisibility(View.VISIBLE)
            view.error = ""
            view.text = error
        }
    }

    private fun setErrorWatcher(input: TextInputEditText, error: View?) {
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                clearError(error)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    private fun setTextErrorWatcher(text: AutoCompleteTextView,error: View?){
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                clearError(error)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


}