package com.easy.eoschain.bean;

import java.util.List;

public class AbiInfo {
    private String version;
    private List<AbiType> types;
    private List<AbiStruct> structs;
    private List<AbiAction> actions;
    private List<AbiTable> tables;
    private List<String> ricardian_clauses;
    private List<String> error_messages;
    private List<String> abi_extensions;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<AbiType> getTypes() {
        return types;
    }

    public void setTypes(List<AbiType> types) {
        this.types = types;
    }

    public List<AbiStruct> getStructs() {
        return structs;
    }

    public void setStructs(List<AbiStruct> structs) {
        this.structs = structs;
    }

    public List<AbiAction> getActions() {
        return actions;
    }

    public void setActions(List<AbiAction> actions) {
        this.actions = actions;
    }

    public List<AbiTable> getTables() {
        return tables;
    }

    public void setTables(List<AbiTable> tables) {
        this.tables = tables;
    }

    public List<String> getRicardian_clauses() {
        return ricardian_clauses;
    }

    public void setRicardian_clauses(List<String> ricardian_clauses) {
        this.ricardian_clauses = ricardian_clauses;
    }

    public List<String> getError_messages() {
        return error_messages;
    }

    public void setError_messages(List<String> error_messages) {
        this.error_messages = error_messages;
    }

    public List<String> getAbi_extensions() {
        return abi_extensions;
    }

    public void setAbi_extensions(List<String> abi_extensions) {
        this.abi_extensions = abi_extensions;
    }
}
