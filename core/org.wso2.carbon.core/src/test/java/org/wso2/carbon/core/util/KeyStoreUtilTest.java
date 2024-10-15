/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.core.util;

import org.apache.axiom.om.OMElement;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.wso2.carbon.CarbonException;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.core.RegistryResources;
import org.wso2.carbon.core.internal.CarbonCoreDataHolder;
import org.wso2.carbon.utils.ServerConstants;

import javax.xml.namespace.QName;

import java.nio.file.Paths;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class KeyStoreUtilTest {

    private static final String basedir = Paths.get("").toAbsolutePath().toString();
    private static final String testDir = Paths.get(basedir, "src", "test", "resources").toString();

    @Mock
    private ServerConfigurationService serverConfigurationService;
    @Mock
    private CarbonCoreDataHolder carbonCoreDataHolder;

    @BeforeClass
    public void setUp() throws Exception {

        initMocks(this);
    }

    @DataProvider(name = "PrimaryStoreDataProvider")
    public Object[][] primaryStoreDataProvider() {
        return new Object[][] {
            {"wso2carbon.jks", true},
            {"", false},
            {"nonexistent.jks", false}
        };
    }

    @Test(dataProvider = "PrimaryStoreDataProvider")
    public void isPrimaryStore(String keyStoreId, boolean expectedResult) {

        String primaryKeystoreFile = testDir + "/wso2carbon.jks";
        try (MockedStatic<CarbonCoreDataHolder> carbonCoreDataHolder = mockStatic(CarbonCoreDataHolder.class)) {

            carbonCoreDataHolder.when(CarbonCoreDataHolder::getInstance).thenReturn(this.carbonCoreDataHolder);
            when(this.carbonCoreDataHolder.getServerConfigurationService()).thenReturn(serverConfigurationService);
            when(serverConfigurationService.getFirstProperty(
                    RegistryResources.SecurityManagement.SERVER_PRIMARY_KEYSTORE_FILE)).thenReturn(primaryKeystoreFile);
            assertEquals(KeyStoreUtil.isPrimaryStore(keyStoreId), expectedResult);
        }
    }

    @DataProvider(name = "KeyStoreNameDataProvider")
    public Object[][] keyStoreNameDataProvider() {

        return new Object[][] {
                {"CUSTOM/myKeyStore", true},
                {"CUSTOM/@#$_keyStore", true},
                {"abcd", false},
                {"", false},
                {" ", false},
        };
    }

    @Test(dataProvider = "KeyStoreNameDataProvider")
    public void testIsCustomKeyStore(String keyStoreName, boolean expectedResult) {

        assertEquals(KeyStoreUtil.isCustomKeyStore(keyStoreName), expectedResult);
    }

    @DataProvider(name = "QNameWithCarbonNSDataProvider")
    public String[] qNameWithCarbonNSDataProvider() {

        return new String[] {
                "localPart",
                "",
        };
    }

    @Test(dataProvider = "QNameWithCarbonNSDataProvider")
    public void testGetQNameWithCarbonNS(String localPart) {

        QName qName = KeyStoreUtil.getQNameWithCarbonNS(localPart);
        assertEquals(ServerConstants.CARBON_SERVER_XML_NAMESPACE, qName.getNamespaceURI());
        assertEquals(localPart, qName.getLocalPart());
    }

    @DataProvider(name = "CustomKeyStoreConfigDataProvider")
    public String[][] customKeyStoreConfigDataProvider() {

        return new String[][] {
                {"custom.jks", "Location", basedir + "/./repository/resources/security/custom.jks"},
                {"custom.jks", "Type", "JKS"},
                {"custom.jks", "Password", "customPassword"},
                {"custom.jks", "KeyAlias", "customAlias"},
                {"custom.jks", "KeyPassword", "customKeyPass"},
                {"testKey.jks", "Location", basedir + "/./repository/resources/security/testKey.jks"},
                {"testKey.jks", "Type", "JKS"},
                {"testKey.jks", "Password", "testPass"},
                {"testKey.jks", "KeyAlias", "testKey"},
                {"testKey.jks", "KeyPassword", "testKeyPass"}
        };
    }

    // This test covers both getCustomKeyStoreConfigElement and getCustomKeyStoreConfig methods
    @Test(dataProvider = "CustomKeyStoreConfigDataProvider")
    public void testReadCustomKeyStoreConfigs(String keyStoreName, String configName, String expectedValue) {

        try {
            String serverConfigPath = Paths.get(testDir, "carbon.xml").toString();
            ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
            serverConfiguration.forceInit(serverConfigPath);
            OMElement keyStoreConfigElement = KeyStoreUtil.getCustomKeyStoreConfigElement(keyStoreName, serverConfiguration);
            assertEquals(KeyStoreUtil.getCustomKeyStoreConfig(keyStoreConfigElement, configName), expectedValue);
        } catch (Exception e) {
            fail();
        }
    }

    @DataProvider(name = "CorrectKeyStoreConfigDataProvider")
    public String[] correctKeyStoreConfigDataProvider() {

        return new String[] {
                "Location",
                "Type",
                "Password",
                "KeyAlias",
                "KeyPassword"
        };
    }

    @Test(dataProvider = "CorrectKeyStoreConfigDataProvider")
    public void testValidateCorrectKeyStoreConfigNames(String configName) {

        try {
            KeyStoreUtil.validateKeyStoreConfigName(configName);
        } catch (CarbonException e) {
            fail();
        }
    }

    @DataProvider(name = "IncorrectKeyStoreConfigDataProvider")
    public String[] incorrectKeyStoreConfigDataProvider() {

        return new String[] {
                "ABCD",
                "",
                " "
        };
    }

    @Test(dataProvider = "IncorrectKeyStoreConfigDataProvider", expectedExceptions = CarbonException.class)
    public void testValidateIncorrectKeyStoreConfigName(String configName) throws Exception {

        KeyStoreUtil.validateKeyStoreConfigName(configName);
    }
}
