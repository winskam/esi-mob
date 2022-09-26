package mobg5.g55047.projet

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import mobg5.g55047.projet.model.User
import mobg5.g55047.projet.database.UserDatabase
import mobg5.g55047.projet.database.UserDatabaseDao
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
class UserDatabaseTest {

    private lateinit var userDao: UserDatabaseDao
    private lateinit var db: UserDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        userDao = db.userDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertUser() {
        runBlocking {
            val user = User(0, "55047@etu.he2b.be", "22:10:23 20-10-1999")
            userDao.insert(user)
            val userToTest = userDao.get("55047@etu.he2b.be")
            assertEquals(userToTest?.email, user.email)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateUser() {
        runBlocking {
            val user = User(55047, "55047@etu.he2b.be", "22:10:23 20-10-1999")
            val newUser = User(55047, "55047@he2b.be", "22:10:23 20-10-2021")
            userDao.insert(user)
            userDao.update(newUser)
            val userToTest2 = userDao.get("55047@he2b.be")
            if (userToTest2 != null) {
                assertEquals(user?.userId, userToTest2.userId)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun clearDatabase() {
        runBlocking {
            val user = User(0, "55047@etu.he2b.be", "22:10:23 20-10-1999")
            userDao.insert(user)
            userDao.clear()
            val userToTest = userDao.get("55047@etu.he2b.be")
            assertNull(userToTest)
        }
    }

}