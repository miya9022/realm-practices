package com.sample.realmpractices;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
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
import com.sample.realmpractices.adapter.SearchAdapter;
import com.sample.realmpractices.adapter.UserAdapter;
import com.sample.realmpractices.helper.ClientBuilder;
import com.sample.realmpractices.helper.RealmProvider;
import com.sample.realmpractices.helper.RecyclerTouchListener;
import com.sample.realmpractices.model.Email;
import com.sample.realmpractices.model.User;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RealmProvider realmProvider;
    private RecyclerView rcvUsers;
    private UserAdapter adapter;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupRecyclerView();
        setupRealm();
        loadDataToView();
        setupSomeListeners();
    }

    private void setupRecyclerView() {
        rcvUsers = (RecyclerView) findViewById(R.id.rcv_users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvUsers.setLayoutManager(layoutManager);
        adapter = new UserAdapter(this, new ArrayList<>());
        rcvUsers.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    User u = adapter.getPosition(position);
                    realmProvider.deleteById(User.class, u.getId());
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvUsers);
    }

    private void setupRealm() {
        RealmProvider.setupRealmConfig();
        realmProvider = new RealmProvider();
    }

    private void loadDataToView() {
        ClientBuilder.createDefaultWebservice()
                .getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    users -> Stream.of(users)
                                .sorted((user1, user2) -> user1.getId() - user2.getId())
                                .forEach(user -> {
                                    if (checkUserExist(user.getId())) {
                                        adapter.addUser(user);
                                    } else {
                                        realmProvider.insert(user);
                                    }
                                }),
                    (e) -> Log.d(TAG, "Error has occur caused by " + e.getClass().getCanonicalName()),
                    () -> Log.d(TAG, "success"));
    }

    private boolean checkUserExist(int uid) {
        return realmProvider.getRealmObjectById(User.class, "id", uid) != null;
    }

    private void setupSomeListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab.setOnClickListener(view -> {
            User u = (User) realmProvider.queryAll(User.class).get(0);
            if(u != null) {
                Snackbar.make(findViewById(R.id.content_main),
                        "first user: " + u.getName() + " with age: " + u.getAge(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
        fab1.setOnClickListener(view -> realmProvider.deleteFirst(User.class));
        fab2.setOnClickListener(view -> showCreateUserDialog());
        realmProvider.addOnChangeListener((type, o) -> {
            switch (type) {
                case RealmProvider.Type.INSERT:
                    if(o instanceof User) {
                        User u = (User) o;
                        runOnUiThread(() -> {
                            adapter.addUser(u);
                            searchAdapter.addData(u);
                        });
                    }
                    break;
                case RealmProvider.Type.DELETE:
                    if (o instanceof User) {
                        User u = (User) o;
                        runOnUiThread(() -> {
                            adapter.deleteUser(u.getId());
                            searchAdapter.removeData(u);
                        });
                    }
                    break;
            }
        });
        rcvUsers.addOnItemTouchListener(new RecyclerTouchListener(this, rcvUsers,
                (view, position) -> showEmailsDialog(adapter.getPosition(position).getEmails())));
    }

    private void showCreateUserDialog() {
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
        builder.setTitle("Create User")
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
                    User user = new User(name, age);
                    Stream.of(lsEmailTF)
                            .filterNot(editText -> editText.toString().isEmpty())
                            .forEach(editText -> {
                                Email email = new Email();
                                email.setEmail(editText.getText().toString().trim());
                                email.setActive(0);
                                realmProvider.insert(email);
                                user.addEmail(email);
                            });
                    realmProvider.insert(user);
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
        emailTextField.setHint("Email");
        emailTextField.setLayoutParams(emailTFParams);
        emailTextField.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        emailTextField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailTextField.setTextColor(ContextCompat.getColor(this, android.R.color.background_dark));
        emailTextFieldLayout.addView(emailTextField, 0);
        return emailTextFieldLayout;
    }

    private void showEmailsDialog(List<Email> lsEmails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List Emails")
                .setView(createDialogView(lsEmails))
                .setCancelable(false)
                .setPositiveButton("OK", ((dialog, which) -> dialog.dismiss()))
                .create().show();
    }

    private View createDialogView(List<Email> lsEmails) {
        if(lsEmails == null || lsEmails.size() <= 0) {
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
            rcvEmails.setAdapter(new EmailAdapter(lsEmails));
            return rcvEmails;
        }
    }

    private class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Email> lsEmails;

        EmailAdapter(List<Email> lsEmails) {
            this.lsEmails = lsEmails;
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
            final Email email = lsEmails.get(position);
            if(email.getActive() == 1) {
                tv.setTextColor(getColor(R.color.cardview_dark_background));
            } else {
                tv.setTextColor(getColor(android.R.color.darker_gray));
            }
            tv.setText(email.getEmail());
        }

        @Override
        public int getItemCount() {
            return lsEmails.size();
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

        Stream.of(realmProvider.getAllByClass(User.class))
                .filter(o -> o instanceof User)
                .forEach(o -> searchAdapter.addData((User) o));

        searchView.setOnItemClickListener((parent, view, position, id) -> {
            final User user = searchAdapter.getItem(position);
            searchView.clearListSelection();
            searchView.setText("");
            Toast.makeText(this, "User Information:" +
                    "\n - Name: " + user.getName() +
                    "\n - Age: " + user.getAge(), Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        realmProvider.deleteTable(User.class);
        realmProvider.closeRealm();
        realmProvider = null;
    }
}
