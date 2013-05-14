# spring-cache-guava


`spring-cache-guava` is a [Spring Framework Cache adapter](http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/cache.html#cache-plug)
backed by [Google Guava](http://code.google.com/p/guava-libraries/).

### Build instructions

`spring-cache-guava` uses a Maven build system. Building the code yourself
should be a straightforward case of:

    git clone git@github.com:irbouho/spring-cache-guava.git
    cd spring-cache-guava
    mvn test

This should cause the modules to be compiled and the tests to be run. To
install these artifacts to your local Maven repo, use:

    mvn install

In a Maven project, one would include the runtime in the dependencies section
of your `pom.xml` (replacing `${spring-cache-guava.version}` with the
appropriate current release):

```xml
<dependencies>
  <dependency>
    <groupId>com.springmodules.cache.guava</groupId>
    <artifactId>core</artifactId>
    <version>${spring-cache-guava.version}</version>
  </dependency>
</dependencies>
```

### Community / Support

* [spring-cache-guava Google Group](https://groups.google.com/forum/?fromgroups#!forum/spring-cache-guava)
* [GitHub Issues](https://github.com/irbouho/spring-cache-guava/issues)

License
-------

    Copyright 2012 the original author or authors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
