# cool-lex-java

The Cool-lex order and algorithms were invented and authored by Frank Ruskey and Aaron Williams (<http://webhome.cs.uvic.ca/~ruskey/Publications/Coollex/CoolComb.html>).
You may need to obtain permission to use Cool-lex, as governed by applicable laws and academic practices.

The code in this repository is authored by the cool-lex-java [contributors](CONTRIBUTORS), and is licensed under Apache License, version 2.0 license.

## Notes

Golang-style iterators offer between 7% and 8% better performance compared to classic Java iterators.

## Examples

[Golang-style](https://pkg.go.dev/iter) iterators:

```java
 CoollexLinkedList.sequence(3, 2)
      .forEach(
          combination -> {
            combination.forEach(System.out::print);
            System.out.println();
          });
```

or

```java
  CoollexLinkedList.sequence(3, 2)
      .doWhile(
          combination -> {
            combination.doWhile(
                element -> {
                  System.out.print(element);
                  return true; // signal the iterator to continue producing elements
                });
            System.out.println();
            return true; // signal the iterator to continue producing combinations
          });

// prints:
// 01
// 12
// 02
```

---
Collections Framework iterators (classic iterators):

```java
CoollexLinkedList.combinations(3, 2)
    .forEachRemaining(
        combination -> {
          combination.forEachRemaining((int element) -> System.out.print(element));
          System.out.println();
        });

// prints:
// 01
// 12
// 02
```

## Dependencies

Minimum Java 8.

Porting to earlier versions is straightforward.

## Development

Ideas:

* Incorporation of additional algorithms as presented in the research paper, potentially better optimized for execution on contemporary architectures.
* Development of algorithm implementations suitable for resource-constrained computing environments.

## Installation

```xml
<project>
  <repositories>
    <repository>
      <id>github</id>
      <name>GitHub dastoikov Apache Maven Packages</name>
      <url>https://maven.pkg.github.com/dastoikov/cool-lex-java</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>com.samldom.coollex</groupId>
      <artifactId>cool-lex-java</artifactId>
      <version>1.1.1</version>
     </dependency>
    </dependencies>
</project>
```
