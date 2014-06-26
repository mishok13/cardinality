io.screen6/estimators
=====================

Experiments in writing cardinality estimators.

# Status

This is a work in progress. This library is NOT used in production
systems by Screen6 and we can't encourage you to use it in production
either. We don't forbid it though. ;)

# Installation

Leiningen dependency: `[io.screen6/estimators "0.1.0"]`

Clojars latest build: [![Clojars Project](http://clojars.org/io.screen6/cardinality/latest-version.svg)](http://clojars.org/io.screen6/cardinality)

[![Build Status](https://travis-ci.org/screen6/cardinality.png?branch=master)](https://travis-ci.org/screen6/cardinality)

# Current goals

 The end goal is to have at least the following:

* HyperLogLog implementation fully in Clojure
* Support for serializing HyperLogLog datastructures that adheres to
  [storage spec](https://github.com/aggregateknowledge/hll-storage-spec/blob/master/STORAGE.md)
  proposed and used by AggregateKnowledge
* [HyperLogLog++](http://static.googleusercontent.com/external_content/untrusted_dlcp/research.google.com/en/us/pubs/archive/40671.pdf)
  implementation
