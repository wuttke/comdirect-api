# Comdirect REST API

![Maven Build Badge](https://github.com/github/docs/actions/workflows/maven.yml/badge.svg)

## Motivation

There are (at least) two great Comdirect REST API libraries:

* https://github.com/jsattler/go-comdirect
* https://github.com/keisentraut/python-comdirect-api

These libraries heavily inspired the classes in this repository.
Why another library?

* I wanted to integrate the API in a Java application and did not want to bundle the app with another package.
* I did not like to "callback" architecture for the challenge/response mechanism, but rather wanted to divide the login process into two stages.

## Usage

### Login process

### Retrieve accounts and balances

### Retrieve account transactions

