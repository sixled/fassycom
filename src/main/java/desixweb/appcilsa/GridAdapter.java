package desixweb.appcilsa;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sixled on 13/10/2017.
 */

public class GridAdapter extends BaseAdapter{
    public ArrayList<Bitmap> icons;
    public ArrayList<String> letters ;
    public Context context;
    public LayoutInflater inflater;

    public GridAdapter(Context context,ArrayList<Bitmap> icons, ArrayList<String> letters) {
        this.icons = icons;
        this.letters = letters;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int position) {
        return letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        View gridview=convertview;
        if(convertview==null)
        {

            gridview=inflater.inflate(R.layout.griditem,null);

        } ImageView icon=(ImageView) gridview.findViewById(R.id.icon);
        TextView letter=(TextView) gridview.findViewById(R.id.letters);
        icon.setImageBitmap(icons.get(position));
        letter.setText(letters.get(position));
        return gridview;
    }
}
