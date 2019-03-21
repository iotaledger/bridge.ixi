# bridge.ixi

## Abstract

The vision of the IOTA eXtension Interface (IXI) is to enable cross-language support. It's describing a future in which modules can be written in any programming language desired. At the moment, however, modules for the Iota Controlled agenT (Ict) must be written in the same language as the core is written. It's a limitation, code written in other languages can not be injected into Ict. This can be solved in several ways: first, by rewriting Ict in all desired programming languages, second, by having a seperate, specific interface for each language (e.g. JNI, Jython, ...) , or third, by having one, language independent interface. Since the first and second solutions are not well scalable, the third solution is preferred. <br><br>
bridge.ixi represents the interface which allows modules written in any programming language to interact with the underlying infrastructure. By giving everyone the opportunity to participate with their desired programming language, bridge.ixi will help grow the module ecosystem as well as have positive impact on the overall development.

### Architecture

bridge.ixi provides a socket mechanism to allow external modules interact with IXI and its internal Environment-Entity-Effect (EEE) infrastructure. By acting like a proxy, bridge.ixi will forward external requests and return internal responses. The interface should be as minimal and straightforward as the underlying IXI. Besides that, bridge.ixi should serve as high-speed I/O engine and handle requests asynchronous.

<img src="https://raw.githubusercontent.com/iotaledger/bridge.ixi/master/docs/bridge.png" />

### Protocol

#### Listening for external connections
The default port should be the same for every bridge.ixi implementation. By this, external modules will be easily pluggable and don't need to know to what core implementation they're talking to. Besides that, each connection will be handled in its own thread.

#### Serialization
bridge.ixi makes use of protocol buffers, an efficient, language-neutral mechanism for serializing structured data.
##### Procedure
1) client builds request message and sends it to the server
2) server processes request message
3) server builds response message and returns it to the client
##### Messages

The definitions of the protocol buffers can be found here in detail: 
https://github.com/iotaledger/bridge.ixi/tree/master/src/main/java/org/iota/ict/ixi/protobuf

The following messages serve as requests:

    FindTransactionsByAddressRequest
    FindTransactionsByTagRequest
 	FindTransactionByHashRequest
 	SubmitTransactionBuilderRequest
 	SubmitTransactionRequest
 	DetermineApprovalConfidenceRequest
 	AddEffectListenerRequest
 	RemoveEffectListenerRequest
 	SubmitEffectRequest

The following messages serve as responses:

 	FindTransactionsByAddressResponse
 	FindTransactionsByTagResponse
 	FindTransactionByHashResponse
 	DetermineApprovalConfidenceResponse
 	
Note that each message must be sent within a wrapper message. This is necessary so that the participants know what kind of message it is.

### Listener
As already mentioned, all connections will be handled in separate threads. Each thread contains exactly those listeners that are relevant for it.
