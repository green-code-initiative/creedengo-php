/*
 * ecoCode - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.statement.BlockTree;
import org.sonar.plugins.php.api.tree.statement.DoWhileStatementTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.tree.statement.ForEachStatementTree;
import org.sonar.plugins.php.api.tree.statement.ForStatementTree;
import org.sonar.plugins.php.api.tree.statement.IfStatementTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "EC72")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "S72")
public class AvoidSQLRequestInLoopCheck extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Avoid SQL request in loop";
    private static final Pattern PATTERN = Pattern.compile("(mysql(i::|_)query\\s*\\(.*)|(oci_execute\\(.*)");

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Kind.FOR_STATEMENT, Kind.FOREACH_STATEMENT, Kind.DO_WHILE_STATEMENT);
    }

    @Override
    public void visitNode(Tree tree) {
        if (tree.is(Kind.FOR_STATEMENT)) {
            StatementTree stTree = ((ForStatementTree) tree).statements().get(0);
            if (stTree.is(Kind.BLOCK)) {
                visitBlockNode((BlockTree) stTree);
            }
        }

        if (tree.is(Kind.FOREACH_STATEMENT)) {
            StatementTree stTree = ((ForEachStatementTree) tree).statements().get(0);
            if (stTree.is(Kind.BLOCK)) {
                visitBlockNode((BlockTree) stTree);
            }
        }

        if (tree.is(Kind.DO_WHILE_STATEMENT)) {
            StatementTree stTree = ((DoWhileStatementTree) tree).statement();
            if (stTree.is(Kind.BLOCK)) {
                visitBlockNode((BlockTree) stTree);
            }
        }
    }

    private void visitBlockNode(BlockTree block) {
        block.statements().forEach(this::visiteChildNode);
    }

    private void visiteChildNode(Tree tree) {
        if (tree.is(Kind.EXPRESSION_STATEMENT)) {
            ExpressionStatementTree expressionStatementTree = (ExpressionStatementTree) tree;
            String expression = expressionStatementTree.expression().toString();
            verifyIfThereIsAError(expression, expressionStatementTree);
        }

        if (tree.is(Kind.IF_STATEMENT)) {
            StatementTree statementTree = ((IfStatementTree) tree).statements().get(0);
            if (statementTree.is(Kind.BLOCK)) {
                visitBlockNode((BlockTree) statementTree);
            } else {
                visiteChildNode(statementTree);
            }
        }
    }

    private void verifyIfThereIsAError(String expression, ExpressionStatementTree expressionStatementTree) {
        if (PATTERN.matcher(expression).find())
            context().newIssue(this, expressionStatementTree, ERROR_MESSAGE);
    }

}
