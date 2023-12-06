package com.dicoding.todoapp.ui.list

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.todoapp.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
@RunWith(AndroidJUnit4::class)
class TaskActivityTest {

    private lateinit var scenario: ActivityScenario<TaskActivity>
    private lateinit var context: Context

    @Before
    fun setUp() {
        // Launch the TaskActivity before each test
        scenario = ActivityScenario.launch(TaskActivity::class.java)
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun tearDown() {
        // Close the TaskActivity after each test
        scenario.close()
    }

    @Test
    fun testAddTaskButton() {
        // Find the FAB (Add Task button) and perform a click action
        Espresso.onView(ViewMatchers.withId(R.id.fab))
            .perform(ViewActions.click())

        // Check if the AddTaskActivity is displayed
        Espresso.onView(ViewMatchers.withId(R.id.coordinator_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}