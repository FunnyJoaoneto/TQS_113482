Feature: Book Search

  Background:
    Given the following were added to the library:
      | title              | author             | published   |
      | The Hobbit         | J.R.R. Tolkien     | 1937-09-21  |
      | The Silmarillion   | J.R.R. Tolkien     | 1977-09-15  |
      | 1984               | George Orwell      | 1949-06-08  |
      | Brave New World    | Aldous Huxley      | 1932-08-31  |
      | Fahrenheit 451     | Ray Bradbury       | 1953-10-19  |
      | The Catcher in the Rye | J.D. Salinger | 1951-07-16  |
      | Animal Farm        | George Orwell      | 1945-08-17  |
      | The Great Gatsby   | F. Scott Fitzgerald| 1925-04-10  |
      | To Kill a Mockingbird | Harper Lee      | 1960-07-11  |
      | Moby-Dick          | Herman Melville    | 1851-11-14  |
      | Pride and Prejudice | Jane Austen       | 1813-01-28  |
      | 1984 Redux         | George Orwell      | 2020-11-05  |

  Scenario: Add a new book
    Given a book with the title "TQS", written by "bitcoder", published in "2025-03-14"
    When the customer searches for books by title "TQS"
    Then the following books should be found:
      | title             | published   |
      | TQS               | 2025-03-14  |
  
  Scenario: Find books by author
    When the customer searches for books by author "George Orwell"
    Then the following books should be found:
      | title             | published   |
      | 1984              | 1949-06-08  |
      | Animal Farm       | 1945-08-17  |
      | 1984 Redux        | 2020-11-05  |

  Scenario: Find books by title
    When the customer searches for books by title "1984"
    Then the following books should be found:
      | title             | published   |
      | 1984              | 1949-06-08  |
      | 1984 Redux        | 2020-11-05  |

  Scenario: Find books published between two dates
    When the customer searches for books published between "1930-01-01" and "1950-12-31"
    Then the following books should be found:
      | title             | published   |
      | The Hobbit        | 1937-09-21  |
      | 1984              | 1949-06-08  |
      | Brave New World   | 1932-08-31  |
      | Animal Farm       | 1945-08-17  |

  Scenario: Find books published in a specific year
    When the customer searches for books published in 1949
    Then the following books should be found:
      | title             | published   |
      | 1984              | 1949-06-08  |

