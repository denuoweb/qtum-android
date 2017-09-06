package com.pixelplex.qtum.ui.fragment.qstore;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.rest_api.QtumService;
import com.pixelplex.qtum.model.gson.qstore.QSearchItem;
import com.pixelplex.qtum.model.gson.qstore.QstoreItem;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class QStorePresenter extends BaseFragmentPresenterImpl {

    private QStoreView view;
    private List<QstoreCategory> categories;
    private int searchOffset;

    public QStorePresenter(QStoreView view){
        this.view = view;
    }

    @Override
    public QStoreView getView() {
        return view;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        categories = new ArrayList<>();
        loadCategories();
    }

    public void loadCategories(){
        QtumService.newInstance()
                .getTrendingNow()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QstoreItem>>() {
                    @Override
                    public void onCompleted() {
                        loadWatsNew();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadWatsNew();
                    }

                    @Override
                    public void onNext(List<QstoreItem> qstoreItems) {
                        categories.add(new QstoreCategory(getView().getContext().getString(R.string.trending_now),qstoreItems));
                        getView().setCategories(categories);

                    }
                });
    }

    public void loadWatsNew(){
        QtumService.newInstance()
                .getWatsNew()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QstoreItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<QstoreItem> qstoreItems) {
                        categories.add(new QstoreCategory(getView().getContext().getString(R.string.whats_new),qstoreItems));
                        getView().setCategories(categories);
                    }
                });
    }

    public void searchItemsByTag(String tag){
        searchOffset = 0;
        QtumService.newInstance()
                .searchContracts(searchOffset,tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QSearchItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<QSearchItem> qstoreItems) {
                        getView().setSearchResult(qstoreItems);
                    }
                });
    }
}