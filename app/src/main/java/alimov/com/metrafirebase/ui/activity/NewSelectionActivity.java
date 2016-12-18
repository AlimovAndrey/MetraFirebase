package alimov.com.metrafirebase.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import alimov.com.metrafirebase.R;
import alimov.com.metrafirebase.db.entity.Section;
import alimov.com.metrafirebase.util.Validator;
import alimov.com.metrafirebase.util.firebase.database.DatabaseHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSelectionActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_name)
    EditText mEditTextName;

    @BindView(R.id.edit_text_description)
    EditText mEditTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_selection);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorActionBar)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }

        mEditTextDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    onClickButtonSave();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right_defalut, R.anim.slide_out_right_default);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                onClickButtonSave();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.item_save, 0, R.string.new_section_save)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    private void onClickButtonSave() {
        if (!checkCorrectInput()) {
            return;
        }

        Section section = new Section();
        section.setmId("");
        section.setmName(mEditTextName.getText().toString());
        section.setmDescription(mEditTextDescription.getText().toString());
        section.setmCreatedAt(System.currentTimeMillis());

        DatabaseHelper.getInstance().addSection(section);

        finish();
    }

    private boolean checkCorrectInput() {
        if (!Validator.validateForName(mEditTextName.getText().toString())) {
            mEditTextName.setError(getString(R.string.new_section_error_invalid_name));
            mEditTextName.requestFocus();
            return false;
        }

        if (!Validator.validateForName(mEditTextDescription.getText().toString())) {
            mEditTextDescription.setError(getString(R.string.new_section_error_invalid_description));
            mEditTextDescription.requestFocus();
            return false;
        }

        if (!Validator.validateForDescription(mEditTextDescription.getText().toString())) {
            mEditTextDescription.setError(getString(R.string.new_section_error_invalid_description));
            mEditTextDescription.requestFocus();
            return false;
        }

        return true;
    }

}
