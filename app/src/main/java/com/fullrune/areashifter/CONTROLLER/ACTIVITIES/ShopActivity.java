package com.fullrune.areashifter.CONTROLLER.ACTIVITIES;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fullrune.areashifter.CONTROLLER.FileObjects.Purchases;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;
import com.fullrune.areashifter.util.IabHelper;
import com.fullrune.areashifter.util.IabResult;
import com.fullrune.areashifter.util.Inventory;
import com.fullrune.areashifter.util.Purchase;

/**
 * Created by Marcus on 2017-05-10.
 */

public class ShopActivity extends Activity {

    private ShopActivity shopActivity;
    private Button backBtn, removeAdsBtn, extraLivesBtn, restorePurchasesBtn;
    private IOManager ioManager;

    private IabHelper mHelper;

    private static final String SKU_PREMIUM = "com.fullrune.areashifter_premium";
    private static final String SKU_EXTRALIVES = "com.fullrune.areashifter_extralives";

   private String[] getSplitted(){
        String ab = new StringBuilder("16 37 58 18 28 07 96 28 47 07 94 68 88 611 78 18 021 35 58 18 97 611 58 87 401 68 48 78 611 401 86 87 021 87 88 89 68 07 601 38 77 68 221 68 57 87 901 98 94 96 17 001 411 77 68 87 021 65 501 99 35 87 48 58 501 07 88 99 78 801 78 09 27 09 58 28 07 28 15 28 77 68 011 18 28 001 901 99 401 07 221 57 77 28 58 58 67 801 48 48 17 401 221 001 05 701 94 18 76 28 86 48 401 211 94 09 35 07 801 001 25 001 701 101 38 401 48 48 25 99 48 101 411 07 601 101 711 001 801 09 57 801 15 58 121 77 96 101 87 09 68 001 601 28 78 87 211 68 07 89 09 87 78 48 84 98 48 48 021 65 84 38 27 28 221 89 77 75 121 001 25 211 96 001 05 96 88 09 68 001 601 79 37 401 601 78 86 07 48 68 47 09 86 09 121 99 701 101 811 401 05 89 18 401 84 77 17 47 94 99 87 401 17 09 57 07 94 48 77 66 801 77 97 801 05 38 221 84 84 48 98 09 86 78 121 511 58 58 221 68 48 38 011 28 221 89 35 58 17 38 09 87 48 68 221 56 221 68 121 611 901 09 77 211 27 78 021 87 15 99 97 87 15 99 05 801 58 48 45 68 801 87 98 401 96 48 611 28 86 09 211 401 84 89 411 96 011 77 18 75 901 89 57 401 601 87 18 28 68 68 211 47 78 68 211 47 011 38 121 56 68 48 711 75 58 18 021 021 78 68 05 84 901 89 45 68 221 89 57 35 901 89 96 401 94 77 15 801 07 97 411 801 48 18 311 68 07 101 67 47 15 48 67 68 07 58 45 09 701 58 68 07 94 57 911 28 011 77 911 47 601 78 27 87 88 68 18 75 58 38 78 47 901 98 76 801 94 89 021 511 96 97 86 401 801 78 021 211 58 79 35 25 05 18 38 47 701 28 76 07 78 38 45 09 15 001 45 47 701 98 68 001 88 89 911 18 801 48 15 87 78 98 121 801 801 38 15 701 78 38 511 35 901 89 611 07 58 28 28 07 84 18 67 001 05 18 76 801 58 38 87 07 96 97 28 07 84 18 08 07 58 18 17 68 58 58 66 47 96 77 15 801 221 28 211 611 17 79 021 611 05 09 76 35 58 18 311 801 701 18 47 801 58 48").reverse().toString();
       return ab.split(" ");
   }
   private byte[] getKode(){
       String[] tmp = getSplitted();
       byte[] koder = new byte[tmp.length];
       for(int i = 0; i < tmp.length; i++){
           koder[i] = Byte.valueOf(tmp[i]);
       }
       byte[] deco = Base64.decode(koder, Base64.DEFAULT);
        return deco;
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopActivity = this;
        ioManager = new IOManager(this);
        backBtn = (Button) findViewById(R.id.shopbackbtn);
        removeAdsBtn = (Button) findViewById(R.id.shopremoveadsbtn);
        extraLivesBtn = (Button) findViewById(R.id.shopextralivesbtn);
        restorePurchasesBtn = (Button) findViewById(R.id.shoprestorepurchasesbtn);

        backBtn.setOnClickListener(new BackBtnListener());
        removeAdsBtn.setOnClickListener(new RemoveAdsBtnListener());
        extraLivesBtn.setOnClickListener(new ExtraLivesBtnListener());
        restorePurchasesBtn.setOnClickListener(new RestorePurchasesBtnListener());


        mHelper = new IabHelper(this, new String(getKode()));
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(shopActivity, result.toString(), Toast.LENGTH_LONG).show();
                } else {
                }
            }
        });

        Purchases purchases = ioManager.loadPurchases();
        if(purchases != null){
            if(purchases.isPremium())
                removeAdsBtn.setEnabled(false);
            if(purchases.isExtraLives())
                extraLivesBtn.setEnabled(false);
        }

    }

    IabHelper.OnIabPurchaseFinishedListener buyListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if(result.isFailure()){
                Toast.makeText(shopActivity, "Purchase failed.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(info.getSku().equals(SKU_PREMIUM)){
                Purchases purchases = ioManager.loadPurchases();
                if(purchases == null)
                    purchases = new Purchases();
                purchases.setPremium(true);
                removeAdsBtn.setEnabled(false);
                ioManager.savePurchases(purchases);
                Toast.makeText(shopActivity, "Purchase completed.", Toast.LENGTH_SHORT).show();
            }
            else if(info.getSku().equals(SKU_EXTRALIVES)){
                Purchases purchases = ioManager.loadPurchases();
                if(purchases == null)
                    purchases = new Purchases();
                purchases.setExtraLives(true);
                extraLivesBtn.setEnabled(false);
                ioManager.savePurchases(purchases);
                Toast.makeText(shopActivity, "Purchase completed.", Toast.LENGTH_SHORT).show();
            }

        }
    };

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (mHelper == null || inventory == null) return;
            Purchases purchases = new Purchases();
            boolean boughtPremium = inventory.hasPurchase(SKU_PREMIUM);
            boolean extralives = inventory.hasPurchase(SKU_EXTRALIVES);
            removeAdsBtn.setEnabled(!boughtPremium);
            extraLivesBtn.setEnabled(!extralives);
            purchases.setPremium(boughtPremium);
            purchases.setExtraLives(extralives);
            ioManager.savePurchases(purchases);
            Toast.makeText(shopActivity, "Task finished.", Toast.LENGTH_SHORT).show();
        }
    };


    private class RemoveAdsBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                mHelper.launchPurchaseFlow(shopActivity, SKU_PREMIUM, 1002, buyListener, "dsdfdTE&Y¤#%#¤grtyhj9043jtern0n43nio/&%4¤#43234eswa");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }

        }
    }

    private class ExtraLivesBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                mHelper.launchPurchaseFlow(shopActivity, SKU_EXTRALIVES, 1001, buyListener, "ahfASDG43tGEDSAFDFG6346AWESDFG6453w6tur/&%4¤#43234eswa");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    private class RestorePurchasesBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    private class BackBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

}
