# Map-Reduce-Java

MapReduce is a programming model and an associated implementation for processing and generating big data sets with a parallel, distributed algorithm on a cluster. [^1]

You can input a custom number of reducers and mappers to be spun up in their own threads for multi-threaded distributed paralell processing.

Better documentation can be created on request. (Create an Issue)

[^1]: (https://en.wikipedia.org/wiki/MapReduce)

---

Map-Reduce model implemented for two paradigms:
- Word Counter
- Mutual Friends

### Word Counter
Takes an input file (/res/cybersla) and creates a file containing the alphebetized formatted count of each file in the input (word:count)

### Mutual Friends
Takes an input file (/res/mutualfriends) and creates a file containing a bi-pair multi-key set of mutual friends (f1,f2[\t]m1, m2, m3, . . .)

---

## Known Issues
1. The number of mappers must be the same as the number of reducers. This was a misunderstanding of the project prompt and can be fixed very easily, should any request it.
2. Built on Linux. Not tested for Windows, but I don't see any reason it shouldn't work.