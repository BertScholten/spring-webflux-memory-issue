# spring-webflux-memory-issue

Small project to test a potential memory issue with spring webflux/netty.

When using WebClient to post/put something for the first time, that body seems to get retained in memory.

Project contains some tests to show this:

* If the first PUT action using WebClient is done with a big body, memory is increased significantly and does reduced, even when triggering garbage collect. Subsequent actions do not add to memory however.
* If the first PUT is done with a small body, memory is not increased as much, and a 2nd (or more) actions with the big body do not increase memory further.
* If the first action is a GET, memory isn't increased on subsequent actions either.

This seems to indicate that the body of the first action is somehow retained in memory, at least with the library versions used in this project.
