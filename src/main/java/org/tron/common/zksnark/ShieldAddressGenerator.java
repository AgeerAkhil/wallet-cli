package org.tron.common.zksnark;

import java.util.Random;
import org.tron.common.crypto.dh25519.MontgomeryOperations;
import org.tron.common.crypto.eddsa.KeyPairGenerator;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.ZksnarkUtils;

public class ShieldAddressGenerator {

  public static final int LENGTH = 32; // bytes

  public byte[] generatePrivateKey() {
    return generatePrivateKey(0L);
  }

  public byte[] generatePrivateKey(long seed) {
    byte[] result = new byte[LENGTH];
    if (seed != 0L) {
      new Random(seed).nextBytes(result);
    } else {
      new Random().nextBytes(result);
    }
    Integer i = result[0] & 0x0F;
    result[0] = i.byteValue();
    return result;
  }


  public static byte[] generatePublicKey(byte[] privateKey) {
//    if (privateKey.length != 32) {
//      throw new RuntimeException("Wrong length，expect：256，real：" + privateKey.length);
//    }
//    if ((privateKey[0] & 0xF0) != 0) {
//      throw new RuntimeException("The first 4 digits must be 0");
//    }
    return Prf.prfAddrAPk(privateKey);
  }

  public byte[] generatePrivateKeyEnc(byte[] privateKey) {
    return Prf.prfAddrSkEnc(privateKey);
  }


  public static byte[] generatePublicKeyEnc(byte[] privateKeyEnc) {
    byte[] base = new byte[]{
        9, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    };
    byte[] output = new byte[32];
    MontgomeryOperations.scalarmult(output, 0, privateKeyEnc, 0, base, 0);
    return output;
    //  return ZksnarkUtils.scalarMultiply(B,privateKeyEnc);
  }

  public static void main(String[] args) {
    ShieldAddressGenerator shieldAddressGenerator = new ShieldAddressGenerator();

    byte[] privateKey = shieldAddressGenerator.generatePrivateKey(100L);
    byte[] publicKey = shieldAddressGenerator.generatePublicKey(privateKey);

    byte[] privateKeyEnc = shieldAddressGenerator.generatePrivateKeyEnc(privateKey);
    byte[] publicKeyEnc = shieldAddressGenerator.generatePublicKeyEnc(privateKey);

    String privateKeyString = ByteArray.toHexString(privateKey);
    String publicKeyString = ByteArray.toHexString(publicKey);
    String privateKeyEncString = ByteArray.toHexString(privateKeyEnc);
    String publicKeyEncString = ByteArray.toHexString(publicKeyEnc);

    System.out.println("privateKey:" + privateKeyString);
    System.out.println("publicKey :" + publicKeyString);
    System.out.println("privateKeyEnc :" + privateKeyEncString);
    System.out.println("publicKeyEnc :" + publicKeyEncString);


  }


}
