package xyz.myrating.stories.mystories.tools;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

/**
 * Created by Emanuel Graf on 01.05.2018.
 */

public class authenticate {

    public static int authenticated =0;
    public static void authenticate(Context c) {

        new async(c).execute();
        authenticated=0;


    }

    static class async extends AsyncTask<Void, Void, String> {
        Context context;
        String id = null;
        async(Context c){
            context = c;

        }
        @Override
        protected String doInBackground (Void...params){
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException e) {
                e.printStackTrace();
            }
            String advertId = null;
            try {
                advertId = idInfo.getId();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return advertId;
        }

        @Override
        protected void onPostExecute (String advertId){
            userdata mUserdata = new userdata(context);
            mUserdata.setAdvertiserID(advertId);

            new PostRequest(context).execute("authenticate", String.valueOf(mUserdata.getId()),mUserdata.getPass() , advertId);

        }

    }

}
