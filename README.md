# web_auth

Exercise to demonstrate basic authentication and authorization in a Java website.

> Users must sign in with the corresponding credentials to view the private pages, depending on their assigned roles. No frameworks are to be used to handle security. 


## Features

- Session handling with sliding expiration
- Authentication and session tracking with cookies
- Resource authorization
- Password salting & hashing
- Secure session id generation
- Configurable parameters

Only servlets & jsp are used.

Despite being only an exercise, care has been taken to follow some of the standard security guidelines.


## Initial configuration

### Users

- Username: user1. Password: password1. Role: PAG_1
- Username: user2. Password: password2. Role: PAG_2
- Username: user3. Password: password3. Role: PAG_3

Configuration file: WEB-INF/users.json


### Resource authorization

- Resource: /private/pag1.jsp. Role: PAG_1
- Resource: /private/pag2.jsp. Role: PAG_2
- Resource: /private/pag3.jsp. Role: PAG_3

Configuration file: WEB-INF/authorization.json


 * Ctrl+S / Cmd+S to save the file
 * Ctrl+Shift+S / Cmd+Shift+S to choose to save as Markdown or HTML
 * Drag and drop a file into here to load it
 * File contents are saved in the URL so you can share files

## Todo

- Anti-CSRF protection on login and logout
- Unit testing

