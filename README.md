[![License MIT][badge-license]](http://opensource.org/licenses/MIT)
[![Build Status](https://travis-ci.org/screen6/cardinality.png?branch=master)](https://travis-ci.org/screen6/cardinality)
[![Coverage Status](https://img.shields.io/coveralls/screen6/cardinality.svg)](https://coveralls.io/r/screen6/cardinality)

io.screen6/estimators
=====================

Experiments in writing cardinality estimators.

# Status

This is a work in progress. This library is NOT used in production
systems by Screen6 and we can't encourage you to use it in production
either. We don't forbid it though. ;)

## Latest version

[![Clojars Project](http://clojars.org/io.screen6/cardinality/latest-version.svg)](http://clojars.org/io.screen6/cardinality)

# Current goals

 The end goal is to have at least the following:

* HyperLogLog implementation fully in Clojure
* Support for serializing HyperLogLog datastructures that adheres to
  [storage spec](https://github.com/aggregateknowledge/hll-storage-spec/blob/master/STORAGE.md)
  proposed and used by AggregateKnowledge
* [HyperLogLog++](http://static.googleusercontent.com/external_content/untrusted_dlcp/research.google.com/en/us/pubs/archive/40671.pdf)
  implementation

[badge-license]: https://img.shields.io/badge/license-MIT-green.svg
