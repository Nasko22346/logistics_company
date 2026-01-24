## To be implemented:

### Part 1
Currently, for the user to be able to access the web application, he/she needs to login to the app first.
If no login credentials are provided, the user must register first.
This is an approach, that is half-way done.
The idea is to add a *not-logged-in* user experience. The user should be able to access the home page. 
On that home page there should be a navbar presented (already implemented) with options to log in or register.
Also, a buttons should be added (they may already be there, but currently I am not sure) - to create a *parcel* and to track a *parcel*.
The task is to:
1. Move the `.css` logic from the `index.html` to a separate `.css` file.
2. Update the `index.html` to have a navbar with the buttons to log in (redirecting to `login.html`), crete a parcel and track a parcel.
3. If a `.html` component is missing, add it.
4. From `login.html` a *register* button should be added, redirecting to `registration.html`.