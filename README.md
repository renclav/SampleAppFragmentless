# Demo Fragmentless Master Detail Project

Please note the dummy api changes the images returned regularly, this can lead to behaviour whereby selecting an image in the list seems to display the incorrect image simply because the backing image itself has changed on the url

Simple app that displays nonsensical data in a list/grid on screen using Google's suggested architecture and ViewGroups in place of fragments. Would have preferred a more Clean Architecture style, but felt that unnecessary for such a small app and... I wanted to try something different :)

# On Phones:
Master view (Recycler view) with switchable layouts, upon row item selection, detail view(image with range slider) is displayed

# On Tablets (sw600dp):
Master detail views are displayed at the same time


## TODO:

1. Extract CluesPresenter out into three presenters, that of
  1. Container Presenter
  2. List Presenter
  3. Detail Presenter

2. Increase Test coverage (only after 1 is complete)
3. Add additional comments
4. Investigate hardware layer morphing
5. Improve Clue loading logic on detail view, currently relies on showing an error image if the load fails, this should be handled by the presenter
6. Add ViewModels

