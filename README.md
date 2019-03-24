# Bridge.ixi

## Abstract

The vision of the IOTA eXtension Interface (IXI) is to enable cross-language support. It's describing a future in which modules can be written in any programming language desired. At the moment, however, modules for the Iota Controlled agenT (Ict) must be written in the same language as the core is written. It's a limitation, code written in other languages can not be injected into Ict. This can be solved in several ways: first, by rewriting Ict in all desired programming languages, second, by having a seperate, specific interface for each language (e.g. JNI, Jython, ...) , or third, by having one uniform, language-neutral interface. Since the first and second solutions are not well scalable, the third solution is preferred. <br><br>
Bridge.ixi represents the interface which allows modules written in any programming language to interact with the underlying infrastructure. By giving everyone the opportunity to participate with their desired programming language, Bridge.ixi will help grow the module ecosystem as well as have positive impact on the overall development.

## Architecture

Bridge.ixi provides a socket mechanism to allow external modules interact with IXI and its internal Environment-Entity-Effect (EEE) infrastructure. By acting like a proxy, Bridge.ixi will forward external requests and return internal responses. The interface should be as minimal and straightforward as the underlying IXI. Besides that, Bridge.ixi should serve as high-speed I/O engine and handle requests asynchronous.
<img src="https://raw.githubusercontent.com/iotaledger/Bridge.ixi/master/docs/bridge.png" />

### Protocol
Bridge.ixi acts as a server and waits for clients (modules) to connect. The default port should be the same for every Bridge.ixi implementation. By this, external modules will be easily pluggable and don't need to know to what core implementation they're talking to. Each connection will be handled in its own service thread.
As soon as a module is connected, it can submit requests. For each request, the service thread interacts with the underlying IXI and returns the appropriate response back to the module.


### Serialization
Bridge.ixi makes use of [protocol buffers](https://developers.google.com/protocol-buffers/), an efficient, language-neutral mechanism for serializing structured data.
With [protobuf3](https://developers.google.com/protocol-buffers/docs/proto3), the following languages are supported:
Java, Python, Objective-C, C++, Dart, Go, Ruby, C#, JavaScript. Third-party implementations are also available for C, Rust, Elixir, Haskell, Swift and [many more](https://github.com/protocolbuffers/protobuf/blob/master/docs/third_party.md).
#### How do I start with the protocol buffers?

1) Download and install the protocol buffer compiler ([protoc](https://github.com/protocolbuffers/protobuf/releases)).
2) Run the protocol buffer compiler for your module's language on the [.proto files](https://github.com/iotaledger/bridge.ixi/tree/master/src/main/java/org/iota/ict/ixi/protobuf/definition) to generate data access classes.
3) Use these data access classes in your module project. They provide simple methods to build, serialize and deserialize your protocol buffer messages.

Note that each message must be sent within a [wrapper message](https://github.com/iotaledger/bridge.ixi/blob/master/src/main/java/org/iota/ict/ixi/protobuf/definition/wrapper.proto). This is necessary so that the participants know what kind of message it is.

## Installation

### Step 1: Install Ict

Please find instructions on [iotaledger/ict](https://github.com/iotaledger/ict#installation).

Make sure you are connected to the main network and not to an island, otherwise you won't be able to message anyone in the main network.

### Step 2: Get Bridge.ixi

There are two ways to do this:

#### Simple Method

Go to [releases](https://github.com/iotaledger/bridge.ixi/releases) and download the **bridge.ixi-{VERSION}.jar**
from the most recent release.

#### Advanced Method

You can also build the .jar file from the source code yourself. You will need **Git** and **Gradle**.

```shell
# download the source code from github to your local machine
git clone https://github.com/iotaledger/bridge.ixi
# if you don't have git, you can also do this instead:
#   wget https://github.com/iotaledger/bridge.ixi/archive/master.zip
#   unzip master.zip

# change into the just created local copy of the repository
cd bridge.ixi

# build the bridge.ixi-{VERSION}.jar file
gradle ixi
```

### Step 3: Install Bridge.ixi
Move bridge.ixi-{VERSION}.jar to the **modules/** directory of your Ict:
```shell
mv bridge.ixi-{VERSION}.jar ict/modules
```

### Step 4: Run Ict
Switch to Ict directory and run:
```shell
java -jar ict-{VERSION}.jar
```