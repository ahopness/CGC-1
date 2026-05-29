package dev.lucasangelo.cgc1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import androidx.fragment.app.Fragment
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(game: Game){
    BackHandler(enabled = true) { /* consume back to prevent pop */ }
    val containerId = remember { View.generateViewId() }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { viewContext ->
            FrameLayout(viewContext).apply {
                id = containerId
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        }
    )

    val activity = LocalActivity.current
    val fragmentActivity = activity as FragmentActivity

    var startEngine by remember { mutableStateOf(false) }
    LaunchedEffect(game) {
        // wait navigation enter transition to finish before booting godot
        // animation gets janky otherwise
        delay(300L)
        startEngine = true
    }

    if (startEngine) {
        DisposableEffect(game, fragmentActivity, containerId) {
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            val godotHostFragment = GodotHostFragment(game)
            fragmentActivity.supportFragmentManager
                .beginTransaction()
                .replace(containerId, godotHostFragment)
                .commit()

            onDispose {
                fragmentActivity.supportFragmentManager.beginTransaction()
                    .remove(godotHostFragment).
                    commit()
            }
        }
    }
}

class GodotHostFragment(val game: Game) : Fragment(), GodotHost{
    override fun getCommandLine(): List<String?> = listOf(
            "--main-pack",
            "res://${game.codename}-${game.version}.pck"
        )

    var godotFragment : GodotFragment? = null
    override fun getGodot(): Godot? = godotFragment?.godot

    private var containerId: Int = View.NO_ID
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val frame = FrameLayout(requireContext())
        containerId = View.generateViewId()
        frame.id = containerId
        frame.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        return frame
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        godotFragment = GodotFragment()
        childFragmentManager
            .beginTransaction()
            .replace(containerId, godotFragment!!)
            .commit()
    }
}