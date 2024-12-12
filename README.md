# Kotlin-Android_Music_Reproducer

Controllers:

    -)we have a SearchManagement.kt file that implements all functions for the recyclerView, such as search
    ,animation and song extraction.

    -)The Object Switcher instead takes care of keeping the current activities in the currentActivity variable,
    switch between the pages and create the notification by interfacing with the MusicService::class.java

        notification:
            -) the Service takes care of the creation of the notification by deciding colors, Intent which reports to the application
            and sendNotification method, key management takes place within the service itself


Model:

    -)CurrentSongProperties is a static database accessible from all classes and holds all the methods for managing the
    mediaPlayer

    -)The Song class instead encapsulates title and author, the latter acts as KEY in the HashMap with RAW resources (VALUE)
    
View:

    -)simply the 2 fragments that deal with the management of buttons, the SearchFragment communicates
    with SearchManagement (acting as its controller), home fragment pays particular attention to the Back calls
    and the handlers with which it manages progressBar and main ImageView animation


PUT YOUR SONG INTO RAW FOLDER AND REGISTER IT INTO CurrentSongProperties.kt  (changhe the parameters into songMap)
