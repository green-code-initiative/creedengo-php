/*
 * creedengo - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
 * Copyright © 2024 Green Code Initiative (https://green-code-initiative.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.greencodeinitiative.php.checks;

import org.junit.jupiter.api.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

class GCI35AvoidTryCatchWithFileOpenedCheckTest {

    // All GCI35 fixtures are grouped in this folder to keep each test focused on one use-case family.
    private static final String TESTFILES_PATH = System.getProperty("testfiles.path") + "/GCI35/";

    @Test
    void shouldValidateAllMainUseCases() {
        // Covers the official non-compliant catalog (fopen/readfile/PDF_OPEN in nested structures)
        // and a previously failing compliant scenario around try/catch traversal.
        PHPCheckTest.check(new GCI35AvoidTryCatchWithFileOpenedCheck(), new PhpTestFile(new File(TESTFILES_PATH + "AvoidTryCatchWithFileOpenedCheck.php")));
    }

    @Test
    void shouldValidateEdgeCasesAndDefensiveBranches() {
        // Covers complementary paths (without duplicating main fixture scenarios):
        // case-insensitive match, dynamic callable (compliant), non-function assignment and
        // unsupported statement kinds that must be ignored.
        PHPCheckTest.check(new GCI35AvoidTryCatchWithFileOpenedCheck(), new PhpTestFile(new File(TESTFILES_PATH + "AvoidTryCatchWithFileOpenedCheckEdgeCases.php")));
    }

}
