# Unit Testing Workshop

Hands-on examples for an introductory unit-testing workshop, available in **Java** (JUnit 4) and **Python** (`unittest`). Both ports implement the same three exercises:

- `HelloUnitTest` — anatomy of a test class (lifecycle hooks, expected exceptions, timeouts, ignored tests).
- `DateTimeUtil` / `DateTimeUtilTest` — parsing and formatting dates across many string conventions.
- `SemVerUtil` / `SemVerUtilTest` — parsing and comparing semantic-version strings.

## Repository layout

```
unit-testing-workshop/
├── src/
│   ├── java/unittesting/        Java sources
│   └── python/                  Python sources
└── test/
    ├── java/unittesting/        JUnit 4 tests
    └── python/                  unittest tests
```

## Running the Java tests

The project ships as an Eclipse project (see `.classpath` / `.project` once imported).

### Option A — Eclipse / IntelliJ IDEA

1. Import the inner `unit-testing-workshop/` directory as an existing Java project.
2. Ensure **JUnit 4** is on the classpath (Eclipse bundles it; in IntelliJ accept the prompt to add it).
3. Right-click any class in `test/java/unittesting/` → **Run As → JUnit Test**.

### Option B — command line

You will need `junit-4.13.2.jar` and `hamcrest-core-1.3.jar` on the classpath. Place them in a `lib/` folder at the repo root, then from `unit-testing-workshop/unit-testing-workshop/`:

```bash
# Compile sources and tests
javac -d bin src/java/unittesting/*.java
javac -d bin -cp "bin:../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar" \
      test/java/unittesting/*.java

# Run all test classes
java -cp "bin:../lib/junit-4.13.2.jar:../lib/hamcrest-core-1.3.jar" \
     org.junit.runner.JUnitCore \
     unittesting.HelloUnitTest \
     unittesting.DateTimeUtilTest \
     unittesting.SemVerUtilTest
```

## Running the Python tests

Requires Python 3.8+. No third-party packages are needed — the ports use only the standard library (see `requirements.txt`).

From the repo root:

```bash
python -m unittest discover -s unit-testing-workshop/src/python -p "*_test.py" -v
```

Or run a single test file:

```bash
python unit-testing-workshop/src/python/sem_ver_util_test.py
```

### Optional: install workshop extras

`requirements.txt` is empty of runtime deps. If you want coverage reports or `pytest` as an alternate runner:

```bash
pip install pytest coverage
pytest unit-testing-workshop/src/python      # pytest also discovers unittest classes
coverage run -m unittest discover -s unit-testing-workshop/src/python -p "*_test.py"
coverage report
```

## Expected test outcomes

Several tests are intentionally failing or stubbed — they are part of the workshop material and demonstrate failure modes, not bugs to fix:

- `HelloUnitTest.bound_to_fail` / `test_bound_to_fail` — always fails on purpose.
- `DateTimeUtilTest.test_from_sql_*` / `test_to_sql_*` — `fail("Not yet implemented")` stubs to be completed during the workshop.
- `DateTimeUtilTest.shouldConvertUnusualStringDates…` — exercises the parser's leniency around out-of-range values.
- `HelloUnitTest.ignoreMe` / `test_ignore_me` — annotated with `@Ignore` / `@unittest.skip` and reported as skipped.
