#!/bin/bash
sort -k 1 res/out.txt > res/mutualfriends_your_output.txt
diff res/mutualfriends_expect.txt res/mutualfriends_your_output.txt
exit $?
