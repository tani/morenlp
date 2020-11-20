# MoreNLP

We can retrieve constituent structure of the sentence by CoreNLP Server.
It however is not JSON friendly, like S-expression.

You can call `edu.stanford.nlp.pipeline.StanfordCoreNLP.process` via JSON-RPC 2.0 by this software and retrieve nice JSON.

## Usage

### JSON-RPC

```sh
$ PORT=8888 lein ring server-headless # or PORT=8888 java -jar morenlp-<version>-standalone.jar
$ curl -X POST -H 'application/json' -d '{"jsonrpc":"2.0", "id":"0000-0000-0000", "method": "process", "params": ["Hello world."]}'
$ curl -X POST -H 'application/json' -d '{"jsonrpc":"2.0", "id":"0000-0000-0000", "method": "process", "params": ["Hello world.", {"annotators": "tokenize,ssplit,pos,lemma,ner,parse"}]}'
$ curl -X POST -H 'application/json' -d '{"jsonrpc":"2.0", "id":"0000-0000-0000", "method": "process", "params": ["Hello world.", {"annotators": "tokenize,ssplit,pos,lemma,ner,parse", "parse.binaryTrees": "true"}]}'
```

### Clojure REPL

```clojure
$ lein repl
morenlp.core > (process "Hello world.")
morenlp.core > (process "Hello world." {"annotators" "tokenize,ssplit,pos,lemma,ner,parse"})
morenlp.core > (process "Hello world." {"annotators" "tokenize,ssplit,pos,lemma,ner,parse" "parse.binaryTrees" "true"})
```

## License

Copyright (c) 2020 TANIGUCHI Masaya All Rights Reserved.

This software is licensed under the MIT license.
