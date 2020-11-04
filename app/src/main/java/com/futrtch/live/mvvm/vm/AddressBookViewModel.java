package com.futrtch.live.mvvm.vm;

import androidx.lifecycle.ViewModel;

import com.futrtch.live.beans.AddressBookBean;

import java.util.ArrayList;
import java.util.List;

public class AddressBookViewModel extends ViewModel {

    List<AddressBookBean> mListData = new ArrayList<>();

    AddressBookViewModel(){
        mListData.add(new AddressBookBean("","用户1","名字：小红", true));
        mListData.add(new AddressBookBean("","用户2","名字：小白", false));
        mListData.add(new AddressBookBean("","用户3","名字：小绿", false));
        mListData.add(new AddressBookBean("","用户4","名字：小紫", false));
        mListData.add(new AddressBookBean("","用户5","名字：小黄", true));
        mListData.add(new AddressBookBean("","用户6","名字：小橙", true));
        mListData.add(new AddressBookBean("","用户7","名字：小青", true));
    }

    public List<AddressBookBean> getmListData() {
        return mListData;
    }
}
