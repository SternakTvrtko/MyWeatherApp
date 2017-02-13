package Services;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.myweather.tvrtkosternak.myweatherapp.R;

import java.util.List;

/**
 * Created by Tvrtko on 13.2.2017..
 */

public class ButtonsAdapter extends RecyclerView.Adapter<ButtonsAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout weatherButtonLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            weatherButtonLayout=(LinearLayout) itemView.findViewById(R.id.buttonsLayout);
        }
    }

    private List<Button> weatherButtons;
    private Context context;

    public ButtonsAdapter(Context context, List<Button> weatherButtons){
        this.weatherButtons=weatherButtons;
        this.context=context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ButtonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View buttonView = inflater.inflate(R.layout.buttonslayout, parent, false);

        ViewHolder viewHolder = new ViewHolder(buttonView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ButtonsAdapter.ViewHolder holder, int position) {
        Button weatherButton=weatherButtons.get(position);

        LinearLayout buttonLayout=holder.weatherButtonLayout;
        buttonLayout.addView(weatherButton);
    }

    @Override
    public int getItemCount() {
        return weatherButtons.size();
    }
}
