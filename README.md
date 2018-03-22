# Android architecture test

## What does this app do?
This app shows the list of all Github repositories by Jake Wharton.

## Some more details
The list is loaded page by page(15 items per page). All loaded data is cached locally in the
Realm(https://realm.io/) database so that data loaded once should not be queried again.
After all available data is loaded and saved in local storage no more network calls will be made.
The next page loading starting when user reaches the bottom of currently displayed list, new
data is appended to the bottom of the list.
The application is based on Android Architecture Components(https://developer.android.com/topic/libraries/architecture/index.html)