/*
 * creedengo - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
 * Copyright © 2023 Green Code Initiative (https://green-code-initiative.org)
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

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.SeparatedList;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ParenthesisedExpressionTree;
import org.sonar.plugins.php.api.tree.statement.DoWhileStatementTree;
import org.sonar.plugins.php.api.tree.statement.ForStatementTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.tree.statement.WhileStatementTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Rule(key = "EC3")
public class AvoidGettingSizeCollectionInLoopCheck extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Avoid getting the size of the collection in the loop";
    private static final Pattern PATTERN = Pattern.compile("\\b(?:count|sizeof|iterator_count)\\b");

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(
                Tree.Kind.FOR_STATEMENT,
                Tree.Kind.ALTERNATIVE_FOR_STATEMENT,
                Tree.Kind.WHILE_STATEMENT,
                Tree.Kind.DO_WHILE_STATEMENT
        );
    }

    @Override
    public void visitNode(Tree tree) {
        if (tree.is(Tree.Kind.FOR_STATEMENT) || tree.is(Tree.Kind.ALTERNATIVE_FOR_STATEMENT)) {
            ForStatementTree conditionTree = (ForStatementTree) tree;
            SeparatedList<ExpressionTree> conditions = conditionTree.condition();

            for (ExpressionTree condition : conditions) {
                verifyAndLaunchError(condition.toString(), conditionTree);
            }
        }

        if (tree.is(Tree.Kind.WHILE_STATEMENT)) {
            WhileStatementTree whileTree = (WhileStatementTree) tree;
            ParenthesisedExpressionTree condition = whileTree.condition();
            verifyAndLaunchError(condition.expression().toString(), whileTree);
        }

        if (tree.is(Tree.Kind.DO_WHILE_STATEMENT)) {
            DoWhileStatementTree doWhileTree = (DoWhileStatementTree) tree;
            ParenthesisedExpressionTree condition = doWhileTree.condition();
            verifyAndLaunchError(condition.expression().toString(), doWhileTree);
        }
    }

    private void verifyAndLaunchError(String condition, StatementTree conditionTree) {
          if (PATTERN.matcher(condition).find()) {
              context().newIssue(this, conditionTree, ERROR_MESSAGE);
          }
    }
}
