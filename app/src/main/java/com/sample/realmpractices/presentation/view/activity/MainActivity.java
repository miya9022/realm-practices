package com.sample.realmpractices.presentation.view.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.github.clans.fab.FloatingActionButton;
import com.sample.realmpractices.R;
import com.sample.realmpractices.data.entity.EmailEntity;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.interactor.CreateUserUseCase;
import com.sample.realmpractices.domain.interactor.DeleteUserUseCase;
import com.sample.realmpractices.domain.interactor.GetUserEmailListUseCase;
import com.sample.realmpractices.domain.interactor.GetUserListUseCase;
import com.sample.realmpractices.presentation.mapper.EmailModelDataMapping;
import com.sample.realmpractices.presentation.mapper.UserModelDataMapping;
import com.sample.realmpractices.presentation.model.EmailModel;
import com.sample.realmpractices.presentation.model.UserModel;
import com.sample.realmpractices.presentation.presenter.UserListPresenter;
import com.sample.realmpractices.presentation.view.UserListView;
import com.sample.realmpractices.presentation.view.adapter.SearchAdapter;
import com.sample.realmpractices.presentation.view.adapter.UserAdapter;
import com.sample.realmpractices.presentation.view.component.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends BaseActivity implements UserListView {
    private static final String TAG = "MainActivity";

    private UserListPresenter userListPresenter;
    private UserAdapter userAdapter;
    private SearchAdapter searchAdapter;

    private RecyclerView rcvUsers;
    private FloatingActionButton btnAddNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupRecyclerView();
        setupUseCase();

        userListPresenter = new UserListPresenter(
                new GetUserListUseCase(
                    getApplicationComponent().getUserRepository(),
                    getApplicationComponent().getThreadExecutor(),
                    getApplicationComponent().getPostExecutionThread()),
                new GetUserEmailListUseCase(
                    getApplicationComponent().getEmailRepository(),
                    getApplicationComponent().getThreadExecutor(),
                    getApplicationComponent().getPostExecutionThread()),
                new DeleteUserUseCase(
                    getApplicationComponent().getUserRepository(),
                    getApplicationComponent().getThreadExecutor(),
                    getApplicationComponent().getPostExecutionThread()),
                new CreateUserUseCase(
                    getApplicationComponent().getUserRepository(),
                    getApplicationComponent().getThreadExecutor(),
                    getApplicationComponent().getPostExecutionThread()),
                new UserModelDataMapping(),
                new EmailModelDataMapping());
        userListPresenter.setUserListView(this);
        userListPresenter.initialize();
//        loadDataToView();
//        setupSomeListeners();
    }

    private void setupRecyclerView() {
        rcvUsers = (RecyclerView) findViewById(R.id.rcv_users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvUsers.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(this);
        rcvUsers.setAdapter(userAdapter);
        rcvUsers.addOnItemTouchListener(new RecyclerTouchListener(this, rcvUsers,
                (view, position) -> userListPresenter.onUserClicked(userAdapter.getPosition(position))));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    UserModel userModel = userAdapter.getPosition(position);
                    userListPresenter.deleteUser(userModel);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvUsers);
    }

    private void setupUseCase() {
        btnAddNewUser = (FloatingActionButton) findViewById(R.id.add_new_user);
        btnAddNewUser.setOnClickListener(this::showCreateUserDialog);
    }

//    private void setupSomeListeners() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
//        fab.setOnClickListener(view -> {
//            UserModel u = (UserModel) realmProvider.queryAll(UserModel.class).get(0);
//            if(u != null) {
//                Snackbar.make(findViewById(R.id.content_main),
//                        "first user: " + u.getName() + " with age: " + u.getAge(),
//                        Snackbar.LENGTH_LONG).show();
//            }
//        });
//        fab1.setOnClickListener(view -> realmProvider.deleteFirst(UserModel.class));
//        fab2.setOnClickListener(view -> showCreateUserDialog());
//        realmProvider.addOnChangeListener((type, o) -> {
//            switch (type) {
//                case RealmProvider.Type.INSERT:
//                    if(o instanceof UserModel) {
//                        UserModel u = (UserModel) o;
//                        runOnUiThread(() -> {
//                            adapter.addUser(u);
//                            searchAdapter.addData(u);
//                        });
//                    }
//                    break;
//                case RealmProvider.Type.DELETE:
//                    if (o instanceof UserModel) {
//                        UserModel u = (UserModel) o;
//                        runOnUiThread(() -> {
//                            adapter.deleteUser(u.getId());
//                            searchAdapter.removeData(u);
//                        });
//                    }
//                    break;
//            }
//        });
//        rcvUsers.addOnItemTouchListener(new RecyclerTouchListener(this, rcvUsers,
//                (view, position) -> showEmailsDialog(adapter.getPosition(position).getEmailModels())));
//    }

    private void showCreateUserDialog(View view) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setPadding(32, 32, 32, 32);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText edtName = new EditText(this);
        edtName.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        edtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        edtName.setHint("Name");
        edtName.setTextColor(ContextCompat.getColor(this, android.R.color.background_dark));
        linearLayout.addView(edtName);

        EditText edtAge = new EditText(this);
        edtAge.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        edtAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        edtAge.setHint("Age");
        edtAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtAge.setTextColor(ContextCompat.getColor(this, android.R.color.background_dark));
        linearLayout.addView(edtAge);

        FloatingActionButton addEmailTFLayoutButton = new FloatingActionButton(this);
        LinearLayout.LayoutParams addEmailTFLayoutButtonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addEmailTFLayoutButtonParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        addEmailTFLayoutButtonParams.setMargins(16, 16, 16, 16);
        addEmailTFLayoutButton.setImageResource(android.R.drawable.ic_input_add);
        addEmailTFLayoutButton.setLayoutParams(addEmailTFLayoutButtonParams);
        linearLayout.addView(addEmailTFLayoutButton);

        List<EditText> lsEmailTF = new ArrayList<>();
        addEmailTFLayoutButton.setOnClickListener(v -> {
            FrameLayout containerView = (FrameLayout) replicateEmailTextField();
            lsEmailTF.add((EditText) containerView.getChildAt(0));
            linearLayout.addView(containerView);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create UserModel")
                .setView(linearLayout)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    String name = edtName.getText().toString().trim();
                    String ageStr = edtAge.getText().toString().trim();
                    int age;
                    try {
                        age = ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        age = 0;
                    }
                    UserEntity userEntity = new UserEntity(name, age);
                    Stream.of(lsEmailTF)
                            .filterNot(editText -> editText.toString().isEmpty())
                            .forEach(editText -> {
                                EmailEntity emailEntity = new EmailEntity();
                                emailEntity.setEmail(editText.getText().toString().trim());
                                emailEntity.setActive(0);
                                userEntity.addEmail(emailEntity);
                            });
                    userListPresenter.insertUser(userEntity);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private View replicateEmailTextField() {
        FrameLayout emailTextFieldLayout = new FrameLayout(this);
        emailTextFieldLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        EditText emailTextField = new EditText(this);
        FrameLayout.LayoutParams emailTFParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        emailTFParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        emailTextField.setHint("EmailModel");
        emailTextField.setLayoutParams(emailTFParams);
        emailTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        emailTextField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTextField.setTextColor(ContextCompat.getColor(this, android.R.color.background_dark));
        emailTextFieldLayout.addView(emailTextField, 0);
        return emailTextFieldLayout;
    }

    @Override
    public void showEmailsDialog(Collection<EmailModel> lsEmailModels) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<EmailModel> emailModels = new ArrayList<>(lsEmailModels);
        builder.setTitle("List Emails")
                .setView(createDialogView(emailModels))
                .setCancelable(false)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .create().show();
    }

    @Override
    public void deleteUser(int uid) {
        String name = userAdapter.getUserById(uid).getName();
        userAdapter.deleteUser(uid);
        searchAdapter.updateData(userAdapter.getUsers());
        Toast.makeText(this, "User deleted: " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void insertUser(UserModel userModel) {
        userAdapter.addUser(userModel);
        searchAdapter.updateData(userAdapter.getUsers());
        Toast.makeText(this, "User inserted: " + userModel.getName(), Toast.LENGTH_SHORT).show();
    }

    private View createDialogView(List<EmailModel> lsEmailModels) {
        if(lsEmailModels == null || lsEmailModels.size() <= 0) {
            TextView emptyEmailView = new TextView(this);
            emptyEmailView.setText("Thằng này làm éo gì có email mà xem =))");
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            lp.gravity = Gravity.CENTER;
            emptyEmailView.setGravity(Gravity.CENTER);
            emptyEmailView.setLayoutParams(lp);
            emptyEmailView.setPadding(32, 32, 32, 32);
            emptyEmailView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            emptyEmailView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            return emptyEmailView;
        } else {
            RecyclerView rcvEmails = new RecyclerView(this);
            rcvEmails.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            rcvEmails.setPadding(32, 24, 32, 8);
            rcvEmails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rcvEmails.setAdapter(new EmailAdapter(lsEmailModels));
            return rcvEmails;
        }
    }

    private class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<EmailModel> lsEmailModels;

        EmailAdapter(List<EmailModel> lsEmailModels) {
            this.lsEmailModels = lsEmailModels;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setPadding(16, 16, 16, 16);
            parent.addView(textView);
            return new RecyclerView.ViewHolder(textView) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView;
            final EmailModel emailModel = lsEmailModels.get(position);
            if(emailModel.getActive() == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextColor(getColor(R.color.cardview_dark_background));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextColor(getColor(android.R.color.darker_gray));
                }
            }
            tv.setText(emailModel.getEmail());
        }

        @Override
        public int getItemCount() {
            return lsEmailModels.size();
        }
    }

    private AutoCompleteTextView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (AutoCompleteTextView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setHint("Search...");
        searchView.setDropDownWidth(WindowManager.LayoutParams.MATCH_PARENT);
        searchView.setDropDownBackgroundResource(android.R.color.white);
        searchView.setThreshold(1);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);

        searchAdapter = new SearchAdapter(this, R.layout.item_user_search, new ArrayList<>());
        searchView.setAdapter(searchAdapter);
        searchView.setOnItemClickListener((parent, view, position, id) -> {
            final UserModel userModel = searchAdapter.getItem(position);
            searchView.clearListSelection();
            searchView.setText("");
            Toast.makeText(this, "UserModel Information:" +
                    "\n - Name: " + userModel.getName() +
                    "\n - Age: " + userModel.getAge(), Toast.LENGTH_SHORT).show();
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            if(searchView != null)
                searchView.requestFocus();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userListPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userListPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userListPresenter.destroy();
    }

    @Override
    public void renderUserList(Collection<UserModel> userModelCollection) {
        if(userModelCollection != null) {
            userAdapter.setUserCollection(userModelCollection);
            searchAdapter.updateData(userModelCollection);
        }
    }

    @Override
    public void viewUser(UserModel userModel) {
        userListPresenter.displayEmails(userModel);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
