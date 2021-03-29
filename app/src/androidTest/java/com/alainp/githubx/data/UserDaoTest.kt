package com.alainp.githubx.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*

/**
 * TODO: Need more time to figure out how to mock the API call.
 */
class UserDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var userDetailDao: UserDetailDao

    private val userA = getTestUser(1L, "user 1", 1999)
    private val userB = getTestUser(2L, "user b", 2000)
    private val userC = getTestUser(3L, "user c", 2001)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDetailDao = database.userDetailDao()

        // Insert plants in non-alphabetical order to test that results are sorted by name
        userDetailDao.insertAll(listOf(userA, userB, userC))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetUsers() = runBlocking {
        val user1 = userDetailDao.getUserDetail(userA.login)
        Assert.assertThat(user1.firstOrNull(), Matchers.equalTo(userA))

        val user2 = userDetailDao.getUserDetail(userB.login)
        Assert.assertThat(user2.firstOrNull(), Matchers.equalTo(userB))

        val user3 = userDetailDao.getUserDetail(userC.login)
        Assert.assertThat(user3.firstOrNull(), Matchers.equalTo(userC))
    }

    @Test
    fun testHasUserDetail() = runBlocking {
        val timeout = 2000L

        // Invalid id should return false
        val hasRestaurantDetailInvalid = userDetailDao.hasUserDetail("no user", timeout)
        Assert.assertFalse(hasRestaurantDetailInvalid)

        // Last updated is greater than or equal to timeout returns false
        val hasRestaurantDetail1 = userDetailDao.hasUserDetail(userA.login, timeout)
        Assert.assertFalse(hasRestaurantDetail1)

        val hasRestaurantDetail2 = userDetailDao.hasUserDetail(userB.login, timeout)
        Assert.assertTrue(hasRestaurantDetail2)

        val hasRestaurantDetail3 = userDetailDao.hasUserDetail(userC.login, timeout)
        Assert.assertTrue(hasRestaurantDetail3)
    }

    companion object {
        fun getTestUser(id: Long, login: String, lastUpdated: Long) =
            UserDetail(
                id,
                login,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null
            )
    }

}