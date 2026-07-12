package com.olezhaku.idsc.ui.widgets

import android.content.Context
import android.widget.Toast
import com.olezhaku.idsc.utils.NoRootError
import com.olezhaku.idsc.utils.TimeoutError


suspend fun Context.runWithToast(
    loadingMessage: String,
    successMessage: String,
    errorMessage: String = "Operation failed.",
    block: suspend () -> Unit
) {
    Toast.makeText(this, loadingMessage, Toast.LENGTH_SHORT).show()

    try {
        block()

        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show()
    } catch (e: NoRootError) {
        Toast.makeText(
            this,
            "Root not found or access denied.",
            Toast.LENGTH_LONG
        ).show()

        throw e
    } catch (e: TimeoutError) {
        Toast.makeText(
            this,
            "Timed out.",
            Toast.LENGTH_LONG
        ).show()

        throw e
    } catch (e: Exception) {
        Toast.makeText(
            this,
            e.message ?: errorMessage,
            Toast.LENGTH_LONG
        ).show()

        throw e
    }
}