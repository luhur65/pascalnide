package com.duy.pascal.compiler.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.compiler.MenuEditor;
import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.data.Preferences;
import com.duy.pascal.compiler.data.TabFileUtils;
import com.duy.pascal.compiler.file_manager.FileManager;
import com.duy.pascal.compiler.view.LockableScrollView;
import com.duy.pascal.compiler.view.SymbolListView;
import com.duy.pascal.compiler.view.code_view.CodeView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 09-Mar-17.
 */

public abstract class FileEditorActivity extends AbstractAppCompatActivity
        implements MenuEditor.EditorControl {
    protected String mFilePath = FileManager.getApplicationPath() + "new_file.pas";
    protected FileManager fileManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view)
    SymbolListView mKeyList;
    @BindView(R.id.scroll)
    LockableScrollView mScrollView;
    @BindView(R.id.edit_editor)
    CodeView mCodeView;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    ArrayList<File> listFile;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileManager = new FileManager(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setTitle("");
        setupTab();
    }

    protected void setupTab() {
        new LoadTabFile().execute();
    }

    protected TabLayout.Tab getTab(File file) {
        final TabLayout.Tab tab = tabLayout.newTab().setText(file.getName());
        tab.setCustomView(R.layout.item_tab_file);
        View root = tab.getCustomView();
        View vClose = tab.getCustomView().findViewById(R.id.img_close);
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTab(tab);
            }
        });
        TextView txtTitle = (TextView) root.findViewById(R.id.txt_title);
        txtTitle.setText(file.getName());
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tab.isSelected()) {
                    tab.select();
                    doSelectTab(tab, true);
                }
            }
        });
        return tab;
    }

    /**
     * remove tab
     *
     * @param tab - tab for remove
     */
    protected void removeTab(TabLayout.Tab tab) {
        //get position
        int position = tab.getPosition();
        mCodeView.clearStackHistory();

        //set last position to view
        if (position > 0) {
            //save file and remove tab entry
            fileManager.saveFile(listFile.get(position), getCode());
            fileManager.removeTabFile(listFile.get(position).getPath());
            listFile.remove(position);
            tabLayout.removeTab(tab);

            loadFile(listFile.get(position - 1).getPath());
            Toast.makeText(this, R.string.closed, Toast.LENGTH_SHORT).show();
        } else if (position == 0) {
            if (listFile.size() >= 2) {
                //save file and remove tab entry
                fileManager.saveFile(listFile.get(position), getCode());
                fileManager.removeTabFile(listFile.get(position).getPath());
                listFile.remove(position);
                tabLayout.removeTab(tab);
                doSelectTab(tabLayout.getTabAt(0), false);
                Toast.makeText(this, R.string.closed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.cant_close_file, Toast.LENGTH_SHORT).show();
            }
        }
        //show toast
    }

    private void createEmptyFile() {
        //auto create empty file
        //create new file
        String filePath = fileManager.createNewFile(FileManager.getApplicationPath() + "new_" +
                Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        File file = new File(filePath);
        //load to view
        addNewTab(file);
        mCodeView.clearStackHistory();
        fileManager.addNewPath(filePath);
        listFile.add(file);
        moveToTab(0);
    }


    protected void doSelectTab(TabLayout.Tab tab, boolean save) {
        moveToTab(tab.getPosition());

        /**
         * save history for undo redo
         */
//        mCodeView.saveHistory(mFilePath);
        if (save) fileManager.saveFile(mFilePath, getCode());

        loadFile(listFile.get(tab.getPosition()).getPath());
        /**
         * restore history undo redo of file
         */
        mCodeView.clearStackHistory();
//        mCodeView.restoreHistory(mFilePath);
    }

    protected void addNewTab(File file) {
        TabLayout.Tab tab = getTab(file);
        tabLayout.addTab(tab);
    }


    /**
     * check file if not in list file, add it to tab and select tab of file
     */
    protected void addNewFile(File file, boolean saveLastFile) {
        if (file.exists()) {
            int index = listFile.indexOf(file);
            if (index != -1) {
                doSelectTab(tabLayout.getTabAt(index), saveLastFile);
            } else {
                fileManager.addNewPath(file.getPath());
                listFile.add(file);
                addNewTab(file);
                doSelectTab(tabLayout.getTabAt(listFile.size() - 1), saveLastFile);
            }
        } else {
        }
    }

    protected void moveToTab(final int i) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.select();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences.put(Preferences.TAB_POSITION_FILE, tabLayout.getSelectedTabPosition());
    }

    protected abstract String getCode();

    protected abstract void loadFile(String path);


    /**
     * load lasted file in database and set data to tablayout
     */
    private class LoadTabFile extends AsyncTask<Void, File, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            listFile = TabFileUtils.getTabFiles(FileEditorActivity.this);
            boolean result = false;
            for (File file : listFile) {
                publishProgress(file);
                result = true;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(File... values) {
            super.onProgressUpdate(values);
            addNewTab(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {//empty file
                createEmptyFile();
            } else {
                int pos = (mPreferences.getInt(Preferences.TAB_POSITION_FILE));
                Log.d(TAG, "onPostExecute: " + pos);
                TabLayout.Tab tab = tabLayout.getTabAt((pos));
                if (tab != null) {
                    doSelectTab(tab, false);
                }
            }
        }
    }

}
