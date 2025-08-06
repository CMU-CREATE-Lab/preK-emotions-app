package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design.students;

import androidx.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

//HOLDER CLASS FOR STUDENT AND DBFILE TO BE PASSED INTO ADAPTER  -- removes the need for calling observer in adapter
public class StudentDisplayItem {
    public final Student student;
    public final DbFile dbFile;

    public StudentDisplayItem(Student student, @Nullable DbFile dbFile) {
        this.student = student;
        this.dbFile = dbFile;
    }

}
