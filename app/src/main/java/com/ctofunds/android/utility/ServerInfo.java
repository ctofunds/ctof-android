package com.ctofunds.android.utility;

import com.google.common.collect.Maps;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by qianhao.zhou on 2/14/15.
 */
public final class ServerInfo {

    public static enum ServerType {
        PRODUCTION,
        STAGING,
        QA,
        DEV,
        CUSTOM
    }

    public static ServerInfo getInstance() {
        return SingletonHolder.instance;
    }

    public Map<ServerType, String> getAllServers() {
        return this.serverList;
    }
    public String getServerHost() {
        return serverList.get(type);
    }
    public String getServerHost(ServerType type) {
        return serverList.get(type);
    }

    public ServerType getServerType() {
        return this.type;
    }
    public void setServerType(ServerType type) {
        this.type = type;
    }

    private ServerType type = ServerType.PRODUCTION;
    private EnumMap<ServerType, String> serverList;

    private static class SingletonHolder {
        private static ServerInfo instance = new ServerInfo();
    }

    private ServerInfo() {
        serverList = Maps.newEnumMap(ServerType.class);
        serverList.put(ServerType.PRODUCTION, "http://localhost:8080");
        serverList.put(ServerType.STAGING, "http://121.40.28.231:8080");
        serverList.put(ServerType.QA, "http://121.40.28.231:8080");
        serverList.put(ServerType.DEV, "http://121.40.28.231:8080");
        serverList.put(ServerType.CUSTOM, "http://121.40.28.231:8080");
    }

}
