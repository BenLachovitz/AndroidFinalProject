## **Android development course final project**
The topic I chose is car insurance management app.

**I used Firebase at the app to save the user:**

 - There is authentication usage for login and signup.
 - There is a use of Realtime database.
 - Also I used firebase Storage to save photos.

I used a Singleton class for Toast messages, Vibrator for vibration and Image loader using **glide**. The reason is to keep the application context as a parameter and to use the toast, vibrate and glide with 1 context. 

## **#1 The opening animation:**
At first, there is an animation activity using Lottie.

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/4dbe81b7-e3d4-45bb-bbd9-858eb2bead35" width=150px height=300px>

## **#2 The login/register activity:**
Here I used the Firebase Auth UI, It's possible to sign in or register via Email / Gmail / Phone.

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/e286c07d-e69b-4db5-bfd8-fbd9163244d6" width=150px height=300px>

## **#3 The profile edit activity:**
Here it's possible to update the car details and to save the new data to the Firebase Realtime Database.
If it's A new user this activity will appear at first, otherwise it is possible to get here thought the menu at the main activity.

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/ea33c13e-0326-4d0a-bff3-26b0b34f3753" width=150px height=300px>

## **#4 The main activity:**  
From the main activity ,first there is the menu that from there it's possible to go to the profile edit activity, like I explained at **#3** or to logout from the user.
On the screen, the user information will be shown, that data is received from the Realtime Database. 

**The data Is:** 
 - Name of the user
 - License plat of the car
 - Car model
 - Year of the production

Under the data there are 4 buttons for each activity:
 1. S.O.S, is to upload a claim at a crash situation.
 2. Drag Rescue, is to send a request for drag to rescue the car in case of a car problem and the car is stacked.
 3. Policy Details, is to management and track after the car insurance policy. 
 4. Tracking, is an history activity of the user claims.

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/b250aa4d-ecb5-433b-aa7f-06f2c110589d" width=150px height=300px>

## **#5 Policy details activity:**
At this activity, it's possible to upload the policy photo, to achieve that I used both Realtime Database and Firebase Storage.
Also it's possible to update the registered date using Google's date picker and to update the price of the insurance for tracking purposes. The save button will save the date and price to the database, after the save is pressed the "good until date" will be updated as well automatically to one year forward. The upload photo button will open the gallery for pick the image. After the image is uploaded, it will appear at the center using glide. 

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/4032d5f9-4430-47dd-8916-ac9da7f84153" width=150px height=300px>

## **#6 Drag rescue activity:**
At this activity, I implemented the Google maps at the bottom fragment.
At the upper fragment it's possible to take the user current location by pressing the "set current location" button and the  location till appear as text underneath. Moreover when the set current location button is pressed a marker at the map will appear and a zoom animation to the location also.
The second button, "Send Request" will trigger a demo feature that demonstrate the drag arrival time using a progress bar, the progress bar will progress via countdown timer.

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/32b9e3e7-011c-48c5-937d-b0bb44c7ba8c" width=150px height=300px>

## **#7 Tracking activity:**

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/31a52c41-f2ba-4143-ab7e-7cb454a27330" width=150px height=300px>

## **#8 S.O.S activity:**

<img src="https://github.com/BenLachovitz/AndroidFinalProject/assets/127788828/178dd0c0-8c1b-4ff2-9a77-29f62f721732" width=150px height=300px>
