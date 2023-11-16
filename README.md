<h1> ML Kit Android Apps </h1>
<h2> <a href = "https://www.udemy.com/course/mlkit-android-programlama/">❤ UDEMY COURSE ❤</a> </h2>

```
git clone https://github.com/icanerdogan/MLKit-Android-Apps
```
![ml-kit-logo (1)](https://github.com/icanerdogan/Google-MLKit-Android-Apps/assets/52867508/5e59d68f-90b9-4c98-86a9-f1aac0d259a7)


# Contents
- <b> [Text Recognition App](#text-recognition-app) </b>
- <b> [Face Detection App](#face-detection-app) </b>
- <b> [Pose Detection App](#pose-detection-app) </b>
- <b> [Selfie Segmentation App](#selfie-segmentation-app) </b>
- <b> [Barcode Scanning App](#barcode-scanning-app) </b>
- <b> [Image Labeling App](#image-labeling-app) </b>
- <b> [Digital Ink Recognition App](#digital-ink-recognition-app) </b>

<!------------------------------------------------------------------------------- TEXT RECOGNITION APP ------------------------------------------------------------------------------>
---

# Text Recognition App <a name="text-recognition-app"></a>

<p>Unleash the power of text within images like never before with [Your App Name], the ultimate Image Text Analyzer and Copy Tool! Say goodbye to manual typing and tedious data entry—our advanced AI-driven technology transforms any photo into editable and copyable text with just a tap. Whether it's a snapshot from your camera or an image from your gallery, our app makes digitizing text a breeze! </p>

<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.textrecognitionapp"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

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


<!--------------------------------------------------------------------------------- FACE DETECTION APP ---------------------------------------------------------------------------->
---
# Face Detection App <a name="face-detection-app"></a>

<p> Welcome to SnapFace: Face Finder AI, the cutting-edge application that revolutionizes face detection using the power of artificial intelligence. Seamlessly designed to work with both front and rear cameras, SnapFace intelligently identifies and locates faces with unparalleled precision, making every snapshot a memorable experience. </p>

<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.facedetectionapp"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

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


<!--------------------------------------------------------------------------------- POSE DETECTION APP ---------------------------------------------------------------------------->
---
<details open>
  <summary><h2><a name="pose-detection-app"> Pose Detection App </a></h2></summary>
<p>Welcome to PoseSnap, your ultimate tool for achieving a perfect posture and a healthier you! Our cutting-edge Posture Detect AI technology brings advanced pose analysis right to your fingertips. With PoseSnap, you can effortlessly assess your body posture from any image in your gallery, gain valuable insights about body angles, and visualize your progress towards a confident and aligned stance.</p>

<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.posedetectionapp"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p> The ML Kit Pose Detection API is a lightweight versatile solution for app developers to detect the pose of a subject's body in real time from a continuous video or static image. A pose describes the body's position at one moment in time with a set of x,y skeletal landmark points. The landmarks correspond to different body parts such as the shoulders and hips. The relative positions of landmarks can be used to distinguish one pose from another. </p>
<p>ML Kit Pose Detection produces a full-body 33 point skeletal match that includes facial landmarks (ears, eyes, mouth, and nose) and points on the hands and feet. Figure 1 below shows the landmarks looking through the camera at the user, so it's a mirror image. The user's right side appears on the left of the image:</p>

<img src="https://raw.githubusercontent.com/icanerdogan/Google-MLKit-Android-Apps/master/Images/Project%20Images/Kotlin/Pose%20Detection/Pose%20Detection%20-%20Example.jpg">

<p>ML Kit Pose Detection doesn't require specialized equipment or ML expertise in order to achieve great results. With this technology developers can create one of a kind experiences for their users with only a few lines of code.</p>

<p>The user's face must be present in order to detect a pose. Pose detection works best when the subject’s entire body is visible in the frame, but it also detects a partial body pose. In that case the landmarks that are not recognized are assigned coordinates outside of the image.</p>

<h2> Key capabilities </h2>
<ul>
<li>Cross-platform support Enjoy the same experience on both Android and iOS.</li>
<li>Full body tracking The model returns 33 key skeletal landmark points, including the positions of the hands and feet.</li>
<li>InFrameLikelihood score For each landmark, a measure that indicates the probability that the landmark is within the image frame. The score has a range of 0.0 to 1.0, where 1.0 indicates high confidence.</li>
<li>Two optimized SDKs The base SDK runs in real time on modern phones like the Pixel 4 and iPhone X. It returns results at the rate of ~30 and ~45 fps respectively. However, the precision of the landmark coordinates may vary. The accurate SDK returns results at a slower framerate, but produces more accurate coordinate values.</li>
<li>Z Coordinate for depth analysis This value can help determine whether parts of the users body are in front or behind the users' hips. For more information, see the Z Coordinate section below.</li>
</ul>

<h2> Pose Detection App Preview </h2>

Main Screen        |  Posture Screen       |  Angle Screen
:-------------------------:|:-------------------------:|:-------------------------:
![](https://github.com/icanerdogan/PoseDetectionApp-MLKit/assets/52867508/0eb424c5-e2fe-4663-a466-6ea3759d2b11)  |  ![](https://github.com/icanerdogan/PoseDetectionApp-MLKit/assets/52867508/c50996a5-ef32-4048-95ce-90d7952d27aa) | ![](https://github.com/icanerdogan/PoseDetectionApp-MLKit/assets/52867508/2550b77f-e15b-466c-bd5f-f4fee496ed78)
  
</details>


<!------------------------------------------------------------------------------------- SELFIE SEGMENTATION APP ------------------------------------------------------------------------>
---
<details open>
  <summary><h2><a name="selfie-segmentation-app"> Selfie Segmentation App </a></h2></summary>
  <p>Welcome to SelfieSnap: Selfie Studio AI, the ultimate selfie editing app powered by cutting-edge artificial intelligence! Unleash your creativity and take your selfies to the next level with our advanced technology that seamlessly separates your face from the background, opening up a world of exciting possibilities.</p>
<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.selfiesegmentationapp"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p>ML Kit's selfie segmentation API allows developers to easily separate the background from users within a scene and focus on what matters. Adding cool effects to selfies or inserting your users into interesting background environments has never been easier.</p>
<p>The selfie segmentation API takes an input image and produces an output mask. By default, the mask will be the same size as the input image. Each pixel of the mask is assigned a float number that has a range between [0.0, 1.0]. The closer the number is to 1.0, the higher the confidence that the pixel represents a person, and vice versa.</p>
<p>The API works with static images and live video use cases. During live video, the API will leverage output from previous frames to return smoother segmentation results.</p>

<img src="https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/93fa0194-e4d7-4feb-b2ff-b50322a9790d">

<h2> Key capabilities </h2>
<ul>
  <li>Cross-platform support Enjoy the same experience on both Android and iOS.</li>
  <li>Single or multiple user support Easily segment multiple people or just a single person without changing any settings.</li>
  <li>Full and half body support The API can segment both full body and upper body portraits and video.</li>
  <li>Real time results The API is CPU-based and runs in real time on most modern smartphones (20 FPS+) and works well with both still image and live video streams.</li>
  <li>Raw size mask support The segmentation mask output is the same size as the input image by default. The API also supports an option that produces a mask with the model output size instead (e.g. 256x256). This option makes it easier to apply customized rescaling logic or reduces latency if rescaling to the input image size is not needed for your use case.</li>
</ul>

<h2> Selfie Segmentation App Preview </h2>

Main Screen        |  Segment       
:-------------------------:|:-------------------------:|
![](https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/dfca1f22-f891-4837-9841-0185ae7f3188) | ![](https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/6d1d17a7-ccb8-4f3d-aa4a-e9ee64936eeb)

</details>

<!--------------------------------------------------------------------------- BARCODE SCANNER APP ---------------------------------------------------------------------------------->
---
<details open>
  <summary><h2><a name="barcode-scanning-app"> Barcode Scanning App </a></h2></summary>
  
  <p>Welcome to QRSnap, your all-in-one barcode scanning solution! Harnessing the power of the state-of-the-art Google MLKit Artificial Intelligence library, QRSnap delivers unparalleled performance and accuracy in scanning barcodes. With the ability to seamlessly capture and decode numerous types of barcodes, including QR codes, UPC codes, and more, this app simplifies the process for users, whether it's through live camera scanning or selecting images from the gallery.</p>

<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.barcodescanner"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p> The API works with static images and live video use cases. During live video, the API will leverage output from previous frames to return smoother segmentation results.</p>
<p>With ML Kit's barcode scanning API, you can read data encoded using most standard barcode formats. Barcode scanning happens on the device, and doesn't require a network connection. </p>
<p>arcodes are a convenient way to pass information from the real world to your app. In particular, when using 2D formats such as QR code, you can encode structured data such as contact information or WiFi network credentials. Because ML Kit can automatically recognize and parse this data, your app can respond intelligently when a user scans a barcode.  </p>

<img src="https://raw.githubusercontent.com/icanerdogan/BarcodeScannerApp-MLKit/master/Documents/Barcode%20Scanner%20Banner.png">

<h2> Key capabilities </h2>
<ul>
<li>Linear formats: Codabar, Code 39, Code 93, Code 128, EAN-8, EAN-13, ITF, UPC-A, UPC-E</li>
<li>2D formats: Aztec, Data Matrix, PDF417, QR Code</li>
<li>Scan for all supported barcode formats at once without having to specify the format you're looking for, or boost scanning speed by restricting the detector to only the formats you're interested in.</li>
<li>Structured data that's stored using one of the supported 2D formats is automatically parsed. Supported information types include URLs, contact information, calendar events, email addresses, phone numbers, SMS message prompts, ISBNs, WiFi connection information, geographic location, and AAMVA-standard driver information.</li>
<li>Barcodes are recognized and scanned regardless of their orientation: right-side-up, upside-down, or sideways.</li>
<li>Barcode scanning is performed completely on the device, and doesn't require a network connection.</li>
</ul>

<h2> Barcode Scanner App Preview </h2>

Main Screen       |  Info Screen
:-------------------------:|:-------------------------:
![](https://raw.githubusercontent.com/icanerdogan/BarcodeScannerApp-MLKit/master/Documents/App%20Image%20-%201.png)  |  ![](https://raw.githubusercontent.com/icanerdogan/BarcodeScannerApp-MLKit/master/Documents/App%20Image%20-%202.png)

</details>


<!----------------------------------------------------------------------------- IMAGE LABELING APP -------------------------------------------------------------------------------->
---
<details open>
  <summary><h2><a name="image-labeling-app"> Image Labeling App </a></h2></summary>

<p>Introducing TagSnap: Detect Image Label AI, a cutting-edge application developed to revolutionize the way you interact with images. Powered by ML Kit's advanced image labeling APIs, TagSnap enables you to effortlessly detect and extract information about a diverse range of entities in your photos. From identifying general objects, places, activities, and animal species to recognizing specific products, our app provides an unparalleled image labeling experience.</p>
<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.imagelabeler"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p>With ML Kit's image labeling APIs you can detect and extract information about entities in an image across a broad group of categories. The default image labeling model can identify general objects, places, activities, animal species, products, and more.</p>
<p>You can also use a custom image classification model to tailor detection to a specific use case.</p>
<p>ML Kit’s base model returns a list of entities that identify people, things, places, activities, and so on. Each entity comes with a score that indicates the confidence the ML model has in its relevance. With this information, you can perform tasks such as automatic metadata generation and content moderation. The default model provided with ML Kit recognizes more than 400 different entities.</p>

<p align="center">
  <img src="https://developers.google.com/static/ml-kit/vision/image-labeling/images/image_labeling2x.png">
</p>

<h2> Key capabilities </h2>
<ul>
  <li>A powerful general-purpose base classifier Recognizes more than 400 categories that describe the most commonly found objects in photos.</li>
  <li>Tailor to your use case with custom models Use other pre-trained models from TensorFlow Hub or your own custom model trained with TensorFlow, AutoML Vision Edge or TensorFlow Lite Model maker.</li>
  <li>Easy-to-use high-level APIs No need to deal with low-level model input/output, image pre- and post-processing, or building a processing pipeline. ML Kit extracts the labels from the TensorFlow Lite model and provides them as a text description.</li>
</ul>

<h2> Image Labeling App Preview </h2>

Main Screen        |  Result Screen       
:-------------------------:|:-------------------------:|
![main-screen](https://github.com/icanerdogan/ImageLabelingApp-MLKit/assets/52867508/84102370-9f91-4fe2-b68b-34f05b63afc2) | ![result-screen](https://github.com/icanerdogan/ImageLabelingApp-MLKit/assets/52867508/4546db84-37d5-43d1-bf39-1f3f79e1b357)

</details>

<!-------------------------------------------------------------------------------- DIGITAL INK RECOGNITION APP ----------------------------------------------------------------------------->
---
<details open>
  <summary><h2><a name="digital-ink-recognition-app"> Digital Ink Recognition App </a></h2></summary>
  
<p>Welcome to "InkSnap: Write Recognition AI," where the art of handwriting meets cutting-edge technology. Whether you're a creative thinker or a productivity enthusiast, InkSnap provides an immersive writing experience like never before.</p>

<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.digitalinkrecognition"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p>With ML Kit's digital ink recognition API, you can recognize handwritten text and classify gestures on a digital surface in hundreds of languages, as well as classify sketches. The digital ink recognition API uses the same technology that powers handwriting recognition in Gboard, Google Translate, and the Quick, Draw! game.</p>
<p>Write on the screen instead of typing on a virtual keyboard. This lets users draw characters that are not available on their keyboard, such as ệ, अ or 森 for latin alphabet keyboards.</p>
<p>Perform basic text operations (navigation, editing, selection, and so on) using gestures.</p>
<p>Recognize hand‑drawn shapes and emojis.</p>

  
![English Handwriting - 1](https://developers.google.com/static/ml-kit/images/vision/digital-ink/handw1.png "English Handwriting - 1") ![English Handwriting - 2](https://developers.google.com/static/ml-kit/images/vision/digital-ink/handw2.png "English Handwriting - 2")

![Emoji - 1](https://developers.google.com/static/ml-kit/images/vision/digital-ink/emoji1.png "Emoji - 1") ![Emoji - 2](https://developers.google.com/static/ml-kit/images/vision/digital-ink/emoji2.png "Emoji - 2")


<h2> Key capabilities </h2>
<ul>
  <li>Converts handwritten text to sequences of unicode characters</li>
  <li>Runs on the device in near real time</li>
  <li>The user's handwriting stays on the device, recognition is performed without any network connection</li>
  <li>Supports 300+ languages and 25+ writing systems, see the complete list of supported languages</li>
  <li>Recognizes emojis and basic shapes</li>
  <li>Keeps on-device storage low by dynamically downloading language packs as needed</li>
</ul>

<h2> Digital Ink Recognition App Preview </h2>

Main Screen        |  Result Screen       
:-------------------------:|:-------------------------:|
![ink-recognition-1](https://github.com/icanerdogan/DigitalInkRecognition-MLKit/assets/52867508/38c04eea-0984-4522-a3e5-8415b9416f50) | ![ink-recognition-2](https://github.com/icanerdogan/DigitalInkRecognition-MLKit/assets/52867508/4403bec6-8342-48cb-94c0-e5b96ee8c936)

</details>
