package com.tmobile.pacbot.gcp.inventory.vo;

import com.google.iam.v1.Binding;

import java.util.List;
import java.util.Map;

public class KmsKeyVH extends GCPVH {
    private String name;
    private String createTime;
    private String cryptoBackend;
    private boolean importOnly;
    private int labelsCount;
    private Map<String, String> labels;
    private List<Binding> bindings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCryptoBackend() {
        return cryptoBackend;
    }

    public void setCryptoBackend(String cryptoBackend) {
        this.cryptoBackend = cryptoBackend;
    }

    public boolean isImportOnly() {
        return importOnly;
    }

    public void setImportOnly(boolean importOnly) {
        this.importOnly = importOnly;
    }

    public int getLabelsCount() {
        return labelsCount;
    }

    public void setLabelsCount(int labelsCount) {
        this.labelsCount = labelsCount;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }
}
