# data-services-application
# data-services-application
#Table of Contents
1.	Solution Approach
2.	Tests
3.	Repository directory structure
4.	Challenge Description
# Solution Approach
The main class is in ‘/src/main/java/data.services.code.challenge/DataServicesServer.java’. The entire solution uses ‘java.io.*’, ‘java.net.*’, ‘java.util.*’. No other dependencies and build.gradle is also available.
The server was limited to a Fixed Thread Pool by using Executors package. Server doesn’t allow any more than 5 clients to connect at a time. Server has a client handler class which handles and reads the input streams for each clients and responding to inputs dynamically as they come in. 
Initially, started using the Client input streams directly fed into the CopyOnWriteArrayList and running it. However the process was quite slow for 200K+ records and also taking more memory. After that, implemented different blocking queues from java.util.concurrent package, in order to be more thread safe, and to avoid interleaving of threads but still the performance is poor. The blocking queue's capacity is set to 2M, however it is adjustable under `main`. The blocking queue is fed in by NumbersProcessor.java class. 

Unfortunately processing speed wasn't much approved with this method. Test results below. The numbers.log file is saved in the `output` folder in c:\ drive. DataServicesServer.java is main application and need to pass an argument for the output file(numbers.log) file creation.

## Future Directions
Maybe having a concurrent and parallel processing and having multiple inner threads that simultaneously consumes from the data structure would work much better. Overall found that consuming of the data structure and finding out uniques was taking more time. 
 # Tests
A "dummy" client is provided in the test folder. Along with some tests (no assertions).
 
    Max 5 Clients                           : Successful
    Handling leading zeros                  : Successful
    numbers.log created anew                : Successful
    No duplicates in log file               : Successful
    Non-conforming Data/Client disconnect   : Successful
    Summary standard output                 : Successful
    terminate / system shutdown             : Successful
2M numbers across 5 clients test, Specs:
* 0.4 seconds for 2M numbers to get in to queue.

# References
1.	http://www.baeldung.com/a-guide-to-java-sockets
2.	https://stackify.com/streams-guide-java-8/
 
# Challenge Description
Write a server (“Application”) in Java that opens a socket and restricts input to at most 5 concurrent clients. Clients will connect to the
Application and write any number of 9 digit numbers, and then close the connection. The Application must write a de-duplicated list of these
numbers to a log file in no particular order.

*Primary Considerations*
The Application should work correctly as defined below in Requirements.
- The overall structure of the Application should be simple.
- The code of the Application should be descriptive and easy to read, and the build method and runtime parameters must be well-described and work.
- The design should be resilient with regard to data loss.
- The Application should be optimized for maximum throughput, weighed along with the other Primary Considerations and the Requirements below.
- The solution should have detailed instructions that will guide someone new to coding on how to execute and test the solution.

*Requirements*
1. The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000.
2. Input lines presented to the Application via its socket must either be composed of exactly nine decimal digits (e.g.: 314159265 or 007007009)
immediately followed by a server-native newline sequence; or a termination sequence as detailed in #9, below.
3. Numbers presented to the Application must include leading zeros as necessary to ensure they are each 9 decimal digits.
4. The log file, to be named "numbers.log”, must be created anew and/or cleared when the Application starts.
5. Only numbers may be written to the log file. Each number must be followed by a server-native newline sequence.
6. No duplicate numbers may be written to the log file.
7. Any data that does not conform to a valid line of input should be discarded and the client connection terminated immediately and without comment.
8. Every 10 seconds, the Application must print a report to standard output:
  A. The difference since the last report of the count of new unique numbers that have been received.
  B. The difference since the last report of the count of new duplicate numbers that have been received.
  C. The total number of unique numbers received for this run of the Application.
  D. Example text for #8: Received 50 unique numbers, 2 duplicates. Unique total: 567231
9. If any connected client writes a single line with only the word "terminate" followed by a server-native newline sequence, the Application
must disconnect all clients and perform a clean shutdown as quickly as possible.
10. Clearly, state all of the assumptions you made in completing the Application.

*Notes*
- You may write tests at your own discretion. Tests are useful to ensure your Application passes Primary Considerations.
- You may use common libraries in your project such as Apache Commons and Google Guava, particularly if their use helps improve Application
simplicity and readability. However, the use of large frameworks, such as Akka, is prohibited.
- Your Application may not for any part of its operation use or require the use of external systems, for example, Apache Kafka or Redis.
- At your discretion, leading zeroes present in the input may be stripped—or not used—when writing output to the log or console.
- Robust implementations of the Application typically handle more than 2M numbers per 10-second reporting period on a modern MacBook Pro laptop
(e.g.: 16 GiB of RAM and a 2.5 GHz Intel i7 processor).

