package com.example.supermarket.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 9) text.text.substring(0..8) else text.text
        val annotatedString = AnnotatedString.Builder().apply {
            for (i in trimmed.indices) {
                if (i == 0) append("(")
                append(trimmed[i])
                if (i == 1) append(") ")
                if (i == 4) append(" ")
                if (i == 6) append(" ")
            }
        }.toAnnotatedString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 1) return offset + 1
                if (offset <= 4) return offset + 3
                if (offset <= 6) return offset + 4
                if (offset <= 8) return offset + 5
                return 14
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return 0
                if (offset <= 3) return offset - 1
                if (offset <= 5) return 2
                if (offset <= 8) return offset - 3
                if (offset <= 9) return 5
                if (offset <= 11) return offset - 4
                if (offset <= 12) return 7
                if (offset <= 14) return offset - 5
                return 9
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}