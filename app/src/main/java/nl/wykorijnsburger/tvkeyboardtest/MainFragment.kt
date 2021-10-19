package nl.wykorijnsburger.tvkeyboardtest

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import logcat.LogPriority
import logcat.LogPriority.*
import logcat.logcat
import nl.wykorijnsburger.tvkeyboardtest.databinding.FragmentMainBinding

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)

        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            logcat(INFO) { "Keyboard: Received new window insets $insets" }

            val keyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            binding.windowInsetsStatus.text = "On Apply Window Insets listener:\nKeyboard visible: $keyboardVisible\nKeyboard height: $keyboardHeight"

            insets
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            logcat(INFO) { "Keyboard: onGlobalLayout" }
            legacyCalculation(rootView, binding)

            val insets = ViewCompat.getRootWindowInsets(rootView)
            val keyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            val keyboardHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

            binding.globalLayoutListenerStatus.text = "Using Window Insets on Global Layout:\nKeyboard visible: $keyboardVisible\nKeyboard height: $keyboardHeight"
        }
    }

    private fun legacyCalculation(rootView: View, binding: FragmentMainBinding) {
        val rect = Rect().apply { rootView.getWindowVisibleDisplayFrame(this) }
        val rootViewBottom = rect.bottom
        val screenHeight = rootView.height

        val keyboardHeight = screenHeight - rootViewBottom
        val keyboardVisible = keyboardHeight > screenHeight * KEYPAD_HEIGHT_RATIO

        binding.legacyStatus.text = "Using visibleDisplayFrame on Global Layout:\nKeyboard visible: $keyboardVisible\nKeyboard height: $keyboardHeight"
    }

    companion object {
        const val KEYPAD_HEIGHT_RATIO = 0.15f
    }
}
