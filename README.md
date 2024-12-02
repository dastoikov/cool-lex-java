# cool-lex-java

The Cool-lex order and algorithms were invented and authored by Frank Ruskey and Aaron Williams (<http://webhome.cs.uvic.ca/~ruskey/Publications/Coollex/CoolComb.html>).
You may need to obtain permission to use Cool-lex, as governed by applicable laws and academic practices.

The code in this repository is authored by the cool-lex-java [contributors](CONTRIBUTORS), and is licensed under Apache License, version 2.0.License.

## Example

```java
CoollexLinkedList.combinations(4, 3)
    .forEachRemaining(
        combination -> {
          combination.forEachRemaining((int element) -> System.out.format("%d ", element));
          System.out.println();
        });
// prints:
// 0 1 2
// 1 2 3
// 0 2 3
// 0 1 3
```

## Usage

Maven (repository: `<url>https://maven.pkg.github.com/dastoikov/cool-lex-java</url>`)

```xml
<dependency>
  <groupId>com.samldom.coollex</groupId>
  <artifactId>cool-lex-java</artifactId>
  <version>1.0</version>
</dependency>
```

## Dependencies

Minimum Java 8.

Can easily be re-written for lower versions, as primitive iterators and an exact-arithmetic function (in tests) are the sole dependencies on version 8.

## Development

Ideas:

**General**

* Additional algorithms as found in the paper.
* Algorithm implementations for constrained computing environments.

**Linked list**

* Flyweight implementation of the iterator over the elements of a combination (CoollexLinkedList, SelectedIndicesIterator).
* Analyze if it would be possible to make the CoollexLinkedList algorithm run in parallel.
