package com.hograna.liga.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hograna.liga.data.local.entity.MatchEntity;
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class MatchDao_Impl implements MatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MatchEntity> __insertionAdapterOfMatchEntity;

  private final EntityInsertionAdapter<MatchPlayerEventEntity> __insertionAdapterOfMatchPlayerEventEntity;

  private final EntityDeletionOrUpdateAdapter<MatchEntity> __deletionAdapterOfMatchEntity;

  private final EntityDeletionOrUpdateAdapter<MatchEntity> __updateAdapterOfMatchEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMatchById;

  private final SharedSQLiteStatement __preparedStmtOfDeletePlayerEventsForMatch;

  public MatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMatchEntity = new EntityInsertionAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `matches` (`id`,`stage`,`matchdayOrRound`,`knockoutTieId`,`legNumber`,`homeTeamId`,`awayTeamId`,`isBye`,`status`,`homeGoals`,`awayGoals`,`homeFouls`,`awayFouls`,`mvpPlayerName`,`mvpRating`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStage());
        statement.bindLong(3, entity.getMatchdayOrRound());
        if (entity.getKnockoutTieId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getKnockoutTieId());
        }
        statement.bindLong(5, entity.getLegNumber());
        statement.bindLong(6, entity.getHomeTeamId());
        if (entity.getAwayTeamId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getAwayTeamId());
        }
        final int _tmp = entity.isBye() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getStatus());
        statement.bindLong(10, entity.getHomeGoals());
        statement.bindLong(11, entity.getAwayGoals());
        statement.bindLong(12, entity.getHomeFouls());
        statement.bindLong(13, entity.getAwayFouls());
        if (entity.getMvpPlayerName() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getMvpPlayerName());
        }
        if (entity.getMvpRating() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getMvpRating());
        }
      }
    };
    this.__insertionAdapterOfMatchPlayerEventEntity = new EntityInsertionAdapter<MatchPlayerEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `match_player_events` (`id`,`matchId`,`teamId`,`playerName`,`eventType`,`count`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchPlayerEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMatchId());
        statement.bindLong(3, entity.getTeamId());
        statement.bindString(4, entity.getPlayerName());
        statement.bindString(5, entity.getEventType());
        statement.bindLong(6, entity.getCount());
      }
    };
    this.__deletionAdapterOfMatchEntity = new EntityDeletionOrUpdateAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `matches` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMatchEntity = new EntityDeletionOrUpdateAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `matches` SET `id` = ?,`stage` = ?,`matchdayOrRound` = ?,`knockoutTieId` = ?,`legNumber` = ?,`homeTeamId` = ?,`awayTeamId` = ?,`isBye` = ?,`status` = ?,`homeGoals` = ?,`awayGoals` = ?,`homeFouls` = ?,`awayFouls` = ?,`mvpPlayerName` = ?,`mvpRating` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStage());
        statement.bindLong(3, entity.getMatchdayOrRound());
        if (entity.getKnockoutTieId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getKnockoutTieId());
        }
        statement.bindLong(5, entity.getLegNumber());
        statement.bindLong(6, entity.getHomeTeamId());
        if (entity.getAwayTeamId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getAwayTeamId());
        }
        final int _tmp = entity.isBye() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getStatus());
        statement.bindLong(10, entity.getHomeGoals());
        statement.bindLong(11, entity.getAwayGoals());
        statement.bindLong(12, entity.getHomeFouls());
        statement.bindLong(13, entity.getAwayFouls());
        if (entity.getMvpPlayerName() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getMvpPlayerName());
        }
        if (entity.getMvpRating() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getMvpRating());
        }
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMatchById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM matches WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePlayerEventsForMatch = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM match_player_events WHERE matchId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMatch(final MatchEntity match, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMatchEntity.insertAndReturnId(match);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMatches(final List<MatchEntity> matches,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMatchEntity.insert(matches);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPlayerEvents(final List<MatchPlayerEventEntity> events,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMatchPlayerEventEntity.insert(events);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMatch(final MatchEntity match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMatchEntity.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMatch(final MatchEntity match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMatchEntity.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replacePlayerEvents(final long matchId, final List<MatchPlayerEventEntity> events,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> MatchDao.DefaultImpls.replacePlayerEvents(MatchDao_Impl.this, matchId, events, __cont), $completion);
  }

  @Override
  public Object completeMatch(final MatchEntity match, final List<MatchPlayerEventEntity> events,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> MatchDao.DefaultImpls.completeMatch(MatchDao_Impl.this, match, events, __cont), $completion);
  }

  @Override
  public Object deleteMatchById(final long matchId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMatchById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, matchId);
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
          __preparedStmtOfDeleteMatchById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlayerEventsForMatch(final long matchId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePlayerEventsForMatch.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, matchId);
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
          __preparedStmtOfDeletePlayerEventsForMatch.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MatchEntity>> getRegularMatches() {
    final String _sql = "SELECT * FROM matches WHERE stage = 'REGULAR' ORDER BY matchdayOrRound ASC, id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Flow<List<MatchEntity>> getKnockoutMatches() {
    final String _sql = "SELECT * FROM matches WHERE stage = 'KNOCKOUT' ORDER BY matchdayOrRound ASC, id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Object getMatchById(final long matchId,
      final Continuation<? super MatchEntity> $completion) {
    final String _sql = "SELECT * FROM matches WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MatchEntity>() {
      @Override
      @Nullable
      public MatchEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final MatchEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _result = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Flow<List<MatchEntity>> getMatchesByMatchday(final int matchday) {
    final String _sql = "SELECT * FROM matches \n"
            + "           WHERE stage = 'REGULAR' AND matchdayOrRound = ? \n"
            + "           ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchday);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Object getMatchesByMatchdaySuspend(final int matchday,
      final Continuation<? super List<MatchEntity>> $completion) {
    final String _sql = "SELECT * FROM matches\n"
            + "           WHERE stage = 'REGULAR' AND matchdayOrRound = ?\n"
            + "           ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchday);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Flow<Integer> getLatestMatchday() {
    final String _sql = "SELECT MAX(matchdayOrRound) FROM matches WHERE stage = 'REGULAR'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Flow<List<MatchEntity>> getMatchesByTie(final long tieId) {
    final String _sql = "SELECT * FROM matches \n"
            + "           WHERE knockoutTieId = ? \n"
            + "           ORDER BY legNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tieId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Object getMatchesByTieSuspend(final long tieId,
      final Continuation<? super List<MatchEntity>> $completion) {
    final String _sql = "SELECT * FROM matches \n"
            + "           WHERE knockoutTieId = ? \n"
            + "           ORDER BY legNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tieId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfMatchdayOrRound = CursorUtil.getColumnIndexOrThrow(_cursor, "matchdayOrRound");
          final int _cursorIndexOfKnockoutTieId = CursorUtil.getColumnIndexOrThrow(_cursor, "knockoutTieId");
          final int _cursorIndexOfLegNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "legNumber");
          final int _cursorIndexOfHomeTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamId");
          final int _cursorIndexOfAwayTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamId");
          final int _cursorIndexOfIsBye = CursorUtil.getColumnIndexOrThrow(_cursor, "isBye");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfHomeGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "homeGoals");
          final int _cursorIndexOfAwayGoals = CursorUtil.getColumnIndexOrThrow(_cursor, "awayGoals");
          final int _cursorIndexOfHomeFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "homeFouls");
          final int _cursorIndexOfAwayFouls = CursorUtil.getColumnIndexOrThrow(_cursor, "awayFouls");
          final int _cursorIndexOfMvpPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpPlayerName");
          final int _cursorIndexOfMvpRating = CursorUtil.getColumnIndexOrThrow(_cursor, "mvpRating");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final int _tmpMatchdayOrRound;
            _tmpMatchdayOrRound = _cursor.getInt(_cursorIndexOfMatchdayOrRound);
            final Long _tmpKnockoutTieId;
            if (_cursor.isNull(_cursorIndexOfKnockoutTieId)) {
              _tmpKnockoutTieId = null;
            } else {
              _tmpKnockoutTieId = _cursor.getLong(_cursorIndexOfKnockoutTieId);
            }
            final int _tmpLegNumber;
            _tmpLegNumber = _cursor.getInt(_cursorIndexOfLegNumber);
            final long _tmpHomeTeamId;
            _tmpHomeTeamId = _cursor.getLong(_cursorIndexOfHomeTeamId);
            final Long _tmpAwayTeamId;
            if (_cursor.isNull(_cursorIndexOfAwayTeamId)) {
              _tmpAwayTeamId = null;
            } else {
              _tmpAwayTeamId = _cursor.getLong(_cursorIndexOfAwayTeamId);
            }
            final boolean _tmpIsBye;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBye);
            _tmpIsBye = _tmp != 0;
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpHomeGoals;
            _tmpHomeGoals = _cursor.getInt(_cursorIndexOfHomeGoals);
            final int _tmpAwayGoals;
            _tmpAwayGoals = _cursor.getInt(_cursorIndexOfAwayGoals);
            final int _tmpHomeFouls;
            _tmpHomeFouls = _cursor.getInt(_cursorIndexOfHomeFouls);
            final int _tmpAwayFouls;
            _tmpAwayFouls = _cursor.getInt(_cursorIndexOfAwayFouls);
            final String _tmpMvpPlayerName;
            if (_cursor.isNull(_cursorIndexOfMvpPlayerName)) {
              _tmpMvpPlayerName = null;
            } else {
              _tmpMvpPlayerName = _cursor.getString(_cursorIndexOfMvpPlayerName);
            }
            final Double _tmpMvpRating;
            if (_cursor.isNull(_cursorIndexOfMvpRating)) {
              _tmpMvpRating = null;
            } else {
              _tmpMvpRating = _cursor.getDouble(_cursorIndexOfMvpRating);
            }
            _item = new MatchEntity(_tmpId,_tmpStage,_tmpMatchdayOrRound,_tmpKnockoutTieId,_tmpLegNumber,_tmpHomeTeamId,_tmpAwayTeamId,_tmpIsBye,_tmpStatus,_tmpHomeGoals,_tmpAwayGoals,_tmpHomeFouls,_tmpAwayFouls,_tmpMvpPlayerName,_tmpMvpRating);
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
  public Flow<List<MatchPlayerEventEntity>> getPlayerEventsForMatch(final long matchId) {
    final String _sql = "SELECT * FROM match_player_events WHERE matchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"match_player_events"}, new Callable<List<MatchPlayerEventEntity>>() {
      @Override
      @NonNull
      public List<MatchPlayerEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
          final int _cursorIndexOfPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "playerName");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
          final List<MatchPlayerEventEntity> _result = new ArrayList<MatchPlayerEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchPlayerEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final long _tmpTeamId;
            _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpEventType;
            _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new MatchPlayerEventEntity(_tmpId,_tmpMatchId,_tmpTeamId,_tmpPlayerName,_tmpEventType,_tmpCount);
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
  public Object getPlayerEventsForMatchSuspend(final long matchId,
      final Continuation<? super List<MatchPlayerEventEntity>> $completion) {
    final String _sql = "SELECT * FROM match_player_events WHERE matchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MatchPlayerEventEntity>>() {
      @Override
      @NonNull
      public List<MatchPlayerEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamId");
          final int _cursorIndexOfPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "playerName");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
          final List<MatchPlayerEventEntity> _result = new ArrayList<MatchPlayerEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchPlayerEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final long _tmpTeamId;
            _tmpTeamId = _cursor.getLong(_cursorIndexOfTeamId);
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            final String _tmpEventType;
            _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new MatchPlayerEventEntity(_tmpId,_tmpMatchId,_tmpTeamId,_tmpPlayerName,_tmpEventType,_tmpCount);
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
