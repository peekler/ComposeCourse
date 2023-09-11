package hu.ait.composeviewcompatibledemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import hu.ait.composeviewcompatibledemo.databinding.ActivityLauncherBinding
import hu.ait.composeviewcompatibledemo.databinding.LayoutDemoBinding
import hu.ait.composeviewcompatibledemo.ui.theme.ComposeViewCompatibleDemoTheme

class LauncherActivity : AppCompatActivity() {

    lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        binding.composeView.setContent {
            ComposeViewCompatibleDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DemoItems("Title Demo")
                }
            }
        }
    }
}


@Composable
fun DemoItems(title: String) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Hello $title!")
        Button(onClick = {
            context.startActivity(
                Intent(context,
                MainActivity::class.java)
            )
        }) {
            Text(text = "Navigate to main")
        }

        repeat(20) {
            Text(
                "Demo item $it",
                modifier = Modifier.background(
                    Color.Green,
                    RectangleShape
                )
            )
        }
    }

}