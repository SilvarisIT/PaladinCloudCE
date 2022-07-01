package com.tmobile.pacbot.gcp.inventory.collector;


import com.google.cloud.kms.v1.CryptoKey;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyRing;
import com.google.cloud.kms.v1.KeyRingName;
import com.google.cloud.location.Location;
import com.google.iam.v1.Policy;
import com.tmobile.pacbot.gcp.inventory.InventoryConstants;
import com.tmobile.pacbot.gcp.inventory.auth.GCPCredentialsProvider;
import com.tmobile.pacbot.gcp.inventory.vo.CloudSqlVH;
import com.tmobile.pacbot.gcp.inventory.vo.IPAddress;
import com.tmobile.pacbot.gcp.inventory.vo.KmsKeyVH;
import com.tmobile.pacbot.gcp.inventory.vo.ServerCaCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class KmsKeyInventoryCollector {

    @Autowired
    GCPCredentialsProvider gcpCredentialsProvider;

    private static final Logger logger = LoggerFactory.getLogger(KmsKeyInventoryCollector.class);

    public List<KmsKeyVH> fetchKmsKeysInventory(String projectId) throws IOException {
        logger.info("Running collector for cloud KMS keys inventory.");
        List<KmsKeyVH> kmsKeyList = new ArrayList<>();

        try {
            KeyManagementServiceClient kmsKeyClient = gcpCredentialsProvider.getKmsKeyServiceClient();
            String keyRingName="cool-bay-349411-cloudsql-key-ring";
            String parent = KeyRingName.of(projectId, "us-east1", keyRingName).toString();
            logger.info("Fetching keyList for parent:{}", parent);
            KeyManagementServiceClient.ListCryptoKeysPagedResponse listCryptoKeysPagedResponse = kmsKeyClient.listCryptoKeys(parent);
            logger.info("Crypto list response: {}",listCryptoKeysPagedResponse);
            for (CryptoKey cryptoKey : listCryptoKeysPagedResponse.iterateAll()) {
                KmsKeyVH kmsKeyVH = new KmsKeyVH();
                logger.info("Crypto key details: {}", cryptoKey);
                kmsKeyVH.set_cloudType(InventoryConstants.CLOUD_TYPE_GCP);
                kmsKeyVH.setId(cryptoKey.getName());
                kmsKeyVH.setProjectName(projectId);
                kmsKeyVH.setCreateTime(cryptoKey.getCreateTime().toString());
                kmsKeyVH.setName(cryptoKey.getName());
                kmsKeyVH.setCryptoBackend(cryptoKey.getCryptoKeyBackend());
                kmsKeyVH.setImportOnly(cryptoKey.getImportOnly());
                kmsKeyVH.setLabelsCount(cryptoKey.getLabelsCount());
                kmsKeyVH.setLabels(cryptoKey.getLabelsMap());
                setIamPolicies(kmsKeyVH, kmsKeyClient.getIamPolicy(cryptoKey.getName()));
                kmsKeyList.add(kmsKeyVH);
            }

        } catch (Exception e) {
            logger.error("Exception in fetching kms key data", e);
        }

        logger.info("KMS key data collected list size: {}", kmsKeyList.size());
        return kmsKeyList;
    }

    private void setIamPolicies(KmsKeyVH kmsKeyVH, Policy iamPolicy) {
        logger.info("Iam policies: {}",iamPolicy);
        if (iamPolicy != null && !CollectionUtils.isEmpty(iamPolicy.getBindingsList())) {
            kmsKeyVH.setBindings(iamPolicy.getBindingsList());
        }

    }

}
