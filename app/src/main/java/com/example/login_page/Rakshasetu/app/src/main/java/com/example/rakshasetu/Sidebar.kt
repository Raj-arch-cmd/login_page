package com.example.rakshasetu

// These are the essential imports for your Sidebar component
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import com.example.rakshasetu.ui.components.sidebar.SidebarItem
import com.example.rakshasetu.ui.components.sidebar.sidebarItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    onItemSelected: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(240.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {


                Text(
                    text = "RakshaSetu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
//                Image(
//                    painter = painterResource(id = R.drawable.rakshasetu_logo),
//                    contentDescription = "App Logo",
//                    modifier = Modifier
//                        .size(70.dp)
//                )
            }


            sidebarItems.forEach { item ->
                NavigationItem(
                    item = item,
                    isSelected = selectedItem == item.route,
                    onClick = { onItemSelected(item.route) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            EmergencyAlertSection()
        }
    }
}

@Composable
fun NavigationItem(
    item: SidebarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    val iconColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Display either vector icon or drawable resource icon
        when {
            item.iconVector != null -> {
                Icon(
                    imageVector = item.iconVector,
                    contentDescription = item.title,
                    tint = iconColor
                )
            }
            item.iconResId != null -> {
                Icon(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = item.title,
                    tint = iconColor
                )
            }
        }

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )

        if (item.hasAlert) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            )
        }
    }
}
@Composable
fun EmergencyAlertSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "2.8 magnitude strikes Delhi",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            // Add other emergency information here
            Text(
                text = "5 High Risk Zones",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Fall Back To Safe Zone",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SidebarPreview() {
    MaterialTheme {
        Sidebar(
            selectedItem = "Dashboard",
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 240, heightDp = 800)
@Composable
fun SidebarFullPreview() {
    MaterialTheme {
        Sidebar(
            selectedItem = "Alerts",
            onItemSelected = {}
        )
    }
}