package com.akira.pisowifitimer.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.akira.pisowifitimer.pojos.HistoryModel;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface HistoryDAO {

    @Insert
    void insert(HistoryModel model);

    @Update
    void update(HistoryModel model);

    @Delete
    void delete(HistoryModel model);

    @Query("SELECT * FROM HistoryTable ORDER BY id DESC")
    Single<List<HistoryModel>> getHistory();

    @Query("SELECT SUM(amount) FROM HistoryTable")
    Single<Integer> getTotalAmount();

    @Query("SELECT SUM(amount) FROM HistoryTable WHERE date BETWEEN :startDate AND :endDate")
    Single<Integer> getTotalAmountBetween(String startDate, String endDate);

    @Query("SELECT * FROM HistoryTable WHERE date BETWEEN :startDate AND :endDate")
    Single<List<HistoryModel>> getHistoryBetween(String startDate, String endDate);

    @Query("SELECT date FROM HistoryTable ORDER BY id ASC LIMIT 1")
    Single<String> getStartDate();

    @Query("SELECT date FROM HistoryTable ORDER BY id DESC LIMIT 1")
    Single<String> getEndDate();

    @Query("SELECT COUNT(*) FROM HistoryTable")
    Single<Integer> getTotalTime();
}
