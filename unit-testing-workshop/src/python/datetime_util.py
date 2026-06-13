from __future__ import annotations

import re
from datetime import datetime
from typing import Optional


STANDARD_DATE = "%d/%m/%Y"
STANDARD_DATETIME = "%d/%m/%Y %H:%M:%S"

STANDARD_DATE_HYPHENATED = "%d-%m-%Y"
STANDARD_DATETIME_HYPHENATED = "%d-%m-%Y %H:%M:%S"

SQL_DATE = "%Y-%m-%d"
SQL_DATESTAMP = "%Y%m%d"
SQL_DATETIME = "%Y-%m-%d %H:%M:%S"
SQL_TIMESTAMP = "%Y%m%d%H%M%S"

US_DATE = "%m/%d/%Y"
US_SHORT_DATE = "%m/%d/%y"

ISO8601 = "%Y-%m-%dT%H:%M:%S"
ISO8601_FULL = "%Y-%m-%dT%H:%M:%S.Z"


_DATE_FORMATS: "dict[str, str]" = {
    # Time only
    r"^[0-2]\d[0-5]\d$": "%H%M",
    r"^[0-2]\d:[0-5]\d$": "%H:%M",
    r"^[0-2]\d([0-5]\d){2}$": "%H%M%S",
    r"^[0-2]\d:[0-5]\d:[0-5]\d$": "%H:%M:%S",
    # Date only
    r"^\d{2}[0-1]\d[0-3]\d$": "%y%m%d",
    r"^\d{4}[0-1]\d[0-3]\d$": SQL_DATESTAMP,
    r"^\d{2}-[0-1]\d-[0-3]\d$": "%y-%m-%d",
    r"^\d{4}-[0-1]\d-[0-3]\d$": SQL_DATE,
    r"^[0-3]\d-[0-1]\d-\d{2}$": "%d-%m-%y",
    r"^[0-3]\d-[0-1]\d-\d{4}$": STANDARD_DATE_HYPHENATED,
    r"^[0-3]\d [0-1]\d \d{2}$": "%d %m %y",
    r"^[0-3]\d [0-1]\d \d{4}$": "%d %m %Y",
    r"^[0-3]\d/[0-1]\d/\d{2}$": "%d/%m/%y",
    r"^[0-3]\d/[0-1]\d/\d{4}$": STANDARD_DATE,
    r"^[0-3]\d [a-z]{3} \d{2}$": "%d %b %y",
    r"^[0-3]\d [a-z]{3} \d{4}$": "%d %b %Y",
    # Date and Time
    r"^\d{12,14}$": SQL_TIMESTAMP,
    r"^\d{4}[0-1]\d[0-3]\d[0-2]\d[0-5]\d$": "%Y%m%d%H%M",
    r"^\d{4}[0-1]\d[0-3]\d [0-2]\d[0-5]\d$": "%Y%m%d %H%M",
    r"^\d{4}[0-1]\d[0-3]\d[0-2]\d([0-5]\d){2}$": SQL_TIMESTAMP,
    r"^\d{4}[0-1]\d[0-3]\d [0-2]\d([0-5]\d){2}$": "%Y%m%d %H%M%S",
    r"^\d{4}-[0-1]\d-[0-3]\d [0-2]\d:[0-5]\d$": "%Y-%m-%d %H:%M",
    r"^\d{4}-[0-1]\d-[0-3]\d [0-2]\d:[0-5]\d:[0-5]\d$": SQL_DATETIME,
    # ISO8601
    r"^\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{2}:\d{2}\.\d{2,4}$": "%Y-%m-%d %H:%M:%S.Z",
}


class DateTimeUtil:
    """Static helpers for parsing and formatting datetimes."""

    STANDARD_DATE = STANDARD_DATE
    STANDARD_DATETIME = STANDARD_DATETIME
    STANDARD_DATE_HYPHENATED = STANDARD_DATE_HYPHENATED
    STANDARD_DATETIME_HYPHENATED = STANDARD_DATETIME_HYPHENATED
    SQL_DATE = SQL_DATE
    SQL_DATESTAMP = SQL_DATESTAMP
    SQL_DATETIME = SQL_DATETIME
    SQL_TIMESTAMP = SQL_TIMESTAMP
    US_DATE = US_DATE
    US_SHORT_DATE = US_SHORT_DATE
    ISO8601 = ISO8601
    ISO8601_FULL = ISO8601_FULL

    def __init__(self) -> None:
        raise TypeError("DateTimeUtil is a utility class and cannot be instantiated")

    @staticmethod
    def from_string(string: Optional[str], fmt: str) -> Optional[datetime]:
        if string is None:
            return None
        try:
            return datetime.strptime(string, fmt)
        except ValueError:
            try:
                detected = DateTimeUtil.detect_date_format(string)
                return datetime.strptime(string, detected)
            except ValueError:
                return None

    @staticmethod
    def to_string(date: Optional[datetime], fmt: str) -> Optional[str]:
        if date is None:
            return None
        return date.strftime(fmt)

    @staticmethod
    def from_sql_date_string(sql_date: str) -> Optional[datetime]:
        return DateTimeUtil.from_string(sql_date, SQL_DATE)

    @staticmethod
    def from_sql_datetime_string(sql_date: str) -> Optional[datetime]:
        return DateTimeUtil.from_string(sql_date, SQL_DATETIME)

    @staticmethod
    def to_sql_date_string(date: Optional[datetime]) -> Optional[str]:
        return DateTimeUtil.to_string(date, SQL_DATE)

    @staticmethod
    def to_sql_datetime_string(date: Optional[datetime]) -> Optional[str]:
        return DateTimeUtil.to_string(date, SQL_DATETIME)

    @staticmethod
    def detect_date_format(date_string: str) -> str:
        """Return the closest matching format string for ``date_string``.

        Raises ``ValueError`` if nothing matches (mirrors Java's
        ``InvalidParameterException``, which is unchecked).
        """
        lowered = date_string.lower()
        for pattern, fmt in _DATE_FORMATS.items():
            if re.match(pattern, lowered):
                return fmt
        raise ValueError("Given date does not match any of the standard conventions.")


def _main() -> None:
    client_format = "%d/%m/%Y %H:%M"
    date = DateTimeUtil.from_string("01/01/2018 16:55", client_format)
    if (
        date is not None
        and date.year == 2018
        and date.month == 1
        and date.day == 1
        and date.hour == 16
        and date.minute == 55
        and date.second == 0
    ):
        print("Conversion to client format OK!")
    else:
        print("Failed to conversion to client format!")
    date1 = DateTimeUtil.from_string("12/05/2018 00:00", client_format)
    date2 = DateTimeUtil.from_string("05/12/2018 00:00", client_format)
    if date1 is not None and date2 is not None and date1 < date2:
        print("Ambiguity handled well!")
    else:
        print("Ambiguity check failed.")


if __name__ == "__main__":
    _main()
