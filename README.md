# EventBus

EventBus:
---------

EventBus is a publish/subscribe event bus for Android and Java.

//Advantages
1)simplifies the communication between components
2)decouples event senders and receivers
3)performs well with Activities, Fragments, and background threads
4)avoids complex and error-prone dependencies and life cycle issues
5)makes your code simpler
6)is fast
7)is tiny (~60k jar)
8)has advanced features like delivery threads, subscriber priorities, etc.

Steps:
------

1) Defining Events

data class MessageEvent(var title : String, var description: String)

2) Post Events

EventBus.getDefault().post(MessageEvent("Event Bus",input_edittext.text.toString()))

3) Prepare Subscribers

@Subscribe(threadMode = ThreadMode.MAIN)
fun onMessageEvent(event: MessageEvent) {
    Toast.makeText(this, event.description, Toast.LENGTH_SHORT).show()
}

4) Registering events

override fun onStart() {
    EventBus.getDefault().register(this)
    super.onStart()
}

override fun onStop() {
    EventBus.getDefault().unregister(this)
    super.onStop()
}

ThreadMode:
-----------

1) ThreadMode: POSTING

Subscribers will be called in the same thread posting the event.

@Subscribe(threadMode = ThreadMode.POSTING)
public void onMessage(MessageEvent event) {
    log(event.message);
}

2) ThreadMode: MAIN

Subscribers will be called in Android’s main thread.

@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessage(MessageEvent event) {
    textField.setText(event.message);
}

3) ThreadMode: MAIN_ORDERED

Subscribers will be called in Android’s main thread with consisting order.

@Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
public void onMessage(MessageEvent event) {
    textField.setText(event.message);
}

4) ThreadMode: BACKGROUND

Subscribers will be called in a background thread.
If posting thread is not the main thread, event handler methods will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single background thread that will deliver all its events sequentially.

@Subscribe(threadMode = ThreadMode.BACKGROUND)
public void onMessage(MessageEvent event){
    saveToDisk(event.message);
}

5) ThreadMode: ASYNC

Event handler methods are called in a separate thread. This is always independent from the posting thread and the main thread.

@Subscribe(threadMode = ThreadMode.ASYNC)
public void onMessage(MessageEvent event){
    backend.send(event.message);
}

Sticky Events:
--------------

EventBus keeps the last sticky event of a certain type in memory.
For example, if you have some sensor or location data and you want to hold on the most recent values.

EventBus.getDefault().postSticky(new MessageEvent("Hello everyone!"));

MessageEvent stickyEvent = EventBus.getDefault().getStickyEvent(MessageEvent.class);

MessageEvent stickyEvent = EventBus.getDefault().removeStickyEvent(MessageEvent.class);

Priorities:
-----------

Changing the order of event delivery by providing a priority to the subscriber during registration.
Within the same delivery thread (ThreadMode), higher priority subscribers will receive events before others with a lower priority. The default priority is 0.
The priority does not affect the order of delivery among subscribers with different ThreadModes.

@Subscribe(priority = 1);
public void onEvent(MessageEvent event) {
    //handle event
}

Event Cancellation:
-------------------

Any further event delivery will be cancelled, subsequent subscribers won’t receive the event.

@Subscribe
public void onEvent(MessageEvent event){
    // Process the event
    ...
    // Prevent delivery to other subscribers
    EventBus.getDefault().cancelEventDelivery(event) ;
}

Subscriber Index:
-----------------

Using a subscriber index avoids expensive look-ups of subscriber methods at run time using reflection. Instead, the EventBus annotation processor looks them up at build time.

It is recommended to use the index for Android apps in production. It is faster and avoids crashes due to reflection.
