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

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.SeparatedList;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.statement.ForStatementTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "GCI69")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "S69")
public class NoFunctionCallWhenDeclaringForLoop extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Do not call a function in for-type loop declaration";

    @Override
    public List<Kind> nodesToVisit() {
        return Collections.singletonList(Kind.FOR_STATEMENT);
    }

    @Override
    public void visitNode(Tree tree) {
        ForStatementTree method = (ForStatementTree) tree;
        checkExpressionsTree(method.update());
        checkExpressionsTree(method.condition());
    }

    public void checkExpressionsTree(SeparatedList<ExpressionTree> treeSeparatedList) {
        treeSeparatedList.forEach(this::checkBothSideExpression);
    }

    public void checkBothSideExpression(ExpressionTree expressionTree) {
        if (expressionTree.getKind().getAssociatedInterface() == BinaryExpressionTree.class) {
            BinaryExpressionTree binaryExpressionTree = (BinaryExpressionTree) expressionTree;
            isFunctionCall(binaryExpressionTree.leftOperand());
            isFunctionCall(binaryExpressionTree.rightOperand());
        } else
            isFunctionCall(expressionTree);
    }

    public void isFunctionCall(ExpressionTree expressionTree) {
        if (expressionTree.getKind() == Tree.Kind.FUNCTION_CALL)
            context().newIssue(this, expressionTree, ERROR_MESSAGE);
    }
}
