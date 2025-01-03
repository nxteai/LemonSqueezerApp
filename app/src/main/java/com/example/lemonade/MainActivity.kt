package com.example.lemonade

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge layout (removes padding at the edges)
        setContent {
            LemonadeTheme {
                // Set the main content of the app with a Scaffold
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    LemonadeApp() // Display the LemonadeApp composable
                }
            }
        }
    }
}

@Composable
fun LemonadeApp() {
    // State to track the current step of the app (1: Lemon tree, 2: Lemon, 3: Lemonade, 4: Empty glass)
    val currentStep = remember { mutableIntStateOf(1) } // Use mutableIntStateOf for currentStep as it's an Int
    // State to track how many times the user has tapped to squeeze the lemon
    val currentTaps = remember { mutableIntStateOf(0) } // Use mutableIntStateOf for Int state
    // Randomize the total number of taps needed to squeeze the lemon (between 2 and 4)
    val totalTaps = remember { mutableIntStateOf((2..4).random()) } // Use mutableIntStateOf for Int state

    // Handle image click for progressing through the app
    fun onImageClick() {
        // Check which step the app is currently on and handle user interaction accordingly
        when (currentStep.intValue) {
            1 -> { // Tap on the lemon tree to move to the next step
                currentStep.intValue = 2
            }
            2 -> { // Tap on the lemon to squeeze
                currentTaps.value += 1
                // Once the required number of taps is reached, move to the lemonade step
                if (currentTaps.intValue >= totalTaps.intValue) {
                    currentStep.intValue = 3
                }
            }
            3 -> { // Tap on the lemonade glass to finish
                currentStep.intValue = 4
            }
            4 -> { // Tap on the empty glass to restart the game
                currentStep.intValue = 1
                currentTaps.intValue = 0
                totalTaps.intValue = (2..4).random() // Reset the number of taps required
            }
        }
    }

    // The Box holds the entire content and listens for tap events
    Box(
        modifier = Modifier
            .fillMaxSize() // Fill the entire screen
            .padding(16.dp) // Add padding around the content
            .wrapContentSize(Alignment.Center) // Center the content
            .clickable { onImageClick() } // Make the entire Box clickable to trigger the step change
    ) {
        // Display content based on the current step
        when (currentStep.intValue) {
            1 -> LemonadeStep(
                imageRes = R.drawable.lemon_tree, // Display the lemon tree image
                textRes = R.string.tap_lemon_tree, // Display the text for the lemon tree
                contentDescriptionRes = R.string.content_desc_lemon_tree // Content description for accessibility
            )
            2 -> LemonadeStep(
                imageRes = R.drawable.lemon_squeeze, // Display the lemon squeezing image
                textRes = R.string.squeeze_lemon, // Display the squeeze text
                remainingTapsText = "${totalTaps.intValue - currentTaps.intValue} taps left", // Show remaining taps
                contentDescriptionRes = R.string.content_desc_lemon // Content description for accessibility
            )
            3 -> LemonadeStep(
                imageRes = R.drawable.lemon_drink, // Display the lemonade drink image
                textRes = R.string.drink_lemonade, // Display the drink lemonade text
                contentDescriptionRes = R.string.content_desc_lemonade // Content description for accessibility
            )
            4 -> LemonadeStep(
                imageRes = R.drawable.lemon_restart, // Display the empty glass image for restarting
                textRes = R.string.restart_glass, // Display the restart text
                contentDescriptionRes = R.string.content_desc_empty_glass // Content description for accessibility
            )
        }
    }
}

@Composable
fun LemonadeStep(
    imageRes: Int, // The resource ID for the image to display
    textRes: Int, // The resource ID for the text to display
    remainingTapsText: String = "", // Renamed to indicate it's specific to step 2 (remaining taps)
    contentDescriptionRes: Int // The resource ID for the content description
) {
    // Column to stack the image and text vertically
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Center align horizontally
        verticalArrangement = Arrangement.spacedBy(16.dp) // Add spacing between elements
    ) {
        // Image display with circular shape, border, and size adjustments
        Box(
            contentAlignment = Alignment.Center // Center the content (image) inside the box
        ) {
            // Green circle border around the image
            Box(
                modifier = Modifier
                    .size(180.dp) // The green circle size, slightly larger than the image to create a border effect
                    .clip(CircleShape) // Clip to a circular shape
                    .border(2.dp, Color.Green, CircleShape) // Green border around the image
            )
            // Actual image inside the circle
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(id = contentDescriptionRes), // For accessibility
                modifier = Modifier
                    .size(160.dp) // Size of the image itself, matching the image size
                    .clip(CircleShape) // Clip the image to a circular shape
            )
        }
        // Main text for the step (e.g., tap instructions)
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold, // Bold the text
                textAlign = TextAlign.Center // Center the text
            ),
            color = Color(0xFF4CAF50) // Set the text color to green
        )
        // Optionally show the remaining taps text (specific to step 2)
        if (remainingTapsText.isNotEmpty()) {
            Text(
                text = remainingTapsText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                color = Color(0xFF4CAF50) // Set the remaining taps text color to green
            )
        }
    }
}


// Preview of the app, useful for showing the UI in Android Studio
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LemonadeTheme {
        LemonadeApp() // Preview the LemonadeApp composable
    }
}
