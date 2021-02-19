package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.mvvm.repository.ViewPagerRepository;

import java.util.List;

public class ViewPagerViewModel extends ViewModel {

    ViewPagerRepository mRepository;

    ViewPagerViewModel(){
        mRepository = ViewPagerRepository.getInstance();
    }

    public String[] getLiveTitles() {
        return mRepository.getLiveTitles();
    }

    public String[] getFriendTitles() {
        return mRepository.getFriendTitles();
    }

    public List<Integer> getIndexThree() {
        return mRepository.getIndexThree();
    }

    public List<Integer> getIndexTwo(){
        return mRepository.getIndexTwo();
    }

}
