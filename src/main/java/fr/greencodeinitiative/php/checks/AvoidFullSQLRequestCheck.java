/*
 * ecoCode - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
 * Copyright © 2023 Green Code Initiative (https://www.ecocode.io)
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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "EC74")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "S74")
public class AvoidFullSQLRequestCheck extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Don't use the query SELECT * FROM";

    private static final Pattern PATTERN = Pattern.compile("(?i).*select.*\\*.*from.*");

    @Override
    public List<Kind> nodesToVisit() {
        return Arrays.asList(Kind.REGULAR_STRING_LITERAL);
    }

    @Override
    public void visitNode(Tree tree) {

        LiteralTree literal = (LiteralTree) tree;
        if (PATTERN.matcher(literal.value()).matches()) {
            context().newIssue(this, tree, ERROR_MESSAGE);
        }
    }
}
