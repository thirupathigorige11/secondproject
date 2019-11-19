package com.sumadhura.util;
public class Base64EncoderDecoder
{
  public static final String logName = "Base64EncoderDecoder";
  static final char[] valueToChar = new char[64];

  static final int[] charToValue = new int[256];
  static final int IGNORE = -1;
  static final int PAD = -2;

  static
  {
    for (int i = 0; i <= 25; i++) {
      valueToChar[i] = ((char)(65 + i));
    }

    for (int i = 0; i <= 25; i++) {
      valueToChar[(i + 26)] = ((char)(97 + i));
    }

    for (int i = 0; i <= 9; i++) {
      valueToChar[(i + 52)] = ((char)(48 + i));
    }
    valueToChar[62] = '+';
    valueToChar[63] = '/';

    for (int i = 0; i < 256; i++) {
      charToValue[i] = -1;
    }

    for (int i = 0; i < 64; i++) {
      charToValue[valueToChar[i]] = i;
    }

    charToValue[61] = -2;
  }

  public static byte[] decode(String s)
  {
    s.trim();

    byte[] b = new byte[s.length() / 4 * 3];

    int cycle = 0;

    int combined = 0;

    int j = 0;

    int len = s.length();
    int dummies = 0;
    for (int i = 0; i < len; i++) {
      int c = s.charAt(i);
      int value = c <= 255 ? charToValue[c] : -1;

      switch (value)
      {
      case -1:
        break;
      case -2:
        value = 0;
        dummies++;
      default:
        switch (cycle) {
        case 0:
          combined = value;
          cycle = 1;
          break;
        case 1:
          combined <<= 6;
          combined |= value;
          cycle = 2;
          break;
        case 2:
          combined <<= 6;
          combined |= value;
          cycle = 3;
          break;
        case 3:
          combined <<= 6;
          combined |= value;

          b[(j + 2)] = ((byte)combined);
          combined >>>= 8;
          b[(j + 1)] = ((byte)combined);
          combined >>>= 8;
          b[j] = ((byte)combined);
          j += 3;
          cycle = 0;
        }

        break;
      }

    }

    j -= dummies;
    if (b.length != j) {
      byte[] b2 = new byte[j];
      System.arraycopy(b, 0, b2, 0, j);
      b = b2;
    }
    return b;
  }

  public static String encode(byte[] binInput)
  {
    int outputLength = (binInput.length + 2) / 3 * 4;

    outputLength += (outputLength - 1) / 76 * 2;

    StringBuffer sb = new StringBuffer(outputLength);

    int linePos = 0;

    int evenLength = binInput.length / 3 * 3;
    int leftover = binInput.length - evenLength;
    for (int i = 0; i < evenLength; i += 3) {
      linePos += 4;
      if (linePos > 76) {
        linePos = 0;
        sb.append("\r\n");
      }

      int combined = binInput[(i + 0)] & 0xFF;
      combined <<= 8;
      combined |= binInput[(i + 1)] & 0xFF;
      combined <<= 8;
      combined |= binInput[(i + 2)] & 0xFF;

      int c3 = combined & 0x3F;
      combined >>>= 6;
      int c2 = combined & 0x3F;
      combined >>>= 6;
      int c1 = combined & 0x3F;
      combined >>>= 6;
      int c0 = combined & 0x3F;

      sb.append(valueToChar[c0]);
      sb.append(valueToChar[c1]);
      sb.append(valueToChar[c2]);
      sb.append(valueToChar[c3]);
    }

    switch (leftover)
    {
    case 0:
    default:
      break;
    case 1:
      linePos += 4;
      if (linePos > 76) {
        linePos = 0;
        sb.append("\r\n");
      }

      sb.append(
        encode(new byte[] { binInput[evenLength] }).substring(0, 2));
      sb.append("==");
      break;
    case 2:
      linePos += 4;
      if (linePos > 76) {
        linePos = 0;
        sb.append("\r\n");
      }

      sb.append(encode(
        new byte[] { binInput[evenLength], 
        binInput[(evenLength + 1)] }).substring(0, 3));
      sb.append("=");
    }

    return sb.toString();
  }
}