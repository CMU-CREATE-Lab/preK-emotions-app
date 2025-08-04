package org.cmucreatelab.android.flutterprek.activities.adapters;

import static org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo.setArc;

import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.views.ArcViewOverlay;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.embedded_models.StudentWithCustomizations;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class StudentHighlightWithCustomizationsIndexAdapter extends AbstractListAdapter<StudentWithCustomizations> {

    private final AppCompatActivity activity;
    private final List<StudentWithCustomizations> students;
    private final boolean onClickListener, hasAddNewStudent;
    private final ClickListener clickListener;
    private final ClickAddNewStudentListener clickAddNewStudentListener;
    public static final int MODE_DAY = 0;
    public static final int MODE_WEEK = 1;
    public static final int MODE_MONTH = 2;
    public static final int MODE_YEAR = 3;
    private static int displayMode = MODE_WEEK;

    public void setDisplayMode(int mode) {
        this.displayMode = mode;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(StudentWithCustomizations student);
    }
    public interface ClickAddNewStudentListener {
       void onClick();
    }


    public StudentHighlightWithCustomizationsIndexAdapter(AppCompatActivity activity, List<StudentWithCustomizations> students) {
        this(activity, students, null, null);
    }


    public StudentHighlightWithCustomizationsIndexAdapter(AppCompatActivity activity, List<StudentWithCustomizations> students, ClickListener clickListener,ClickAddNewStudentListener clickAddNewStudentListener) {
        this.activity = activity;
        this.students = students;
        this.clickListener = clickListener;
        this.clickAddNewStudentListener = clickAddNewStudentListener;
        this.onClickListener = (clickListener != null);
        this.hasAddNewStudent = (clickAddNewStudentListener != null);
    }
    @Override
    public int getCount() {
        if (hasAddNewStudent) {
            return getList().size() +1;
        }
        return getList().size();
    }

    private View populateStudentView(int position, View convertView, ViewGroup parent) {
        final View result;
        final ViewHolder holder;

        if (convertView == null) {
            // Inflate new view and create ViewHolder
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_student_circle, parent, false);
            holder = new ViewHolder();
            holder.imageView = result.findViewById(R.id.imageView);
            holder.textView = result.findViewById(R.id.text1);
            holder.arcView = result.findViewById(R.id.arcView);
            holder.dbFileObserver = null;  // Initialize observer to null
            result.setTag(holder);

            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(false);
        } else {
            result = convertView;
            holder = (ViewHolder) result.getTag();

            // Before reuse, remove previous observer if exists
            if (holder.dbFileObserver != null && holder.imageView.getTag() != null) {
                String oldUuid = holder.imageView.getTag().toString();
                AppDatabase.getInstance(activity.getApplicationContext()).dbFileDAO()
                        .getDbFile(oldUuid)
                        .removeObserver(holder.dbFileObserver);
                holder.dbFileObserver = null;
                holder.imageView.setImageDrawable(null); // Clear previous image
                holder.imageView.setTag(null);
            }
        }

        final StudentWithCustomizations studentWithCustomizations = students.get(position);
        final Student student = studentWithCustomizations.student;
        holder.textView.setText(student.getName());

// Determine date range for ring view based on display mode
        CalculateHighlightInfo.OverviewDateRange dateRange;
        switch (displayMode) {
            case MODE_DAY:
                dateRange = CalculateHighlightInfo.OverviewDateRange.DAY;
                break;
            case MODE_WEEK:
                dateRange = CalculateHighlightInfo.OverviewDateRange.WEEK;
                break;
            case MODE_MONTH:
                dateRange = CalculateHighlightInfo.OverviewDateRange.MONTH;
                break;
            case MODE_YEAR:
                dateRange = CalculateHighlightInfo.OverviewDateRange.YEAR;
                break;
            default:
                dateRange = CalculateHighlightInfo.OverviewDateRange.WEEK;
        }

        CalculateHighlightInfo calculateHighlightInfo = new CalculateHighlightInfo(null, activity.getApplicationContext(), activity);
        calculateHighlightInfo.studentOverview(student, dateRange, new HighlightCalculationCallback() {
            @Override
            public void onHighlightsCalculated() {
                setArc(calculateHighlightInfo.getStudentEmotionCounts(), holder.arcView, 10f, false);
            }
        });

        final Context appContext = activity.getApplicationContext();

        if (student.getPictureFileUuid() != null) {
            final String uuid = student.getPictureFileUuid();
            holder.imageView.setTag(uuid);

            // Create a new observer for this view
            holder.dbFileObserver = new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    if (uuid.equals(holder.imageView.getTag())) {
                        Util.setImageViewWithDbFile(appContext, holder.imageView, dbFile);
                    }
                }
            };

            // Attach the observer
            AppDatabase.getInstance(appContext).dbFileDAO()
                    .getDbFile(uuid)
                    .observe(activity, holder.dbFileObserver);

        } else {
            // No image UUID, clear tag and set placeholder
            holder.imageView.setTag(null);
            holder.imageView.setImageResource(R.drawable.ic_placeholder);
        }

        if (onClickListener) {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(studentWithCustomizations);
                }
            });
        }

        return result;

    }


    private View populateAddNewStudentView(int position, View convertView, ViewGroup parent) {
        // TODO populate new student view, use clickAddNewStudentListener
        final View result;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_student_circle, parent, false);
            // NOTE: requires api level 21
            result.findViewById(R.id.imageView).setClipToOutline(false);
        } else {
            result = convertView;
        }
        TextView textView = result.findViewById(R.id.text1);
        textView.setText("Add New Student");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);


        ArcViewOverlay arcView = result.findViewById(R.id.arcView);


//        List<ArcSegment> segments = new ArrayList<>();
//        segments.add(new ArcSegment(Color.GRAY, Color.GRAY, false, 90));
//        arcView.setSegments(segments);



        ((ImageView) result.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_add_student);
        setArc(null, arcView,10,true);

        if (onClickListener) {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAddNewStudentListener.onClick();
                }
            });
        }

        return result;
    }



    @Override
    public List<StudentWithCustomizations> getList() {
        return students;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return populateStudentView(position, convertView, parent);

        if(hasAddNewStudent) {
            boolean lastPosition;
            if (position == getCount() - 1) {
                return populateAddNewStudentView(position, convertView, parent);
            } else {
                return populateStudentView(position, convertView, parent);
            }
        } else {
            return populateStudentView(position, convertView, parent);
        }
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows;

        if (items > 0) {
            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            int itemHeight = listItem.getMeasuredHeight();


            rows = (int) Math.ceil((double) items / columns);

            totalHeight = itemHeight * rows;

            // Add spacing if needed (optional)
//            totalHeight += gridView.getVerticalSpacing() * (rows - 1);
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public interface HighlightCalculationCallback {
        void onHighlightsCalculated();
    }
    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        ArcViewOverlay arcView;
        Observer<DbFile> dbFileObserver;
    }

}



