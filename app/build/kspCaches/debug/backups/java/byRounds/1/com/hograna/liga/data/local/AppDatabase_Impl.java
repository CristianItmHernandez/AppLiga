package com.hograna.liga.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.hograna.liga.data.local.dao.KnockoutTieDao;
import com.hograna.liga.data.local.dao.KnockoutTieDao_Impl;
import com.hograna.liga.data.local.dao.MatchDao;
import com.hograna.liga.data.local.dao.MatchDao_Impl;
import com.hograna.liga.data.local.dao.StandingsDao;
import com.hograna.liga.data.local.dao.StandingsDao_Impl;
import com.hograna.liga.data.local.dao.StatsDao;
import com.hograna.liga.data.local.dao.StatsDao_Impl;
import com.hograna.liga.data.local.dao.TeamDao;
import com.hograna.liga.data.local.dao.TeamDao_Impl;
import com.hograna.liga.data.local.dao.VideoHistoryDao;
import com.hograna.liga.data.local.dao.VideoHistoryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TeamDao _teamDao;

  private volatile MatchDao _matchDao;

  private volatile StandingsDao _standingsDao;

  private volatile StatsDao _statsDao;

  private volatile KnockoutTieDao _knockoutTieDao;

  private volatile VideoHistoryDao _videoHistoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `teams` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `leagueOrigin` TEXT NOT NULL, `crestUri` TEXT NOT NULL, `captainName` TEXT NOT NULL, `byeCount` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_teams_name` ON `teams` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `matches` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `stage` TEXT NOT NULL, `matchdayOrRound` INTEGER NOT NULL, `knockoutTieId` INTEGER, `legNumber` INTEGER NOT NULL, `homeTeamId` INTEGER NOT NULL, `awayTeamId` INTEGER, `isBye` INTEGER NOT NULL, `status` TEXT NOT NULL, `homeGoals` INTEGER NOT NULL, `awayGoals` INTEGER NOT NULL, `homeFouls` INTEGER NOT NULL, `awayFouls` INTEGER NOT NULL, `mvpPlayerName` TEXT, `mvpRating` REAL, FOREIGN KEY(`homeTeamId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`awayTeamId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`knockoutTieId`) REFERENCES `knockout_ties`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_matches_homeTeamId` ON `matches` (`homeTeamId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_matches_awayTeamId` ON `matches` (`awayTeamId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_matches_knockoutTieId` ON `matches` (`knockoutTieId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_matches_stage` ON `matches` (`stage`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_matches_status` ON `matches` (`status`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `match_player_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `matchId` INTEGER NOT NULL, `teamId` INTEGER NOT NULL, `playerName` TEXT NOT NULL, `eventType` TEXT NOT NULL, `count` INTEGER NOT NULL, FOREIGN KEY(`matchId`) REFERENCES `matches`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`teamId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_match_player_events_matchId` ON `match_player_events` (`matchId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_match_player_events_teamId` ON `match_player_events` (`teamId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_match_player_events_eventType` ON `match_player_events` (`eventType`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `knockout_ties` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `round` INTEGER NOT NULL, `slotIndex` INTEGER NOT NULL, `teamAId` INTEGER, `teamBId` INTEGER, `teamAPenalties` INTEGER, `teamBPenalties` INTEGER, `winnerTeamId` INTEGER, FOREIGN KEY(`teamAId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`teamBId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`winnerTeamId`) REFERENCES `teams`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_knockout_ties_teamAId` ON `knockout_ties` (`teamAId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_knockout_ties_teamBId` ON `knockout_ties` (`teamBId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_knockout_ties_winnerTeamId` ON `knockout_ties` (`winnerTeamId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_knockout_ties_round_slotIndex` ON `knockout_ties` (`round`, `slotIndex`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `video_highlights` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `homeTeamName` TEXT NOT NULL, `awayTeamName` TEXT NOT NULL, `scoreSummary` TEXT NOT NULL, `scorersList` TEXT NOT NULL, `videoSourceType` TEXT NOT NULL, `videoUrlOrUri` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e700178e5780d030b9464356245dc878')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `teams`");
        db.execSQL("DROP TABLE IF EXISTS `matches`");
        db.execSQL("DROP TABLE IF EXISTS `match_player_events`");
        db.execSQL("DROP TABLE IF EXISTS `knockout_ties`");
        db.execSQL("DROP TABLE IF EXISTS `video_highlights`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTeams = new HashMap<String, TableInfo.Column>(6);
        _columnsTeams.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("leagueOrigin", new TableInfo.Column("leagueOrigin", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("crestUri", new TableInfo.Column("crestUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("captainName", new TableInfo.Column("captainName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("byeCount", new TableInfo.Column("byeCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeams = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTeams = new HashSet<TableInfo.Index>(1);
        _indicesTeams.add(new TableInfo.Index("index_teams_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoTeams = new TableInfo("teams", _columnsTeams, _foreignKeysTeams, _indicesTeams);
        final TableInfo _existingTeams = TableInfo.read(db, "teams");
        if (!_infoTeams.equals(_existingTeams)) {
          return new RoomOpenHelper.ValidationResult(false, "teams(com.hograna.liga.data.local.entity.TeamEntity).\n"
                  + " Expected:\n" + _infoTeams + "\n"
                  + " Found:\n" + _existingTeams);
        }
        final HashMap<String, TableInfo.Column> _columnsMatches = new HashMap<String, TableInfo.Column>(15);
        _columnsMatches.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("stage", new TableInfo.Column("stage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("matchdayOrRound", new TableInfo.Column("matchdayOrRound", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("knockoutTieId", new TableInfo.Column("knockoutTieId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("legNumber", new TableInfo.Column("legNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("homeTeamId", new TableInfo.Column("homeTeamId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("awayTeamId", new TableInfo.Column("awayTeamId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("isBye", new TableInfo.Column("isBye", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("homeGoals", new TableInfo.Column("homeGoals", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("awayGoals", new TableInfo.Column("awayGoals", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("homeFouls", new TableInfo.Column("homeFouls", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("awayFouls", new TableInfo.Column("awayFouls", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("mvpPlayerName", new TableInfo.Column("mvpPlayerName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("mvpRating", new TableInfo.Column("mvpRating", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMatches = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysMatches.add(new TableInfo.ForeignKey("teams", "RESTRICT", "NO ACTION", Arrays.asList("homeTeamId"), Arrays.asList("id")));
        _foreignKeysMatches.add(new TableInfo.ForeignKey("teams", "SET NULL", "NO ACTION", Arrays.asList("awayTeamId"), Arrays.asList("id")));
        _foreignKeysMatches.add(new TableInfo.ForeignKey("knockout_ties", "SET NULL", "NO ACTION", Arrays.asList("knockoutTieId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMatches = new HashSet<TableInfo.Index>(5);
        _indicesMatches.add(new TableInfo.Index("index_matches_homeTeamId", false, Arrays.asList("homeTeamId"), Arrays.asList("ASC")));
        _indicesMatches.add(new TableInfo.Index("index_matches_awayTeamId", false, Arrays.asList("awayTeamId"), Arrays.asList("ASC")));
        _indicesMatches.add(new TableInfo.Index("index_matches_knockoutTieId", false, Arrays.asList("knockoutTieId"), Arrays.asList("ASC")));
        _indicesMatches.add(new TableInfo.Index("index_matches_stage", false, Arrays.asList("stage"), Arrays.asList("ASC")));
        _indicesMatches.add(new TableInfo.Index("index_matches_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        final TableInfo _infoMatches = new TableInfo("matches", _columnsMatches, _foreignKeysMatches, _indicesMatches);
        final TableInfo _existingMatches = TableInfo.read(db, "matches");
        if (!_infoMatches.equals(_existingMatches)) {
          return new RoomOpenHelper.ValidationResult(false, "matches(com.hograna.liga.data.local.entity.MatchEntity).\n"
                  + " Expected:\n" + _infoMatches + "\n"
                  + " Found:\n" + _existingMatches);
        }
        final HashMap<String, TableInfo.Column> _columnsMatchPlayerEvents = new HashMap<String, TableInfo.Column>(6);
        _columnsMatchPlayerEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchPlayerEvents.put("matchId", new TableInfo.Column("matchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchPlayerEvents.put("teamId", new TableInfo.Column("teamId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchPlayerEvents.put("playerName", new TableInfo.Column("playerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchPlayerEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatchPlayerEvents.put("count", new TableInfo.Column("count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMatchPlayerEvents = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMatchPlayerEvents.add(new TableInfo.ForeignKey("matches", "CASCADE", "NO ACTION", Arrays.asList("matchId"), Arrays.asList("id")));
        _foreignKeysMatchPlayerEvents.add(new TableInfo.ForeignKey("teams", "CASCADE", "NO ACTION", Arrays.asList("teamId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMatchPlayerEvents = new HashSet<TableInfo.Index>(3);
        _indicesMatchPlayerEvents.add(new TableInfo.Index("index_match_player_events_matchId", false, Arrays.asList("matchId"), Arrays.asList("ASC")));
        _indicesMatchPlayerEvents.add(new TableInfo.Index("index_match_player_events_teamId", false, Arrays.asList("teamId"), Arrays.asList("ASC")));
        _indicesMatchPlayerEvents.add(new TableInfo.Index("index_match_player_events_eventType", false, Arrays.asList("eventType"), Arrays.asList("ASC")));
        final TableInfo _infoMatchPlayerEvents = new TableInfo("match_player_events", _columnsMatchPlayerEvents, _foreignKeysMatchPlayerEvents, _indicesMatchPlayerEvents);
        final TableInfo _existingMatchPlayerEvents = TableInfo.read(db, "match_player_events");
        if (!_infoMatchPlayerEvents.equals(_existingMatchPlayerEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "match_player_events(com.hograna.liga.data.local.entity.MatchPlayerEventEntity).\n"
                  + " Expected:\n" + _infoMatchPlayerEvents + "\n"
                  + " Found:\n" + _existingMatchPlayerEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsKnockoutTies = new HashMap<String, TableInfo.Column>(8);
        _columnsKnockoutTies.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("round", new TableInfo.Column("round", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("slotIndex", new TableInfo.Column("slotIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("teamAId", new TableInfo.Column("teamAId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("teamBId", new TableInfo.Column("teamBId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("teamAPenalties", new TableInfo.Column("teamAPenalties", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("teamBPenalties", new TableInfo.Column("teamBPenalties", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsKnockoutTies.put("winnerTeamId", new TableInfo.Column("winnerTeamId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysKnockoutTies = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysKnockoutTies.add(new TableInfo.ForeignKey("teams", "SET NULL", "NO ACTION", Arrays.asList("teamAId"), Arrays.asList("id")));
        _foreignKeysKnockoutTies.add(new TableInfo.ForeignKey("teams", "SET NULL", "NO ACTION", Arrays.asList("teamBId"), Arrays.asList("id")));
        _foreignKeysKnockoutTies.add(new TableInfo.ForeignKey("teams", "SET NULL", "NO ACTION", Arrays.asList("winnerTeamId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesKnockoutTies = new HashSet<TableInfo.Index>(4);
        _indicesKnockoutTies.add(new TableInfo.Index("index_knockout_ties_teamAId", false, Arrays.asList("teamAId"), Arrays.asList("ASC")));
        _indicesKnockoutTies.add(new TableInfo.Index("index_knockout_ties_teamBId", false, Arrays.asList("teamBId"), Arrays.asList("ASC")));
        _indicesKnockoutTies.add(new TableInfo.Index("index_knockout_ties_winnerTeamId", false, Arrays.asList("winnerTeamId"), Arrays.asList("ASC")));
        _indicesKnockoutTies.add(new TableInfo.Index("index_knockout_ties_round_slotIndex", true, Arrays.asList("round", "slotIndex"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoKnockoutTies = new TableInfo("knockout_ties", _columnsKnockoutTies, _foreignKeysKnockoutTies, _indicesKnockoutTies);
        final TableInfo _existingKnockoutTies = TableInfo.read(db, "knockout_ties");
        if (!_infoKnockoutTies.equals(_existingKnockoutTies)) {
          return new RoomOpenHelper.ValidationResult(false, "knockout_ties(com.hograna.liga.data.local.entity.KnockoutTieEntity).\n"
                  + " Expected:\n" + _infoKnockoutTies + "\n"
                  + " Found:\n" + _existingKnockoutTies);
        }
        final HashMap<String, TableInfo.Column> _columnsVideoHighlights = new HashMap<String, TableInfo.Column>(8);
        _columnsVideoHighlights.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("homeTeamName", new TableInfo.Column("homeTeamName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("awayTeamName", new TableInfo.Column("awayTeamName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("scoreSummary", new TableInfo.Column("scoreSummary", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("scorersList", new TableInfo.Column("scorersList", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("videoSourceType", new TableInfo.Column("videoSourceType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("videoUrlOrUri", new TableInfo.Column("videoUrlOrUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVideoHighlights.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVideoHighlights = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVideoHighlights = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVideoHighlights = new TableInfo("video_highlights", _columnsVideoHighlights, _foreignKeysVideoHighlights, _indicesVideoHighlights);
        final TableInfo _existingVideoHighlights = TableInfo.read(db, "video_highlights");
        if (!_infoVideoHighlights.equals(_existingVideoHighlights)) {
          return new RoomOpenHelper.ValidationResult(false, "video_highlights(com.hograna.liga.data.local.entity.VideoHighlightEntity).\n"
                  + " Expected:\n" + _infoVideoHighlights + "\n"
                  + " Found:\n" + _existingVideoHighlights);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e700178e5780d030b9464356245dc878", "304ba6eabe4646ad21ee0c47c68c92a7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "teams","matches","match_player_events","knockout_ties","video_highlights");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `matches`");
      _db.execSQL("DELETE FROM `teams`");
      _db.execSQL("DELETE FROM `match_player_events`");
      _db.execSQL("DELETE FROM `knockout_ties`");
      _db.execSQL("DELETE FROM `video_highlights`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TeamDao.class, TeamDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MatchDao.class, MatchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StandingsDao.class, StandingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StatsDao.class, StatsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(KnockoutTieDao.class, KnockoutTieDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VideoHistoryDao.class, VideoHistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TeamDao teamDao() {
    if (_teamDao != null) {
      return _teamDao;
    } else {
      synchronized(this) {
        if(_teamDao == null) {
          _teamDao = new TeamDao_Impl(this);
        }
        return _teamDao;
      }
    }
  }

  @Override
  public MatchDao matchDao() {
    if (_matchDao != null) {
      return _matchDao;
    } else {
      synchronized(this) {
        if(_matchDao == null) {
          _matchDao = new MatchDao_Impl(this);
        }
        return _matchDao;
      }
    }
  }

  @Override
  public StandingsDao standingsDao() {
    if (_standingsDao != null) {
      return _standingsDao;
    } else {
      synchronized(this) {
        if(_standingsDao == null) {
          _standingsDao = new StandingsDao_Impl(this);
        }
        return _standingsDao;
      }
    }
  }

  @Override
  public StatsDao statsDao() {
    if (_statsDao != null) {
      return _statsDao;
    } else {
      synchronized(this) {
        if(_statsDao == null) {
          _statsDao = new StatsDao_Impl(this);
        }
        return _statsDao;
      }
    }
  }

  @Override
  public KnockoutTieDao knockoutTieDao() {
    if (_knockoutTieDao != null) {
      return _knockoutTieDao;
    } else {
      synchronized(this) {
        if(_knockoutTieDao == null) {
          _knockoutTieDao = new KnockoutTieDao_Impl(this);
        }
        return _knockoutTieDao;
      }
    }
  }

  @Override
  public VideoHistoryDao videoHistoryDao() {
    if (_videoHistoryDao != null) {
      return _videoHistoryDao;
    } else {
      synchronized(this) {
        if(_videoHistoryDao == null) {
          _videoHistoryDao = new VideoHistoryDao_Impl(this);
        }
        return _videoHistoryDao;
      }
    }
  }
}
