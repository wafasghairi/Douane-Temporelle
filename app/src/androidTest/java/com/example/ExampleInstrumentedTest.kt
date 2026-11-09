package com.example

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

// Déclarina classe ExampleInstrumentedTest bech ntestiw biha l'application 3la Android device wala émulateur
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    //7atina test bech nthabtou eli l'application t'exécuti b package name s7i7
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.example", appContext.packageName)
  }
}
