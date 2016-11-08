# SampleAppFragmentless

Please note the dummy api changes the images returned regularly, this can lead to behaviour whereby selecting an image in the list seems to display the incorrect image simply because the backing image itself has changed on the url

Simple app that displays nonsensical data in a list/grid on screen using Google's suggested architecture and ViewGroups in place of fragments. Would have preferred a more Clean Architecture style, but felt that unnecessary for such a small app and... I wanted to try something different :)


TODO:
1. Extract CluesPresenter out into 3
1.1 Container Presenter
1.2 List Presenter
1.3 Detail Presenter

2. Increate Test coverage (only after 1 is complete)
3. Add additional comments
4. Investigate hardware layer morphing
5. Improve Clue loading logic on detail view, currently relies on showing an error image if the load fails, this should be handled by the presenter
6. Add ViewModels

