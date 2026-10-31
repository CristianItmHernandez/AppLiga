package com.hograna.liga.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import com.hograna.liga.domain.model.StatRow;
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
public final class StatsDao_Impl implements StatsDao {
  private final RoomDatabase __db;

  public StatsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public Flow<List<StatRow>> getTopScorers() {
    final String _sql = "\n"
            + "        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total\n"
            + "        FROM match_player_events mpe\n"
            + "        JOIN teams t ON mpe.teamId = t.id\n"
            + "        JOIN matches m ON mpe.matchId = m.id\n"
            + "        WHERE mpe.eventType = 'GOAL' AND m.status = 'COMPLETED'\n"
            + "        GROUP BY mpe.playerName, mpe.teamId\n"
            + "        ORDER BY total DESC, mpe.playerName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events", "teams",
        "matches"}, new Callable<List<StatRow>>() {
      @Override
      @NonNull
      public List<StatRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerName = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfTotal = 3;
          final List<StatRow> _result = new ArrayList<StatRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatRow _item;
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new StatRow(_tmpPlayerName,_tmpTeamName,_tmpCrestUri,_tmpTotal);
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

  @Override
  public Flow<List<StatRow>> getTopAssisters() {
    final String _sql = "\n"
            + "        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total\n"
            + "        FROM match_player_events mpe\n"
            + "        JOIN teams t ON mpe.teamId = t.id\n"
            + "        JOIN matches m ON mpe.matchId = m.id\n"
            + "        WHERE mpe.eventType = 'ASSIST' AND m.status = 'COMPLETED'\n"
            + "        GROUP BY mpe.playerName, mpe.teamId\n"
            + "        ORDER BY total DESC, mpe.playerName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events", "teams",
        "matches"}, new Callable<List<StatRow>>() {
      @Override
      @NonNull
      public List<StatRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerName = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfTotal = 3;
          final List<StatRow> _result = new ArrayList<StatRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatRow _item;
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new StatRow(_tmpPlayerName,_tmpTeamName,_tmpCrestUri,_tmpTotal);
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

  @Override
  public Flow<List<StatRow>> getTopSavers() {
    final String _sql = "\n"
            + "        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total\n"
            + "        FROM match_player_events mpe\n"
            + "        JOIN teams t ON mpe.teamId = t.id\n"
            + "        JOIN matches m ON mpe.matchId = m.id\n"
            + "        WHERE mpe.eventType = 'SAVE' AND m.status = 'COMPLETED'\n"
            + "        GROUP BY mpe.playerName, mpe.teamId\n"
            + "        ORDER BY total DESC, mpe.playerName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events", "teams",
        "matches"}, new Callable<List<StatRow>>() {
      @Override
      @NonNull
      public List<StatRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerName = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfTotal = 3;
          final List<StatRow> _result = new ArrayList<StatRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatRow _item;
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new StatRow(_tmpPlayerName,_tmpTeamName,_tmpCrestUri,_tmpTotal);
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

  @Override
  public Flow<List<StatRow>> getYellowCards() {
    final String _sql = "\n"
            + "        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total\n"
            + "        FROM match_player_events mpe\n"
            + "        JOIN teams t ON mpe.teamId = t.id\n"
            + "        JOIN matches m ON mpe.matchId = m.id\n"
            + "        WHERE mpe.eventType = 'YELLOW_CARD' AND m.status = 'COMPLETED'\n"
            + "        GROUP BY mpe.playerName, mpe.teamId\n"
            + "        ORDER BY total DESC, mpe.playerName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events", "teams",
        "matches"}, new Callable<List<StatRow>>() {
      @Override
      @NonNull
      public List<StatRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerName = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfTotal = 3;
          final List<StatRow> _result = new ArrayList<StatRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatRow _item;
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new StatRow(_tmpPlayerName,_tmpTeamName,_tmpCrestUri,_tmpTotal);
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

  @Override
  public Flow<List<StatRow>> getRedCards() {
    final String _sql = "\n"
            + "        SELECT mpe.playerName, t.name AS teamName, t.crestUri, SUM(mpe.count) AS total\n"
            + "        FROM match_player_events mpe\n"
            + "        JOIN teams t ON mpe.teamId = t.id\n"
            + "        JOIN matches m ON mpe.matchId = m.id\n"
            + "        WHERE mpe.eventType = 'RED_CARD' AND m.status = 'COMPLETED'\n"
            + "        GROUP BY mpe.playerName, mpe.teamId\n"
            + "        ORDER BY total DESC, mpe.playerName ASC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events", "teams",
        "matches"}, new Callable<List<StatRow>>() {
      @Override
      @NonNull
      public List<StatRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerName = 0;
          final int _cursorIndexOfTeamName = 1;
          final int _cursorIndexOfCrestUri = 2;
          final int _cursorIndexOfTotal = 3;
          final List<StatRow> _result = new ArrayList<StatRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StatRow _item;
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpTeamName;
            _tmpTeamName = _cursor.getString(_cursorIndexOfTeamName);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new StatRow(_tmpPlayerName,_tmpTeamName,_tmpCrestUri,_tmpTotal);
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
