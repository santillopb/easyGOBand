package com.androidstudio.easyGOBand;

import java.util.List;

public class Provider {
    private int id;
    private String name;
    private int accessGroupId;
    private String accessGroupName;
    private int totalUses;
    private List<Session> sessions;
    private int eventId;
    private boolean structureDecode;
    private String modified;
    private int basicProductId;

    public Provider(int id, String name, int accessGroupId, String accessGroupName, int totalUses, List<Session> sessions, int eventId, boolean structureDecode, String modified, int basicProductId) {
        this.id = id;
        this.name = name;
        this.accessGroupId = accessGroupId;
        this.accessGroupName = accessGroupName;
        this.totalUses = totalUses;
        this.sessions = sessions;
        this.eventId = eventId;
        this.structureDecode = structureDecode;
        this.modified = modified;
        this.basicProductId = basicProductId;
    }

    public Provider() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccessGroupId() {
        return accessGroupId;
    }

    public void setAccessGroupId(int accessGroupId) {
        this.accessGroupId = accessGroupId;
    }

    public String getAccessGroupName() {
        return accessGroupName;
    }

    public void setAccessGroupName(String accessGroupName) {
        this.accessGroupName = accessGroupName;
    }

    public int getTotalUses() {
        return totalUses;
    }

    public void setTotalUses(int totalUses) {
        this.totalUses = totalUses;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isStructureDecode() {
        return structureDecode;
    }

    public void setStructureDecode(boolean structureDecode) {
        this.structureDecode = structureDecode;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getBasicProductId() {
        return basicProductId;
    }

    public void setBasicProductId(int basicProductId) {
        this.basicProductId = basicProductId;
    }
}
