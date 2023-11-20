import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.presentation.setting_screen.SettingViewModel
import com.mobile.expenseapp.presentation.ui.theme.Red500
import com.mobile.expenseapp.util.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun LanguageContent(
    modalBottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navController: NavController,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium)
    ) {
        val context = LocalContext.current

        Text(
            text = "CHOOSE LANGUAGE",
            style = MaterialTheme.typography.subtitle2,
        )

        LanguageOption("English") {
            settingViewModel.editLanguage("en")
            scope.launch {
                modalBottomSheetState.hide()
                Toast.makeText(context, "Language set to English", Toast.LENGTH_SHORT).show()
            }
        }

        LanguageOption("Tiếng Việt") {
            settingViewModel.editLanguage("vi")
            scope.launch {
                modalBottomSheetState.hide()
                Toast.makeText(context, "Ngôn ngữ đã được đặt thành Tiếng Việt", Toast.LENGTH_SHORT).show()
            }
        }

        TextButton(
            onClick = {
                scope.launch { modalBottomSheetState.hide() }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray.copy(alpha = 0.4f),
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = "CANCEL",
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun LanguageOption(
    text: String,
    onLanguageSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.primaryVariant)
            .clickable { onLanguageSelected() }
            .padding(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
        )
    }
}
