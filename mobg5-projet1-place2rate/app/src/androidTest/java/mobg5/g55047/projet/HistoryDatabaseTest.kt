package mobg5.g55047.projet

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import mobg5.g55047.projet.database.HistoryDatabase
import mobg5.g55047.projet.database.HistoryDatabaseDao
import mobg5.g55047.projet.model.History
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HistoryDatabaseTest {
    private lateinit var historyDao: HistoryDatabaseDao
    private lateinit var db: HistoryDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        historyDao = db.historyDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertHistory() {
        runBlocking {
            val history = History("Atomium")
            historyDao.insert(history)
            val historyToTest = historyDao.get("Atomium")
            assertEquals(historyToTest?.search, history.search)
        }
    }

    @Test
    @Throws(Exception::class)
    fun clearDatabase() {
        runBlocking {
            val history = History("Atomium")
            historyDao.insert(history)
            historyDao.clear()
            val historyToTest = historyDao.get("Atomium")
            assertNull(historyToTest)
        }
    }

}
