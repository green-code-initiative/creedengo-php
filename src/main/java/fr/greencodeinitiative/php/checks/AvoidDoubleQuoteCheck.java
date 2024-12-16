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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "GCI66")
@DeprecatedRuleKey(repositoryKey = "ecocode-php", ruleKey = "EC66")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "S66")
public class AvoidDoubleQuoteCheck extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Avoid using double quote (\"), prefer using simple quote (')";
    private static final Map<String, Collection<Integer>> linesWithIssuesByFile = new HashMap<>();

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.REGULAR_STRING_LITERAL);
    }

    @Override
    public void visitNode(Tree tree) {
        LiteralTree method = (LiteralTree) tree;
        checkIssue(method);
    }

    public void checkIssue(LiteralTree literalTree) {
        if (lineAlreadyHasThisIssue(literalTree)) return;
        if (literalTree.value().indexOf("\"") == 0 && literalTree.value().lastIndexOf("\"") == literalTree.value().length() - 1) {
            repport(literalTree);
        }
    }

    private void repport(LiteralTree literalTree) {
        if (literalTree.token() != null) {

            final String classname = context().getPhpFile().toString();
            final int line = literalTree.token().line();
            linesWithIssuesByFile.computeIfAbsent(classname, k -> new ArrayList<>());
            linesWithIssuesByFile.get(classname).add(line);
        }
        context().newIssue(this, literalTree, ERROR_MESSAGE);

    }

    private boolean lineAlreadyHasThisIssue(LiteralTree literalTree) {
        if (literalTree.token() != null) {
            final String filename = context().getPhpFile().toString();
            final int line = literalTree.token().line();

            return linesWithIssuesByFile.containsKey(filename)
                    && linesWithIssuesByFile.get(filename).contains(line);
        }

        return false;
    }

}
