import time
import unittest


class HelloUnitTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        print("setUpClass() is executed before first test")

    @classmethod
    def tearDownClass(cls):
        print("tearDownClass() is executed after all tests")

    def setUp(self):
        print("setUp() is executed before each test")

    def tearDown(self):
        print("tearDown() is executed after each test")

    def test_bound_to_fail(self):
        self.fail("test_bound_to_fail() will just fail the test case")

    def test_should_throw_type_error(self):
        with self.assertRaises(TypeError):
            float(None)

    def test_should_not_take_more_than_twenty_seconds(self):
        start = time.monotonic()
        for i in range(10_000_000):
            i * i
        elapsed = time.monotonic() - start
        self.assertLess(elapsed, 20.0, msg=f"Took {elapsed:.2f}s, expected < 20s")

    def test_should_convert_double(self):
        numbers = ["0", "1.2", "2", "3.33", "3.14", "-1", "-99.99", "1234567890.987654321"]
        try:
            for num in numbers:
                float(num)
        except Exception:
            self.fail("Should convert all the numbers.")

    @unittest.skip("Demonstrates @Ignore equivalent")
    def test_ignore_me(self):
        if 1 > 0:
            self.fail("I don't existed")


if __name__ == "__main__":
    unittest.main()
