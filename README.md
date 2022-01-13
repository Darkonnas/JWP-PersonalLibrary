# JWP-PersonalLibrary

### Summary

The application aims to help persons in possession of a lot of books by providing a system of monitoring the storage area of each and one of them, as well as a way to keep track of books that were currently or previously lent to their friends.

### Business requirements
- The Owner can add new books and, if necessary, introduce new genres and authors.
- The Owner can modify the current library layout.
- The Owner can enforce rules for shelves, including their maximum capacity as well as the first letter of the stored books.
- The Owner can obtain the current storage distribution of its books within their library.
- A book can be lent to a friend if they aren't past due date with any other previous lends.
- A book cannot be lent if there are no further available copies in good shape.
- Friends can ask for a lent extension on one of their current books, and it needs to be further approved.
- The Owner can see the last friend that lent a book in case the book was deteriorated.
- Friends can only review a book that they have previously lent.
- Friends should be able to up-vote/ down-vote reviews as well as comment on them.

### Main features

1. Adding a new book into the system and managing its storage location.
2. Exporting the current library layout in various formats.
3. Mechanism for lending a book to a friend as well as viewing the previous lends of the book.
4. Reviewing a lent book by friends.

Swagger is mounter on `/docs/swagger-ui` and can be used to test each and every endpoint.
