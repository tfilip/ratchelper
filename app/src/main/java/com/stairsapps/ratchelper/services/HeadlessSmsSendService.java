package com.stairsapps.ratchelper.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by filip on 6/26/2016.
 */
public class HeadlessSmsSendService extends IntentService {
    public HeadlessSmsSendService() {
        super(HeadlessSmsSendService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}