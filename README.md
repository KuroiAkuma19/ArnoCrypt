ArnoCrypt🔒:
Welcome to ArnoCrypt. I built this app to bridge the gap between classic cryptography and modern mobile UX. Instead of a standard "app" feel, I went for a monochromatic, terminal-inspired aesthetic that puts the focus purely on the data.

The Logic Used
I implemented four distinct cryptographic techniques to handle different types of message security:

Additive (Caesar) Cipher: Standard shift-based logic for quick text masking.
Morse Code: A full mapping of alphanumeric characters into international Morse signals.
Rail Fence: A transposition cipher that uses a zigzag pattern across customizable "rails".
Playfair: A more advanced 5x5 matrix-based symmetric encryption that handles digraphs and security keys.

How to Use It
Enter your message in the Message box.
Input your Security Key (use a number for Rail/Additive or a word for Playfair).
Choose your cipher from the Spinner.
Hit ENCRYPT to see your code, or DECRYPT to reveal the secret.
Copy the result by long-pressing the text or hit CLEAR to start over.

Technical Setup
Language: Kotlin
Platform: Android SDK (Material Design 3)
Architecture: Empty Views Activity with responsive ConstraintLayout

App ScreenShots:
Please check the Screenshots folder added 
