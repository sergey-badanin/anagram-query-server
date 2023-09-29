
This repo represents an implementation of a toy **Anagram Query Server** which is aimed to handle **Anagram Query Protocol**.

The **Anagram Query Protocol** is a simple text protocol comprising of the following set of commands:
- [_string1,string2_] - To assess if _string1_ and _string2_ are anagrams
- <_query_string_> - To request a list of previously assessed strings which are anagrams for the _query_string_

#### Running server:
```
$ docker build --target app --tag anagram-server .
```
```
$ docker run -d --publish 8091:8091 --name anagram-server anagram-server
```
#### Connecting:
```
telnet localhost 8091
```
#### Example:
Just send text to the server
```
[Hello,hellO]
```
to receive a response
```
ASSESS: [hello,HELLO]
true
```
![Live example](/media/example.gif)

#### Clean up
Don't forget to remove container and image:
```
$ docker container stop anagram-server
$ docker container rm anagram-server
$ docker image rm anagram-server
```

#### This repo is an implementation for the spec:
![Task](/media/task_statement.png)