package com.example.faceplant.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.jar.Attributes

class FaceplantTextViewBold(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {

    init {
        //call the function to apply the font to components
        applyFonts()
    }

    private fun applyFonts() {
        //This is used to get the file from the assets folder and set it to the title textView
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "bold_font.ttf")
        setTypeface(typeface)
    }
}