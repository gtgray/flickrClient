package com.core.flickrclient;

import android.os.Bundle;

public interface OnServerAnswerListener {

	public abstract void onServerAnswer(int type, int status, Bundle answer);

}
