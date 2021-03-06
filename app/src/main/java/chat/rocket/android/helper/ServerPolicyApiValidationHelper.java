package chat.rocket.android.helper;

import android.support.annotation.NonNull;

import chat.rocket.android.api.rest.ServerPolicyApi;
import rx.Observable;

public class ServerPolicyApiValidationHelper {

  private final ServerPolicyApi serverPolicyApi;

  public ServerPolicyApiValidationHelper(@NonNull ServerPolicyApi serverPolicyApi) {
    this.serverPolicyApi = serverPolicyApi;
  }

  public Observable<ServerPolicyHelper.ServerInfo> getApiVersion() {
    return serverPolicyApi.getApiInfoSecurely()
        .onErrorResumeNext(serverPolicyApi.getApiInfoInsecurely())
        .map(response -> new ServerPolicyHelper.ServerInfo(
            response.getProtocol().equals(ServerPolicyApi.SECURE_PROTOCOL),
            response.getData()
        ));
  }
}
