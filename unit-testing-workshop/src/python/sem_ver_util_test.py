import unittest

from sem_ver_util import SemVerUtil


class SemVerUtilTest(unittest.TestCase):

    invalid_versions = []
    valid_pre_alpha_versions = []
    valid_alpha_versions = []
    valid_beta_versions = []
    valid_release_versions = []

    @classmethod
    def setUpClass(cls):
        cls.invalid_versions = [
            None,
            "",
            "1",
            "1.2",
            "1.0.0-alpha",
            "1.0.0-beta",
            "0.0.1-prealpha",
            "0.9.2+alpha",
            "2.1.0-rc",
            "-1.6.0",
            "a.b.c",
            "1.2.6-20181130",
        ]
        cls.valid_pre_alpha_versions = ["0.0.0", "0.0.1", "0.0.99"]
        cls.valid_alpha_versions = ["0.1.0-alpha", "0.1.0-ALPHA", "0.9.19-alpha"]
        cls.valid_beta_versions = ["0.1.0-beta", "0.1.0-BETA", "0.9.19-beta"]
        cls.valid_release_versions = ["1.0.0", "1.1.1", "3.2.10"]

    @classmethod
    def tearDownClass(cls):
        pass

    def test_should_throw_null_pointer_exception_on_null_version(self):
        with self.assertRaises(TypeError):
            SemVerUtil(None)

    def test_should_throw_number_format_exception_on_invalid_version(self):
        for version in self.invalid_versions:
            with self.assertRaises((ValueError, TypeError)):
                SemVerUtil("" if version is None else version)

    def test_should_validate_pre_alpha(self):
        for version in self.valid_pre_alpha_versions:
            self.assertTrue(SemVerUtil(version).is_pre_alpha())

    def test_should_validate_alpha(self):
        for version in self.valid_alpha_versions:
            self.assertTrue(SemVerUtil(version).is_alpha())

    def test_should_validate_beta(self):
        for version in self.valid_beta_versions:
            self.assertTrue(SemVerUtil(version).is_beta())

    def test_should_validate_release(self):
        for version in self.valid_release_versions:
            self.assertTrue(SemVerUtil(version).is_release())

    def test_compare_versions(self):
        olders = ["0.0.1", "0.0.8", "0.2.3-alpha", "1.2.3", "1.2.3", "1.2.3"]
        newers = ["0.0.2", "0.0.8-alpha", "0.2.3-beta", "1.2.4", "1.3.3", "2.2.3"]
        for older, newer in zip(olders, newers):
            print(f"{older} {newer}")
            self.assertEqual(
                SemVerUtil.compare_versions(SemVerUtil(older), SemVerUtil(newer)), 1
            )

    def test_compare_same_versions(self):
        self.assertEqual(
            SemVerUtil.compare_versions(SemVerUtil("0.0.1"), SemVerUtil("0.0.1")), 0
        )
        self.assertEqual(
            SemVerUtil.compare_versions(
                SemVerUtil("0.5.3-beta"), SemVerUtil("0.5.3-beta")
            ),
            0,
        )

    def test_backwards_compatibility(self):
        version = SemVerUtil("1.5.5")
        self.assertTrue(version.is_backwards_compatible_with(SemVerUtil("1.5.4")))

    def test_backwards_incompatibility(self):
        version = SemVerUtil("1.5.5")
        self.assertFalse(version.is_backwards_compatible_with(SemVerUtil("1.6.5")))

    def test_to_string(self):
        version = "0.2.16-beta"
        self.assertEqual(str(SemVerUtil(version)), version)


if __name__ == "__main__":
    unittest.main()
