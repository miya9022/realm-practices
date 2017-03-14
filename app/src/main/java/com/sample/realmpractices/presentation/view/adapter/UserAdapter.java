package com.sample.realmpractices.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.sample.realmpractices.R;
import com.sample.realmpractices.presentation.model.UserModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by app on 2/14/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String TAG = UserAdapter.class.getSimpleName();
    private Context context;
    private List<UserModel> userModels;

    public UserAdapter(Context context) {
        this.context = context;
        this.userModels = Collections.emptyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public List<UserModel> getUsers() {
        return userModels;
    }

    public UserModel getPosition(int position) {
        return userModels.get(position);
    }

    public void setUserCollection(Collection<UserModel> userCollection) {
        if(userCollection == null) {
            throw new NullPointerException("userCollection canot be null");
        }

        this.userModels = (List<UserModel>) userCollection;
        notifyDataSetChanged();
    }

    public void addUser(UserModel u) {
        if (userModels != null) {
            userModels.add(u);
            notifyItemRangeInserted(userModels.size()-1, 1);
        }
    }
//
    public void deleteUser(int id) {
        Stream.of(userModels)
            .filter(user -> id == user.getId())
            .findFirst()
            .ifPresent(user -> {
                int i = userModels.indexOf(user);
                userModels.remove(user);
                notifyItemRangeRemoved(i, 1);
            });
    }

    public UserModel getUserById(int uid) {
        return Stream.of(userModels)
                .filter(user -> uid == user.getId())
                .findFirst().get();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvUsername, tvAge;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvAge = (TextView) itemView.findViewById(R.id.tv_age);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        void bind(UserModel u){
            tvUsername.setText(u.getName());
            tvAge.setText(String.valueOf(u.getAge()));
//            itemView.setOnClickListener(v -> Toast.makeText(context, "Name: " + u.getName(), Toast.LENGTH_SHORT).show());
//            cardView.setOnTouchListener((v, event) -> {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    int scrollX = (int) -event.getX();
//                    Log.d(TAG, "scroll X: " + scrollX);
//                    cardView.setScrollX(scrollX);
//                    return true;
//                }
//                return false;
//            });
        }
    }
}
