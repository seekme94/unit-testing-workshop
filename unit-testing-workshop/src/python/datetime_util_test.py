import unittest
from datetime import datetime

from datetime_util import DateTimeUtil


class DateTimeUtilTest(unittest.TestCase):

    sql_format = "%Y-%m-%d %H:%M:%S"
    iso_format = "%Y-%m-%dT%H:%M:%S.Z"
    client_format = "%d/%m/%Y %H:%M"

    def test_should_convert_string_date_in_client_format(self):
        date = DateTimeUtil.from_string("01/01/2018 00:00", self.client_format)
        self.assertIsNotNone(date)

    def test_should_convert_string_datetime_in_client_format(self):
        date = DateTimeUtil.from_string("01/01/2018 16:55", self.client_format)
        self.assertIsNotNone(date)

    def test_should_convert_unusual_string_dates_in_client_format(self):
        unusual = [
            "-1/01/2018 00:00",
            "32/01/2018 00:00",
            "01/13/2018 00:00",
            "01/01/-800 00:00",
            "01/01/2018 24:00",
            "01/01/2018 00:60",
        ]
        for date_string in unusual:
            self.assertIsNotNone(
                DateTimeUtil.from_string(date_string, self.client_format),
                msg=f"Should not convert {date_string}",
            )

    def test_ambiguous_client_format_dates_from_string(self):
        date1 = DateTimeUtil.from_string("12/05/2018 00:00", self.client_format)
        date2 = DateTimeUtil.from_string("05/12/2018 00:00", self.client_format)
        self.assertTrue(date1 < date2)

    def test_should_convert_dates_to_client_format_string(self):
        dates = {
            datetime(2018, 5, 26, 0, 0): "26/05/2018 00:00",
            datetime(2018, 5, 26, 12, 30): "26/05/2018 12:30",
            datetime(2000, 1, 1, 0, 0): "01/01/2000 00:00",
        }
        for date, expected in dates.items():
            self.assertEqual(DateTimeUtil.to_string(date, self.client_format), expected)

    def test_from_sql_date_string(self):
        self.fail("Not yet implemented")

    def test_from_sql_datetime_string(self):
        self.fail("Not yet implemented")

    def test_to_sql_date_string(self):
        self.fail("Not yet implemented")

    def test_to_sql_datetime_string(self):
        self.fail("Not yet implemented")

    def test_should_throw_invalid_parameter_exception_on_unknown_format(self):
        date_string = "14081947 010000"
        with self.assertRaises(ValueError):
            DateTimeUtil.detect_date_format(date_string)

    def test_should_detect_sql_date_format(self):
        date_string = "1947-08-14 01:00:00"
        detected_format = DateTimeUtil.detect_date_format(date_string)
        self.assertEqual(detected_format, self.sql_format)

    def test_should_detect_client_date_format(self):
        date_string = "14/08/1947 01:00"
        detected_format = DateTimeUtil.detect_date_format(date_string)
        self.assertEqual(detected_format, self.client_format)

    def test_should_not_detect_client_date_format(self):
        date_string = "1947-08-14 01:00:00"
        detected_format = DateTimeUtil.detect_date_format(date_string)
        self.assertNotEqual(detected_format, self.client_format)


if __name__ == "__main__":
    unittest.main()
