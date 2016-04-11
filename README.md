# AndroidApp
Angela Yang (asy24) and Abigail Watson (alw233)

How To Run This App
This app has been tested and works on Marshmallow, API 23.
Open the app by clicking its icon in the menu of the Android, then click the Search Toys button to begin searching through the toys. Press and hold a toy item to choose it and drag that item to the shopping cart image to add to your shopping cart. If you would like to return to the main page, click Cancel Search. If you are finished choosing toys and want to proceed with your purchase, click Purchase. On the next screen, you can see a list of the toys you chose and the final price of your shopping cart. If you would like to remove a toy, press and hold on that toy item. Finally, click the Get Map button to proceed to a map to the Toy Store (Georgetown University) where you can pick up your toys.

Design Choices
To download the toys from the URL, we used an AsyncTask and read in the file as a byte array which we then parsed into a ToyList. This ToyList is then outputted to the screen in a Listview. We overloaded the view for a toy in the list using the class ToyListAdapter to include the toy’s image, name and price in the ListView. In order to accommodate lists that may be longer than the screen allows, the view is scrollable and drag and drop is initiated by long click rather than click. Once a toy is long-clicked it can be dragged to the shopping cart image and added to the cart. On the next screen, you can view a list of toys chosen along with their individual prices and then the overall price on the bottom. We added the functionality of removing a toy by long-clicking on the toy. The view is automatically updated to show the change, including the final price at the bottom. Finally to get the map, you click on the button Get Map, which opens a link to Google Maps and shows you a map pointing to Georgetown University.

Additional Features
We added a remove toy feature to the Purchase page, which allows you to remove toys from your list and see the list and the overall price to change immediately.
We also accommodated longer lists by implementing drag and drop using long click only, so that you can still scroll through the list to view more items if there are any.