/*
 * java-tron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * java-tron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tron.common.utils;


import static org.tron.protos.Protocol.Transaction.Contract.ContractType.ZksnarkV0TransferContract;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.ECKey.ECDSASignature;
import org.tron.common.crypto.Sha256Hash;
import org.tron.common.crypto.blake2b.Blake2b;
import org.tron.common.crypto.eddsa.EdDSAEngine;
import org.tron.common.crypto.eddsa.EdDSAPublicKey;
import org.tron.common.crypto.eddsa.spec.EdDSANamedCurveSpec;
import org.tron.common.crypto.eddsa.spec.EdDSANamedCurveTable;
import org.tron.common.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.tron.protos.Contract.ZksnarkV0TransferContract;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Transaction.Contract;

public class ZksnarkUtils {

  public static byte[] computeHSig(ZksnarkV0TransferContract zkContract) {
    byte[] message = ByteUtil
        .merge(zkContract.getRandomSeed().toByteArray(), zkContract.getNf1().toByteArray(),
            zkContract.getNf2().toByteArray(), zkContract.getPksig().toByteArray());
    return Blake2b.hash(message);
  }

  public static byte[] computeZkSignInput(ZksnarkV0TransferContract zkContract) {
    byte[] hSig = computeHSig(zkContract);
    ZksnarkV0TransferContract.Builder builder = zkContract.toBuilder();
    builder.setRandomSeed(ByteString.EMPTY);
    builder.setPksig(ByteString.copyFrom(hSig));
    return builder.build().toByteArray();
  }

  public static EdDSAPublicKey byte2PublicKey(byte[] pk) {
    EdDSANamedCurveSpec curveSpec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
    EdDSAPublicKeySpec spec = new EdDSAPublicKeySpec(pk, curveSpec);
    EdDSAPublicKey publicKey = new EdDSAPublicKey(spec);
    return publicKey;
  }
}
