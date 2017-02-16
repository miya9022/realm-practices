package com.sample.realmpractices.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.realmpractices.R;
import com.sample.realmpractices.model.User;

import java.util.List;

/**
 * Created by app on 2/14/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String TAG = UserAdapter.class.getSimpleName();
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public List<User> getUsers() {
        return users;
    }

    public User getPosition(int position) {
        return users.get(position);
    }

    public void addUser(User u) {
        if (users != null) {
            users.add(u);
            notifyItemRangeInserted(users.size()-1, 1);
        }
    }

    public void deleteUser(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            users
                .parallelStream()
                .filter(user -> id == user.getId())
                .findFirst()
                .ifPresent(user -> {
                    int i = users.indexOf(user);
                    users.remove(user);
                    notifyItemRangeRemoved(i, 1);
                });
        } else {
            User user1 = null;
            for (User user: users) {
                if(id == user.getId()) {
                    user1 = user;
                    break;
                }
            }
            if (user1 != null) {
                int i = users.indexOf(user1);
                users.remove(user1);
                notifyItemRangeRemoved(i, 1);
            }
        }
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

        void bind(User u){
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
