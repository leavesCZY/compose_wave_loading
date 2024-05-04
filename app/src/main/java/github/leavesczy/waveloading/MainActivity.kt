package github.leavesczy.waveloading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

/**
 * @Author: leavesCZY
 * @Date: 2022/2/16 15:41
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWaveLoadingTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            WaveLoading(
                                modifier = Modifier
                                    .size(size = 220.dp),
                                text = "開",
                                textSize = 150.sp,
                                waveColor = Color(0xFF3949AB)
                            )
                            WaveLoading(
                                modifier = Modifier
                                    .size(size = 220.dp),
                                text = "心",
                                textSize = 150.sp,
                                waveColor = Color(0xFF00897B)
                            )
                            WaveLoading(
                                modifier = Modifier
                                    .size(size = 220.dp),
                                text = "最",
                                textSize = 150.sp,
                                waveColor = Color(0xFF29B6F6)
                            )
                            WaveLoading(
                                modifier = Modifier
                                    .size(size = 220.dp),
                                text = "重",
                                textSize = 150.sp,
                                waveColor = Color(0xFFFFA726)
                            )
                            WaveLoading(
                                modifier = Modifier
                                    .size(size = 220.dp),
                                text = "要",
                                textSize = 150.sp,
                                waveColor = Color(0xFFFF7043)
                            )
                        }
                    }
                }
            }
        }
    }
}