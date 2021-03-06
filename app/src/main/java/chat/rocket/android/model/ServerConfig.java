package chat.rocket.android.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.json.JSONObject;

import bolts.Task;
import chat.rocket.android.helper.LogcatIfError;
import chat.rocket.android.realm_helper.RealmStore;
import hugo.weaving.DebugLog;

/**
 * Server configuration.
 */
public class ServerConfig extends RealmObject {

  @SuppressWarnings({"PMD.ShortVariable"})
  public static final String ID = "serverConfigId";
  public static final String HOSTNAME = "hostname";
  public static final String STATE = "state";
  public static final String SESSION = "session";
  public static final String SECURE_CONNECTION = "secureConnection";
  public static final String ERROR = "error";

  public static final int STATE_READY = 0;
  public static final int STATE_CONNECTING = 1;
  public static final int STATE_CONNECTED = 2;
  public static final int STATE_CONNECTION_ERROR = 3;

  @PrimaryKey private String serverConfigId;
  private String hostname;
  private int state;
  private String session;
  private boolean secureConnection;
  private String error;

  /**
   * Log the server connection is lost due to some exception.
   */
  @DebugLog
  public static void logConnectionError(String serverConfigId, Exception exception) {
    RealmStore.getDefault().executeTransaction(
        realm -> realm.createOrUpdateObjectFromJson(ServerConfig.class, new JSONObject()
            .put(ID, serverConfigId)
            .put(STATE, STATE_CONNECTION_ERROR)
            .put(ERROR, exception.getMessage())))
        .continueWith(new LogcatIfError());
  }

  /**
   * Update the state of the ServerConfig with serverConfigId.
   */
  public static Task<Void> updateState(final String serverConfigId, int state) {
    return RealmStore.getDefault().executeTransaction(realm -> {
      ServerConfig config =
          realm.where(ServerConfig.class).equalTo(ID, serverConfigId).findFirst();
      if (config == null || config.getState() != state) {
        realm.createOrUpdateObjectFromJson(ServerConfig.class, new JSONObject()
            .put(ID, serverConfigId)
            .put(STATE, state));
      }
      return null;
    });
  }

  public String getServerConfigId() {
    return serverConfigId;
  }

  public void setServerConfigId(String serverConfigId) {
    this.serverConfigId = serverConfigId;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }

  public boolean usesSecureConnection() {
    return secureConnection;
  }

  public void setSecureConnection(boolean usesSecureConnection) {
    this.secureConnection = usesSecureConnection;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
