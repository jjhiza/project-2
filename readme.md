E-commerce Store Concept:

Initial thoughts...

    1) Soccer jersey sales app
    2) Sections for retro kits and current kits (retro may not be added, as it would baloon the size of the database exponentially)
    3) La Liga, EPL, Serie A included
    
The concept is that users will be shown a (1)linear layout of the leages available, a (2)grid layout of the teams in said league with imageviews for team badges, and a (2)grid layout of the players on each team. Players will have team photos, and on click, the image will open a (3)scrolling layout with an action photo, bio, image of their jersey, and (at the bottom) an option to purchase the jersey for themselves.

** Linear Layouts - straight-forward lists, occasionally accompanied by imageviews.
** Grid layouts - a more detailed view of the content, along with relevant images.
** Scrolling Layout - used to enable scrolling through the grids.

I want the user to be greeted by an action-shot homescreen that has search enabled (FAB?). Users should be able to search by team name, city, counrty, or player name, and recieve the appropriate results. Users should be able to navigate the app easily, and find the jersey they want to purchase. A FAB should serve as the 'cart' button, and the user should have the ability to add up to 10items to their cart at once. Once checkout is complete, I want to thank them for their purchase (use a toast for this), and return the user to the main screen. 
    
Tables concepts...

    TABLE 1:
        COL_LEAGUE 
        COL_TEAM (r/id 1)
        COL_CITY
        
    TABLE 2:
        COL_TEAM (f/id 1)
        COL_PLAYER (r/ id 2)
        COL_SHIRT #
        
    TABLE 3:
        COL_PLAYER (f/ id 2)
        COL_PRICE // price of the individual's jersey
        
11-2:
        
!! May need to reduce the scope of this project. Attempting to create a database for ~100 teams and over 400 players is unrealistic at this point.

##11-8:

Ditching the above concept in favor of a smaller Ramen app. This is due to an unforseen technical issue (my cat somehow destroying my unsaved work) and subsequent time constraints.

    App is working as expected with the exception of search. I feel like I've got everything set up correctly, but search simply isn't working (maybe because I have a bunch of very similar items?). Also, scrolling too fast seems to crash the app. This needs to be addressed.
