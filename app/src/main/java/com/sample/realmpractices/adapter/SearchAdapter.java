package com.sample.realmpractices.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.sample.realmpractices.R;
import com.sample.realmpractices.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by app on 2/20/17.
 */

public class SearchAdapter extends ArrayAdapter<User> {
    private Context mContext;

    private ArrayList<User> lsUsers;
    private ArrayList<User> lsAllUsers;
    private ArrayList<User> lsRequestedSearch;
    private LayoutInflater inflater;
    private int resId;
    private String constraints;

    public SearchAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.lsUsers = new ArrayList<>(objects);
        this.lsAllUsers = new ArrayList<>(objects);
        this.lsRequestedSearch = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.resId = resource == 0 ? android.R.layout.simple_list_item_1 : resource;
    }

    public void updateData(List<User> objects) {
        this.lsAllUsers = new ArrayList<>(objects);
        this.lsUsers = new ArrayList<>(objects);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lsUsers.size();
    }

    @Override
    public User getItem(int position) {
        return lsUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User u = getItem(position);
        Holder holder;
        if(convertView == null) {
            convertView = inflater.inflate(resId, parent, false);
            holder = new Holder();
            if(resId == android.R.layout.simple_list_item_1) {
                holder.tvName = (TextView) convertView.findViewById(android.R.id.text1);
            } else {
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if(u != null) {
            String name = u.getName();
            if (!name.isEmpty()) {
                String nameLowerCase = name.toLowerCase();
                if (nameLowerCase.contains(constraints)) {
                    int start = nameLowerCase.indexOf(constraints);
                    int end = start + constraints.length();
                    String startSt = start > 0 ? name.substring(0, start) : "";
                    String suggested = name.substring(start, end);
                    String endSt = end == name.length() ? "" : name.substring(end, name.length());

                    String htmlSource = startSt + "<span style='color:blue'>"+ suggested +"</span>" + endSt;
                    holder.tvName.setText(Html.fromHtml(htmlSource));
                }
            }
            holder.tvAge.setText(String.valueOf(u.getAge()));
        }
        return convertView;
    }

    public class Holder {
        public TextView tvName, tvAge;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((User) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint != null) {
                    constraints = constraint.toString().toLowerCase();
                    lsRequestedSearch.clear();
                    for(User u : lsAllUsers) {
                        if (u.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            lsRequestedSearch.add(u);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = lsRequestedSearch;
                    filterResults.count = lsRequestedSearch.size();
                    return filterResults;
                }
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lsUsers.clear();
                if(results != null && results.count > 0) {
                    List<?> result = (List<?>) results.values;
                    for(Object o : result) {
                        if(o instanceof User) {
                            lsUsers.add((User) o);
                        }
                    }
                } else if(constraint == null) {
                    lsUsers.addAll(lsAllUsers);
                }
                notifyDataSetChanged();
            }
        };
    }
}
