# BmvSchool

Tech Stack: **Core Android Development (Java, XML, Android Studio), Firebase, OneSignal.**

An android app for a school. Students are notified about day wise homework and holidays. Discussions can be held with teachers and among peers.

Firstly, user is required to register themselves on the app. They fill in their information like name, class, designation (Student, Montior, Teacher or Principal).
Once submitted, an account creation request is raised on existing teachers' account, and approval is awaited. After being approved, user is asked to create a new email and password.

User can now access the features of the app.

OneSignal is used to send push notifications to notify students about homework and holidays.

# Privileged Access Management

The app uses PAM. There are four different privilege levels.

### Student 

 - Access holidays. 
 - Access homework of their own class. 
 - Participate in their own class discussion.

### Monitor 

 - Add new Homework of their own class.

### Teacher 

 - Access homework of all the classes. 
 - Participate in discussion of all the classes. 
 - Add new homework of any class. 
 - Add new holidays. 
 - Approve / Reject account requests of students and monitors.
 - Edit any student's information, i.e., name, class, and designation.

### Principal 

 - Approve / Reject account requests of teachers.
 - Edit any teacher's information, i.e., name, class, and post.


https://user-images.githubusercontent.com/37345795/176167905-6249a68a-ffb7-43a7-8510-2efcd21ecdbf.mp4



