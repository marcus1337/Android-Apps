package test.morris.CONTROLLER.LOADCONTROLLER;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import test.morris.CONTROLLER.BOARDCONTROLLER.CustomBoard;
import test.morris.MODELLEN.NineMenMorrisRules;
import test.morris.R;


//DENNA KLASS ÄR TAGEN FRÅN STACK OVERFLOW
public class ListAdapter extends ArrayAdapter<NineMenMorrisRules> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<NineMenMorrisRules> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list_row, null);
        }

        NineMenMorrisRules p = getItem(position);
        final Button deleteBtn = (Button) v.findViewById(R.id.delete_btn);
        CustomBoard tmpBoard = (CustomBoard) v.findViewById(R.id.imageView1);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAdapter.super.remove(ListAdapter.super.getItem(position));
                notifyDataSetChanged();
            }
        });

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);

            if (tt1 != null) {
                String tmp = "";
                if (p.getTurn() == 1)
                    tmp = "RED TURN";
                else
                    tmp = "BLUE TURN";
                tt1.setText(tmp);
            }

            if (tt2 != null) {
                tt2.setText(p.toString());
            }

            tmpBoard.setSpecialLayout(true);
            tmpBoard.updateMarks(p.getBoard(), p.getBlueUnPlacedMarkers(), p.getRedUnPlacedMarkers(), p.getLevel());
        }

        return v;
    }

}