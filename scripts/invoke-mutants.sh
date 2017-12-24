#!/bin/bash

seq 10 | parallel -j0 --joblog mutants-jobs-log curl -H "Content-Type: application/json" -X POST --data-binary @dna-mutant.json -s http://localhost:4567/mutants/ ">>" mutants-api-responses.txt

