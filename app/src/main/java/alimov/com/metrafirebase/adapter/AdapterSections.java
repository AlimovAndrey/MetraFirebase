package alimov.com.metrafirebase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import alimov.com.metrafirebase.R;
import alimov.com.metrafirebase.db.entity.Section;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Roma on 06.10.2015.
 */
public class AdapterSections extends RecyclerView.Adapter<AdapterSections.SectionViewHolder> {

    private List<Section> mSections = new ArrayList<>();
    private SectionsCallback mCallback;

    public AdapterSections(List<Section> sections, SectionsCallback callback) {
        mSections.clear();
        mSections.addAll(sections);
        mCallback = callback;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SectionViewHolder holder, int position) {
        final Section section = mSections.get(position);

        holder.mTextViewDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(section.getmCreatedAt()));

        holder.mTextViewName.setText(section.getmName());
        holder.mTextViewDescription.setText(section.getmDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onClick(section);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSections.size();
    }

    public void updateSections(List<Section> sections) {
        mSections.clear();
        mSections.addAll(sections);
        notifyDataSetChanged();
    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_name)
        TextView mTextViewName;

        @BindView(R.id.text_view_date)
        TextView mTextViewDate;

        @BindView(R.id.text_view_description)
        TextView mTextViewDescription;

        public SectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SectionsCallback {
        void onClick(Section section);
    }
}
