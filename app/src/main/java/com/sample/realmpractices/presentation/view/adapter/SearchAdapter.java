package com.sample.realmpractices.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.sample.realmpractices.R;
import com.sample.realmpractices.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by app on 2/20/17.
 */

public class SearchAdapter extends ArrayAdapter<UserModel> {
    private ArrayList<UserModel> lsUserModels;
    private ArrayList<UserModel> lsAllUserModels;
    private ArrayList<UserModel> lsRequestedSearch;
    private LayoutInflater inflater;
    private int resId;
    private String constraints;

    public SearchAdapter(Context context, int resource, List<UserModel> objects) {
        super(context, resource, objects);
        this.lsUserModels = new ArrayList<>(objects);
        this.lsAllUserModels = new ArrayList<>(objects);
        this.lsRequestedSearch = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.resId = resource == 0 ? android.R.layout.simple_list_item_1 : resource;
    }

    public void updateData(List<UserModel> objects) {
        this.lsAllUserModels = new ArrayList<>(objects);
        this.lsUserModels = new ArrayList<>(objects);
        notifyDataSetChanged();
    }

    public void addData(UserModel userModel) {
        lsAllUserModels.add(userModel);
        notifyDataSetChanged();
    }

    public void removeData(UserModel data) {
        Stream.of(lsAllUserModels)
            .filter(user -> user.getId() == data.getId())
            .findFirst()
            .ifPresent(user -> {
                lsAllUserModels.remove(user);
                notifyDataSetChanged();
            });
    }

    @Override
    public int getCount() {
        return lsUserModels.size();
    }

    @Override
    public UserModel getItem(int position) {
        return lsUserModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final UserModel u = getItem(position);
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

    private class Holder {
        private TextView tvName, tvAge;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((UserModel) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint != null) {
                    constraints = constraint.toString().toLowerCase();
                    lsRequestedSearch.clear();
                    Stream.of(lsAllUserModels)
                            .filter(u -> u.getName().toLowerCase().contains(constraints))
                            .forEach(lsRequestedSearch::add);

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = lsRequestedSearch;
                    filterResults.count = lsRequestedSearch.size();
                    return filterResults;
                }
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lsUserModels.clear();
                if(results != null && results.count > 0) {
                    List<?> result = (List<?>) results.values;
                    Stream.of(result)
                            .filter(o -> o instanceof UserModel)
                            .forEach(o -> lsUserModels.add((UserModel) o));
                } else if(constraint == null) {
                    lsUserModels.addAll(lsAllUserModels);
                }
                notifyDataSetChanged();
            }
        };
    }
}
