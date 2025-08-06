package org.cmucreatelab.android.flutterprek.activities.adapters;

import static org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.CalculateHighlightInfo.setArc;

import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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
        // TODO @tasota trying to recycle views will cause duplicate student images to be drawn (tested on Aug6 2025)
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_student_circle, parent, false);
//            // NOTE: requires api level 21
//            result.findViewById(R.id.imageView).setClipToOutline(false);
//        } else {
//            result = convertView;
//        }
        result = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_student_circle, parent, false);
        // NOTE: requires api level 21
        result.findViewById(R.id.imageView).setClipToOutline(false);
        // ...
        final StudentWithCustomizations studentWithCustomizations = students.get(position);
        final Student student = studentWithCustomizations.student;
        TextView textView = (TextView)result.findViewById(R.id.text1);
        textView.setText(student.getName());

        //get and set ring information
       CalculateHighlightInfo.OverviewDateRange dateRange;
       //set date range for ring view with display mode from toggle button
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
        calculateHighlightInfo.studentOverview(student,dateRange, new HighlightCalculationCallback() {
            @Override
            public void onHighlightsCalculated() {

                ArcViewOverlay arcView = result.findViewById(R.id.arcView);
                setArc(calculateHighlightInfo.getStudentEmotionCounts(),arcView,10f, false);
            }
        });



        if (student.getPictureFileUuid() != null) {
            final Context appContext = activity.getApplicationContext();
            AppDatabase.getInstance(appContext).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(activity, new Observer<DbFile>() {
                @Override
                public void onChanged(@Nullable DbFile dbFile) {
                    Util.setImageViewWithDbFile(appContext, (ImageView) result.findViewById(R.id.imageView), dbFile);
                }
            });
            // TODO @tasota trying to recycle views will cause duplicate student images to be drawn (tested on Aug6 2025)
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    // NOTE: this takes awhile to execute
//                    DbFile dbFile = AppDatabase.getInstance(appContext).embeddedDAO().getDbFile(student.getPictureFileUuid());
//                    // once got row from DB, update on UI thread
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Util.setImageViewWithDbFile(appContext, (ImageView) result.findViewById(R.id.imageView), dbFile);
//                        }
//                    });
//                }
//            });
        } else {
            ((ImageView) result.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_placeholder);
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


}



