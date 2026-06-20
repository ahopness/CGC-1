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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import androidx.fragment.app.Fragment
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(game: Game){
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

    var quitRequested by remember { mutableStateOf(false) }
    BackHandler(enabled = true) { quitRequested = !quitRequested }
    if (quitRequested) {
        AlertDialog(
            containerColor = Color.Black,
            icon = {
                Image(
                    painter = painterResource(R.drawable.icon_quit),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            },
            title = { Text("Are you sure you want to quit?") },
            text = { Text("If you quit the app all your progress will be lost.") },
            onDismissRequest = { quitRequested = false },
            dismissButton = {
                Button(onClick = { quitRequested = false }) { Text("Dismiss") }
            },
            confirmButton = {
                val activity = LocalActivity.current as MainActivity
                OutlinedButton(onClick = { activity.finish(); exitProcess(0) }) { Text("Confirm") }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
//            modifier = Modifier.width(480.dp)
        )
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