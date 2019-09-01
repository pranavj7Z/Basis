# Basis-Swipeable-Cards
• Fetches data and displays as Swipeable cards.<br>
• User can swipe the card to move to next card.<br>
• User can Go back to the previous card.<br>
• Reload the cards from the beginning.<br>
• Tracks the progress of the cards by indicating the current card<br><br>

# Installation
• Get the latest version android-studio<br>
• Clone the repository in any folder.<br>
• Open the cloned project in Android studio<br>
• NOTE: The project requires internet connectivity to sync and download the necessary Components.<br>
• Run the project on successful sync to generate the APK module.<br><br>

# Steps
• The project uses the swipeable card library in the following link https://github.com/yuyakaido/CardStackView <br>
• Initialize the buttons and cards on sucessfull connectivity<br>
• On failure to connect to network display the message in a card<br>
• On succesfull connectivity fetch the JSON from the following link https://git.io/fjaqJ <br>
• Remove all the occurrences of '/' from the JSON string.<br>
• Return the list of data from the JSON response to a Custom Adapter to display them as Swipeable Cards<br>
• Implement an indicator indicating the current card using its position in the adapter.<br><br>

# Screenshot<br>
<img src="https://github.com/pranavj7Z/Basis-Swipeable-Cards/blob/master/Screenshot_2019-08-31-00-17-48-239_com.pranavjayaraj.basis%20(1).png" height="400" alt="Screenshot"/>
