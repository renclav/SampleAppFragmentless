package com.touchnote.renclav.touchnotesampleapp.clues;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.touchnote.renclav.touchnotesampleapp.Injection;
import com.touchnote.renclav.touchnotesampleapp.R;
import com.touchnote.renclav.touchnotesampleapp.clues.container.CluesContainer;
import com.touchnote.renclav.touchnotesampleapp.clues.contract.CluesActivityContract;
import com.touchnote.renclav.touchnotesampleapp.clues.presenter.CluesPresenter;
import com.touchnote.renclav.touchnotesampleapp.util.EspressoIdlingResource;


public class CluesActivity extends AppCompatActivity implements CluesActivityContract {

    private static final String CLUES_PRESENTER_KEY = CluesPresenter.class.getSimpleName();

    private CluesPresenter cluesPresenter;
    private CluesContainer container;

    private MenuItem toggleItem;

    private
    @MenuStates.MenuState
    int menuState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        container = (CluesContainer) findViewById(R.id.container);

        cluesPresenter = new CluesPresenter(
                Injection.provideCluesRepository(getApplicationContext()),
                container,
                Injection.provideSchedulerProvider(), this);
        menuState = MenuStates.LIST;
        if(savedInstanceState != null)
        {
            cluesPresenter.restoreSaveStateParcelable(savedInstanceState.getParcelable(CLUES_PRESENTER_KEY));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cluesPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        cluesPresenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(cluesPresenter.getSaveStateParcelable() != null)
        {
            outState.putParcelable(CLUES_PRESENTER_KEY, cluesPresenter.getSaveStateParcelable());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        toggleItem = menu.findItem(R.id.listLayoutMenuItem);
        setMenuState(menuState);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.listLayoutMenuItem) {
            cluesPresenter.toggleListLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    public void setMenuState(@MenuStates.MenuState int state) {
        this.menuState = state;
        if (toggleItem != null) {
            switch (this.menuState) {
                case MenuStates.LIST:
                    toggleItem.setIcon(R.drawable.ic_grid_black_24dp);
                    toggleItem.setVisible(true);
                    break;
                case MenuStates.GRID:
                    toggleItem.setIcon(R.drawable.ic_list_black_24dp);
                    toggleItem.setVisible(true);
                    break;
                default:
                    toggleItem.setVisible(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean handled = container.onBackPressed();
        if (!handled) {
            finish();
        }
    }

    public CluesPresenter getCluesPresenter() {
        return cluesPresenter;
    }
}
