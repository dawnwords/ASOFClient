package edu.fudan.se.asof.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.fudan.se.asof.R;
import edu.fudan.se.asof.activity.MyActivity;
import edu.fudan.se.asof.network.TemplateListFetcher;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class TemplateAdapter extends BaseAdapter {

    private Activity activity;

    public TemplateAdapter(MyActivity activity) {
        this.activity = activity;
    }

    private List<TemplateListFetcher.Response> templates = new LinkedList<TemplateListFetcher.Response>();

    @Override
    public int getCount() {
        return templates.size();
    }

    @Override
    public TemplateListFetcher.Response getItem(int position) {
        return templates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.template_description, null);
            holder.templateName = (TextView) convertView.findViewById(R.id.template_name);
            holder.templateDescription = (TextView) convertView.findViewById(R.id.template_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TemplateListFetcher.Response response = getItem(position);
        holder.templateName.setText(response.template);
        holder.templateDescription.setText(response.description);
        return convertView;
    }

    public void setTemplates(List<TemplateListFetcher.Response> templates) {
        this.templates = templates;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder {
        TextView templateName, templateDescription;
    }

}
