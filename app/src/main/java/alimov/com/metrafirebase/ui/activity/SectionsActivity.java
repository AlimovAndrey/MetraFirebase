package alimov.com.metrafirebase.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alimov.com.metrafirebase.R;
import alimov.com.metrafirebase.adapter.AdapterSections;
import alimov.com.metrafirebase.db.entity.Section;
import alimov.com.metrafirebase.util.firebase.database.DatabaseHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionsActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_selection)
    RecyclerView mRecyclerViewSelection;

    @BindView(R.id.text_view_empty)
    TextView mTextViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorActionBar)));
        actionBar.setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }


        List<Section> sections = new ArrayList<>();

        final AdapterSections adapter = new AdapterSections(sections, new AdapterSections.SectionsCallback() {
            @Override
            public void onClick(final Section section) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SectionsActivity.this, SectionGalleryActivity.class);
                        intent.putExtra(SectionGalleryActivity.KEY_SECTION_ID, section.getmId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left_default, R.anim.slide_out_left_default);
                    }
                }, 100);
            }
        });

        DatabaseHelper.getInstance().addSectionsListener(new DatabaseHelper.OnChangeSectionsListener() {
            @Override
            public void onChange(List<Section> sections) {
                if (sections.size() == 0) {
                    mRecyclerViewSelection.setVisibility(View.INVISIBLE);
                    mTextViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerViewSelection.setVisibility(View.VISIBLE);
                    mTextViewEmpty.setVisibility(View.INVISIBLE);
                    adapter.updateSections(sections);
                }
            }
        });



        mRecyclerViewSelection.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerViewSelection.setLayoutManager(manager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.new_selection, 0, "")
                .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_plus))
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_selection:
                startActivity(new Intent(SectionsActivity.this, NewSelectionActivity.class));
                overridePendingTransition(R.anim.slide_in_left_default, R.anim.slide_out_left_default);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
