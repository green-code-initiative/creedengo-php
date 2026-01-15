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
package org.greencodeinitiative.creedengo.php.integration.tests;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.sonarqube.ws.Issues;
import org.sonarqube.ws.Measures;

import org.greencodeinitiative.creedengo.integration.tests.GCIRulesBase;

class GCIRulesIT extends GCIRulesBase {

    @Test
    void testMeasuresAndIssues() {
        String projectKey = analyzedProjects.get(0).getProjectKey();

        Map<String, Measures.Measure> measures = getMeasures(projectKey);

        assertThat(ofNullable(measures.get("code_smells")).map(Measures.Measure::getValue).map(Integer::parseInt).orElse(0))
                .isGreaterThan(1);

        List<Issues.Issue> projectIssues = searchIssuesForComponent(projectKey, null).getIssuesList();
        assertThat(projectIssues).isNotEmpty();

    }

    @Test
    void testGCI2() {

        String filePath = "src/AvoidMultipleIfElseStatement.php";
        String ruleId = "creedengo-php:GCI2";
        String ruleMsg = "Use a switch statement instead of multiple if-else if possible";
        int[] startLines = new int[]{213, 232, 234, 260, 277, 298, 300, 319, 323, 325, 345, 351, 377, 396, 398, 399, 401, 423, 444, 446};
        int[] endLines = new int[]{213, 232, 236, 260, 279, 298, 302, 321, 323, 327, 347, 353, 379, 396, 404, 399, 403, 425, 444, 448};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI3() {

        String filePath = "src/AvoidGettingSizeCollectionInLoop.php";
        String ruleId = "creedengo-php:GCI3";
        String ruleMsg = "Avoid getting the size of the collection in the loop";
        int[] startLines = new int[]{8, 17, 26, 38, 47, 56, 69, 82, 95, 111, 124, 137, 153, 166, 179, 195, 208, 221};
        int[] endLines = new int[]{10, 19, 28, 40, 49, 58, 72, 85, 98, 114, 127, 140, 156, 169, 182, 198, 211, 224};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI4() {

        String filePath = "src/AvoidUsingGlobalVariablesCheck.php";
        String ruleId = "creedengo-php:GCI4";
        String ruleMsg = "Prefer local variables to globals";
        int[] startLines = new int[]{5, 10};
        int[] endLines = new int[]{8, 14};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI22() {

        String filePath = "src/UseOfMethodsForBasicOperations.php";
        String ruleId = "creedengo-php:GCI22";
        String ruleMsg = "Use of methods for basic operations";
        int[] startLines = new int[]{3, 11};
        int[] endLines = new int[]{3, 11};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI35() {

        String filePath = "src/AvoidTryCatchWithFileOpenedCheck.php";
        String ruleId = "creedengo-php:GCI35";
        String ruleMsg = "Avoid the use of try-catch with a file open in try block";
        int[] startLines = new int[]{5, 21, 35, 43, 54, 64, 86, 96};
        int[] endLines = new int[]{11, 27, 35, 43, 54, 64, 86, 96};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI67() {

        String filePath = "src/IncrementCheck.php";
        String ruleId = "creedengo-php:GCI67";
        String ruleMsg = "Remove the usage of $i++. prefer ++$i";
        int[] startLines = new int[]{6};
        int[] endLines = new int[]{6};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI69() {

        String filePath = "src/NoFunctionCallWhenDeclaringForLoop.php";
        String ruleId = "creedengo-php:GCI69";
        String ruleMsg = "Do not call a function in for-type loop declaration";
        int[] startLines = new int[]{31, 40};
        int[] endLines = new int[]{31, 40};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_5MIN);
    }

    @Test
    void testGCI72() {

        String filePath = "src/AvoidSQLRequestInLoop.php";
        String ruleId = "creedengo-php:GCI72";
        String ruleMsg = "Avoid SQL request in loop";
        int[] startLines = new int[]{40, 43, 53, 56, 67, 70};
        int[] endLines = new int[]{40, 43, 53, 56, 67, 70};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_10MIN);
    }

    @Test
    void testGCI74() {

        String filePath = "src/AvoidFullSQLRequest.php";
        String ruleId = "creedengo-php:GCI74";
        String ruleMsg = "Don't use the query SELECT * FROM";
        int[] startLines = new int[]{3, 4, 11, 12, 18, 19};
        int[] endLines = new int[]{3, 4, 11, 12, 18, 19};

        checkIssuesForFile(filePath, ruleId, ruleMsg, startLines, endLines, SEVERITY, TYPE, EFFORT_20MIN);
    }

}
