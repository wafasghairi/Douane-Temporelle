package com.example

import org.junit.Assert.*
import org.junit.Test

// Déclarina classe ExampleUnitTest bech ntestiw biha les fonctions mta3 l'application fel unit tests
class ExampleUnitTest {

    @Test
    fun testKeysGeneration() {
        //Testina mta3 les clés b matricule 1964
        assertEquals("1964_NOM_VOYAGEUR", Keys.cleNom(Keys.MATRICULE))
        assertEquals("1964_ANNEE_NAISSANCE", Keys.cleAnneeNaissance(Keys.MATRICULE))
        assertEquals("1964_ANNEE_DESTINATION", Keys.cleAnneeDestination(Keys.MATRICULE))
        assertEquals("1964_MOTIF_VOYAGE", Keys.cleMotif(Keys.MATRICULE))
        assertEquals("1964_VERDICT", Keys.cleVerdict(Keys.MATRICULE))
    }

    @Test
    fun testIsPrime() {
        //Testina fct isPrime kenha testi les nombres premiers
        assertFalse(VerificationActivity.isPrime(-7))
        assertFalse(VerificationActivity.isPrime(0))
        assertFalse(VerificationActivity.isPrime(1))

        assertTrue(VerificationActivity.isPrime(2))
        assertTrue(VerificationActivity.isPrime(3))
        assertTrue(VerificationActivity.isPrime(5))
        assertTrue(VerificationActivity.isPrime(7))
        assertTrue(VerificationActivity.isPrime(11))
        assertTrue(VerificationActivity.isPrime(13))
        assertTrue(VerificationActivity.isPrime(17))
        assertTrue(VerificationActivity.isPrime(19))
        assertTrue(VerificationActivity.isPrime(23))

        assertFalse(VerificationActivity.isPrime(4))
        assertFalse(VerificationActivity.isPrime(6))
        assertFalse(VerificationActivity.isPrime(9))
        assertFalse(VerificationActivity.isPrime(15))
        assertFalse(VerificationActivity.isPrime(21))
        assertFalse(VerificationActivity.isPrime(25))
    }
}
