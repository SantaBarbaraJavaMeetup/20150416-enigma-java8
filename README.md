# 20150416-enigma-java8

Code for cracking an enigma machine with Java 8 Streams API.

Adapted to a special case Enigma machine with reversed rotors, that is:
```java
// Normal enigma machine, 1-2-3
rotor1 = 1
rotor2 = 2
rotor3 = 3
// This code's enigma machine, 1-2-3
rotor1 = 3
rotor2 = 2
rotor3 = 1
```

This matches [this](http://startpad.googlecode.com/hg/labs/js/enigma/enigma-sim.html) website's enigma machine.

### Requirements

JDK 8 to build and JRE 8 to run.

### Building

`./gradlew build`
### Eclipse Project Setup

`./gradlew eclipse`
### IntelliJ Project Setup

`./gradlew idea`
