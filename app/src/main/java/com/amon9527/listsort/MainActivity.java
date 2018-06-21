package com.amon9527.listsort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.amon9527.listsort.adapter.MyContactAdapter;
import com.amon9527.listsort.model.ContactBean;
import com.amon9527.listsort.utils.TestUtils;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_GROUP_NAME = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<ContactBean> contactList = TestUtils.CONTACT_LIST;
        Collections.sort(contactList, new MyComparator());
        Map<Integer, String> groupIndexMap = getGroupIndex(contactList);
        MyContactAdapter contactAdapter = new MyContactAdapter(this, contactList,
                groupIndexMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(contactAdapter);
    }

    /**
     * 获取需要显示groupName的index
     * 规则：
     * 对排序后的列表的每个item的groupName进行比对，和上一个不同的就表示需要显示
     * （第一项必定显示，无需对比）
     *
     * @param contactList
     * @return
     */
    private Map<Integer, String> getGroupIndex(List<ContactBean> contactList) {
        Map<Integer, String> groupIndexMap = new HashMap<>();
        if (!contactList.isEmpty()) {
            int size = contactList.size();
            String letter = getLetter(contactList.get(0).getName());
            groupIndexMap.put(0, letter);
            for (int i = 1; i < size; i++) {
                ContactBean preContactBean = contactList.get(i - 1);
                String preLetter = getLetter(preContactBean.getName());
                ContactBean contactBean = contactList.get(i);
                String curLetter = getLetter(contactBean.getName());
                if (!curLetter.equals(preLetter)) {
                    groupIndexMap.put(i, curLetter);
                }
            }
        }
        return groupIndexMap;
    }

    /**
     * 获取字符串对应的groupName
     * 规则：
     * 1、字母直接转换成大写字母
     * 2、中文转换成拼音并返回拼音首个字符对应的大写字母
     * 3、其他返回#
     *
     * @param name
     * @return
     */
    private String getLetter(String name) {
        String result = DEFAULT_GROUP_NAME;
        if (!TextUtils.isEmpty(name)) {
            String firstNameLetter = String.valueOf(name.charAt(0));
            if (isLetter(firstNameLetter)) {
                result = firstNameLetter.toUpperCase();
            } else if (isChinese(firstNameLetter)) {
                result = String.valueOf(Pinyin.toPinyin(firstNameLetter.charAt(0)).charAt(0))
                        .toUpperCase();
            }
        }
        return result;
    }

    /**
     * 判断是否是字母
     *
     * @param str
     * @return
     */
    private boolean isLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String regex = "^[a-zA-Z]+$";
        return str.matches(regex);
    }

    /**
     * 判断是否是中文
     *
     * @param str
     * @return
     */
    private boolean isChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String regex = "^[\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }


    class MyComparator implements Comparator<ContactBean> {

        @Override
        public int compare(ContactBean o1, ContactBean o2) {
            String pinYin1 = getPinyin(o1.getName());
            String pinYin2 = getPinyin(o2.getName());
            return pinYin1.compareTo(pinYin2);
        }
    }

    /**
     * 获取字符串对应的拼音字符串，分两个步骤
     * 一、先对每个字符进行处理，规则：
     * 1、中文字符，返回对应的大写的拼音
     * 2、字母，转换成大写字母
     * 3、其他，不作处理
     * 二、判断字符串的首个字符是否是字母或汉字，如果是，不做处理，否则在第一步转换后的结果前面添加
     * 一个Unicode值比字母大的任意一个字符（保证compare排序的时候位置在字母后面）
     *
     * @param str
     * @return
     */
    private String getPinyin(String str) {
        StringBuilder resultSb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            for (char item : str.trim().toCharArray()) {
                String itemStr = Character.toString(item);
                if (isChinese(itemStr)) {
                    resultSb.append(Pinyin.toPinyin(item).toUpperCase());
                } else if (isLetter(itemStr)) {
                    resultSb.append(Character.toString(item).toUpperCase());
                } else {
                    resultSb.append(Character.toString(item));
                }
            }
        }
        if (DEFAULT_GROUP_NAME.equals(getLetter(str))) {
            resultSb.insert(0, "~");
        }
        return resultSb.toString();
    }
}
