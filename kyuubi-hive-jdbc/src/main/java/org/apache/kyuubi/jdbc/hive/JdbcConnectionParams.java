/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.jdbc.hive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcConnectionParams {
  // Note on client side parameter naming convention:
  // Prefer using a shorter camelCase param name instead of using the same name as the
  // corresponding
  // HiveServer2 config.
  // For a jdbc url:
  // jdbc:hive2://<host>:<port>/catalogName/dbName;sess_var_list?hive_conf_list#hive_var_list,
  // client side params are specified in sess_var_list

  // Client param names:

  static final String CLIENT_PROTOCOL_VERSION = "clientProtocolVersion";
  // Retry setting
  static final String RETRIES = "retries";

  public static final String AUTH_TYPE = "auth";
  // We're deprecating this variable's name.
  public static final String AUTH_QOP_DEPRECATED = "sasl.qop";
  public static final String AUTH_QOP = "saslQop";
  public static final String AUTH_SIMPLE = "noSasl";
  public static final String AUTH_USER = "user";
  public static final String AUTH_PRINCIPAL = "principal";
  // kyuubiServerPrincipal is alias to principal. And principal will take precedence
  // over kyuubiServerPrincipal when both are set.
  public static final String AUTH_KYUUBI_SERVER_PRINCIPAL = "kyuubiServerPrincipal";
  public static final String AUTH_KYUUBI_CLIENT_PRINCIPAL = "kyuubiClientPrincipal";
  public static final String AUTH_KYUUBI_CLIENT_KEYTAB = "kyuubiClientKeytab";
  public static final String AUTH_KYUUBI_CLIENT_TICKET_CACHE = "kyuubiClientTicketCache";
  public static final String AUTH_PASSWD = "password";
  public static final String AUTH_KERBEROS_AUTH_TYPE = "kerberosAuthType";
  public static final String AUTH_KERBEROS_AUTH_TYPE_FROM_KEYTAB = "fromKeytab";
  public static final String AUTH_KERBEROS_AUTH_TYPE_FROM_SUBJECT = "fromSubject";
  public static final String AUTH_KERBEROS_AUTH_TYPE_FROM_TICKET_CACHE = "fromTicketCache";
  public static final String AUTH_KERBEROS_ENABLE_CANONICAL_HOSTNAME_CHECK =
      "kerberosEnableCanonicalHostnameCheck";
  public static final String AUTH_TYPE_JWT = "jwt";
  public static final String AUTH_TYPE_JWT_KEY = "jwt";
  public static final String AUTH_JWT_ENV = "JWT";
  public static final String ANONYMOUS_USER = "anonymous";
  public static final String ANONYMOUS_PASSWD = "anonymous";
  public static final String USE_SSL = "ssl";
  public static final String SSL_TRUST_STORE = "sslTrustStore";
  public static final String SSL_TRUST_STORE_PASSWORD = "trustStorePassword";
  // We're deprecating the name and placement of this in the parsed map (from hive conf vars to
  // hive session vars).
  static final String TRANSPORT_MODE_DEPRECATED = "hive.server2.transport.mode";
  public static final String TRANSPORT_MODE = "transportMode";
  // We're deprecating the name and placement of this in the parsed map (from hive conf vars to
  // hive session vars).
  static final String HTTP_PATH_DEPRECATED = "hive.server2.thrift.http.path";
  public static final String HTTP_PATH = "httpPath";
  public static final String SERVICE_DISCOVERY_MODE = "serviceDiscoveryMode";
  public static final String PROPERTY_DRIVER = "driver";
  public static final String PROPERTY_URL = "url";
  // Don't use dynamic service discovery
  static final String SERVICE_DISCOVERY_MODE_NONE = "none";
  // Use ZooKeeper for indirection while using dynamic service discovery
  static final String SERVICE_DISCOVERY_MODE_ZOOKEEPER = "zooKeeper";
  static final String ZOOKEEPER_NAMESPACE = "zooKeeperNamespace";
  static final String SERVER_SELECT_STRATEGY = "serverSelectStrategy";
  // Default namespace value on ZooKeeper.
  // This value is used if the param "zooKeeperNamespace" is not specified in the JDBC Uri.
  static final String ZOOKEEPER_DEFAULT_NAMESPACE = "hiveserver2";
  static final String COOKIE_AUTH = "cookieAuth";
  static final String COOKIE_NAME = "cookieName";
  // The default value of the cookie name when CookieAuth=true
  static final String DEFAULT_COOKIE_NAMES_HS2 = "hive.server2.auth";
  // The http header prefix for additional headers which have to be appended to the request
  static final String HTTP_HEADER_PREFIX = "http.header.";
  // Set the fetchSize
  static final String FETCH_SIZE = "fetchSize";
  static final String INIT_FILE = "initFile";
  static final String WM_POOL = "wmPool";
  // Cookie prefix
  static final String HTTP_COOKIE_PREFIX = "http.cookie.";

  static final String CONNECT_TIMEOUT = "connectTimeout";
  static final String SOCKET_TIMEOUT = "socketTimeout";
  static final String THRIFT_CLIENT_MAX_MESSAGE_SIZE = "thrift.client.max.message.size";

  // We support ways to specify application name modeled after some existing DBs, since
  // there's no standard approach.
  // MSSQL: applicationName
  // https://docs.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url
  // Postgres 9~: ApplicationName https://jdbc.postgresql.org/documentation/91/connect.html
  // Note: various ODBC names used include "Application Name", "APP", etc. Add those?
  static final String[] APPLICATION = new String[] {"applicationName", "ApplicationName"};

  // --------------- Begin 2 way ssl options -------------------------
  // Use two way ssl. This param will take effect only when ssl=true
  static final String USE_TWO_WAY_SSL = "twoWay";
  static final String TRUE = "true";
  static final String SSL_KEY_STORE = "sslKeyStore";
  static final String SSL_KEY_STORE_PASSWORD = "keyStorePassword";
  static final String SSL_KEY_STORE_TYPE = "JKS";
  static final String SUNX509_ALGORITHM_STRING = "SunX509";
  static final String SUNJSSE_ALGORITHM_STRING = "SunJSSE";
  // --------------- End 2 way ssl options ----------------------------

  // Non-configurable params:
  // Currently supports JKS keystore format
  static final String SSL_TRUST_STORE_TYPE = "JKS";

  static final String SSL_STORE_PASSWORD_PATH = "storePasswordPath";

  static final String HIVE_VAR_PREFIX = "hivevar:";
  static final String HIVE_CONF_PREFIX = "hiveconf:";
  private String host = null;
  private int port = 0;
  private String jdbcUriString;
  private String catalogName;
  private String dbName = Utils.DEFAULT_DATABASE;
  private Map<String, String> hiveConfs = new LinkedHashMap<>();
  private Map<String, String> hiveVars = new LinkedHashMap<>();
  private Map<String, String> sessionVars = new LinkedHashMap<>();
  private String suppliedURLAuthority;
  private String zooKeeperEnsemble = null;
  private String currentHostZnodePath;
  private final List<String> rejectedHostZnodePaths = new ArrayList<>();

  public JdbcConnectionParams() {}

  public JdbcConnectionParams(JdbcConnectionParams params) {
    this.host = params.host;
    this.port = params.port;
    this.jdbcUriString = params.jdbcUriString;
    this.catalogName = params.catalogName;
    this.dbName = params.dbName;
    this.hiveConfs.putAll(params.hiveConfs);
    this.hiveVars.putAll(params.hiveVars);
    this.sessionVars.putAll(params.sessionVars);
    this.suppliedURLAuthority = params.suppliedURLAuthority;
    this.zooKeeperEnsemble = params.zooKeeperEnsemble;
    this.currentHostZnodePath = params.currentHostZnodePath;
    this.rejectedHostZnodePaths.addAll(params.rejectedHostZnodePaths);
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getJdbcUriString() {
    return jdbcUriString;
  }

  public String getCatalogName() {
    return catalogName;
  }

  public String getDbName() {
    return dbName;
  }

  public Map<String, String> getHiveConfs() {
    return hiveConfs;
  }

  public Map<String, String> getHiveVars() {
    return hiveVars;
  }

  public Map<String, String> getSessionVars() {
    return sessionVars;
  }

  public String getSuppliedURLAuthority() {
    return suppliedURLAuthority;
  }

  public String getZooKeeperEnsemble() {
    return zooKeeperEnsemble;
  }

  public List<String> getRejectedHostZnodePaths() {
    return rejectedHostZnodePaths;
  }

  public String getCurrentHostZnodePath() {
    return currentHostZnodePath;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setJdbcUriString(String jdbcUriString) {
    this.jdbcUriString = jdbcUriString;
  }

  public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void setHiveConfs(Map<String, String> hiveConfs) {
    this.hiveConfs = hiveConfs;
  }

  public void setHiveVars(Map<String, String> hiveVars) {
    this.hiveVars = hiveVars;
  }

  public void setSessionVars(Map<String, String> sessionVars) {
    this.sessionVars = sessionVars;
  }

  public void setSuppliedURLAuthority(String suppliedURLAuthority) {
    this.suppliedURLAuthority = suppliedURLAuthority;
  }

  public void setZooKeeperEnsemble(String zooKeeperEnsemble) {
    this.zooKeeperEnsemble = zooKeeperEnsemble;
  }

  public void setCurrentHostZnodePath(String currentHostZnodePath) {
    this.currentHostZnodePath = currentHostZnodePath;
  }
}
