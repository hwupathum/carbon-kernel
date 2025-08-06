package org.wso2.carbon.user.core.hash;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.wso2.carbon.user.core.exceptions.HashProviderException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BCryptHashProvider implements HashProvider {

    private static final String ALGORITHM = "BCrypt";

    @Override
    public void init() {

    }

    @Override
    public void init(Map<String, Object> initProperties) throws HashProviderException {

    }

    @Override
    public byte[] calculateHash(char[] plainText, String salt) throws HashProviderException {

        return OpenBSDBCrypt.generate(plainText, salt.getBytes(StandardCharsets.UTF_8), 2).getBytes();
    }

    @Override
    public boolean verify(char[] plainText, String hashedPassword, String salt) throws HashProviderException {

        return OpenBSDBCrypt.checkPassword(hashedPassword, plainText);
    }

    @Override
    public Map<String, Object> getParameters() {

        return null;
    }

    @Override
    public String getAlgorithm() {

        return null;
    }
}
