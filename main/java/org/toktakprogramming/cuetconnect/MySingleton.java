package org.toktakprogramming.cuetconnect;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private  static MySingleton mInstance;
    private RequestQueue requestQueue;
    private  static Context nCtx;

    private MySingleton(Context context) {
        nCtx=context;
        requestQueue=getRequestQueue();
    }

    public static synchronized MySingleton getmInstance(Context context){
        if(mInstance==null)
        {
            mInstance=new MySingleton(context);
        }
        return  mInstance;

    }
    public  <T> void AddToRequestQueue(Request<T>request){
         requestQueue.add(request);
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue=Volley.newRequestQueue(nCtx.getApplicationContext());
        }
        return  requestQueue;
    }

}
