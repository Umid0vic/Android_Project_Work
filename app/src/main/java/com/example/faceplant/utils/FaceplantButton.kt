package com.example.faceplant.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class FaceplantButton (context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {

    init {
        //The function applies the font to components
        applyFonts()
    }

    private fun applyFonts() {
        //This is used to get the file from the assets folder and set it to the title textView
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "regular_font.ttf")
        setTypeface(typeface)

    }
}