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
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hograna.liga.data.local.entity.KnockoutTieEntity;
import java.lang.Class;
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
public final class KnockoutTieDao_Impl implements KnockoutTieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<KnockoutTieEntity> __insertionAdapterOfKnockoutTieEntity;

  private final EntityDeletionOrUpdateAdapter<KnockoutTieEntity> __deletionAdapterOfKnockoutTieEntity;

  private final EntityDeletionOrUpdateAdapter<KnockoutTieEntity> __updateAdapterOfKnockoutTieEntity;

  private final SharedSQLiteStatement __preparedStmtOfSetWinner;

  private final SharedSQLiteStatement __preparedStmtOfSetPenalties;

  private final SharedSQLiteStatement __preparedStmtOfAssignTeamToTie;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public KnockoutTieDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfKnockoutTieEntity = new EntityInsertionAdapter<KnockoutTieEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `knockout_ties` (`id`,`round`,`slotIndex`,`teamAId`,`teamBId`,`teamAPenalties`,`teamBPenalties`,`winnerTeamId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KnockoutTieEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRound());
        statement.bindLong(3, entity.getSlotIndex());
        if (entity.getTeamAId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTeamAId());
        }
        if (entity.getTeamBId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getTeamBId());
        }
        if (entity.getTeamAPenalties() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getTeamAPenalties());
        }
        if (entity.getTeamBPenalties() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getTeamBPenalties());
        }
        if (entity.getWinnerTeamId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getWinnerTeamId());
        }
      }
    };
    this.__deletionAdapterOfKnockoutTieEntity = new EntityDeletionOrUpdateAdapter<KnockoutTieEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `knockout_ties` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KnockoutTieEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfKnockoutTieEntity = new EntityDeletionOrUpdateAdapter<KnockoutTieEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `knockout_ties` SET `id` = ?,`round` = ?,`slotIndex` = ?,`teamAId` = ?,`teamBId` = ?,`teamAPenalties` = ?,`teamBPenalties` = ?,`winnerTeamId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KnockoutTieEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRound());
        statement.bindLong(3, entity.getSlotIndex());
        if (entity.getTeamAId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getTeamAId());
        }
        if (entity.getTeamBId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getTeamBId());
        }
        if (entity.getTeamAPenalties() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getTeamAPenalties());
        }
        if (entity.getTeamBPenalties() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getTeamBPenalties());
        }
        if (entity.getWinnerTeamId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getWinnerTeamId());
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfSetWinner = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE knockout_ties SET winnerTeamId = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetPenalties = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE knockout_ties SET teamAPenalties = ?, teamBPenalties = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAssignTeamToTie = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE knockout_ties SET \n"
                + "           teamAId = CASE WHEN teamAId IS NULL THEN ? ELSE teamAId END,\n"
                + "           teamBId = CASE WHEN teamAId IS NOT NULL AND teamBId IS NULL THEN ? ELSE teamBId END\n"
                + "           WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM knockout_ties";
        return _query;
      }
    };
  }

  @Override
  public Object insertTie(final KnockoutTieEntity tie,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfKnockoutTieEntity.insertAndReturnId(tie);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTies(final List<KnockoutTieEntity> ties,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfKnockoutTieEntity.insertAndReturnIdsList(ties);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTie(final KnockoutTieEntity tie,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfKnockoutTieEntity.handle(tie);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTie(final KnockoutTieEntity tie,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfKnockoutTieEntity.handle(tie);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setWinner(final long tieId, final long winnerId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetWinner.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, winnerId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, tieId);
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
          __preparedStmtOfSetWinner.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setPenalties(final long tieId, final int penA, final int penB,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetPenalties.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, penA);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, penB);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, tieId);
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
          __preparedStmtOfSetPenalties.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object assignTeamToTie(final long tieId, final long teamId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAssignTeamToTie.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, teamId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, teamId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, tieId);
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
          __preparedStmtOfAssignTeamToTie.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<KnockoutTieEntity>> getTiesByRound(final int round) {
    final String _sql = "SELECT * FROM knockout_ties WHERE round = ? ORDER BY slotIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, round);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"knockout_ties"}, new Callable<List<KnockoutTieEntity>>() {
      @Override
      @NonNull
      public List<KnockoutTieEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRound = CursorUtil.getColumnIndexOrThrow(_cursor, "round");
          final int _cursorIndexOfSlotIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "slotIndex");
          final int _cursorIndexOfTeamAId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAId");
          final int _cursorIndexOfTeamBId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBId");
          final int _cursorIndexOfTeamAPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAPenalties");
          final int _cursorIndexOfTeamBPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBPenalties");
          final int _cursorIndexOfWinnerTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "winnerTeamId");
          final List<KnockoutTieEntity> _result = new ArrayList<KnockoutTieEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KnockoutTieEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpRound;
            _tmpRound = _cursor.getInt(_cursorIndexOfRound);
            final int _tmpSlotIndex;
            _tmpSlotIndex = _cursor.getInt(_cursorIndexOfSlotIndex);
            final Long _tmpTeamAId;
            if (_cursor.isNull(_cursorIndexOfTeamAId)) {
              _tmpTeamAId = null;
            } else {
              _tmpTeamAId = _cursor.getLong(_cursorIndexOfTeamAId);
            }
            final Long _tmpTeamBId;
            if (_cursor.isNull(_cursorIndexOfTeamBId)) {
              _tmpTeamBId = null;
            } else {
              _tmpTeamBId = _cursor.getLong(_cursorIndexOfTeamBId);
            }
            final Integer _tmpTeamAPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamAPenalties)) {
              _tmpTeamAPenalties = null;
            } else {
              _tmpTeamAPenalties = _cursor.getInt(_cursorIndexOfTeamAPenalties);
            }
            final Integer _tmpTeamBPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamBPenalties)) {
              _tmpTeamBPenalties = null;
            } else {
              _tmpTeamBPenalties = _cursor.getInt(_cursorIndexOfTeamBPenalties);
            }
            final Long _tmpWinnerTeamId;
            if (_cursor.isNull(_cursorIndexOfWinnerTeamId)) {
              _tmpWinnerTeamId = null;
            } else {
              _tmpWinnerTeamId = _cursor.getLong(_cursorIndexOfWinnerTeamId);
            }
            _item = new KnockoutTieEntity(_tmpId,_tmpRound,_tmpSlotIndex,_tmpTeamAId,_tmpTeamBId,_tmpTeamAPenalties,_tmpTeamBPenalties,_tmpWinnerTeamId);
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
  public Object getTiesByRoundSuspend(final int round,
      final Continuation<? super List<KnockoutTieEntity>> $completion) {
    final String _sql = "SELECT * FROM knockout_ties WHERE round = ? ORDER BY slotIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, round);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<KnockoutTieEntity>>() {
      @Override
      @NonNull
      public List<KnockoutTieEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRound = CursorUtil.getColumnIndexOrThrow(_cursor, "round");
          final int _cursorIndexOfSlotIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "slotIndex");
          final int _cursorIndexOfTeamAId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAId");
          final int _cursorIndexOfTeamBId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBId");
          final int _cursorIndexOfTeamAPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAPenalties");
          final int _cursorIndexOfTeamBPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBPenalties");
          final int _cursorIndexOfWinnerTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "winnerTeamId");
          final List<KnockoutTieEntity> _result = new ArrayList<KnockoutTieEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KnockoutTieEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpRound;
            _tmpRound = _cursor.getInt(_cursorIndexOfRound);
            final int _tmpSlotIndex;
            _tmpSlotIndex = _cursor.getInt(_cursorIndexOfSlotIndex);
            final Long _tmpTeamAId;
            if (_cursor.isNull(_cursorIndexOfTeamAId)) {
              _tmpTeamAId = null;
            } else {
              _tmpTeamAId = _cursor.getLong(_cursorIndexOfTeamAId);
            }
            final Long _tmpTeamBId;
            if (_cursor.isNull(_cursorIndexOfTeamBId)) {
              _tmpTeamBId = null;
            } else {
              _tmpTeamBId = _cursor.getLong(_cursorIndexOfTeamBId);
            }
            final Integer _tmpTeamAPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamAPenalties)) {
              _tmpTeamAPenalties = null;
            } else {
              _tmpTeamAPenalties = _cursor.getInt(_cursorIndexOfTeamAPenalties);
            }
            final Integer _tmpTeamBPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamBPenalties)) {
              _tmpTeamBPenalties = null;
            } else {
              _tmpTeamBPenalties = _cursor.getInt(_cursorIndexOfTeamBPenalties);
            }
            final Long _tmpWinnerTeamId;
            if (_cursor.isNull(_cursorIndexOfWinnerTeamId)) {
              _tmpWinnerTeamId = null;
            } else {
              _tmpWinnerTeamId = _cursor.getLong(_cursorIndexOfWinnerTeamId);
            }
            _item = new KnockoutTieEntity(_tmpId,_tmpRound,_tmpSlotIndex,_tmpTeamAId,_tmpTeamBId,_tmpTeamAPenalties,_tmpTeamBPenalties,_tmpWinnerTeamId);
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
  public Flow<List<KnockoutTieEntity>> getAllTies() {
    final String _sql = "SELECT * FROM knockout_ties ORDER BY round DESC, slotIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"knockout_ties"}, new Callable<List<KnockoutTieEntity>>() {
      @Override
      @NonNull
      public List<KnockoutTieEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRound = CursorUtil.getColumnIndexOrThrow(_cursor, "round");
          final int _cursorIndexOfSlotIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "slotIndex");
          final int _cursorIndexOfTeamAId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAId");
          final int _cursorIndexOfTeamBId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBId");
          final int _cursorIndexOfTeamAPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAPenalties");
          final int _cursorIndexOfTeamBPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBPenalties");
          final int _cursorIndexOfWinnerTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "winnerTeamId");
          final List<KnockoutTieEntity> _result = new ArrayList<KnockoutTieEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KnockoutTieEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpRound;
            _tmpRound = _cursor.getInt(_cursorIndexOfRound);
            final int _tmpSlotIndex;
            _tmpSlotIndex = _cursor.getInt(_cursorIndexOfSlotIndex);
            final Long _tmpTeamAId;
            if (_cursor.isNull(_cursorIndexOfTeamAId)) {
              _tmpTeamAId = null;
            } else {
              _tmpTeamAId = _cursor.getLong(_cursorIndexOfTeamAId);
            }
            final Long _tmpTeamBId;
            if (_cursor.isNull(_cursorIndexOfTeamBId)) {
              _tmpTeamBId = null;
            } else {
              _tmpTeamBId = _cursor.getLong(_cursorIndexOfTeamBId);
            }
            final Integer _tmpTeamAPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamAPenalties)) {
              _tmpTeamAPenalties = null;
            } else {
              _tmpTeamAPenalties = _cursor.getInt(_cursorIndexOfTeamAPenalties);
            }
            final Integer _tmpTeamBPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamBPenalties)) {
              _tmpTeamBPenalties = null;
            } else {
              _tmpTeamBPenalties = _cursor.getInt(_cursorIndexOfTeamBPenalties);
            }
            final Long _tmpWinnerTeamId;
            if (_cursor.isNull(_cursorIndexOfWinnerTeamId)) {
              _tmpWinnerTeamId = null;
            } else {
              _tmpWinnerTeamId = _cursor.getLong(_cursorIndexOfWinnerTeamId);
            }
            _item = new KnockoutTieEntity(_tmpId,_tmpRound,_tmpSlotIndex,_tmpTeamAId,_tmpTeamBId,_tmpTeamAPenalties,_tmpTeamBPenalties,_tmpWinnerTeamId);
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
  public Object getAllTiesSuspend(final Continuation<? super List<KnockoutTieEntity>> $completion) {
    final String _sql = "SELECT * FROM knockout_ties ORDER BY round DESC, slotIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<KnockoutTieEntity>>() {
      @Override
      @NonNull
      public List<KnockoutTieEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRound = CursorUtil.getColumnIndexOrThrow(_cursor, "round");
          final int _cursorIndexOfSlotIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "slotIndex");
          final int _cursorIndexOfTeamAId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAId");
          final int _cursorIndexOfTeamBId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBId");
          final int _cursorIndexOfTeamAPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAPenalties");
          final int _cursorIndexOfTeamBPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBPenalties");
          final int _cursorIndexOfWinnerTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "winnerTeamId");
          final List<KnockoutTieEntity> _result = new ArrayList<KnockoutTieEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KnockoutTieEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpRound;
            _tmpRound = _cursor.getInt(_cursorIndexOfRound);
            final int _tmpSlotIndex;
            _tmpSlotIndex = _cursor.getInt(_cursorIndexOfSlotIndex);
            final Long _tmpTeamAId;
            if (_cursor.isNull(_cursorIndexOfTeamAId)) {
              _tmpTeamAId = null;
            } else {
              _tmpTeamAId = _cursor.getLong(_cursorIndexOfTeamAId);
            }
            final Long _tmpTeamBId;
            if (_cursor.isNull(_cursorIndexOfTeamBId)) {
              _tmpTeamBId = null;
            } else {
              _tmpTeamBId = _cursor.getLong(_cursorIndexOfTeamBId);
            }
            final Integer _tmpTeamAPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamAPenalties)) {
              _tmpTeamAPenalties = null;
            } else {
              _tmpTeamAPenalties = _cursor.getInt(_cursorIndexOfTeamAPenalties);
            }
            final Integer _tmpTeamBPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamBPenalties)) {
              _tmpTeamBPenalties = null;
            } else {
              _tmpTeamBPenalties = _cursor.getInt(_cursorIndexOfTeamBPenalties);
            }
            final Long _tmpWinnerTeamId;
            if (_cursor.isNull(_cursorIndexOfWinnerTeamId)) {
              _tmpWinnerTeamId = null;
            } else {
              _tmpWinnerTeamId = _cursor.getLong(_cursorIndexOfWinnerTeamId);
            }
            _item = new KnockoutTieEntity(_tmpId,_tmpRound,_tmpSlotIndex,_tmpTeamAId,_tmpTeamBId,_tmpTeamAPenalties,_tmpTeamBPenalties,_tmpWinnerTeamId);
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
  public Object getTieById(final long id,
      final Continuation<? super KnockoutTieEntity> $completion) {
    final String _sql = "SELECT * FROM knockout_ties WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<KnockoutTieEntity>() {
      @Override
      @Nullable
      public KnockoutTieEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRound = CursorUtil.getColumnIndexOrThrow(_cursor, "round");
          final int _cursorIndexOfSlotIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "slotIndex");
          final int _cursorIndexOfTeamAId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAId");
          final int _cursorIndexOfTeamBId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBId");
          final int _cursorIndexOfTeamAPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamAPenalties");
          final int _cursorIndexOfTeamBPenalties = CursorUtil.getColumnIndexOrThrow(_cursor, "teamBPenalties");
          final int _cursorIndexOfWinnerTeamId = CursorUtil.getColumnIndexOrThrow(_cursor, "winnerTeamId");
          final KnockoutTieEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpRound;
            _tmpRound = _cursor.getInt(_cursorIndexOfRound);
            final int _tmpSlotIndex;
            _tmpSlotIndex = _cursor.getInt(_cursorIndexOfSlotIndex);
            final Long _tmpTeamAId;
            if (_cursor.isNull(_cursorIndexOfTeamAId)) {
              _tmpTeamAId = null;
            } else {
              _tmpTeamAId = _cursor.getLong(_cursorIndexOfTeamAId);
            }
            final Long _tmpTeamBId;
            if (_cursor.isNull(_cursorIndexOfTeamBId)) {
              _tmpTeamBId = null;
            } else {
              _tmpTeamBId = _cursor.getLong(_cursorIndexOfTeamBId);
            }
            final Integer _tmpTeamAPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamAPenalties)) {
              _tmpTeamAPenalties = null;
            } else {
              _tmpTeamAPenalties = _cursor.getInt(_cursorIndexOfTeamAPenalties);
            }
            final Integer _tmpTeamBPenalties;
            if (_cursor.isNull(_cursorIndexOfTeamBPenalties)) {
              _tmpTeamBPenalties = null;
            } else {
              _tmpTeamBPenalties = _cursor.getInt(_cursorIndexOfTeamBPenalties);
            }
            final Long _tmpWinnerTeamId;
            if (_cursor.isNull(_cursorIndexOfWinnerTeamId)) {
              _tmpWinnerTeamId = null;
            } else {
              _tmpWinnerTeamId = _cursor.getLong(_cursorIndexOfWinnerTeamId);
            }
            _result = new KnockoutTieEntity(_tmpId,_tmpRound,_tmpSlotIndex,_tmpTeamAId,_tmpTeamBId,_tmpTeamAPenalties,_tmpTeamBPenalties,_tmpWinnerTeamId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
