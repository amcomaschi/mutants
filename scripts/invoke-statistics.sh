#!/bin/bash

seq 200 | parallel -j0 --joblog statistics-jobs-log curl -s http://localhost:4567/stats ">>" statistics-api-responses.txt

