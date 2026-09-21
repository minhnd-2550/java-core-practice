#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
case "${1:-}" in
  shape) exercise=01-shape-inheritance ;;
  inventory) exercise=02-supermarket-inventory ;;
  vehicles) exercise=03-vehicle-management ;;
  check)
    for exercise in 01-shape-inheritance 02-supermarket-inventory 03-vehicle-management; do
      mkdir -p "$exercise/out"
      javac --release 21 -encoding UTF-8 -d "$exercise/out" "$exercise"/src/*.java
      java -ea -cp "$exercise/out" Main --check
    done
    exit 0 ;;
  *) echo 'Usage: bash run.sh {shape|inventory|vehicles|check}'; exit 1 ;;
esac
mkdir -p "$exercise/out"
javac --release 21 -encoding UTF-8 -d "$exercise/out" "$exercise"/src/*.java
java -ea -cp "$exercise/out" Main
