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
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.statement.*;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "GCI35")
@DeprecatedRuleKey(repositoryKey = "ecocode-php", ruleKey = "EC35")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "S34")
@DeprecatedRuleKey(ruleKey = "EC34")

public class GCI35AvoidTryCatchWithFileOpenedCheck extends PHPSubscriptionCheck {

    public static final String ERROR_MESSAGE = "Avoid the use of try-catch with a file open in try block";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.TRY_STATEMENT);
    }

    @Override
    public void visitNode(@SuppressWarnings("NullableProblems") Tree tree) {

        if (!(tree instanceof TryStatementTree tryStatement)) {
            return;
        }

        // parcours des statements du bloc try
        if (tryStatement.block() != null) {
            visitStatementsList(tryStatement.block().statements());
        }

    }

    private void visitStatementsList(List<StatementTree> lstStmts) {
        if (lstStmts == null || lstStmts.isEmpty()) {
            return;
        }
        for (StatementTree stmt : lstStmts){
            if (stmt == null) {
                continue;
            }
            Tree.Kind kind = stmt.getKind();
            switch (kind) {
                case EXPRESSION_STATEMENT -> visitExpressionStatement(((ExpressionStatementTree) stmt).expression());
                case BLOCK -> visitIfNotNullStatements(((BlockTree) stmt).statements());
                case IF_STATEMENT, ALTERNATIVE_IF_STATEMENT -> visitIfStatement((IfStatementTree) stmt);
                case FOR_STATEMENT, ALTERNATIVE_FOR_STATEMENT -> visitIfNotNullStatements(((ForStatementTree) stmt).statements());
                case WHILE_STATEMENT, ALTERNATIVE_WHILE_STATEMENT -> visitIfNotNullStatements(((WhileStatementTree) stmt).statements());
                case DO_WHILE_STATEMENT -> visitStatementsList(Collections.singletonList(((DoWhileStatementTree) stmt).statement()));
                case FOREACH_STATEMENT, ALTERNATIVE_FOREACH_STATEMENT -> visitIfNotNullStatements(((ForEachStatementTree) stmt).statements());
                case CASE_CLAUSE -> visitIfNotNullStatements(((CaseClauseTree) stmt).statements());
                case SWITCH_STATEMENT -> visitSwitchStatement((SwitchStatementTree) stmt);
                case DEFAULT_CLAUSE -> visitIfNotNullStatements(((DefaultClauseTree) stmt).statements());
                case TRY_STATEMENT -> visitTryStatement((TryStatementTree) stmt);
                default -> {
                    // Nothing to visit for unsupported statement kinds.
                }
            }
        }
    }

    private void visitIfNotNullStatements(List<StatementTree> statements) {
        if (statements != null) {
            visitStatementsList(statements);
        }
    }

    private void visitIfStatement(IfStatementTree ifStatement) {
        if (ifStatement == null) {
            return;
        }
        visitStatementsList(ifStatement.statements());

        ElseClauseTree elseClause = ifStatement.elseClause();
        if (elseClause != null) {
            visitIfNotNullStatements(elseClause.statements());
        }

        List<ElseifClauseTree> elseIfClauses = ifStatement.elseifClauses();
        if (elseIfClauses != null) {
            for (ElseifClauseTree stmtElseIf : elseIfClauses) {
                if (stmtElseIf != null) {
                    visitIfNotNullStatements(stmtElseIf.statements());
                }
            }
        }
    }

    private void visitTryStatement(TryStatementTree tryStatement) {
        if (tryStatement == null) {
            return;
        }
        for (CatchBlockTree stmtCatch : tryStatement.catchBlocks()) {
            if (stmtCatch.block() != null) {
                visitIfNotNullStatements(stmtCatch.block().statements());
            }
        }

        var finallyBlock = tryStatement.finallyBlock();
        if (finallyBlock != null) {
            visitIfNotNullStatements(finallyBlock.statements());
        }
    }

    private void visitSwitchStatement(SwitchStatementTree switchStatement) {
        if (switchStatement == null || switchStatement.cases() == null) {
            return;
        }
        for (SwitchCaseClauseTree switchCaseStmt : switchStatement.cases()) {
            if (switchCaseStmt != null && switchCaseStmt.statements() != null) {
                visitIfNotNullStatements(switchCaseStmt.statements());
            }
        }
    }

    private void visitExpressionStatement(ExpressionTree exprTree){
        if (exprTree == null) {
            return;
        }
        if (exprTree.is(Tree.Kind.FUNCTION_CALL)) { // si directement "function call"
            visitCallExpression((FunctionCallTree) exprTree);
        } else if (exprTree.is(Tree.Kind.ASSIGNMENT)) { // ou si assignment
            AssignmentExpressionTree assignTree = (AssignmentExpressionTree) exprTree;
            if (assignTree.value().is(Tree.Kind.FUNCTION_CALL)) { // et si "function call"
                visitCallExpression((FunctionCallTree) assignTree.value());
            }
        }
    }

    private void visitCallExpression(FunctionCallTree functionCall){
        String funcName = getFunctionNameFromCallExpression(functionCall);
        if (funcName.toUpperCase().startsWith("PDF_OPEN")
                || "fopen".equalsIgnoreCase(funcName)
                || "readfile".equalsIgnoreCase(funcName)) {
            context().newIssue(this, functionCall, ERROR_MESSAGE);
        }
    }

    private String getFunctionNameFromCallExpression(FunctionCallTree functionCall) {
        if (functionCall == null || !(functionCall.callee() instanceof NamespaceNameTree nspTree)) {
            return "";
        }
        return nspTree.fullName() != null ? nspTree.fullName() : "";
    }

}
