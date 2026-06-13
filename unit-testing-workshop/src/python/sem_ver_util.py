from __future__ import annotations

import re
from typing import Optional


_VALID_PREALPHA_REGEX = r"^0\.0\.[0-9]+$"
_VALID_ALPHA_REGEX = r"^0\.[0-9]+\.[0-9]+\-(alpha)$"
_VALID_BETA_REGEX = r"^0\.[0-9]+\.[0-9]+\-(beta)$"
_VALID_RELEASE_REGEX = r"^[0-9]+\.[0-9]+\.[0-9]+$"
_VERSION_SPLITTER = "."
_EXTENSION_SEPARATOR = "-"


class SemVerUtil:
    """Parsed semantic-version triple with optional ``alpha``/``beta`` extension."""

    def __init__(self, version: str) -> None:
        if version is None:
            raise TypeError("Input cannot be null")
        self._major = 0
        self._minor = 0
        self._micro = 0
        self._extension: Optional[str] = None
        self._parse_version(version.lower())

    def _parse_version(self, version: str) -> None:
        if re.match(_VALID_PREALPHA_REGEX, version):
            parts = version.split(_VERSION_SPLITTER)
            self.set_major(0)
            self.set_minor(0)
            self.set_micro(int(parts[2]))
            self._extension = None
        elif re.match(_VALID_ALPHA_REGEX, version):
            self._extension = "alpha"
            version = version[: version.rfind(_EXTENSION_SEPARATOR)]
            parts = version.split(_VERSION_SPLITTER)
            self.set_major(0)
            self.set_minor(int(parts[1]))
            self.set_micro(int(parts[2]))
        elif re.match(_VALID_BETA_REGEX, version):
            self._extension = "beta"
            version = version[: version.rfind(_EXTENSION_SEPARATOR)]
            parts = version.split(_VERSION_SPLITTER)
            self.set_major(0)
            self.set_minor(int(parts[1]))
            self.set_micro(int(parts[2]))
        elif re.match(_VALID_RELEASE_REGEX, version):
            self._extension = None
            parts = version.split(_VERSION_SPLITTER)
            self.set_major(int(parts[0]))
            self.set_minor(int(parts[1]))
            self.set_micro(int(parts[2]))
        else:
            raise ValueError("Invalid input!")

    def get_major(self) -> int:
        return self._major

    def set_major(self, major: int) -> None:
        self._major = major

    def get_minor(self) -> int:
        return self._minor

    def set_minor(self, minor: int) -> None:
        self._minor = minor

    def get_micro(self) -> int:
        return self._micro

    def set_micro(self, micro: int) -> None:
        self._micro = micro

    def is_pre_alpha(self) -> bool:
        return self.get_major() == 0 and self.get_minor() == 0 and self._extension is None

    def is_alpha(self) -> bool:
        if self._extension is None:
            return False
        return self._extension == "alpha"

    def is_beta(self) -> bool:
        if self._extension is None:
            return False
        return self._extension == "beta"

    def is_release(self) -> bool:
        return not (self.is_pre_alpha() or self.is_alpha() or self.is_beta()) and self._extension is None

    @staticmethod
    def compare_versions(older: "SemVerUtil", newer: "SemVerUtil") -> int:
        """Return 0 if equal, 1 if ``newer`` is ahead of ``older``, -1 otherwise."""
        if str(older) == str(newer):
            return 0
        if older.is_pre_alpha() and not newer.is_pre_alpha():
            return 1
        if older.is_alpha() and (newer.is_beta() or newer.is_release()):
            return 1
        if older.is_beta() and newer.is_release():
            return 1
        all_flags = (
            older.is_pre_alpha() == newer.is_pre_alpha()
            or older.is_alpha() == newer.is_alpha()
            or older.is_beta() == newer.is_beta()
            or older.is_release() == newer.is_release()
        )
        if all_flags:
            if older.get_major() < newer.get_major():
                return 1
            if older.get_major() == newer.get_major() and older.get_minor() < newer.get_minor():
                return 1
            if (
                older.get_major() == newer.get_major()
                and older.get_minor() == newer.get_minor()
                and older.get_micro() < newer.get_micro()
            ):
                return 1
        return -1

    def is_backwards_compatible_with(self, other: "SemVerUtil") -> bool:
        # TODO: Two versions are backwards compatible if they are equal or if the major and minor versions are equal
        return False

    def __str__(self) -> str:
        extension = "" if self._extension is None else "-" + self._extension
        return f"{self.get_major()}.{self.get_minor()}.{self.get_micro()}{extension}"
