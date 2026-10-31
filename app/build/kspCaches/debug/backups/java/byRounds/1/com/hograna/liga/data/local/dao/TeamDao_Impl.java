package com.hograna.liga.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hograna.liga.data.local.entity.TeamEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamEntity> __insertionAdapterOfTeamEntity;

  private final EntityDeletionOrUpdateAdapter<TeamEntity> __deletionAdapterOfTeamEntity;

  private final EntityDeletionOrUpdateAdapter<TeamEntity> __updateAdapterOfTeamEntity;

  private final SharedSQLiteStatement __preparedStmtOfIncrementByeCount;

  public TeamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamEntity = new EntityInsertionAdapter<TeamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `teams` (`id`,`name`,`leagueOrigin`,`crestUri`,`captainName`,`byeCount`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TeamEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLeagueOrigin());
        statement.bindString(4, entity.getCrestUri());
        statement.bindString(5, entity.getCaptainName());
        statement.bindLong(6, entity.getByeCount());
      }
    };
    this.__deletionAdapterOfTeamEntity = new EntityDeletionOrUpdateAdapter<TeamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `teams` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TeamEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTeamEntity = new EntityDeletionOrUpdateAdapter<TeamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `teams` SET `id` = ?,`name` = ?,`leagueOrigin` = ?,`crestUri` = ?,`captainName` = ?,`byeCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TeamEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getLeagueOrigin());
        statement.bindString(4, entity.getCrestUri());
        statement.bindString(5, entity.getCaptainName());
        statement.bindLong(6, entity.getByeCount());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfIncrementByeCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE teams SET byeCount = byeCount + 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTeam(final TeamEntity team, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTeamEntity.insertAndReturnId(team);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTeam(final TeamEntity team, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTeamEntity.handle(team);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTeam(final TeamEntity team, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTeamEntity.handle(team);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementByeCount(final long teamId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementByeCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, teamId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementByeCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TeamEntity>> getAllTeams() {
    final String _sql = "SELECT * FROM teams ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<List<TeamEntity>>() {
      @Override
      @NonNull
      public List<TeamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLeagueOrigin = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueOrigin");
          final int _cursorIndexOfCrestUri = CursorUtil.getColumnIndexOrThrow(_cursor, "crestUri");
          final int _cursorIndexOfCaptainName = CursorUtil.getColumnIndexOrThrow(_cursor, "captainName");
          final int _cursorIndexOfByeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "byeCount");
          final List<TeamEntity> _result = new ArrayList<TeamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TeamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLeagueOrigin;
            _tmpLeagueOrigin = _cursor.getString(_cursorIndexOfLeagueOrigin);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpByeCount;
            _tmpByeCount = _cursor.getInt(_cursorIndexOfByeCount);
            _item = new TeamEntity(_tmpId,_tmpName,_tmpLeagueOrigin,_tmpCrestUri,_tmpCaptainName,_tmpByeCount);
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
  public Object getAllTeamsSuspend(final Continuation<? super List<TeamEntity>> $completion) {
    final String _sql = "SELECT * FROM teams ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TeamEntity>>() {
      @Override
      @NonNull
      public List<TeamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLeagueOrigin = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueOrigin");
          final int _cursorIndexOfCrestUri = CursorUtil.getColumnIndexOrThrow(_cursor, "crestUri");
          final int _cursorIndexOfCaptainName = CursorUtil.getColumnIndexOrThrow(_cursor, "captainName");
          final int _cursorIndexOfByeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "byeCount");
          final List<TeamEntity> _result = new ArrayList<TeamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TeamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLeagueOrigin;
            _tmpLeagueOrigin = _cursor.getString(_cursorIndexOfLeagueOrigin);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpByeCount;
            _tmpByeCount = _cursor.getInt(_cursorIndexOfByeCount);
            _item = new TeamEntity(_tmpId,_tmpName,_tmpLeagueOrigin,_tmpCrestUri,_tmpCaptainName,_tmpByeCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTeamById(final long id, final Continuation<? super TeamEntity> $completion) {
    final String _sql = "SELECT * FROM teams WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TeamEntity>() {
      @Override
      @Nullable
      public TeamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLeagueOrigin = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueOrigin");
          final int _cursorIndexOfCrestUri = CursorUtil.getColumnIndexOrThrow(_cursor, "crestUri");
          final int _cursorIndexOfCaptainName = CursorUtil.getColumnIndexOrThrow(_cursor, "captainName");
          final int _cursorIndexOfByeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "byeCount");
          final TeamEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLeagueOrigin;
            _tmpLeagueOrigin = _cursor.getString(_cursorIndexOfLeagueOrigin);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpByeCount;
            _tmpByeCount = _cursor.getInt(_cursorIndexOfByeCount);
            _result = new TeamEntity(_tmpId,_tmpName,_tmpLeagueOrigin,_tmpCrestUri,_tmpCaptainName,_tmpByeCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTeamsByIds(final List<Long> ids,
      final Continuation<? super List<TeamEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM teams WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TeamEntity>>() {
      @Override
      @NonNull
      public List<TeamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLeagueOrigin = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueOrigin");
          final int _cursorIndexOfCrestUri = CursorUtil.getColumnIndexOrThrow(_cursor, "crestUri");
          final int _cursorIndexOfCaptainName = CursorUtil.getColumnIndexOrThrow(_cursor, "captainName");
          final int _cursorIndexOfByeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "byeCount");
          final List<TeamEntity> _result = new ArrayList<TeamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TeamEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLeagueOrigin;
            _tmpLeagueOrigin = _cursor.getString(_cursorIndexOfLeagueOrigin);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpByeCount;
            _tmpByeCount = _cursor.getInt(_cursorIndexOfByeCount);
            _item_1 = new TeamEntity(_tmpId,_tmpName,_tmpLeagueOrigin,_tmpCrestUri,_tmpCaptainName,_tmpByeCount);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getTeamCount() {
    final String _sql = "SELECT COUNT(*) FROM teams";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Object getTeamsOrderedByByeCount(
      final Continuation<? super List<TeamEntity>> $completion) {
    final String _sql = "SELECT * FROM teams ORDER BY byeCount ASC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TeamEntity>>() {
      @Override
      @NonNull
      public List<TeamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLeagueOrigin = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueOrigin");
          final int _cursorIndexOfCrestUri = CursorUtil.getColumnIndexOrThrow(_cursor, "crestUri");
          final int _cursorIndexOfCaptainName = CursorUtil.getColumnIndexOrThrow(_cursor, "captainName");
          final int _cursorIndexOfByeCount = CursorUtil.getColumnIndexOrThrow(_cursor, "byeCount");
          final List<TeamEntity> _result = new ArrayList<TeamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TeamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpLeagueOrigin;
            _tmpLeagueOrigin = _cursor.getString(_cursorIndexOfLeagueOrigin);
            final String _tmpCrestUri;
            _tmpCrestUri = _cursor.getString(_cursorIndexOfCrestUri);
            final String _tmpCaptainName;
            _tmpCaptainName = _cursor.getString(_cursorIndexOfCaptainName);
            final int _tmpByeCount;
            _tmpByeCount = _cursor.getInt(_cursorIndexOfByeCount);
            _item = new TeamEntity(_tmpId,_tmpName,_tmpLeagueOrigin,_tmpCrestUri,_tmpCaptainName,_tmpByeCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
