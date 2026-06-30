#!/usr/bin/env python3
"""Decode a GitHub Actions KEYSTORE_BASE64 secret into a keystore file.

The script accepts raw Base64, copied KEYSTORE_BASE64=... text, quoted text,
line-wrapped text, and data-URI style values. It strips invisible whitespace
and validates the final Base64 before writing bytes.
"""

from __future__ import annotations

import base64
import binascii
import re
import sys
from pathlib import Path

BASE64_CHARS = set("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=")


def fail(message: str) -> None:
    print(f"::error::{message}", file=sys.stderr)
    raise SystemExit(1)


def normalize_secret(value: str) -> str:
    cleaned = value.strip().strip('"').strip("'")

    for prefix in ("KEYSTORE_BASE64=", "KEYSTORE_BASE64:"):
        if cleaned.startswith(prefix):
            cleaned = cleaned[len(prefix) :].strip()

    if "," in cleaned and "base64" in cleaned[:120].lower():
        cleaned = cleaned.split(",", 1)[1]

    cleaned = re.sub(r"\s+", "", cleaned)
    cleaned = "".join(char for char in cleaned if char in BASE64_CHARS)
    cleaned = cleaned.rstrip("=")

    if len(cleaned) < 100:
        fail("KEYSTORE_BASE64 is too short. Regenerate it from release-keystore.jks.")

    cleaned += "=" * (-len(cleaned) % 4)
    return cleaned


def main() -> None:
    if len(sys.argv) != 3:
        fail("Usage: decode_keystore_secret.py <KEYSTORE_BASE64> <output-file>")

    secret_value = sys.argv[1]
    output_path = Path(sys.argv[2])
    normalized = normalize_secret(secret_value)

    try:
        decoded = base64.b64decode(normalized, validate=True)
    except binascii.Error as error:
        fail(f"KEYSTORE_BASE64 could not be decoded: {error}")

    if len(decoded) < 100:
        fail("Decoded keystore file is too small. The secret is incomplete or corrupted.")

    output_path.write_bytes(decoded)
    print(f"Decoded keystore bytes: {len(decoded)}")


if __name__ == "__main__":
    main()
