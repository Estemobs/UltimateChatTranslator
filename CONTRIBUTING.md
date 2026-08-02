# Contributing

Thanks for wanting to contribute to Ultimate Chat Translator!

## Development

```bash
./build.sh        # installs Java 21 + Gradle helper
# or, with Java 21 + Gradle 8.8 already installed:
gradle build
```

The built jar is placed in `build/libs/`.

## Before a PR

```bash
./gradlew test
```

The test suite covers the pure translation/config logic and runs automatically on every push and pull request.

## License

MIT.
