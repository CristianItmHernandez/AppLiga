package com.hograna.liga.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import com.hograna.liga.domain.model.StandingRow;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StandingsDao_Impl implements StandingsDao {
  private final RoomDatabase __db;

  public StandingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public Flow<List<StandingRow>> getStandings() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            t.id AS teamId,\n"
            + "            t.name AS teamName,\n"
            + "            t.crestUri AS crestUri,\n"
            + "            t.captainName AS captainName,\n"
            + "            COUNT(m.id) AS played,\n"
            + "            SUM(CASE \n"
            + "                WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR \n"
            + "                     (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 1 ELSE 0 END) AS won,\n"
            + "            SUM(CASE \n"
            + "                WHEN m.homeGoals = m.awayGoals THEN 1 ELSE 0 END) AS drawn,\n"
            + "            SUM(CASE \n"
            + "                WHEN (m.homeTeamId = t.id AND m.homeGoals < m.awayGoals) OR \n"
            + "                     (m.awayTeamId = t.id AND m.awayGoals < m.homeGoals) THEN 1 ELSE 0 END) AS lost,\n"
            + "            SUM(CASE \n"
            + "                WHEN m.homeTeamId = t.id THEN m.homeGoals \n"
            + "                WHEN m.awayTeamId = t.id THEN m.awayGoals\n"
            + "                ELSE 0 END) AS goalsFor,\n"
            + "            SUM(CASE \n"
            + "                WHEN m.homeTeamId = t.id THEN m.awayGoals\n"
            + "                WHEN m.awayTeamId = t.id THEN m.homeGoals\n"
            + "                ELSE 0 END) AS goalsAgainst,\n"
            + "            SUM(CASE \n"
            + "                WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR \n"
            + "                     (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 3\n"
            + "                WHEN m.homeGoals = m.awayGoals THEN 1 \n"
            + "                ELSE 0 END) AS points\n"
            + "        FROM teams t\n"
            + "        LEFT JOIN matches m ON (m.homeTeamId = t.id OR m.awayTeamId = t.id) \n"
            + "            AND m.stage = 'REGULAR' AND m.status = 'COMPLETED' AND m.isBye = 0\n"
            + "        GROUP BY t.id\n"
            + "        ORDER BY points DESC, \n"
            + "                 (SUM(CASE WHEN m.homeTeamId = t.id THEN m.homeGoals ELSE m.awayGoals END) -\n"
            + "                  SUM(CASE WHEN m.homeTeamId = t.id THEN m.awayGoals ELSE m.homeGoals END)) DESC,\n"
            + "                 goalsFor DESC,\n"
            + "                 t.name ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams",
        "matches"}, new Callable<List<StandingRow>>() {
      @Override
      @NonNull
      public List<StandingRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTeamId = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfCaptainName = 3;
          final int _cursorIndexOfPlayed = 4;
          final int _cursorIndexOfWon = 5;
          final int _cursorIndexOfDrawn = 6;
          final int _cursorIndexOfLost = 7;
          final int _cursorIndexOfGoalsFor = 8;
          final int _cursorIndexOfGoalsAgainst = 9;
          final int _cursorIndexOfPoints = 10;
          final List<StandingRow> _result = new ArrayList<StandingRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StandingRow _item;
            final long _tmpTeamId;
            _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpPlayed;
            _tmpPlayed = _cursor.getInt(_cursorIndexOfPlayed);
            final int _tmpWon;
            _tmpWon = _cursor.getInt(_cursorIndexOfWon);
            final int _tmpDrawn;
            _tmpDrawn = _cursor.getInt(_cursorIndexOfDrawn);
            final int _tmpLost;
            _tmpLost = _cursor.getInt(_cursorIndexOfLost);
            final int _tmpGoalsFor;
            _tmpGoalsFor = _cursor.getInt(_cursorIndexOfGoalsFor);
            final int _tmpGoalsAgainst;
            _tmpGoalsAgainst = _cursor.getInt(_cursorIndexOfGoalsAgainst);
            final int _tmpPoints;
            _tmpPoints = _cursor.getInt(_cursorIndexOfPoints);
            _item = new StandingRow(_tmpTeamId,_tmpTeamName,_tmpCrestUri,_tmpCaptainName,_tmpPlayed,_tmpWon,_tmpDrawn,_tmpLost,_tmpGoalsFor,_tmpGoalsAgainst,_tmpPoints);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
