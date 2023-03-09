<h1> ML Kit Android Apps </h1>
<h2> <a href = "https://www.udemy.com/course/mlkit-android-programlama/">❤ UDEMY COURSE ❤</a> </h2>

```
git clone https://github.com/icanerdogan/MLKit-Android-Apps
```

<!------------------------------------------------------------------------------------------------------------------------------------------------------------->
<h2> Text Recognition App </h2>

<h4><a href="https://medium.com/@ibrahimcanerdogan/text-recognition-app-with-mlkit-android-7d0b29f522cd" target="_blank"> MEDIUM </a></h4>

<p> The ML Kit Text Recognition API can recognize text in any Latin-based character set. It can also be used to automate data-entry tasks such as processing credit cards, receipts, and business cards. </p>

<h3> Key capabilities </h3>
<ul>
<li>Recognize text across Latin-based languages Supports recognizing text using Latin script</li>
<li>Analyze structure of text Supports detection of words/elements, lines and paragraphs</li>
<li>Identify language of text Identifies the language of the recognized text</li>
<li>Small application footprint On Android, the API is offered as an unbundled library through Google Play Services</li>
<li>Real-time recognition Can recognize text in real-time on a wide range of devices</li>
</ul>

<h3> Text structure </h3>
<p>The Text Recognizer segments text into blocks, lines, elements and symbols. Roughly speaking:</p>
<ul>
<li>a Block is a contiguous set of text lines, such as a paragraph or column</li>
<li>a Line is a contiguous set of words on the same axis, and</li>
<li>an Element is a contiguous set of alphanumeric characters ("word") on the same axis in most Latin languages, or a word in others</li>
<li>an Symbol is a single alphanumeric character on the same axis in most Latin languages, or a character in others</li>
</ul>

<p> The image below highlights examples of each of these in descending order. The first highlighted block, in cyan, is a Block of text. The second set of highlighted blocks, in blue, are Lines of text. Finally, the third set of highlighted blocks, in dark blue, are Words. </p>

<img src="https://developers.google.com/static/ml-kit/vision/text-recognition/images/text-structure.png">

<p> For all detected blocks, lines, elements and symbols, the API returns the bounding boxes, corner points, rotation information, confidence score, recognized languages and recognized text. </p>

<h3> Text Recognition App Preview </h3>

App        |  Main Screen
:-------------------------:|:-------------------------:
![](https://github.com/icanerdogan/TextRecognitionApp-MLKit/blob/master/documents/TextRecognition.gif?raw=true)  |  ![](https://raw.githubusercontent.com/icanerdogan/TextRecognitionApp-MLKit/master/documents/MainActivity.png)

<!------------------------------------------------------------------------------------------------------------------------------------------------------------->

<h2> Face Detection App </h2>

<b><u><a href="https://medium.com/@ibrahimcanerdogan/face-detection-app-with-mlkit-android-696ce42d4be4" target="_blank"> MEDIUM </a></u></b>

<p> With ML Kit's face detection API, you can detect faces in an image, identify key facial features, and get the contours of detected faces. Note that the API detects faces, it does not recognize people .

With face detection, you can get the information you need to perform tasks like embellishing selfies and portraits, or generating avatars from a user's photo. Because ML Kit can perform face detection in real time, you can use it in applications like video chat or games that respond to the player's expressions. </p>

<h3> Key capabilities </h3>
<ul>
<li>Recognize and locate facial features Get the coordinates of the eyes, ears, cheeks, nose, and mouth of every face detected.</li>
<li>Get the contours of facial features Get the contours of detected faces and their eyes, eyebrows, lips, and nose.</li>
<li>Recognize facial expressions Determine whether a person is smiling or has their eyes closed.</li>
<li>Track faces across video frames Get an identifier for each unique detected face. The identifier is consistent across invocations, so you can perform image manipulation on a particular person in a video stream.</li>
<li>Process video frames in real time Face detection is performed on the device, and is fast enough to be used in real-time applications, such as video manipulation.</li>
</ul>

<img src="https://raw.githubusercontent.com/icanerdogan/FaceDetectionApp-MLKit/master/documents/Face%20Detect.PNG">

<h3> Face detection concepts </h3>

<p> Face detection locates human faces in visual media such as digital images or video. When a face is detected it has an associated position, size, and orientation; and it can be searched for landmarks such as the eyes and nose.

Here are some of the terms that we use regarding the face detection feature of ML Kit: </p>

<ul>
<li>Face tracking extends face detection to video sequences. Any face that appears in a video for any length of time can be tracked from frame to frame. This means a face detected in consecutive video frames can be identified as being the same person. Note that this isn't a form of face recognition; face tracking only makes inferences based on the position and motion of the faces in a video sequence.</li>
<li>A landmark is a point of interest within a face. The left eye, right eye, and base of the nose are all examples of landmarks. ML Kit provides the ability to find landmarks on a detected face.</li>
<li>A contour is a set of points that follow the shape of a facial feature. ML Kit provides the ability to find the contours of a face.</li>
<li>Classification determines whether a certain facial characteristic is present. For example, a face can be classified by whether its eyes are open or closed, or if the face is smiling or not.</li>
</ul>

<h3> Face Detection App Preview </h3>

Front Camera       |  Rear Camera
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/FaceDetectionApp-MLKit/master/documents/Front%20Camera.png)  |  ![](https://raw.githubusercontent.com/icanerdogan/FaceDetectionApp-MLKit/master/documents/Rear%20Camera.png)


<!------------------------------------------------------------------------------------------------------------------------------------------------------------->

<h2>  <a href = "https://github.com/icanerdogan/MLKit-Android-Apps/tree/master/PoseDetection" > Pose Detection App</a> </h2>
<p> The ML Kit Pose Detection API is a lightweight versatile solution for app developers to detect the pose of a subject's body in real time from a continuous video or static image. A pose describes the body's position at one moment in time with a set of x,y skeletal landmark points. The landmarks correspond to different body parts such as the shoulders and hips. The relative positions of landmarks can be used to distinguish one pose from another. </p>

Main Activity 1         |  Main Activity 2
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/PoseDetectionApp1.jpg)  |  ![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/PoseDetectionApp2.jpg)

<h2> <a href="https://github.com/icanerdogan/MLKit-Android-Apps/tree/master/BarcodeScanner"> Barcode Scanning App </a></h2>
<p>With ML Kit's barcode scanning API, you can read data encoded using most standard barcode formats. Barcode scanning happens on the device, and doesn't require a network connection.

Barcodes are a convenient way to pass information from the real world to your app. In particular, when using 2D formats such as QR code, you can encode structured data such as contact information or WiFi network credentials. Because ML Kit can automatically recognize and parse this data, your app can respond intelligently when a user scans a barcode.  </p>
Interface View        |  Get Information From Barcode
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/BarcodeScannerApp1.jpg)  |  ![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/BarcodeScannerApp2.jpg)


<h2> <a href="https://github.com/icanerdogan/MLKit-Android-Apps/tree/master/ImageLabeler"> Image Labeling App </a></h2>

<p>With ML Kit's image labeling APIs you can detect and extract information about entities in an image across a broad group of categories. The default image labeling model can identify general objects, places, activities, animal species, products, and more. </p>

Interface View        |  Prediction
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/ImageLabelerApp1.jpg)  |  ![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/ImageLabelerApp2.jpg)


<h2> <a href="https://github.com/icanerdogan/MLKit-Android-Apps/tree/master/ObjectDetectTracking"> Object Detection and Tracking App </a></h2>

<p>With ML Kit's on-device Object Detection and Tracking API, you can detect and track objects in an image or live camera feed. </p>

Object Detection 1       |  Object Detection 2
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/ObjectDetectTrackingApp1.jpg)  |  ![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/ObjectDetectTrackingApp2.jpg)


<h2> <a href="https://github.com/icanerdogan/MLKit-Android-Apps/tree/master/DigitalInkRecognition"> Digital Ink Recognition App </a></h2>

<p>With ML Kit's Digital Ink Recognition API, you can recognize handwritten text on a digital surface in hundreds of languages, as well as classify sketches. The Digital Ink Recognition API uses the same technology that powers handwriting recognition in Gboard, Google Translate, and the Quick, Draw! game.

Digital Ink Recognition makes it possible to write on the screen instead of typing on a virtual keyboard. This lets users draw characters that are not available on their keyboard, such as ệ, अ or 森 for latin alphabet keyboards. The API can also transcribe handwritten notes and recognize hand‑drawn shapes and emojis.</p>

Digital Ink Recognition 1       |  Digital Ink Recognition 2
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/DigitalInkRecognitionApp1.jpg)  |  ![](https://raw.githubusercontent.com/icanerdogan/MLKit-Android-Apps/master/App%20Images/DigitalInkRecognitionApp2.jpg)

<h4> <b> Follow me on Github :) </b> </h4>
