package org.tron.common.zksnark.merkle;

import java.util.ArrayList;
import java.util.List;

public class MerkleUtils {

  // Convert bytes into boolean vector. (MSB to LSB)
  public static List<Boolean> convertBytesVectorToVector(final byte[] bytes) {
    List<Boolean> ret = new ArrayList<>();

    byte c;
    for (int i = 0; i < bytes.length; i++) {
      c = bytes[i];
      for (int j = 0; j < 8; j++) {
        ret.add(((c >> (7 - j)) & 1) == 1);
      }
    }

    return ret;
  }
}
