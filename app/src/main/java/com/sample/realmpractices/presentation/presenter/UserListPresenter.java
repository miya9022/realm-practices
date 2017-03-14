package com.sample.realmpractices.presentation.presenter;

import android.util.Log;

import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.Email;
import com.sample.realmpractices.domain.User;
import com.sample.realmpractices.domain.interactor.CreateUserUseCase;
import com.sample.realmpractices.domain.interactor.DefaultSubscriber;
import com.sample.realmpractices.domain.interactor.DeleteUserUseCase;
import com.sample.realmpractices.domain.interactor.GetUserEmailListUseCase;
import com.sample.realmpractices.domain.interactor.GetUserListUseCase;
import com.sample.realmpractices.presentation.mapper.EmailModelDataMapping;
import com.sample.realmpractices.presentation.mapper.UserModelDataMapping;
import com.sample.realmpractices.presentation.model.EmailModel;
import com.sample.realmpractices.presentation.model.UserModel;
import com.sample.realmpractices.presentation.view.UserListView;

import java.util.Collection;
import java.util.List;

/**
 * Created by app on 3/9/17.
 */

public class UserListPresenter implements Presenter {

    private UserListView userListView;

    private final GetUserListUseCase getUserListUseCase;
    private final GetUserEmailListUseCase getUserEmailListUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UserModelDataMapping userModelDataMapping;
    private final EmailModelDataMapping emailModelDataMapping;

    public UserListPresenter(GetUserListUseCase getUserListUseCase,
                             GetUserEmailListUseCase getUserEmailListUseCase,
                             DeleteUserUseCase deleteUserUseCase,
                             CreateUserUseCase createUserUseCase,
                             UserModelDataMapping userModelDataMapping,
                             EmailModelDataMapping emailModelDataMapping) {
        this.getUserListUseCase = getUserListUseCase;
        this.getUserEmailListUseCase = getUserEmailListUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.createUserUseCase = createUserUseCase;
        this.userModelDataMapping = userModelDataMapping;
        this.emailModelDataMapping = emailModelDataMapping;
    }

    public void setUserListView(UserListView userListView) {
        this.userListView = userListView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.userListView = null;
    }

    public void initialize() {
        loadUserList();
    }

    private void loadUserList() {
        hideViewRetry();
        showViewLoading();
        getUserListUseCase.execute(new UserListSubscriber());
    }

    public void displayEmails(UserModel userModel) {
        getUserEmailListUseCase
                .setId(userModel.getId())
                .execute(new EmailListSubscriber());
    }

    public void onUserClicked(UserModel userModel) {
        userListView.viewUser(userModel);
    }

    public void insertUser(final UserEntity userEntity) {
        createUserUseCase.setUserEntity(userEntity);
        createUserUseCase.execute(new CreateUserSubscriber());
    }

    public void deleteUser(final UserModel userModel) {
        deleteUserUseCase.setUserId(userModel.getId());
        deleteUserUseCase.execute(new DeleteUserSubscriber());
    }

    private void showViewLoading() {
        userListView.showLoading();
    }

    private void hideViewLoading() {
        userListView.hideLoading();
    }

    private void showViewRetry() {
        userListView.showRetry();
    }

    private void hideViewRetry() {
        userListView.hideRetry();
    }

    private void showErrorMessage(String mess) {
        userListView.showError(mess);
    }

    private void showUserCollectionInView(Collection<User> userCollection) {
        final Collection<UserModel> userModelCollection = userModelDataMapping.parseList(userCollection);
        userListView.renderUserList(userModelCollection);
    }

    private void showEmailsToView(Collection<Email> emailCollection) {
        final Collection<EmailModel> emailModelCollection = emailModelDataMapping.parseList(emailCollection);
        userListView.showEmailsDialog(emailModelCollection);
    }

    private void showUserInserted(User user) {
        final UserModel userModel = userModelDataMapping.parse(user);
        userListView.insertUser(userModel);
    }

    private void showUserDeleted(int uid) {
        userListView.deleteUser(uid);
    }

    private final class UserListSubscriber extends DefaultSubscriber<List<User>> {

        @Override
        public void onCompleted() {
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            UserListPresenter.this.hideViewLoading();
            UserListPresenter.this.showErrorMessage(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
            UserListPresenter.this.showViewRetry();
        }

        @Override
        public void onNext(List<User> users) {
            UserListPresenter.this.showUserCollectionInView(users);
        }
    }

    private final class EmailListSubscriber extends DefaultSubscriber<List<Email>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            UserListPresenter.this.showErrorMessage(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        }

        @Override
        public void onNext(List<Email> emails) {
            UserListPresenter.this.showEmailsToView(emails);
        }
    }

    private final class CreateUserSubscriber extends DefaultSubscriber<User> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(User integer) {
            showUserInserted(integer);
        }
    }

    private final class DeleteUserSubscriber extends DefaultSubscriber<Integer> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Integer userId) {
            Log.d("User id deleted ", userId.toString());
            showUserDeleted(userId);
        }
    }
}
