package com.sample.realmpractices.presentation.presenter;

import com.sample.realmpractices.domain.User;
import com.sample.realmpractices.domain.interactor.GetUserListUseCase;
import com.sample.realmpractices.domain.interactor.DefaultSubscriber;
import com.sample.realmpractices.presentation.mapper.UserModelDataMapping;
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
    private final UserModelDataMapping userModelDataMapping;

    public UserListPresenter(GetUserListUseCase getUserListUseCase,
                             UserModelDataMapping userModelDataMapping) {
        this.getUserListUseCase = getUserListUseCase;
        this.userModelDataMapping = userModelDataMapping;
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
        this.getUserListUseCase.execute(new UserListSubscriber());
    }

    public void onUserClicked(UserModel userModel) {
        userListView.viewUser(userModel);
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
}
