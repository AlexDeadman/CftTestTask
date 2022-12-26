package ru.alexdeadman.cfttesttask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State,
    collector: FlowCollector<T>
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            collect(collector)
        }
    }
}

fun Fragment.showToast(resId: Int) {
    Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
}

fun String?.uppercaseFirstChar(): String? = this?.replaceFirstChar { it.uppercaseChar() }

fun Boolean?.toYesOrNo(): String? = this?.let { if (this) "Yes" else "No" }

fun TextView.toHyperlink(): TextView {
    setText(
        SpannableString(text).apply {
            setSpan(
                URLSpan(""),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        },
        TextView.BufferType.SPANNABLE
    )
    return this
}

fun TextView.toGeoLink(context: Context, query: String): TextView {
    toHyperlink()
    setOnClickListener {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:?q=${query}")
            )
        )
    }
    return this
}