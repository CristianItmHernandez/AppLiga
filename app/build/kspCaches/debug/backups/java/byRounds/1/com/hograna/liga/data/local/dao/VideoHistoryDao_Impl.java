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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.hograna.liga.data.local.entity.VideoHighlightEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class VideoHistoryDao_Impl implements VideoHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VideoHighlightEntity> __insertionAdapterOfVideoHighlightEntity;

  private final EntityDeletionOrUpdateAdapter<VideoHighlightEntity> __deletionAdapterOfVideoHighlightEntity;

  public VideoHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVideoHighlightEntity = new EntityInsertionAdapter<VideoHighlightEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `video_highlights` (`id`,`homeTeamName`,`awayTeamName`,`scoreSummary`,`scorersList`,`videoSourceType`,`videoUrlOrUri`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VideoHighlightEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getHomeTeamName());
        statement.bindString(3, entity.getAwayTeamName());
        statement.bindString(4, entity.getScoreSummary());
        statement.bindString(5, entity.getScorersList());
        statement.bindString(6, entity.getVideoSourceType());
        statement.bindString(7, entity.getVideoUrlOrUri());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfVideoHighlightEntity = new EntityDeletionOrUpdateAdapter<VideoHighlightEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `video_highlights` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VideoHighlightEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertVideo(final VideoHighlightEntity video,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVideoHighlightEntity.insertAndReturnId(video);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVideo(final VideoHighlightEntity video,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVideoHighlightEntity.handle(video);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<VideoHighlightEntity>> getAllVideos() {
    final String _sql = "SELECT * FROM video_highlights ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"video_highlights"}, new Callable<List<VideoHighlightEntity>>() {
      @Override
      @NonNull
      public List<VideoHighlightEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHomeTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamName");
          final int _cursorIndexOfAwayTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamName");
          final int _cursorIndexOfScoreSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreSummary");
          final int _cursorIndexOfScorersList = CursorUtil.getColumnIndexOrThrow(_cursor, "scorersList");
          final int _cursorIndexOfVideoSourceType = CursorUtil.getColumnIndexOrThrow(_cursor, "videoSourceType");
          final int _cursorIndexOfVideoUrlOrUri = CursorUtil.getColumnIndexOrThrow(_cursor, "videoUrlOrUri");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<VideoHighlightEntity> _result = new ArrayList<VideoHighlightEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VideoHighlightEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpHomeTeamName;
            _tmpHomeTeamName = _cursor.getString(_cursorIndexOfHomeTeamName);
            final String _tmpAwayTeamName;
            _tmpAwayTeamName = _cursor.getString(_cursorIndexOfAwayTeamName);
            final String _tmpScoreSummary;
            _tmpScoreSummary = _cursor.getString(_cursorIndexOfScoreSummary);
            final String _tmpScorersList;
            _tmpScorersList = _cursor.getString(_cursorIndexOfScorersList);
            final String _tmpVideoSourceType;
            _tmpVideoSourceType = _cursor.getString(_cursorIndexOfVideoSourceType);
            final String _tmpVideoUrlOrUri;
            _tmpVideoUrlOrUri = _cursor.getString(_cursorIndexOfVideoUrlOrUri);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new VideoHighlightEntity(_tmpId,_tmpHomeTeamName,_tmpAwayTeamName,_tmpScoreSummary,_tmpScorersList,_tmpVideoSourceType,_tmpVideoUrlOrUri,_tmpCreatedAt);
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
  public Object getVideoById(final long id,
      final Continuation<? super VideoHighlightEntity> $completion) {
    final String _sql = "SELECT * FROM video_highlights WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VideoHighlightEntity>() {
      @Override
      @Nullable
      public VideoHighlightEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHomeTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "homeTeamName");
          final int _cursorIndexOfAwayTeamName = CursorUtil.getColumnIndexOrThrow(_cursor, "awayTeamName");
          final int _cursorIndexOfScoreSummary = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreSummary");
          final int _cursorIndexOfScorersList = CursorUtil.getColumnIndexOrThrow(_cursor, "scorersList");
          final int _cursorIndexOfVideoSourceType = CursorUtil.getColumnIndexOrThrow(_cursor, "videoSourceType");
          final int _cursorIndexOfVideoUrlOrUri = CursorUtil.getColumnIndexOrThrow(_cursor, "videoUrlOrUri");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final VideoHighlightEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpHomeTeamName;
            _tmpHomeTeamName = _cursor.getString(_cursorIndexOfHomeTeamName);
            final String _tmpAwayTeamName;
            _tmpAwayTeamName = _cursor.getString(_cursorIndexOfAwayTeamName);
            final String _tmpScoreSummary;
            _tmpScoreSummary = _cursor.getString(_cursorIndexOfScoreSummary);
            final String _tmpScorersList;
            _tmpScorersList = _cursor.getString(_cursorIndexOfScorersList);
            final String _tmpVideoSourceType;
            _tmpVideoSourceType = _cursor.getString(_cursorIndexOfVideoSourceType);
            final String _tmpVideoUrlOrUri;
            _tmpVideoUrlOrUri = _cursor.getString(_cursorIndexOfVideoUrlOrUri);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new VideoHighlightEntity(_tmpId,_tmpHomeTeamName,_tmpAwayTeamName,_tmpScoreSummary,_tmpScorersList,_tmpVideoSourceType,_tmpVideoUrlOrUri,_tmpCreatedAt);
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
