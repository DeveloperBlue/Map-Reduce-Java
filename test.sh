#!/bin/bash
sort -k 1 res/out.txt > res/cybersla_your_output.txt
diff res/cybersla_expect.txt res/cybersla_your_output.txt
exit $?
